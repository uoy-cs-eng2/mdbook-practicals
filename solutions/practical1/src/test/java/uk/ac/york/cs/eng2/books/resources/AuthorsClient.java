package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.exceptions.HttpStatusException;
import uk.ac.york.cs.eng2.books.dto.Author;
import uk.ac.york.cs.eng2.books.dto.Book;

import java.util.Collection;

@Client("/authors")
public interface AuthorsClient {
  @Get
  Collection<Author> getAuthors();

  @Post
  void createAuthor(@Body Author author);

  @Get("/{id}")
  Author getAuthor(@PathVariable("id") Integer id);

  @Put("/{id}")
  void updateAuthor(@Body Author author, @PathVariable("id") Integer id);

  @Delete("/{id}")
  void deleteAuthor(@PathVariable("id") Integer id);

  @Get("/{id}/books")
  Collection<Book> getBooks(@PathVariable int id);
}