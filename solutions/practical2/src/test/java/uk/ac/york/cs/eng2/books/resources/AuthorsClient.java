package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import uk.ac.york.cs.eng2.books.domain.Author;
import uk.ac.york.cs.eng2.books.dto.AuthorCreateDTO;

import java.util.List;

@Client(AuthorsController.PREFIX)
public interface AuthorsClient {
  @Get
  List<Author> list();

  @Get("/{id}")
  Author get(@PathVariable long id);

  @Post
  HttpResponse<Void> create(@Body AuthorCreateDTO dto);

  @Put("/{id}")
  void update(@Body AuthorCreateDTO dto, @PathVariable long id);

  @Delete("/{id}")
  void delete(@PathVariable long id);
}
