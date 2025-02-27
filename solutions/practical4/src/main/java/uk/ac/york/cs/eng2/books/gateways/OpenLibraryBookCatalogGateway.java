package uk.ac.york.cs.eng2.books.gateways;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import uk.ac.york.cs.eng2.books.openlibrary.api.BooksApi;

import java.util.List;
import java.util.Map;

@Singleton
public class OpenLibraryBookCatalogGateway implements BookCatalogGateway {

  @Inject
  private BooksApi booksApi;

  @Override
  public BookCatalogInfo findByIsbn(String isbn) {
    Object response = booksApi.readIsbnIsbnIsbnGet(isbn);

    BookCatalogInfo info = new BookCatalogInfo();
    if (response instanceof Map map) {
      Map<String, Object> mapResponse = (Map<String, Object>) response;
      List<String> publishers = (List<String>) mapResponse.get("publishers");
      info.getPublishers().addAll(publishers);
    }

    return info;
  }

}
