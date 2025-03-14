package uk.ac.york.cs.eng2.book.e2e;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import org.junit.jupiter.api.Test;
import uk.ac.york.cs.eng2.books.api.AuthorsApi;
import uk.ac.york.cs.eng2.books.api.BooksApi;
import uk.ac.york.cs.eng2.books.model.AuthorCreateDTO;
import uk.ac.york.cs.eng2.books.model.BookCreateDTO;
import uk.ac.york.cs.eng2.books.model.Publisher;

import java.time.Duration;
import java.util.concurrent.Callable;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class BookWorkflowTest {
  @Inject private BooksApi booksApi;
  @Inject private AuthorsApi authorsApi;

  @Test
  public void createBookWithIsbn() {
    BookCreateDTO dto = new BookCreateDTO();
    dto.setTitle("Minecraft bestiary");
    dto.setIsbn("1524797162");

    // Create the book and obtain the ID from the response header
    HttpResponse<Void> bookCreatedResponse = booksApi.createBook(dto);
    assertEquals(HttpStatus.CREATED, bookCreatedResponse.getStatus());
    long id = Long.parseLong(bookCreatedResponse.header(HttpHeaders.LOCATION).split("/")[2]);

    // Create an author and associate it with the book
    AuthorCreateDTO authorDto = new AuthorCreateDTO();
    authorDto.setName("Alex Wiltshire");
    HttpResponse<Void> authorCreatedResponse = authorsApi.create(authorDto);
    assertEquals(HttpStatus.CREATED, authorCreatedResponse.getStatus());
    long authorId = Long.parseLong(authorCreatedResponse.header(HttpHeaders.LOCATION).split("/")[2]);

    // Associate the book with the author
    HttpResponse<Void> addBookAuthorResponse = booksApi.addBookAuthor(id, authorId);
    assertEquals(HttpStatus.OK, addBookAuthorResponse.getStatus());

    // Check that the Kafka consumer has obtained the publisher from OpenLibrary
    await().atMost(Duration.ofSeconds(20)).until(publisherBecomes(id, "Del Rey"));
  }

  protected Callable<Boolean> publisherBecomes(long bookId, String name) {
    return () -> {
      HttpResponse<@Valid Publisher> bookPublisherResponse = booksApi.getBookPublisher(bookId);
      if (bookPublisherResponse.getBody().isPresent()) {
        return name.equals(bookPublisherResponse.getBody().get().getName());
      }
      return false;
    };
  }

}
