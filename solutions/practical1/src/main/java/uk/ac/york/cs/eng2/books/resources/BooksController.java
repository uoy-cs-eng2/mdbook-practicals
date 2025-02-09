package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.exceptions.HttpStatusException;
import uk.ac.york.cs.eng2.books.dto.Book;

import java.util.*;

@Controller("/books")
public class BooksController {
  private Map<Integer, Book> books = new HashMap<>();

  @Get
  public List<Book> getBooks() {
    return new ArrayList<>(books.values());
  }

  @Post
  public void createBook(@Body Book book) {
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

    if (book.getTitle() != null) {
      mapBook.setTitle(book.getTitle());
    }
    if (book.getAuthor() != null) {
      mapBook.setAuthor(book.getAuthor());
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
}
