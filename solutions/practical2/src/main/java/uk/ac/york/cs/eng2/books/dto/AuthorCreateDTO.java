package uk.ac.york.cs.eng2.books.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class AuthorCreateDTO {
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
