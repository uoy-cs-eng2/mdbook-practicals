package uk.ac.york.cs.eng2.books.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.PageableRepository;
import uk.ac.york.cs.eng2.books.domain.Book;

import java.util.List;

@Repository
public interface BookRepository extends PageableRepository<Book, Long> {

  List<Book> findByPublisherId(long publisherId);
  List<Book> findByAuthorsId(long authorsId);

}
