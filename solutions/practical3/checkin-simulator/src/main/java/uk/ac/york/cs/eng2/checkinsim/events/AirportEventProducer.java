package uk.ac.york.cs.eng2.checkinsim.events;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface AirportEventProducer {

  @Topic(AirportTopicFactory.TOPIC_CANCELLED)
  void checkInCancelled(@KafkaKey long terminalId, TerminalInfo terminalInfo);

  @Topic(AirportTopicFactory.TOPIC_CHECKIN)
  void checkInStarted(@KafkaKey long terminalId, TerminalInfo terminalInfo);

  @Topic(AirportTopicFactory.TOPIC_COMPLETED)
  void checkInCompleted(@KafkaKey long terminalId, TerminalInfo terminalInfo);

  @Topic(AirportTopicFactory.TOPIC_LOWPAPER)
  void checkInDeskLowOnPaper(@KafkaKey long terminalId, TerminalInfo terminalInfo);

  @Topic(AirportTopicFactory.TOPIC_OUTOFORDER)
  void checkInDeskOutOfOrder(@KafkaKey long terminalId, TerminalInfo terminalInfo);

  @Topic(AirportTopicFactory.TOPIC_STATUS)
  void checkInStatus(@KafkaKey long terminalId, TerminalInfo terminalInfo);

}
