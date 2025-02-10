package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import uk.ac.york.cs.eng2.books.dto.BookDTO;

import java.util.List;

@Client("/books")
public interface BooksClient {
  @Get
  List<BookDTO> getBooks();

  @Post
  HttpResponse<Void> createBook(@Body BookDTO book);

  @Get("/{id}")
  BookDTO getBook(@PathVariable long id);

  @Put("/{id}")
  HttpResponse updateBook(@Body BookDTO book, @PathVariable long id);

  @Delete("/{id}")
  HttpResponse deleteBook(@PathVariable long id);
}
