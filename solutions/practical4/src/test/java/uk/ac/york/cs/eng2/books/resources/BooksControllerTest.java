package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.york.cs.eng2.books.domain.Author;
import uk.ac.york.cs.eng2.books.domain.Book;
import uk.ac.york.cs.eng2.books.domain.Publisher;
import uk.ac.york.cs.eng2.books.dto.BookCreateDTO;
import uk.ac.york.cs.eng2.books.repository.AuthorRepository;
import uk.ac.york.cs.eng2.books.repository.BookRepository;
import uk.ac.york.cs.eng2.books.repository.PublisherRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false)
public class BooksControllerTest {
  @Inject
  private BooksClient booksClient;

  @Inject
  private BookRepository bookRepository;

  @Inject
  private PublisherRepository publisherRepository;

  @Inject
  private AuthorRepository authorRepository;

  @BeforeEach
  public void setup() {
    bookRepository.deleteAll();
    authorRepository.deleteAll();
    publisherRepository.deleteAll();
  }

  @Test
  public void noBooks() {
    assertEquals(0, booksClient.getBooks().size());
  }

  @Test
  public void createThenList() {
    BookCreateDTO b = new BookCreateDTO();
    b.setTitle("Nice Book");

    booksClient.createBook(b);
    assertEquals(1, booksClient.getBooks().size());
  }

  @Test
  public void createThenFetch() {
    BookCreateDTO b = new BookCreateDTO();
    b.setTitle("Nice Book");

    Long bookId = createBook(b);
    Book fetchedBook = booksClient.getBook(bookId);
    assertEquals(b.getTitle(), fetchedBook.getTitle());
  }

  @Test
  public void createWithMissingPublisher() {
    BookCreateDTO b = new BookCreateDTO();
    b.setTitle("Nice Book");
    b.setPublisherId(1234L);
    try {
      booksClient.createBook(b);
      fail("An exception was expected");
    } catch (HttpClientResponseException ex) {
      assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }
  }

  @Test
  public void createWithPublisher() {
    Publisher p = new Publisher();
    p.setName("P Ublisher");
    p = publisherRepository.save(p);

    BookCreateDTO b = new BookCreateDTO();
    b.setTitle("Nice Book");
    b.setPublisherId(p.getId());
    Long bookId = createBook(b);

    HttpResponse<Publisher> bookPublisherResponse = booksClient.getBookPublisher(bookId);
    Publisher bookPublisher = bookPublisherResponse.getBody().get();
    assertEquals(p.getName(), bookPublisher.getName());
    assertEquals(p.getId(), bookPublisher.getId());
  }

  @Test
  public void fetchMissingBook() {
    assertNull(booksClient.getBook(123));
  }

  @Test
  public void updateOnlyTitle() {
    BookCreateDTO b = new BookCreateDTO();
    b.setTitle("Nice Book");
    Long bookId = createBook(b);

    b.setTitle("Bad Book");
    booksClient.updateBook(b, bookId);

    Book updatedBook = booksClient.getBook(bookId);
    assertEquals(b.getTitle(), updatedBook.getTitle());
  }

  @Test
  public void updateOnlyPublisher() {
    BookCreateDTO b = new BookCreateDTO();
    b.setTitle("Nice Book");
    Long bookId = createBook(b);

    Publisher p = new Publisher();
    p.setName("P Ublisher");
    p = publisherRepository.save(p);

    b.setPublisherId(p.getId());
    booksClient.updateBook(b, bookId);

    HttpResponse<Publisher> bookPublisherResponse = booksClient.getBookPublisher(bookId);
    Publisher bookPublisher = bookPublisherResponse.getBody().get();
    assertEquals(p.getId(), bookPublisher.getId());
  }

  @Test
  public void updateUnsetPublisher() {
    Publisher p = new Publisher();
    p.setName("P Ublisher");
    p = publisherRepository.save(p);

    BookCreateDTO b = new BookCreateDTO();
    b.setTitle("Nice Book");
    b.setPublisherId(p.getId());
    Long bookId = createBook(b);

    b.setPublisherId(null);
    booksClient.updateBook(b, bookId);

    HttpResponse<Publisher> response = booksClient.getBookPublisher(bookId);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    assertTrue(response.getBody().isEmpty());
  }

  private Long createBook(BookCreateDTO b) {
    HttpResponse<Void> createResponse = booksClient.createBook(b);
    Long bookId = Long.valueOf(createResponse.header(HttpHeaders.LOCATION).split("/")[2]);
    return bookId;
  }

  @Test
  public void createThenDelete() {
    BookCreateDTO b = new BookCreateDTO();
    b.setTitle("Nice Book");
    Long bookId = createBook(b);
    booksClient.deleteBook(bookId);

    assertEquals(0, booksClient.getBooks().size());
  }

  @Test
  public void updateMissing() {
    BookCreateDTO update = new BookCreateDTO();
    update.setTitle("Nice Book");

    HttpResponse response = booksClient.updateBook(update, 23);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
  }

  @Test
  public void deleteMissing() {
    HttpResponse response = booksClient.deleteBook(23);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
  }

  @Test
  public void listAuthors() {
    BookCreateDTO b = new BookCreateDTO();
    b.setTitle("Nice Book");
    Long bookId = createBook(b);

    assertEquals(0, booksClient.getBookAuthors(bookId).size());

    Author author = new Author();
    author.setName("Jane Smith");
    author = authorRepository.save(author);

    booksClient.addBookAuthor(bookId, author.getId());
    List<Author> bookAuthors = booksClient.getBookAuthors(bookId);
    assertEquals(1, bookAuthors.size());
    assertEquals(author.getId(), bookAuthors.get(0).getId());

    booksClient.removeBookAuthor(bookId, author.getId());
    assertEquals(0, booksClient.getBookAuthors(bookId).size());
  }
}
