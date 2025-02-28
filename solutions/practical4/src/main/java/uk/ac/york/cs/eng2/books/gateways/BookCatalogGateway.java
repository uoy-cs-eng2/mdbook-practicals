package uk.ac.york.cs.eng2.books.gateways;

import java.util.Optional;

public interface BookCatalogGateway {
  Optional<BookCatalogInfo> findByIsbn(String isbn);
}
