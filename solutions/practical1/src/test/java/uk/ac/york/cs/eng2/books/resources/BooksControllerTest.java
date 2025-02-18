package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import uk.ac.york.cs.eng2.books.dto.Author;
import uk.ac.york.cs.eng2.books.dto.Book;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(rebuildContext = true)
public class BooksControllerTest {
  @Inject
  private BooksClient booksClient;

  @Inject
  private AuthorsClient authorsClient;

  @Test
  public void noBooks() {
    assertEquals(0, booksClient.getBooks().size());
  }

  @Test
  public void createThenList() {
    Book b = new Book();
    b.setId(1);
    b.setTitle("Nice Book");

    booksClient.createBook(b);
    assertEquals(1, booksClient.getBooks().size());
  }

  @Test
  public void createThenFetch() {
    Book b = new Book();
    b.setId(123);
    b.setTitle("Nice Book");

    Author a = new Author();
    a.setId(456);
    a.setFirstName("John");
    a.setLastName("Doe");
    authorsClient.createAuthor(a);
    b.setAuthor(a);

    booksClient.createBook(b);
    Book fetchedBook = booksClient.getBook(b.getId());
    assertEquals(b.getTitle(), fetchedBook.getTitle());
    assertEquals(a.getId(), fetchedBook.getAuthor().getId());
    assertEquals(a.getId(), booksClient.getAuthor(b.getId()).getId());
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
    booksClient.createBook(b);

    b.setTitle("Bad Book");
    booksClient.updateBook(b, b.getId());

    Book updatedBook = booksClient.getBook(b.getId());
    assertEquals(b.getTitle(), updatedBook.getTitle());
  }

  @Test
  public void updateOnlyAuthor() {
    Book b = new Book();
    b.setId(1);
    b.setTitle("Nice Book");

    Author oldAuthor = new Author();
    oldAuthor.setId(1);
    oldAuthor.setFirstName("John");
    oldAuthor.setLastName("Doe");
    authorsClient.createAuthor(oldAuthor);
    b.setAuthor(oldAuthor);
    booksClient.createBook(b);
    assertEquals(1, authorsClient.getBooks(oldAuthor.getId()).size());

    Author newAuthor = new Author();
    newAuthor.setId(2);
    newAuthor.setFirstName("Jane");
    newAuthor.setLastName("Smith");
    authorsClient.createAuthor(newAuthor);
    b.setAuthor(newAuthor);
    booksClient.updateBook(b, b.getId());
    assertEquals(1, authorsClient.getBooks(newAuthor.getId()).size());
    assertEquals(0, authorsClient.getBooks(oldAuthor.getId()).size());

    Book updatedBook = booksClient.getBook(b.getId());
    assertEquals(b.getTitle(), updatedBook.getTitle());
    assertEquals(b.getAuthor().getId(), updatedBook.getAuthor().getId());
  }

  @Test
  public void createThenDelete() {
    Book b = new Book();
    b.setId(1);
    b.setTitle("Nice Book");
    booksClient.createBook(b);
    booksClient.deleteBook(b.getId());

    assertEquals(0, booksClient.getBooks().size());
  }

  @Test
  public void updateMissing() {
    Book update = new Book();

    HttpResponse response = booksClient.updateBook(update, 23);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
  }

  @Test
  public void deleteMissing() {
    HttpResponse response = booksClient.deleteBook(23);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
  }
}
