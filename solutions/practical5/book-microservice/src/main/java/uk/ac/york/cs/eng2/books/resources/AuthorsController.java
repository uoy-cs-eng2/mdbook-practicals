package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.exceptions.HttpStatusException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uk.ac.york.cs.eng2.books.domain.Author;
import uk.ac.york.cs.eng2.books.domain.Book;
import uk.ac.york.cs.eng2.books.dto.AuthorCreateDTO;
import uk.ac.york.cs.eng2.books.repository.AuthorRepository;
import uk.ac.york.cs.eng2.books.repository.BookRepository;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Tag(name="authors")
@Controller(AuthorsController.PREFIX)
public class AuthorsController {
  public static final String PREFIX = "/authors";

  @Inject
  private AuthorRepository authorRepository;

  @Inject
  private BookRepository bookRepository;

  // Only this one has been done pageable, as an example of how to do paging
  @Get("/{?page}")
  public Page<Author> list(@QueryValue(defaultValue = "0") int page) {
    return authorRepository.findAll(Pageable.from(page));
  }

  @Get("/{id}")
  public Author get(@PathVariable long id) {
    return authorRepository.findById(id).orElse(null);
  }

  @Get("/{id}/books")
  public List<Book> listBooks(@PathVariable long id) {
    return bookRepository.findByAuthorsId(id);
  }

  @Post
  public HttpResponse<Void> create(@Body AuthorCreateDTO dto) {
    Author author = new Author();
    author.setName(dto.getName());
    author = authorRepository.save(author);

    return HttpResponse.created(URI.create(PREFIX + "/" + author.getId()));
  }

  @Transactional
  @Put("/{id}")
  public void update(@Body AuthorCreateDTO dto, @PathVariable long id) {
    @NonNull Optional<Author> oAuthor = authorRepository.findById(id);
    if (!oAuthor.isPresent()) {
      throw new HttpStatusException(HttpStatus.NOT_FOUND, "Author not found");
    }

    Author author = oAuthor.get();
    author.setName(dto.getName());
    authorRepository.save(author);
  }

  @Delete("/{id}")
  public void delete(@PathVariable long id) {
    if (!authorRepository.existsById(id)) {
      throw new HttpStatusException(HttpStatus.NOT_FOUND, "Author not found");
    }
    authorRepository.deleteById(id);
  }
}
