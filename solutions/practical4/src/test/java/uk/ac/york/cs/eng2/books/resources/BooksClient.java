package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.data.model.Page;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import uk.ac.york.cs.eng2.books.domain.Author;
import uk.ac.york.cs.eng2.books.domain.Book;
import uk.ac.york.cs.eng2.books.domain.Publisher;
import uk.ac.york.cs.eng2.books.dto.BookCreateDTO;

import java.util.List;

@Client("/books")
public interface BooksClient {
  @Get
  List<Book> getBooks();

  @Post
  HttpResponse<Void> createBook(@Body BookCreateDTO book);

  @Get("/{id}")
  Book getBook(@PathVariable long id);

  @Get("/{id}/publisher")
  HttpResponse<Publisher> getBookPublisher(@PathVariable long id);

  @Put("/{id}")
  HttpResponse updateBook(@Body BookCreateDTO book, @PathVariable long id);

  @Delete("/{id}")
  HttpResponse deleteBook(@PathVariable long id);

  @Get("/{id}/authors")
  List<Author> getBookAuthors(@PathVariable long id);

  @Put("/{id}/authors/{authorId}")
  void addBookAuthor(@PathVariable long id, @PathVariable long authorId);

  @Delete("/{id}/authors/{authorId}")
  void removeBookAuthor(@PathVariable long id, @PathVariable long authorId);
}
