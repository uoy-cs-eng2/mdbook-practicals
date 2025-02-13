# 1:N with Publisher

We created the `Book` entity that mapped the rows in our `book` table, and we created a controller that allowed for managing these `Book`s via HTTP requests.
However, we also need to deal with other entities that may be related to books.
For instance:

* Each `Book` is published by a `Publisher` (a "many-to-one" relationship).
* A `Publisher` publishes many `Book`s (a "one-to-many" relationship).

In this section, we will practice with implementing such a relationship in our application.

## Migrating the database

We need to add a Flyway database migration that will create the appropriate `publisher` table, and add the `publisher_id` foreign key to the `book` table.
Create a `src/main/resources/db/migration/V2__add-publisher.sql` file with this content:

```sql
# Creates the table for our publishers
create table publisher (
    id bigint primary key not null,
    name varchar(255) unique not null
);

# Adds the foreign key from a book to its publisher
alter table book
add publisher_id bigint references publisher (id);
```

## Basic functionality for publishers

Create the `Publisher` entity in your application and its repository.
Add a controller that allows you to create, list, update the name, and delete its entries, by handling these HTTP requests:

* `GET /publishers`: list all the publishers
* `POST /publishers`: add a publisher
* `GET /publishers/{id}`: get a specific publisher by ID
* `PUT /publishers/{id}`: update the name of a publisher by ID
* `DELETE /publishers/{id}`: delete a publisher

As these steps will be the same as for `Book`, we will not provide detailed step-by-step instructions on how to do it.
Ignore the relationship between `Book` and `Publisher` for now.

Don't forget to write the appropriate tests for it!

## Adding the relationship to the entities

Let's extend `Book` and `Publisher` so they know about each other.

First, add this to `Book`:

```java
@ManyToOne
private Publisher publisher;
```

Generate the getter and setter methods (getPublisher / setPublisher) as usual in your IDE.

In addition, if your `Book` domain entity is `@Serdeable`, add `@JsonIgnore` to the new `publisher` field, as we want to avoid a scenario where we serialise the entire `Publisher` and all their books when we're trying to send a single `Book` over the network.
`@JsonIgnore` means that the field will not be serialised via JSON.

Likewise, add this to `Publisher`, as well as the appropriate getter and setter methods:

```java
@OneToMany(mappedBy = "publisher")
private Set<Book> books = new HashSet<>();
```

If you remember from the lecture, in bidirectional relationships like this one, there is one side that *owns* the relationship: in other words, the side that you should change - the other side is only for reading.
In the case of one-to-many + many-to-one relationships, the owning side is always many-to-one.
In other words, `Book` is the owner of this relationship.
The side that does not own the relationship (`Publisher`) indicates via `mappedBy` the name of the property on the other side that owns the relationship (in this case, `publisher` in `Book`).

Likewise, if you made your `Publisher` to be `@Serdeable`, add the `@JsonIgnore` annotation to your `books` field.

Before we move on, check that all your tests are still passing.

## Extending the controllers

We want to add support for this `Book->Publisher` relationship to our controllers.
Specifically, we want to support these features:

* `POST /books/{id}` should allow for specifying the publisher.
* `PUT /books/{id}` should allow for setting and unsetting the publisher of a `Book`.
* `GET /books/{id}/publisher` should return the publisher of a `Book`.
* `GET /publishers/{id}/books` should list the `Book`s of a `Publisher`.

You should be able to implement these yourself with what you have learned so far for the most part.
There are a few things to take into account, though:

  * For specifying the publisher while creating or updating a `Book`, you may now need to create a `BookCreateDTO` with a dedicated `Long publisherId` field, as `Book` itself will not allow you to specify that information (since it will just have a `Publisher publisher` field).
  * Fetching the publisher of a `Book` can be done in two main ways:
    * If you use `repo.findById(id)` to get the `Book` and then use `book.getPublisher()` to get the `Publisher`, you will need to add the `@Transactional` annotation to the controller method so both queries will run in the same transaction.
    Otherwise, you may get an error message on the `book.getPublisher()` call, as `book` will no longer be connected to a database session.
    * Alternatively, you can add a custom query method to your `PublisherRepository` and retrieve the appropriate `Publisher` in one call, like this one - we picked this name specifically so we'd obtain the `Publisher` that has the given `id` among its `books`:

      ```java
      Optional<Publisher> findByBooksId(Long id);
      ```
  * When fetching the `Book`s of a `Publisher`, you can follow two approaches:
    * Use a `@Transactional` controller method that first finds the `Publisher`, copies `publisher.getBooks()` to a new `List<Book>` (to avoid any issues with lazy collections) which it then returns.
    * Use a custom query method in your `BookRepository` which fetches those `Book`s directly - again, we picked the name specifically to find the `Book`s whose `publisher` has the given ID:
      ```java
      List<Book> findByPublisherId(long publisherId);
      ```

When testing, consider that you will need to modify the `@BeforeEach` method so it deletes all the `Book`s first, and then all the `Publisher`s.
If you try to delete all the `Publisher`s first, you may see errors as some `Book`s may still be pointing at them.

Once you are done with the above functionality and your tests pass, move on to the next section.