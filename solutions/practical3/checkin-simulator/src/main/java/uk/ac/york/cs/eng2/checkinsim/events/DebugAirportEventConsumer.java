package uk.ac.york.cs.eng2.checkinsim.events;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaListener
public class DebugAirportEventConsumer {

  @Topic(AirportTopicFactory.TOPIC_CANCELLED)
  public void checkInCancelled(@KafkaKey long terminalId, TerminalInfo terminalInfo) {
    System.out.printf("Check-in cancelled for terminal %d: %s%s", terminalId, terminalInfo, System.lineSeparator());
  }

  @Topic(AirportTopicFactory.TOPIC_CHECKIN)
  public void checkInStarted(@KafkaKey long terminalId, TerminalInfo terminalInfo) {
    System.out.printf("Check-in started for terminal %d: %s%s", terminalId, terminalInfo, System.lineSeparator());
  }

  @Topic(AirportTopicFactory.TOPIC_COMPLETED)
  public void checkInCompleted(@KafkaKey long terminalId, TerminalInfo terminalInfo) {
    System.out.printf("Check-in completed for terminal %d: %s%s", terminalId, terminalInfo, System.lineSeparator());
  }

  @Topic(AirportTopicFactory.TOPIC_LOWPAPER)
  public void checkInDeskLowOnPaper(@KafkaKey long terminalId, TerminalInfo terminalInfo) {
    System.out.printf("Check-in desk for terminal %d is low on paper: %s%s", terminalId, terminalInfo, System.lineSeparator());
  }

  @Topic(AirportTopicFactory.TOPIC_OUTOFORDER)
  public void checkInDeskOutOfOrder(@KafkaKey long terminalId, TerminalInfo terminalInfo) {
    System.out.printf("Check-in desk for terminal %d is out of order: %s%s", terminalId, terminalInfo, System.lineSeparator());
  }

  @Topic(AirportTopicFactory.TOPIC_STATUS)
  public void checkInStatus(@KafkaKey long terminalId, TerminalInfo terminalInfo) {
    System.out.printf("Check-in desk for terminal %d status report: %s%s", terminalId, terminalInfo, System.lineSeparator());
  }

}
