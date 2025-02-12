package uk.ac.york.cs.eng2.books.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class BookDTO {
  private Long id;
  private String title;
  private String author;
  private Long publisherId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public Long getPublisherId() {
    return publisherId;
  }

  public void setPublisherId(Long publisherId) {
    this.publisherId = publisherId;
  }
}
