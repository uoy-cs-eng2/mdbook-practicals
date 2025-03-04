package uk.ac.york.cs.eng2.books.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.PageableRepository;
import uk.ac.york.cs.eng2.books.domain.Publisher;

import java.util.Optional;

@Repository
public interface PublisherRepository extends PageableRepository<Publisher, Long> {

  Optional<Publisher> findByBooksId(Long id);
  Optional<Publisher> findByName(String name);

}
