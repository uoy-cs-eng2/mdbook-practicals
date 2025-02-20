package uk.ac.york.cs.eng2.checkinstats.events.windowed;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;

@Requires(bean=AdminClient.class)
@Factory
public class WindowedAreaCheckinsTopicFactory {

  public static final String TOPIC_WINDOWED_CHECKINS = "windowed-checkins-by-area";

  @Bean
  public NewTopic windowedCheckinsTopic() {
    return new NewTopic(TOPIC_WINDOWED_CHECKINS, 3, (short) 1);
  }

}
