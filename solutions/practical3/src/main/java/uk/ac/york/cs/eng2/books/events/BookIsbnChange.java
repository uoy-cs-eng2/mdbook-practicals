package uk.ac.york.cs.eng2.books.events;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record BookIsbnChange(String oldIsbn, String newIsbn) {}
