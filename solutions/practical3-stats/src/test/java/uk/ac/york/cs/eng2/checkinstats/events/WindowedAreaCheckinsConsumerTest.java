package uk.ac.york.cs.eng2.checkinstats.events;

import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.york.cs.eng2.checkinstats.domain.WindowedAreaCheckinStat;
import uk.ac.york.cs.eng2.checkinstats.events.windowed.CheckinAreaWindow;
import uk.ac.york.cs.eng2.checkinstats.events.windowed.WindowedAreaCheckinsConsumer;
import uk.ac.york.cs.eng2.checkinstats.events.windowed.WindowedAreaCheckinsProducer;
import uk.ac.york.cs.eng2.checkinstats.repositories.WindowedAreaCheckinStatRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.ac.york.cs.eng2.checkinstats.events.windowed.WindowedAreaCheckinsConsumer.WINDOW_SIZE_MILLIS;

import java.time.Instant;
import java.util.Optional;

@MicronautTest(transactional = false)
public class WindowedAreaCheckinsConsumerTest {

  @Inject
  private WindowedAreaCheckinsConsumer consumer;

  @Inject
  private WindowedAreaCheckinStatRepository repo;

  @Inject
  private WindowedAreaCheckinsProducer producer;

  @BeforeEach
  public void setup() throws Exception {
    repo.deleteAll();
  }

  @MockBean(WindowedAreaCheckinsProducer.class)
  public WindowedAreaCheckinsProducer getProducer() {
    return mock(WindowedAreaCheckinsProducer.class);
  }

  @Test
  public void eventIsRekeyed() {
    consumer.checkInEvent(123L, null, CheckinTopics.TOPIC_CHECKIN, WINDOW_SIZE_MILLIS + 200);

    verify(producer).checkin(
        eq(new CheckinAreaWindow(1, WINDOW_SIZE_MILLIS)),
        eq(CheckinTopics.TOPIC_CHECKIN));
  }

  @Test
  public void countsAreWindowed() {
    consumer.windowedCheckin(new CheckinAreaWindow(1, WINDOW_SIZE_MILLIS), CheckinTopics.TOPIC_CHECKIN);
    consumer.windowedCheckin(new CheckinAreaWindow(1, WINDOW_SIZE_MILLIS), CheckinTopics.TOPIC_CHECKIN);
    consumer.windowedCheckin(new CheckinAreaWindow(1, WINDOW_SIZE_MILLIS * 2), CheckinTopics.TOPIC_CHECKIN);

    assertAreaCountIs(1, WINDOW_SIZE_MILLIS, "started", 2);
    assertAreaCountIs(1, WINDOW_SIZE_MILLIS * 2, "started", 1);
  }

  @Test
  public void countsAreDividedByArea() {
    consumer.windowedCheckin(new CheckinAreaWindow(2, WINDOW_SIZE_MILLIS), CheckinTopics.TOPIC_COMPLETED);
    consumer.windowedCheckin(new CheckinAreaWindow(3, WINDOW_SIZE_MILLIS), CheckinTopics.TOPIC_COMPLETED);

    assertAreaCountIs(2, WINDOW_SIZE_MILLIS, "completed", 1);
    assertAreaCountIs(3, WINDOW_SIZE_MILLIS, "completed", 1);
  }

  protected void assertAreaCountIs(int area, long startMillis, String name, long expected) {
    Optional<WindowedAreaCheckinStat> oStat = repo
        .findByAreaAndWindowStartAtAndName(area, Instant.ofEpochMilli(startMillis), name);
    assertTrue(oStat.isPresent(), String.format(
        "The count for (area=%d, startMillis=%d, name=%s) exists", area, startMillis, name));
    assertEquals(expected, oStat.get().getValue(), String.format(
        "The count for (area=%d, startMillis=%d, name=%s) has the expected value", area, startMillis, name));
  }

}
