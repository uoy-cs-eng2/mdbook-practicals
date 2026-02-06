package uk.ac.york.cs.eng2.checkinstats.events;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.york.cs.eng2.checkinstats.repositories.CheckInDeskRepository;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(transactional = false)
public class StatusUpdateConsumerTest {

  @Inject
  private StatusUpdateConsumer consumer;

  @Inject
  private CheckInDeskRepository repo;

  @BeforeEach
  public void setup() {
    repo.deleteAll();
  }

  @Test
  public void statusUpdated() {
    long nowMillis = Instant.now().toEpochMilli();
    consumer.statusUpdate(234, null, nowMillis);
    assertEquals(nowMillis, repo.findByDeskId(234).get().getLastStatusAt().toEpochMilli());
    consumer.statusUpdate(234, null, nowMillis + 1000);
    assertEquals(nowMillis + 1000, repo.findByDeskId(234).get().getLastStatusAt().toEpochMilli());
  }

}
