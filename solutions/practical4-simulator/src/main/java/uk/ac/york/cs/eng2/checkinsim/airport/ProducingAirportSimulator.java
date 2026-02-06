package uk.ac.york.cs.eng2.checkinsim.airport;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import uk.ac.york.cs.eng2.checkinsim.events.AirportEventProducer;
import uk.ac.york.cs.eng2.checkinsim.events.TerminalInfo;

import java.util.Random;

@Singleton
public class ProducingAirportSimulator extends AirportSimulator {

  @Inject
  private AirportEventProducer producer;

  public ProducingAirportSimulator() {
    super(500, new Random(1));
  }

  @Scheduled(fixedDelay = "${airport.tick.delay}")
  @Override
  public void tick() {
    super.tick();
  }

  @Override
  protected void fireEvent(EventType type, Terminal t) {
    final TerminalInfo terminalInfo = new TerminalInfo(t.isStuck(), t.getPaperLeft());
    switch (type) {
      case STATUS -> producer.checkInStatus(t.getId(), terminalInfo);
      case CANCELLED ->  producer.checkInCancelled(t.getId(), terminalInfo);
      case COMPLETED -> producer.checkInCompleted(t.getId(), terminalInfo);
      case CHECK_IN -> producer.checkInStarted(t.getId(), terminalInfo);
      case LOW_PAPER -> producer.checkInDeskLowOnPaper(t.getId(), terminalInfo);
      case OUT_OF_ORDER -> producer.checkInDeskOutOfOrder(t.getId(), terminalInfo);
    }
  }

}
