package uk.ac.york.cs.eng2.books.events;

import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.york.cs.eng2.books.domain.Book;
import uk.ac.york.cs.eng2.books.domain.Publisher;
import uk.ac.york.cs.eng2.books.gateways.BookCatalogGateway;
import uk.ac.york.cs.eng2.books.gateways.BookCatalogInfo;
import uk.ac.york.cs.eng2.books.repository.BookRepository;
import uk.ac.york.cs.eng2.books.repository.PublisherRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@MicronautTest(transactional = false)
public class OpenLibraryConsumerTest {

  private static final String ISBN_PUBLISHER_A = "1234567890";
  private static final String PUBLISHER_A = "Publisher A";

  private static final String ISBN_CRASHING = "FAIL";
  private static final String ISBN_NO_RESULT = "NORES";
  private static final String ISBN_NO_PUBLISHER = "NOPUB";

  @Inject
  private OpenLibraryConsumer consumer;

  @Inject
  private BookRepository repository;

  @Inject
  private PublisherRepository publisherRepository;

  @Inject
  private BookCatalogGateway gateway;

  @BeforeEach
  public void setup() {
    repository.deleteAll();
    publisherRepository.deleteAll();
  }

  @Test
  public void bookDoesNotExist() {
    /*
     * Smoke test checking that we simply ignore the event
     * if it refers to a book that doesn't exist.
     */
    consumer.isbnChanged(1234, null);
  }

  @Test
  public void bookHasPublisherAlready() {
    Publisher publisher = new Publisher();
    publisher.setName("P Ublisher");
    publisher = publisherRepository.save(publisher);

    Book b = new Book();
    b.setTitle("My Book");
    b.setIsbn("404");
    b.setPublisher(publisher);
    b = repository.save(b);

    consumer.isbnChanged(b.getId(),
        new BookIsbnChange(b.getIsbn(), ISBN_PUBLISHER_A));

    b = repository.findById(b.getId()).get();
    assertEquals(publisher.getId(), b.getPublisher().getId(),
        "The original publisher should remain");

    // We can use Mockito to also check that a given interaction
    // *never* took place. In this case, if the book already had
    // a publisher, it shouldn't even ask the gateway.
    verify(gateway, never())
        .findByIsbn(eq(ISBN_PUBLISHER_A));
  }

  @Test
  public void gatewayCrashes() {
    assertBadIsbnIsProcessedWithoutErrors(ISBN_CRASHING);
  }

  @Test
  public void gatewayNoResult() {
    assertBadIsbnIsProcessedWithoutErrors(ISBN_NO_RESULT);
  }

  @Test
  public void gatewayNoPublishers() {
    assertBadIsbnIsProcessedWithoutErrors(ISBN_NO_PUBLISHER);
  }

  @Test
  public void publisherDoesNotExist() {
    Book b = new Book();
    b.setTitle("My Book");
    b.setIsbn(ISBN_PUBLISHER_A);
    b = repository.save(b);

    consumer.isbnChanged(b.getId(),
        new BookIsbnChange(null, ISBN_PUBLISHER_A));

    b = repository.findById(b.getId()).get();
    assertEquals(PUBLISHER_A, b.getPublisher().getName());
  }

  @Test
  public void publisherAlreadyExists() {
    Publisher publisher = new Publisher();
    publisher.setName(PUBLISHER_A);
    publisher = publisherRepository.save(publisher);

    Book b = new Book();
    b.setTitle("My Book");
    b.setIsbn(ISBN_PUBLISHER_A);
    b = repository.save(b);

    consumer.isbnChanged(b.getId(),
        new BookIsbnChange(null, ISBN_PUBLISHER_A));

    b = repository.findById(b.getId()).get();
    assertEquals(publisher.getId(), b.getPublisher().getId());
    assertEquals(1, repository.count(),
        "The existing publisher should have been reused");
  }

  private void assertBadIsbnIsProcessedWithoutErrors(String isbn) {
    Book b = new Book();
    b.setTitle("My Book");
    b.setIsbn(isbn);
    b = repository.save(b);

    consumer.isbnChanged(b.getId(),
        new BookIsbnChange(null, isbn));

    b = repository.findById(b.getId()).get();
    assertNull(b.getPublisher());
  }

  @MockBean(BookCatalogGateway.class)
  public BookCatalogGateway getGateway() {
    BookCatalogInfo infoPubA = new BookCatalogInfo();
    infoPubA.getPublishers().add(PUBLISHER_A);

    /*
     * We'll set up a few ISBNs with specific behaviour.
     * Some will have predefined responses, and some will
     * throw exceptions.
     */
    BookCatalogGateway mock = mock(BookCatalogGateway.class);
    when(mock.findByIsbn(eq(ISBN_PUBLISHER_A)))
        .thenReturn(Optional.of(infoPubA));
    when(mock.findByIsbn(eq(ISBN_CRASHING)))
        .thenThrow(RuntimeException.class);
    when(mock.findByIsbn(eq(ISBN_NO_RESULT)))
        .thenReturn(Optional.empty());
    when(mock.findByIsbn(eq(ISBN_NO_PUBLISHER)))
        .thenReturn(Optional.of(new BookCatalogInfo()));

    return mock;
  }
}
