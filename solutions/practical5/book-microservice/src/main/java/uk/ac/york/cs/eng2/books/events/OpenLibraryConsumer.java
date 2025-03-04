package uk.ac.york.cs.eng2.books.events;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uk.ac.york.cs.eng2.books.domain.Book;
import uk.ac.york.cs.eng2.books.domain.Publisher;
import uk.ac.york.cs.eng2.books.gateways.BookCatalogGateway;
import uk.ac.york.cs.eng2.books.gateways.BookCatalogInfo;
import uk.ac.york.cs.eng2.books.repository.BookRepository;
import uk.ac.york.cs.eng2.books.repository.PublisherRepository;

import java.util.List;
import java.util.Optional;

@KafkaListener(groupId="open-library")
public class OpenLibraryConsumer {
  @Inject
  private BookRepository repo;

  @Inject
  private PublisherRepository publisherRepo;

  @Inject
  private BookCatalogGateway catalogGateway;

  @Inject
  private BookRepository bookRepository;

  @Transactional
  @Topic(BooksTopicFactory.TOPIC_ISBN_CHANGED)
  public void isbnChanged(@KafkaKey long bookId, BookIsbnChange change) {
    @NonNull Optional<Book> oBook = repo.findById(bookId);
    if (oBook.isEmpty()) {
      // Book does not exist anymore - ignore event
      return;
    }

    Book book = oBook.get();
    if (book.getPublisher() != null) {
      // Book already has a publisher - stop
      return;
    }

    try {
      Optional<BookCatalogInfo> oPublishers = catalogGateway.findByIsbn(change.newIsbn());
        if (oPublishers.isEmpty()) {
        // Could not obtain publishers or there are none - stop here
        return;
      }
      handleGatewayResponse(oPublishers.get(), book);
    } catch (Exception e) {
      // In a production environment, we would log this using a logging API (e.g. SLF4J).
      e.printStackTrace();
    }
  }

  protected void handleGatewayResponse(BookCatalogInfo info, Book book) {
    List<String> publishers = info.getPublishers();
    if (!publishers.isEmpty()) {
      String publisherName = publishers.get(0);

      Optional<Publisher> p = publisherRepo.findByName(publisherName);
      if (p.isPresent()) {
        book.setPublisher(p.get());
      } else {
        Publisher newPublisher = new Publisher();
        newPublisher.setName(publisherName);
        newPublisher = publisherRepo.save(newPublisher);
        book.setPublisher(newPublisher);
      }
      bookRepository.update(book);
    }
  }

}
