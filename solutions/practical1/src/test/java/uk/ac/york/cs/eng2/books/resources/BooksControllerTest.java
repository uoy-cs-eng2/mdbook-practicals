package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import uk.ac.york.cs.eng2.books.dto.Book;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(rebuildContext = true)
public class BooksControllerTest {
  @Inject
  private BooksClient booksClient;

  @Test
  public void noBooks() {
    assertEquals(0, booksClient.getBooks().size());
  }

  @Test
  public void createThenList() {
    Book b = new Book();
    b.setId(1);
    b.setTitle("Nice Book");
    b.setAuthor("John Doe");

    booksClient.createBook(b);
    assertEquals(1, booksClient.getBooks().size());
  }

  @Test
  public void createThenFetch() {
    Book b = new Book();
    b.setId(1);
    b.setTitle("Nice Book");
    b.setAuthor("John Doe");

    booksClient.createBook(b);
    Book fetchedBook = booksClient.getBook(b.getId());
    assertEquals(b.getTitle(), fetchedBook.getTitle());
  }

  @Test
  public void fetchMissingBook() {
    assertNull(booksClient.getBook(123));
  }

  @Test
  public void updateOnlyTitle() {
    Book b = new Book();
    b.setId(1);
    b.setTitle("Nice Book");
    b.setAuthor("John Doe");
    booksClient.createBook(b);

    Book update = new Book();
    update.setTitle("Bad Book");
    booksClient.updateBook(update, b.getId());

    Book updatedBook = booksClient.getBook(b.getId());
    assertEquals(update.getTitle(), updatedBook.getTitle());
    assertEquals(b.getAuthor(), updatedBook.getAuthor());
  }

  @Test
  public void updateOnlyAuthor() {
    Book b = new Book();
    b.setId(1);
    b.setTitle("Nice Book");
    b.setAuthor("John Doe");
    booksClient.createBook(b);

    Book update = new Book();
    update.setAuthor("Jane Smith");
    booksClient.updateBook(update, b.getId());

    Book updatedBook = booksClient.getBook(b.getId());
    assertEquals(b.getTitle(), updatedBook.getTitle());
    assertEquals(update.getAuthor(), updatedBook.getAuthor());
  }

  @Test
  public void createThenDelete() {
    Book b = new Book();
    b.setId(1);
    b.setTitle("Nice Book");
    b.setAuthor("John Doe");
    booksClient.createBook(b);
    booksClient.deleteBook(b.getId());

    assertEquals(0, booksClient.getBooks().size());
  }

  @Test
  public void updateMissing() {
    Book update = new Book();
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
