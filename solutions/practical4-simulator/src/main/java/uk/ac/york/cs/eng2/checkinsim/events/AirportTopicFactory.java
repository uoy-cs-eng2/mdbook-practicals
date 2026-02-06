package uk.ac.york.cs.eng2.checkinsim.events;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;

@Requires(bean = AdminClient.class)
@Factory
public class AirportTopicFactory {
  public static final String TOPIC_CANCELLED = "selfservice-cancelled";
  public static final String TOPIC_CHECKIN = "selfservice-checkin";
  public static final String TOPIC_COMPLETED = "selfservice-completed";
  public static final String TOPIC_LOWPAPER = "selfservice-lowpaper";
  public static final String TOPIC_OUTOFORDER = "selfservice-outoforder";
  public static final String TOPIC_STATUS = "selfservice-status";

  @Bean
  public NewTopic createCancelledTopic() {
    return new NewTopic(TOPIC_CANCELLED, 3, (short) 1);
  }

  @Bean
  public NewTopic createCheckinTopic() {
    return new NewTopic(TOPIC_CHECKIN, 3, (short) 1);
  }

  @Bean
  public NewTopic createCompletedTopic() {
    return new NewTopic(TOPIC_COMPLETED, 3, (short) 1);
  }

  @Bean
  public NewTopic createLowPaperTopic() {
    return new NewTopic(TOPIC_LOWPAPER, 3, (short) 1);
  }

  @Bean
  public NewTopic createOutOfOrderTopic() {
    return new NewTopic(TOPIC_OUTOFORDER, 3, (short) 1);
  }

  @Bean
  public NewTopic createStatusTopic() {
    return new NewTopic(TOPIC_STATUS, 3, (short) 1);
  }

}
