package uk.ac.york.cs.eng2.books.gateways;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import uk.ac.york.cs.eng2.books.openlibrary.api.BooksApi;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class OpenLibraryBookCatalogGateway implements BookCatalogGateway {

  @Inject
  private BooksApi booksApi;

  @Override
  public Optional<BookCatalogInfo> findByIsbn(String isbn) {
    try {
      Object response = booksApi.readIsbnIsbnIsbnGet(isbn);
      if (response instanceof Map map) {
        BookCatalogInfo info = new BookCatalogInfo();
        Map<String, Object> mapResponse = (Map<String, Object>) response;
        List<String> publishers = (List<String>) mapResponse.get("publishers");
        info.getPublishers().addAll(publishers);
        return Optional.of(info);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return Optional.empty();
  }

}
