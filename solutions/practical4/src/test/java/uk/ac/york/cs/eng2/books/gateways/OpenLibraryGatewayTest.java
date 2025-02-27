package uk.ac.york.cs.eng2.books.gateways;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class OpenLibraryGatewayTest {
  @Inject
  OpenLibraryBookCatalogGateway gateway;

  private static final String ISBN_MINECRAFT = "1524797162";

  @Test
  public void minecraft() {
    BookCatalogInfo bookInfo = gateway.findByIsbn(ISBN_MINECRAFT);
    assertEquals(Collections.singletonList("Del Rey"), bookInfo.getPublishers());
  }

}
