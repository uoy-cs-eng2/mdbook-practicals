package uk.ac.york.cs.eng2.books.gateways;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import uk.ac.york.cs.eng2.books.openlibrary.api.SearchApi;
import uk.ac.york.cs.eng2.books.openlibrary.model.SearchResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class OpenLibraryBookCatalogGateway implements BookCatalogGateway {

  @Inject
  private SearchApi searchApi;

  @Override
  public Optional<BookCatalogInfo> findByIsbn(String isbn) {
    try {
      SearchResponse response = new OpenLibrarySearch(searchApi)
          .isbn(isbn)
          .fields("key", "editions", "editions.publisher")
          .limit(1)
          .execute();

      /*
       * Each result in "docs" is a work, and its "editions" property holds
       * the editions that matched the ISBN, e.g.:
       *
       * {"key": "...", "editions": {"docs": [{"publisher": ["Del Rey"]}]}}
       */
      for (Map<String, Object> work : response.getDocs()) {
        if (work.get("editions") instanceof Map<?, ?> editions
            && editions.get("docs") instanceof List<?> editionDocs) {
          for (Object edition : editionDocs) {
            if (edition instanceof Map<?, ?> editionMap
                && editionMap.get("publisher") instanceof List<?> publishers) {
              BookCatalogInfo info = new BookCatalogInfo();
              for (Object publisher : publishers) {
                info.getPublishers().add(publisher.toString());
              }
              return Optional.of(info);
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return Optional.empty();
  }

}
