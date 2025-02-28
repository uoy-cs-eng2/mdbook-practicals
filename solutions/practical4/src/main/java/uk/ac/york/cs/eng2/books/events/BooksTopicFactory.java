package uk.ac.york.cs.eng2.books.events;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;

@Requires(bean= AdminClient.class)
@Factory
public class BooksTopicFactory {

  public static final String TOPIC_ISBN_CHANGED = "isbn-changed";

  @Bean
  public NewTopic booksTopic() {
    return new NewTopic(TOPIC_ISBN_CHANGED, 3, (short) 1);
  }

}
