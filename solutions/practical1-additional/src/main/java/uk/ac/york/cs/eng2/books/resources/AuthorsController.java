package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.exceptions.HttpStatusException;
import uk.ac.york.cs.eng2.books.dto.Author;
import uk.ac.york.cs.eng2.books.dto.Book;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller("/authors")
public class AuthorsController {
  private Map<Integer, Author> authors = new HashMap<>();

  @Get
  public Collection<Author> getAuthors() {
    return authors.values();
  }

  @Post
  void createAuthor(@Body Author author) {
    authors.put(author.getId(), author);
  }

  @Get("/{id}")
  public Author getAuthor(@PathVariable Integer id) {
    return authors.get(id);
  }

  @Put("/{id}")
  public void updateAuthor(@Body Author author, @PathVariable Integer id) {
    Author mapAuthor = authors.get(id);
    if (mapAuthor == null) {
      throw new HttpStatusException(HttpStatus.NOT_FOUND, "Author not found");
    }

    mapAuthor.setFirstName(author.getFirstName());
    mapAuthor.setLastName(author.getLastName());
  }

  @Delete("/{id}")
  public void deleteAuthor(@PathVariable Integer id) {
    if (authors.containsKey(id)) {
      authors.remove(id);
    } else {
      throw new HttpStatusException(HttpStatus.NOT_FOUND, "Author not found");
    }
  }

  @Get("/{id}/books")
  public Collection<Book> getBooks(@PathVariable int id) {
    Author author = authors.get(id);
    if (author == null) {
      throw new HttpStatusException(HttpStatus.NOT_FOUND, "Author not found");
    }
    return author.getBooks();
  }

}
