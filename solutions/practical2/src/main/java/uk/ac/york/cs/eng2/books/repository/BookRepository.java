package uk.ac.york.cs.eng2.books.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.PageableRepository;
import uk.ac.york.cs.eng2.books.domain.Book;
import uk.ac.york.cs.eng2.books.dto.BookDTO;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends PageableRepository<Book, Long> {

  List<Book> findByPublisherId(long publisherId);

}
