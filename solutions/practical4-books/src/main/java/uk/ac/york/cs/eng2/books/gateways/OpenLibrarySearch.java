package uk.ac.york.cs.eng2.books.gateways;

import uk.ac.york.cs.eng2.books.openlibrary.api.SearchApi;
import uk.ac.york.cs.eng2.books.openlibrary.model.SearchResponse;

import java.util.List;

/**
 * Fluent builder around the generated {@link SearchApi#searchJsonSearchJsonGet} method,
 * which takes over 30 positional arguments. Only the options that have been set are
 * sent to OpenLibrary: the rest are passed as <code>null</code>, which Micronaut omits
 * from the request.
 */
public class OpenLibrarySearch {

  private final SearchApi searchApi;

  private String q;
  private String title;
  private String author;
  private String publisher;
  private String isbn;
  private List<String> fields;
  private Integer limit;
  private Integer page;

  public OpenLibrarySearch(SearchApi searchApi) {
    this.searchApi = searchApi;
  }

  public OpenLibrarySearch q(String q) {
    this.q = q;
    return this;
  }

  public OpenLibrarySearch title(String title) {
    this.title = title;
    return this;
  }

  public OpenLibrarySearch author(String author) {
    this.author = author;
    return this;
  }

  public OpenLibrarySearch publisher(String publisher) {
    this.publisher = publisher;
    return this;
  }

  public OpenLibrarySearch isbn(String isbn) {
    this.isbn = isbn;
    return this;
  }

  public OpenLibrarySearch fields(String... fields) {
    this.fields = List.of(fields);
    return this;
  }

  public OpenLibrarySearch limit(int limit) {
    this.limit = limit;
    return this;
  }

  public OpenLibrarySearch page(int page) {
    this.page = page;
    return this;
  }

  public SearchResponse execute() {
    /*
     * Note: the list-valued parameters must be empty lists rather than null.
     * Otherwise, Micronaut tries to use their default value (""), and fails
     * to convert it to a list of values.
     */
    return searchApi.searchJsonSearchJsonGet(
        limit, null, page, q, title, publisher,
        null, null, null, null, null, null, null,
        null, List.of(), null, null,
        List.of(), List.of(), List.of(), List.of(), List.of(),
        List.of(), List.of(), List.of(), List.of(),
        isbn, author, fields == null ? List.of() : fields,
        null, null, null
    );
  }

}
