package uk.ac.york.cs.eng2.books.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.util.Collections;
import java.util.Set;

@Serdeable
@Entity
public class Author {
  @Id
  @GeneratedValue
  private Long id;

  @Column
  private String name;

  @JsonIgnore
  @ManyToMany(mappedBy="authors")
  private Set<Book> books = Collections.emptySet();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Book> getBooks() {
    return books;
  }

  public void setBooks(Set<Book> books) {
    this.books = books;
  }
}
