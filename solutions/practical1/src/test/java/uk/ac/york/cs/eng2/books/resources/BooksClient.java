package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import uk.ac.york.cs.eng2.books.dto.Book;

import java.util.List;

@Client("/books")
public interface BooksClient {
  @Get
  List<Book> getBooks();

  @Post
  void createBook(@Body Book book);

  @Get("/{id}")
  Book getBook(@PathVariable int id);

  @Put("/{id}")
  HttpResponse updateBook(@Body Book book, @PathVariable int id);

  @Delete("/{id}")
  HttpResponse deleteBook(@PathVariable int id);
}
