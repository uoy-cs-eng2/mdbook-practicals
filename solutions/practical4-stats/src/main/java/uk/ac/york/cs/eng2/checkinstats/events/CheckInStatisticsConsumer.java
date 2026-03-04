package uk.ac.york.cs.eng2.checkinstats.events;

import io.micronaut.configuration.kafka.annotation.*;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uk.ac.york.cs.eng2.checkinstats.domain.PartitionedCheckInStat;
import uk.ac.york.cs.eng2.checkinstats.repositories.PartitionedCheckInStatRepository;

@KafkaListener(groupId="check-in-stats", threads = 3, offsetReset = OffsetReset.EARLIEST)
public class CheckInStatisticsConsumer {
  @Inject
  private PartitionedCheckInStatRepository repo;

  @Transactional
  @Topic(CheckInTopics.TOPIC_CHECKIN)
  public void checkInStarted(@KafkaPartition int partition) {
    incrementStatistic(partition, "started");
  }

  @Transactional
  @Topic(CheckInTopics.TOPIC_CANCELLED)
  public void checkInCancelled(@KafkaPartition int partition) {
    incrementStatistic(partition, "cancelled");
  }

  @Transactional
  @Topic(CheckInTopics.TOPIC_COMPLETED)
  public void checkInCompleted(@KafkaPartition int partition) {
    incrementStatistic(partition, "completed");
  }

  private void incrementStatistic(int partition, String statName) {
    PartitionedCheckInStat current = repo
        .findByPartitionIdAndName(partition, statName)
        .orElse(new PartitionedCheckInStat(partition, statName));

    current.setValue(current.getValue() + 1);
    repo.save(current);
  }

}
