package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.york.cs.eng2.books.domain.Publisher;
import uk.ac.york.cs.eng2.books.dto.PublisherCreateDTO;
import uk.ac.york.cs.eng2.books.repository.PublisherRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(transactional = false)
public class PublishersControllerTest {

  @Inject
  private PublishersClient client;

  @Inject
  private PublisherRepository repository;

  @BeforeEach
  public void setUp() {
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

  private long createPublisher(PublisherCreateDTO dto) {
    HttpResponse<Void> response = client.create(dto);
    assertEquals(HttpStatus.CREATED, response.getStatus());
    long id = Long.valueOf(response.header(HttpHeaders.LOCATION).split("/")[2]);
    return id;
  }

}
