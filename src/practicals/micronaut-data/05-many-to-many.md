# M:N with Author

In the previous section, we added `Publisher` entities and a bidrectional one-to-many relationship between `Book` and `Publisher`.

In this section, we will promote the authors of a book to its own `Author` entity, and implement a many-to-many relationship between `Book` and `Author`.

## Migrate the database

Create a new database migration script named `V3__add-author.sql`, with this content:

```sql
-- Authors
create table author (
    id bigint primary key not null,
    name varchar(255) not null
);

-- Join table between books and authors
-- Book owns the relationship, so the name is book_author, rather than author_book
create table book_author (
    -- Name is (field name)_(primary key of referenced entity)
    books_id bigint not null references book (id),
    authors_id bigint not null references author (id),
    -- Primary key of this table is the composite of the
    constraint primary key (books_id, authors_id)
);

-- Migrate existing authors into author rows
insert into author
select nextval(hibernate_sequence), b.author
from (select distinct author from book) b;

-- Join up books and authors
insert into book_author (books_id, authors_id)
select book.id, author.id
from book join author
where book.author = author.name;

-- Drop the old author column
alter table book
drop column author;
```

This script creates the new table for our `Author` entity, and a join table between `Book` and `Author` as this is a many-to-many relationship.

You'll notice that it also migrates the existing data into this new schema, and drops the old `author` column as that is no longer relevant.

This is an advantage of a database migration tool like Flyway: if we had deployed a previous version in production, this script would automatically migrate existing data during the upgrade.
Obviously, migrating production data is a sensitive matter, so this migration would have to be carefully tested in your development environment first, and you would also want to do periodic backups and to perform a backup right before such a migration.

## Update the Book entity

Remove the `author` field and its setter and getter from the `Book` entity, and correct any tests that may have been affected.

If you use DTOs for some of your `Book`-related requests or responses, remove any `author` fields from them as well.

Before moving on, ensure all your tests pass again.

## Create the Author entity

Create the `Author` entity class, add the controller for it, as well as any appropriate tests.
Ignore the relationship between `Author` and `Book` for now.

## Add the Book-Author relationship

Let's add the bidrectional many-to-many relationship between `Book` and `Author`.
We will make `Book` own the relationship, so add this to `Book`, plus its getter and setter:

```java
@ManyToMany
private Set<Author> authors = new HashSet<>();
```

As we said before, if you made your `Book` to be `@Serdeable`, you should add `@JsonIgnore` to the `authors` field so that it will not be part of its JSON serialisation by default.

Add this to `Author`, together with its getter and setter methods:

```java
@ManyToMany(mappedBy="authors")
private Set<Book> books = Collections.emptySet();
```

You'll notice that here were used `mappedBy` to indicate the name of the field in `Book` that owns this bidirectional relationship.
As said above, the `mappedBy` side is the one that *does not* own the relationship (it's only for reading, not for modifying).

We can make the default value of the `books` field to be an unmodifiable empty set, so it will immediately complain if someone tries to modify it.
When we query the databse, our ORM will automatically replace this set with a lazily-loaded collection, which will be strictly limited to reading.

## Extending the controllers

It's time to expose this many-to-many relationship from our API.
There are many different ways we could do it, but for this practical, implement these endpoints:

* `GET /books/{id}/authors`: list the authors of a book.
* `PUT /books/{id}/authors/{authorId}`: add the given author to the book. Do nothing if the author is already listed on the book.
* `DELETE /books/{id}/authors/{authorId}`: remove the given author from the book. Do nothing if the author is not listed on the book.
* `GET /authors/{id}/books`: list the books of an author.

These are some aspects to take into account:

* To update the set of authors associated to a `Book`, you only need to save the `Book` itself (i.e. `repository.save(book)`) after adding or removing the relevant `Author`  from `book.getAuthors()`.
  The ORM will figure out what needs inserting and deleting.
* Remember from the previous section that if you need to chain multiple queries (e.g. find a `Book` and then copy its authors to a new `List` to avoid issues around trying to serialise a lazily-loaded collection outside a database session), you will need to annotate the controller method as `@Transactional`.
* You could use custom repository methods to directly find the `Author`s by the ID of the `Book`, or find the `Book`s by the ID of the `Author`. Review the notes from the previous section for inspiration.
