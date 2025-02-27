package uk.ac.york.cs.eng2.books.gateways;

import java.util.ArrayList;
import java.util.List;

public class BookCatalogInfo {
  private List<String> publishers = new ArrayList<>();

  public List<String> getPublishers() {
    return publishers;
  }

  public void setPublishers(List<String> publishers) {
    this.publishers = publishers;
  }
}
