package uk.ac.york.cs.eng2.checkinstats.events.windowed;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uk.ac.york.cs.eng2.checkinstats.domain.WindowedAreaCheckinStat;
import uk.ac.york.cs.eng2.checkinstats.events.CheckinTopics;
import uk.ac.york.cs.eng2.checkinstats.events.TerminalInfo;
import uk.ac.york.cs.eng2.checkinstats.repositories.WindowedAreaCheckinStatRepository;

import java.time.Instant;

@KafkaListener(groupId = "windowed-area-checkins", threads=3, offsetReset = OffsetReset.EARLIEST)
public class WindowedAreaCheckinsConsumer {

  /*
   * Time windows are "tumbling": they start at the epoch (midnight on
   * 1970-01-01 UTC+0), and there's a new one every 60s.
   */
  public static final int WINDOW_SIZE_MILLIS = 60_000;

  @Inject
  private WindowedAreaCheckinsProducer producer;

  @Inject
  private WindowedAreaCheckinStatRepository repo;

  @Topic({ CheckinTopics.TOPIC_CHECKIN, CheckinTopics.TOPIC_COMPLETED, CheckinTopics.TOPIC_CANCELLED })
  public void checkInEvent(@KafkaKey long deskId, TerminalInfo tInfo, String topic, long timestamp) {
    // We funnel all check-in started/completed/cancelled events into a time-windowed topic
    long windowStartAtMillis = timestamp - (timestamp % WINDOW_SIZE_MILLIS);
    CheckinAreaWindow key = new CheckinAreaWindow((int) deskId / 100, windowStartAtMillis);
    producer.checkin(key, topic);
  }

  @Transactional
  @Topic(WindowedAreaCheckinsTopicFactory.TOPIC_WINDOWED_CHECKINS)
  public void windowedCheckin(@KafkaKey CheckinAreaWindow key, String originalTopic) {
    String statName = switch(originalTopic) {
      case CheckinTopics.TOPIC_CHECKIN -> "started";
      case CheckinTopics.TOPIC_COMPLETED -> "completed";
      case CheckinTopics.TOPIC_CANCELLED -> "cancelled";
      default -> null;
    };
    if (statName == null) {
      // ignore - unknown topic
      //
      // In a production environment, we would produce a WARNING logging message
      return;
    }

    Instant windowStartAt = Instant.ofEpochMilli(key.windowStartEpochMillis());
    WindowedAreaCheckinStat stat = repo
        .findByAreaAndWindowStartAtAndName(key.area(), windowStartAt, statName)
        .orElse(new WindowedAreaCheckinStat(key.area(), windowStartAt, statName));

    stat.setValue(stat.getValue() + 1);
    repo.save(stat);
  }

}
