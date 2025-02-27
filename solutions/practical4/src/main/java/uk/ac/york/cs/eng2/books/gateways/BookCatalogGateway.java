package uk.ac.york.cs.eng2.books.gateways;

public interface BookCatalogGateway {
  BookCatalogInfo findByIsbn(String isbn);
}
