package uk.ac.york.cs.eng2.checkinstats.events.windowed;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface WindowedAreaCheckinsProducer {

  @Topic(WindowedAreaCheckinsTopicFactory.TOPIC_WINDOWED_CHECKINS)
  void checkin(@KafkaKey CheckinAreaWindow windowedArea, String originalTopic);

}
