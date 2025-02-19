package uk.ac.york.cs.eng2.checkinstats.events;

import io.micronaut.configuration.kafka.annotation.*;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uk.ac.york.cs.eng2.checkinstats.domain.PartitionedCheckinStat;
import uk.ac.york.cs.eng2.checkinstats.repositories.PartitionedCheckinStatRepository;

@KafkaListener(groupId="checkin-stats", threads = 3, offsetReset = OffsetReset.EARLIEST)
public class CheckinCounterConsumer {
  @Inject
  private PartitionedCheckinStatRepository repo;

  @Transactional
  @Topic(CheckinTopics.TOPIC_CHECKIN)
  public void checkInStarted(@KafkaKey long id, @KafkaPartition int partition) {
    incrementStatistic(partition, "started");
  }

  @Transactional
  @Topic(CheckinTopics.TOPIC_CANCELLED)
  public void checkInCancelled(@KafkaKey long id, @KafkaPartition int partition) {
    incrementStatistic(partition, "cancelled");
  }

  @Transactional
  @Topic(CheckinTopics.TOPIC_COMPLETED)
  public void checkInCompleted(@KafkaKey long id, @KafkaPartition int partition) {
    incrementStatistic(partition, "completed");
  }

  private void incrementStatistic(int partition, String statName) {
    PartitionedCheckinStat current = repo
        .findByPartitionIdAndName(partition, statName)
        .orElse(new PartitionedCheckinStat(partition, statName));

    current.setValue(current.getValue() + 1);
    repo.save(current);
  }

}
