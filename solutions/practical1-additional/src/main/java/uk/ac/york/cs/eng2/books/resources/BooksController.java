package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Inject;
import uk.ac.york.cs.eng2.books.dto.Author;
import uk.ac.york.cs.eng2.books.dto.Book;

import java.util.*;

@Controller("/books")
public class BooksController {

  @Inject
  private AuthorsController authorsController;

  private Map<Integer, Book> books = new HashMap<>();

  @Get
  public Collection<Book> getBooks() {
    return books.values();
  }

  @Post
  public void createBook(@Body Book book) {
    if (book.getAuthor() != null) {
      Author author = authorsController.getAuthor(book.getAuthor().getId());
      if (author == null) {
        throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Author not found");
      }
      book.setAuthor(author);
      author.getBooks().add(book);
    }
    books.put(book.getId(), book);
  }

  @Get("/{id}")
  public Book getBook(@PathVariable int id) {
    return books.get(id);
  }

  @Put("/{id}")
  public void updateBook(@Body Book book, @PathVariable int id) {
    Book mapBook = books.get(id);
    if (mapBook == null) {
      throw new HttpStatusException(HttpStatus.NOT_FOUND, "Book not found");
    }

    mapBook.setTitle(book.getTitle());
    if (book.getAuthor() != null) {
      Integer authorId = book.getAuthor().getId();
      if (authorId != null) {
        Author author = authorsController.getAuthor(authorId);
        if (author == null) {
          throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Author not found");
        }

        if (mapBook.getAuthor() != null) {
          mapBook.getAuthor().getBooks().remove(mapBook);
        }
        mapBook.setAuthor(author);
        mapBook.getAuthor().getBooks().add(mapBook);
      }
    }
  }

  @Delete("/{id}")
  public void deleteBook(@PathVariable int id) {
    if (books.containsKey(id)) {
      books.remove(id);
    } else {
      throw new HttpStatusException(HttpStatus.NOT_FOUND, "Book not found");
    }
  }

  @Get("/{id}/author")
  public Author getAuthor(@PathVariable int id) {
    return books.get(id).getAuthor();
  }
}
