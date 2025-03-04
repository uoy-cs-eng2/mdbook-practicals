package uk.ac.york.cs.eng2.books.events;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface BooksProducer {

  @Topic(BooksTopicFactory.TOPIC_ISBN_CHANGED)
  void isbnChanged(@KafkaKey long bookId, BookIsbnChange body);

}
