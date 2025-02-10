package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.york.cs.eng2.books.dto.BookDTO;
import uk.ac.york.cs.eng2.books.repository.BookRepository;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false)
public class BooksControllerTest {
  @Inject
  private BooksClient booksClient;

  @Inject
  private BookRepository bookRepository;

  @BeforeEach
  public void setup() {
    bookRepository.deleteAll();
  }

  @Test
  public void noBooks() {
    assertEquals(0, booksClient.getBooks().size());
  }

  @Test
  public void createThenList() {
    BookDTO b = new BookDTO();
    b.setId(1);
    b.setTitle("Nice Book");
    b.setAuthor("John Doe");

    booksClient.createBook(b);
    assertEquals(1, booksClient.getBooks().size());
  }

  @Test
  public void createThenFetch() {
    BookDTO b = new BookDTO();
    b.setTitle("Nice Book");
    b.setAuthor("John Doe");

    Long bookId = createBook(b);
    BookDTO fetchedBook = booksClient.getBook(bookId);
    assertEquals(b.getTitle(), fetchedBook.getTitle());
  }

  @Test
  public void fetchMissingBook() {
    assertNull(booksClient.getBook(123));
  }

  @Test
  public void updateOnlyTitle() {
    BookDTO b = new BookDTO();
    b.setTitle("Nice Book");
    b.setAuthor("John Doe");
    Long bookId = createBook(b);

    BookDTO update = new BookDTO();
    update.setTitle("Bad Book");
    booksClient.updateBook(update, bookId);

    BookDTO updatedBook = booksClient.getBook(bookId);
    assertEquals(update.getTitle(), updatedBook.getTitle());
    assertEquals(b.getAuthor(), updatedBook.getAuthor());
  }

  private Long createBook(BookDTO b) {
    HttpResponse<Void> createResponse = booksClient.createBook(b);
    Long bookId = Long.valueOf(createResponse.header(HttpHeaders.LOCATION).split("/")[2]);
    return bookId;
  }

  @Test
  public void updateOnlyAuthor() {
    BookDTO b = new BookDTO();
    b.setTitle("Nice Book");
    b.setAuthor("John Doe");
    Long bookId = createBook(b);

    BookDTO update = new BookDTO();
    update.setAuthor("Jane Smith");
    booksClient.updateBook(update, bookId);

    BookDTO updatedBook = booksClient.getBook(bookId);
    assertEquals(b.getTitle(), updatedBook.getTitle());
    assertEquals(update.getAuthor(), updatedBook.getAuthor());
  }

  @Test
  public void createThenDelete() {
    BookDTO b = new BookDTO();
    b.setTitle("Nice Book");
    b.setAuthor("John Doe");
    Long bookId = createBook(b);
    booksClient.deleteBook(bookId);

    assertEquals(0, booksClient.getBooks().size());
  }

  @Test
  public void updateMissing() {
    BookDTO update = new BookDTO();
    update.setAuthor("Jane Smith");

    HttpResponse response = booksClient.updateBook(update, 23);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
  }

  @Test
  public void deleteMissing() {
    HttpResponse response = booksClient.deleteBook(23);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
  }
}
