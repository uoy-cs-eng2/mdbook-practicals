package uk.ac.york.cs.eng2.checkinstats.events;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.york.cs.eng2.checkinstats.domain.PartitionedCheckinStat;
import uk.ac.york.cs.eng2.checkinstats.repositories.PartitionedCheckinStatRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(transactional = false)
public class CheckinStatisticsConsumerTest {

  @Inject
  private CheckinStatisticsConsumer consumer;

  @Inject
  private PartitionedCheckinStatRepository repo;

  @BeforeEach
  public void setup() {
    repo.deleteAll();
  }

  @Test
  public void startedIsRecorded() {
    consumer.checkInStarted(0);

    PartitionedCheckinStat stat = repo.findByPartitionIdAndName(0, "started").get();
    assertEquals(1, stat.getValue());
  }

  @Test
  public void valueIsIncremented() {
    // We change the partition here, to ensure the consumer is taking into account
    // the source partition (instead of sending everything to partition 0)
    consumer.checkInCompleted(2);
    consumer.checkInCompleted(2);

    PartitionedCheckinStat stat = repo.findByPartitionIdAndName(2, "completed").get();
    assertEquals(2, stat.getValue());
  }

  @Test
  public void valuesAreSeparated() {
    consumer.checkInCancelled(2);
    consumer.checkInCompleted(2);

    PartitionedCheckinStat stat = repo.findByPartitionIdAndName(2, "cancelled").get();
    assertEquals(1, stat.getValue());
    stat = repo.findByPartitionIdAndName(2, "completed").get();
    assertEquals(1, stat.getValue());
  }

}
