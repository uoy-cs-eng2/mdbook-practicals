package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uk.ac.york.cs.eng2.books.domain.Book;
import uk.ac.york.cs.eng2.books.dto.BookDTO;
import uk.ac.york.cs.eng2.books.repository.BookRepository;

import java.net.URI;
import java.util.*;

@Controller("/books")
public class BooksController {
  @Inject
  private BookRepository repository;

  @Get
  public List<Book> getBooks() {
    return repository.findAll();
  }

  @Post
  public HttpResponse<Void> createBook(@Body BookDTO dto) {
    Book book = new Book();
    book.setTitle(dto.getTitle());
    book.setAuthor(dto.getAuthor());
    book = repository.save(book);

    return HttpResponse.created(URI.create("/books/" + book.getId()));
  }

  @Get("/{id}")
  public Book getBook(@PathVariable long id) {
    return repository.findById(id).orElse(null);
  }

  @Transactional
  @Put("/{id}")
  public void updateBook(@Body BookDTO dto, @PathVariable long id) {
    @NonNull Optional<Book> oBook = repository.findById(id);
    if (oBook.isEmpty()) {
      throw new HttpStatusException(HttpStatus.NOT_FOUND, "Book not found");
    }
    Book book = oBook.get();

    if (dto.getTitle() != null) {
      book.setTitle(dto.getTitle());
    }
    if (dto.getAuthor() != null) {
      book.setAuthor(dto.getAuthor());
    }
    repository.save(book);
  }

  @Delete("/{id}")
  public void deleteBook(@PathVariable long id) {
    if (repository.existsById(id)) {
      repository.deleteById(id);
    } else {
      throw new HttpStatusException(HttpStatus.NOT_FOUND, "Book not found");
    }
  }
}
