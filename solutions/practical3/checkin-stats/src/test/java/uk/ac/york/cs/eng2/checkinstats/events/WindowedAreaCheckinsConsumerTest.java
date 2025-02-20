package uk.ac.york.cs.eng2.checkinstats.events;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.york.cs.eng2.checkinstats.domain.WindowedAreaCheckinStat;
import uk.ac.york.cs.eng2.checkinstats.events.windowed.WindowedAreaCheckinsConsumer;
import uk.ac.york.cs.eng2.checkinstats.events.windowed.WindowedAreaCheckinsTopicFactory;
import uk.ac.york.cs.eng2.checkinstats.repositories.WindowedAreaCheckinStatRepository;

import static org.awaitility.Awaitility.await;
import static uk.ac.york.cs.eng2.checkinstats.events.windowed.WindowedAreaCheckinsConsumer.WINDOW_SIZE_MILLIS;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.Callable;

@MicronautTest(transactional = false)
public class WindowedAreaCheckinsConsumerTest {

  @Inject
  private WindowedAreaCheckinsConsumer consumer;

  @Inject
  private WindowedAreaCheckinStatRepository repo;

  @Inject
  private AdminClient adminClient;

  @BeforeEach
  public void setup() throws Exception {
    // 1. Find out if the topics we need are in the cluster, and how many partitions they have
    DescribeTopicsResult describeResult = adminClient
        .describeTopics(Collections.singleton(WindowedAreaCheckinsTopicFactory.TOPIC_WINDOWED_CHECKINS));

    // 2. We ask for the offsets in the existing topics
    var offsetsRequest = new HashMap<TopicPartition, OffsetSpec>();
    for (var entry : describeResult.topicNameValues().entrySet()) {
      TopicDescription topicDescription = entry.getValue().get();
      if (topicDescription != null) {
        for (TopicPartitionInfo part : topicDescription.partitions()) {
          TopicPartition tp = new TopicPartition(entry.getKey(), part.partition());
          offsetsRequest.put(tp, OffsetSpec.latest());
        }
      }
    }
    var offsetsResponse = adminClient.listOffsets(offsetsRequest).all().get();

    // 3. Send request to delete records before those offsets
    var deleteRequest = new HashMap<TopicPartition, RecordsToDelete>();
    for (var entry : offsetsResponse.entrySet()) {
      deleteRequest.put(entry.getKey(), RecordsToDelete.beforeOffset(entry.getValue().offset()));
    }
    adminClient.deleteRecords(deleteRequest);

    // 4. Clean the table as well
    repo.deleteAll();
  }

  @Test
  public void countsAreWindowed() {
    consumer.checkInEvent(123L, null, CheckinTopics.TOPIC_CHECKIN, WINDOW_SIZE_MILLIS + 200);
    consumer.checkInEvent(123L, null, CheckinTopics.TOPIC_CHECKIN, WINDOW_SIZE_MILLIS + 300);
    consumer.checkInEvent(123L, null, CheckinTopics.TOPIC_CHECKIN, WINDOW_SIZE_MILLIS * 2 + 100);

    await().atMost(Duration.ofSeconds(10)).until(areaCountBecomes(1, WINDOW_SIZE_MILLIS, "started", 2));
    await().atMost(Duration.ofSeconds(10)).until(areaCountBecomes(1, WINDOW_SIZE_MILLIS * 2, "started", 1));
  }

  @Test
  public void countsAreDividedByArea() {
    consumer.checkInEvent(234L, null, CheckinTopics.TOPIC_COMPLETED, WINDOW_SIZE_MILLIS + 200);
    consumer.checkInEvent(345L, null, CheckinTopics.TOPIC_COMPLETED, WINDOW_SIZE_MILLIS + 340);
    await().atMost(Duration.ofSeconds(10)).until(areaCountBecomes(2, WINDOW_SIZE_MILLIS, "completed", 1));
    await().atMost(Duration.ofSeconds(10)).until(areaCountBecomes(3, WINDOW_SIZE_MILLIS, "completed", 1));
  }

  protected Callable<Boolean> areaCountBecomes(int area, long startMillis, String name, long expected) {
    return () -> {
      Optional<WindowedAreaCheckinStat> oStat = repo.findByAreaAndWindowStartAtAndName(area, Instant.ofEpochMilli(startMillis), name);
      return oStat.isPresent() && oStat.get().getValue() == expected;
    };
  }

}
