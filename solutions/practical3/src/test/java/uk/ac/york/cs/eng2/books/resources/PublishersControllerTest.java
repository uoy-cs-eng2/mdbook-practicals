package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.york.cs.eng2.books.domain.Book;
import uk.ac.york.cs.eng2.books.domain.Publisher;
import uk.ac.york.cs.eng2.books.dto.PublisherCreateDTO;
import uk.ac.york.cs.eng2.books.repository.BookRepository;
import uk.ac.york.cs.eng2.books.repository.PublisherRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(transactional = false)
public class PublishersControllerTest {

  @Inject
  private PublishersClient client;

  @Inject
  private PublisherRepository repository;

  @Inject
  private BookRepository bookRepository;

  @BeforeEach
  public void setUp() {
    bookRepository.deleteAll();
    repository.deleteAll();
  }

  @Test
  public void listNoPublishers() {
    assertEquals(0, client.list().size());
  }

  @Test
  public void createThenList() {
    PublisherCreateDTO dto = new PublisherCreateDTO();
    dto.setName("P Ublisher");
    long id = createPublisher(dto);

    List<Publisher> publisherList = client.list();
    assertEquals(1, publisherList.size());
    Publisher publisher = publisherList.get(0);
    assertEquals("P Ublisher", publisher.getName());
    assertEquals(id, publisher.getId());
  }

  @Test
  public void createThenDelete() {
    PublisherCreateDTO dto = new PublisherCreateDTO();
    dto.setName("P Ublisher");
    long id = createPublisher(dto);

    client.delete(id);
    assertEquals(0, client.list().size());
  }

  @Test
  public void createThenUpdateAndFetch() {
    PublisherCreateDTO dto = new PublisherCreateDTO();
    dto.setName("P Ublisher");
    long id = createPublisher(dto);

    dto.setName("Something Else");
    client.update(dto, id);

    Publisher updatedPublisher = client.get(id);
    assertEquals(dto.getName(), updatedPublisher.getName());
    assertEquals(id, updatedPublisher.getId());
  }

  @Test
  public void listBooks() {
    Publisher p = new Publisher();
    p.setName("P Ublisher");
    p = repository.save(p);

    Book b1 = new Book();
    b1.setTitle("El Quijote");
    b1.setPublisher(p);
    bookRepository.save(b1);

    Book b2 = new Book();
    b2.setTitle("Hamlet");
    b2.setPublisher(p);
    bookRepository.save(b2);

    List<Book> publisherBooks = client.listBooks(p.getId());
    assertEquals(2, publisherBooks.size());
  }

  private long createPublisher(PublisherCreateDTO dto) {
    HttpResponse<Void> response = client.create(dto);
    assertEquals(HttpStatus.CREATED, response.getStatus());
    return Long.parseLong(response.header(HttpHeaders.LOCATION).split("/")[2]);
  }

}
