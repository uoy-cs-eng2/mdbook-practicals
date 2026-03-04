package uk.ac.york.cs.eng2.checkinstats.events.windowed;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uk.ac.york.cs.eng2.checkinstats.domain.WindowedAreaCheckInStat;
import uk.ac.york.cs.eng2.checkinstats.events.CheckInTopics;
import uk.ac.york.cs.eng2.checkinstats.events.TerminalInfo;
import uk.ac.york.cs.eng2.checkinstats.repositories.WindowedAreaCheckInStatRepository;

import java.time.Instant;

@KafkaListener(groupId = "windowed-area-check-ins", threads=3, offsetReset = OffsetReset.EARLIEST)
public class WindowedAreaCheckInsConsumer {

  /*
   * Time windows are "tumbling": they start at the epoch (midnight on
   * 1970-01-01 UTC+0), and there's a new one every 60s.
   */
  public static final int WINDOW_SIZE_MILLIS = 60_000;

  @Inject
  private WindowedAreaCheckInsProducer producer;

  @Inject
  private WindowedAreaCheckInStatRepository repo;

  @Topic({ CheckInTopics.TOPIC_CHECKIN, CheckInTopics.TOPIC_COMPLETED, CheckInTopics.TOPIC_CANCELLED })
  public void checkInEvent(@KafkaKey long deskId, TerminalInfo tInfo, String topic, long timestamp) {
    // We funnel all check-in started/completed/cancelled events into a time-windowed topic
    long windowStartAtMillis = timestamp - (timestamp % WINDOW_SIZE_MILLIS);
    CheckInAreaWindow key = new CheckInAreaWindow((int) deskId / 100, windowStartAtMillis);
    producer.checkin(key, topic);
  }

  @Transactional
  @Topic(WindowedAreaCheckInsTopicFactory.TOPIC_WINDOWED_CHECKINS)
  public void windowedCheckIn(@KafkaKey CheckInAreaWindow key, String originalTopic) {
    String statName = switch(originalTopic) {
      case CheckInTopics.TOPIC_CHECKIN -> "started";
      case CheckInTopics.TOPIC_COMPLETED -> "completed";
      case CheckInTopics.TOPIC_CANCELLED -> "cancelled";
      default -> null;
    };
    if (statName == null) {
      // ignore - unknown topic
      //
      // In a production environment, we would produce a WARNING logging message
      return;
    }

    Instant windowStartAt = Instant.ofEpochMilli(key.windowStartEpochMillis());
    WindowedAreaCheckInStat stat = repo
        .findByAreaAndWindowStartAtAndName(key.area(), windowStartAt, statName)
        .orElse(new WindowedAreaCheckInStat(key.area(), windowStartAt, statName));

    stat.setValue(stat.getValue() + 1);
    repo.save(stat);
  }

}
