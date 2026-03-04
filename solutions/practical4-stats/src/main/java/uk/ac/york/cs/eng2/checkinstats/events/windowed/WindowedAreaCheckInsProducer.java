package uk.ac.york.cs.eng2.checkinstats.events.windowed;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface WindowedAreaCheckInsProducer {

  @Topic(WindowedAreaCheckInsTopicFactory.TOPIC_WINDOWED_CHECKINS)
  void checkin(@KafkaKey CheckInAreaWindow windowedArea, String originalTopic);

}
