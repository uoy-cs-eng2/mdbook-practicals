package uk.ac.york.cs.eng2.books.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class BookCreateDTO {
  private String title, isbn;
  private Long publisherId;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Long getPublisherId() {
    return publisherId;
  }

  public void setPublisherId(Long publisherId) {
    this.publisherId = publisherId;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }
}
