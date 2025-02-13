package uk.ac.york.cs.eng2.books.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.PageableRepository;
import uk.ac.york.cs.eng2.books.domain.Author;

@Repository
public interface AuthorRepository extends PageableRepository<Author, Long> {
}
