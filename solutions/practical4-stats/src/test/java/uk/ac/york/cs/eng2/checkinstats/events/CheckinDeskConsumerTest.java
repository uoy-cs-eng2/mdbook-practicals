package uk.ac.york.cs.eng2.checkinstats.events;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.york.cs.eng2.checkinstats.domain.CheckInDesk;
import uk.ac.york.cs.eng2.checkinstats.repositories.CheckInDeskRepository;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false)
public class CheckinDeskConsumerTest {

  @Inject
  private CheckinDeskConsumer consumer;

  @Inject
  private CheckInDeskRepository repo;

  @BeforeEach
  public void setup() {
    repo.deleteAll();
  }

  @Test
  public void started() {
    long nowMillis = Instant.now().toEpochMilli();
    consumer.checkInStarted(123, null, nowMillis);

    CheckInDesk checkInDesk = repo.findByDeskId(123).get();
    assertEquals(123, checkInDesk.getDeskId());
    assertEquals(nowMillis, checkInDesk.getCheckinStartedAt().toEpochMilli());
    assertNull(checkInDesk.getLastStatusAt());
    assertFalse(checkInDesk.getOutOfOrder());
  }

  @Test
  public void checkInDone() {
    long nowMillis = Instant.now().toEpochMilli();
    consumer.checkInStarted(123, null, nowMillis);
    consumer.checkInDone(123, null, nowMillis);

    CheckInDesk checkInDesk = repo.findByDeskId(123).get();
    assertEquals(123, checkInDesk.getDeskId());
    assertNull(checkInDesk.getCheckinStartedAt());
    assertNull(checkInDesk.getLastStatusAt());
    assertFalse(checkInDesk.getOutOfOrder());
  }

  @Test
  public void outOfOrder() {
    long nowMillis = Instant.now().toEpochMilli();
    consumer.checkInStarted(123, null, nowMillis);
    consumer.checkInDeskOutOfOrder(123, null, nowMillis);

    CheckInDesk checkInDesk = repo.findByDeskId(123).get();
    assertEquals(123, checkInDesk.getDeskId());
    assertEquals(nowMillis, checkInDesk.getCheckinStartedAt().toEpochMilli());
    assertNull(checkInDesk.getLastStatusAt());
    assertTrue(checkInDesk.getOutOfOrder());
  }

}
