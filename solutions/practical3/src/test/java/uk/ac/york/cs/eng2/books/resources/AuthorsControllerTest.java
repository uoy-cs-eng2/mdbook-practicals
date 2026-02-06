package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.york.cs.eng2.books.domain.Author;
import uk.ac.york.cs.eng2.books.domain.Book;
import uk.ac.york.cs.eng2.books.dto.AuthorCreateDTO;
import uk.ac.york.cs.eng2.books.repository.AuthorRepository;
import uk.ac.york.cs.eng2.books.repository.BookRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(transactional = false)
public class AuthorsControllerTest {
  @Inject
  private AuthorsClient client;

  @Inject
  private AuthorRepository repository;

  @Inject
  private BookRepository bookRepository;

  @BeforeEach
  public void setUp() {
    bookRepository.deleteAll();
    repository.deleteAll();
  }

  @Test
  public void noAuthors() {
    assertEquals(0, client.list().getContent().size());
  }

  @Test
  public void createAuthor() {
    var dto = new AuthorCreateDTO();
    dto.setName("John Doe");
    HttpResponse<Void> createResponse = client.create(dto);
    long id = Long.parseLong(createResponse.header(HttpHeaders.LOCATION).split("/")[2]);

    var createdAuthor = client.get(id);
    assertEquals(dto.getName(), createdAuthor.getName());

    var listedAuthors = client.list().getContent();
    assertEquals(1, listedAuthors.size());
    assertEquals(id, listedAuthors.get(0).getId());
  }

  @Test
  public void updateAuthor() {
    Author author = new Author();
    author.setName("John Doe");
    author = repository.save(author);

    AuthorCreateDTO dto = new AuthorCreateDTO();
    dto.setName("Mary Sue");
    client.update(dto, author.getId());

    var updatedAuthor = client.get(author.getId());
    assertEquals(dto.getName(), updatedAuthor.getName());
  }

  @Test
  public void deleteAuthor() {
    Author author = new Author();
    author.setName("John Doe");
    author = repository.save(author);

    client.delete(author.getId());
    assertEquals(0, client.list().getContent().size());
  }

  @Test
  public void listAuthorBooks() {
    Author author = new Author();
    author.setName("John Doe");
    author = repository.save(author);

    assertEquals(0, client.listBooks(author.getId()).size());

    Book book = new Book();
    book.setTitle("Hamlet");
    book.getAuthors().add(author);
    book = bookRepository.save(book);

    List<Book> books = client.listBooks(author.getId());
    assertEquals(1, books.size());
    assertEquals(book.getId(), books.get(0).getId());
  }
}
