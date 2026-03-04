package uk.ac.york.cs.eng2.checkinstats.events.windowed;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;

@Requires(bean=AdminClient.class)
@Factory
public class WindowedAreaCheckInsTopicFactory {

  public static final String TOPIC_WINDOWED_CHECKINS = "windowed-check-ins-by-area";

  @Bean
  public NewTopic windowedCheckInsTopic() {
    return new NewTopic(TOPIC_WINDOWED_CHECKINS, 3, (short) 1);
  }

}
