package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uk.ac.york.cs.eng2.books.domain.Author;
import uk.ac.york.cs.eng2.books.domain.Book;
import uk.ac.york.cs.eng2.books.domain.Publisher;
import uk.ac.york.cs.eng2.books.dto.BookCreateDTO;
import uk.ac.york.cs.eng2.books.events.BookIsbnChange;
import uk.ac.york.cs.eng2.books.events.BooksProducer;
import uk.ac.york.cs.eng2.books.repository.AuthorRepository;
import uk.ac.york.cs.eng2.books.repository.BookRepository;
import uk.ac.york.cs.eng2.books.repository.PublisherRepository;

import java.net.URI;
import java.util.*;

@ExecuteOn(TaskExecutors.BLOCKING)
@Tag(name="books")
@Controller("/books")
public class BooksController {
  @Inject
  private BookRepository repository;

  @Inject
  private PublisherRepository publisherRepository;

  @Inject
  private AuthorRepository authorRepository;

  @Inject
  private BooksProducer booksProducer;

  @Get
  public List<Book> getBooks() {
    return repository.findAll();
  }

  @Post
  public HttpResponse<Void> createBook(@Body BookCreateDTO dto) {
    Book book = new Book();
    book.setTitle(dto.getTitle());
    book.setIsbn(dto.getIsbn());
    updatePublisher(dto, book);

    book = repository.save(book);
    booksProducer.isbnChanged(book.getId(), new BookIsbnChange(null, dto.getIsbn()));
    return HttpResponse.created(URI.create("/books/" + book.getId()));
  }

  protected void updatePublisher(BookCreateDTO dto, Book book) {
    if (dto.getPublisherId() != null) {
      @NonNull Optional<Publisher> publisher = publisherRepository.findById(dto.getPublisherId());
      if (publisher.isEmpty()) {
        throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Publisher not found");
      }
      book.setPublisher(publisher.get());
    } else {
      book.setPublisher(null);
    }
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

  @Get("/{id}/authors")
  public List<Author> getBookAuthors(@PathVariable long id) {
    return authorRepository.findByBooksId(id);
  }

  @Transactional
  @Put("/{id}/authors/{authorId}")
  public void addBookAuthor(@PathVariable long id, @PathVariable long authorId) {
    BookAuthor result = getBookAuthor(id, authorId);
    result.book.getAuthors().add(result.author);
    repository.save(result.book);
  }

  @Transactional
  @Delete("/{id}/authors/{authorId}")
  public void removeBookAuthor(@PathVariable long id, @PathVariable long authorId) {
    BookAuthor result = getBookAuthor(id, authorId);
    result.book.getAuthors().remove(result.author);
    repository.save(result.book());
  }

  private record BookAuthor(Author author, Book book) {}

  private BookAuthor getBookAuthor(long id, long authorId) {
    @NonNull Optional<Author> oAuthor = authorRepository.findById(authorId);
    if (oAuthor.isEmpty()) {
      throw new HttpStatusException(HttpStatus.NOT_FOUND, "Author not found");
    }
    Author author = oAuthor.get();

    @NonNull Optional<Book> oBook = repository.findById(id);
    if (oBook.isEmpty()) {
      throw new HttpStatusException(HttpStatus.NOT_FOUND, "Book not found");
    }
    Book book = oBook.get();
    return new BookAuthor(author, book);
  }

  @Transactional
  @Put("/{id}")
  public void updateBook(@Body BookCreateDTO dto, @PathVariable long id) {
    @NonNull Optional<Book> oBook = repository.findById(id);
    if (oBook.isEmpty()) {
      throw new HttpStatusException(HttpStatus.NOT_FOUND, "Book not found");
    }
    Book book = oBook.get();

    book.setTitle(dto.getTitle());
    String oldIsbn = book.getIsbn();
    book.setIsbn(dto.getIsbn());

    updatePublisher(dto, book);
    repository.save(book);
    if (!Objects.equals(oldIsbn, dto.getIsbn())) {
      booksProducer.isbnChanged(book.getId(), new BookIsbnChange(oldIsbn, dto.getIsbn()));
    }
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
