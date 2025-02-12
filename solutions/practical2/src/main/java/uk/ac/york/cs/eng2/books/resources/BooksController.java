package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uk.ac.york.cs.eng2.books.domain.Book;
import uk.ac.york.cs.eng2.books.domain.Publisher;
import uk.ac.york.cs.eng2.books.dto.BookDTO;
import uk.ac.york.cs.eng2.books.repository.BookRepository;
import uk.ac.york.cs.eng2.books.repository.PublisherRepository;

import java.net.URI;
import java.util.*;

@Controller("/books")
public class BooksController {
  @Inject
  private BookRepository repository;

  @Inject
  private PublisherRepository publisherRepository;

  @Get
  public List<Book> getBooks() {
    return repository.findAll();
  }

  @Post
  public HttpResponse<Void> createBook(@Body BookDTO dto) {
    Book book = new Book();
    book.setTitle(dto.getTitle());
    book.setAuthor(dto.getAuthor());

    if (dto.getPublisherId() != null) {
      @NonNull Optional<Publisher> publisher = publisherRepository.findById(dto.getPublisherId());
      if (!publisher.isPresent()) {
        throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Publisher not found");
      }
      book.setPublisher(publisher.get());
    }

    book = repository.save(book);
    return HttpResponse.created(URI.create("/books/" + book.getId()));
  }

  @Transactional
  @Get("/{id}")
  public Book getBook(@PathVariable long id) {
    return repository.findById(id).orElse(null);
  }

  @Get("/{id}/publisher")
  public Publisher getBookPublisher(@PathVariable long id) {
    return publisherRepository.findByBooksId(id).orElse(null);
  }

  @Transactional
  @Put("/{id}")
  public void updateBook(@Body BookDTO dto, @PathVariable long id) {
    @NonNull Optional<Book> oBook = repository.findById(id);
    if (oBook.isEmpty()) {
      throw new HttpStatusException(HttpStatus.NOT_FOUND, "Book not found");
    }
    Book book = oBook.get();

    book.setTitle(dto.getTitle());
    book.setAuthor(dto.getAuthor());

    if (dto.getPublisherId() != null) {
      @NonNull Optional<Publisher> publisher = publisherRepository.findById(dto.getPublisherId());
      if (!publisher.isPresent()) {
        throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Publisher not found");
      }
      book.setPublisher(publisher.get());
    } else {
      book.setPublisher(null);
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
