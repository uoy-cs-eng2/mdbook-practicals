package uk.ac.york.cs.eng2.checkinstats.events;

import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.york.cs.eng2.checkinstats.domain.WindowedAreaCheckInStat;
import uk.ac.york.cs.eng2.checkinstats.events.windowed.CheckInAreaWindow;
import uk.ac.york.cs.eng2.checkinstats.events.windowed.WindowedAreaCheckInsConsumer;
import uk.ac.york.cs.eng2.checkinstats.events.windowed.WindowedAreaCheckInsProducer;
import uk.ac.york.cs.eng2.checkinstats.repositories.WindowedAreaCheckInStatRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.ac.york.cs.eng2.checkinstats.events.windowed.WindowedAreaCheckInsConsumer.WINDOW_SIZE_MILLIS;

import java.time.Instant;
import java.util.Optional;

@MicronautTest(transactional = false)
public class WindowedAreaCheckInsConsumerTest {

  @Inject
  private WindowedAreaCheckInsConsumer consumer;

  @Inject
  private WindowedAreaCheckInStatRepository repo;

  @Inject
  private WindowedAreaCheckInsProducer producer;

  @BeforeEach
  public void setup() throws Exception {
    repo.deleteAll();
  }

  @MockBean(WindowedAreaCheckInsProducer.class)
  public WindowedAreaCheckInsProducer getProducer() {
    return mock(WindowedAreaCheckInsProducer.class);
  }

  @Test
  public void eventIsRekeyed() {
    consumer.checkInEvent(123L, null, CheckInTopics.TOPIC_CHECKIN, WINDOW_SIZE_MILLIS + 200);

    verify(producer).checkin(
        eq(new CheckInAreaWindow(1, WINDOW_SIZE_MILLIS)),
        eq(CheckInTopics.TOPIC_CHECKIN));
  }

  @Test
  public void countsAreWindowed() {
    consumer.windowedCheckIn(new CheckInAreaWindow(1, WINDOW_SIZE_MILLIS), CheckInTopics.TOPIC_CHECKIN);
    consumer.windowedCheckIn(new CheckInAreaWindow(1, WINDOW_SIZE_MILLIS), CheckInTopics.TOPIC_CHECKIN);
    consumer.windowedCheckIn(new CheckInAreaWindow(1, WINDOW_SIZE_MILLIS * 2), CheckInTopics.TOPIC_CHECKIN);

    assertAreaCountIs(1, WINDOW_SIZE_MILLIS, "started", 2);
    assertAreaCountIs(1, WINDOW_SIZE_MILLIS * 2, "started", 1);
  }

  @Test
  public void countsAreDividedByArea() {
    consumer.windowedCheckIn(new CheckInAreaWindow(2, WINDOW_SIZE_MILLIS), CheckInTopics.TOPIC_COMPLETED);
    consumer.windowedCheckIn(new CheckInAreaWindow(3, WINDOW_SIZE_MILLIS), CheckInTopics.TOPIC_COMPLETED);

    assertAreaCountIs(2, WINDOW_SIZE_MILLIS, "completed", 1);
    assertAreaCountIs(3, WINDOW_SIZE_MILLIS, "completed", 1);
  }

  protected void assertAreaCountIs(int area, long startMillis, String name, long expected) {
    Optional<WindowedAreaCheckInStat> oStat = repo
        .findByAreaAndWindowStartAtAndName(area, Instant.ofEpochMilli(startMillis), name);
    assertTrue(oStat.isPresent(), String.format(
        "The count for (area=%d, startMillis=%d, name=%s) exists", area, startMillis, name));
    assertEquals(expected, oStat.get().getValue(), String.format(
        "The count for (area=%d, startMillis=%d, name=%s) has the expected value", area, startMillis, name));
  }

}
