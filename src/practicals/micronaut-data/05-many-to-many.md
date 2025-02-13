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

Before moving on, ensure all your tests pass again.

## Create the Author entity

Create the `Author` entity class, add the controller for it, as well as any appropriate tests.
Ignore the relationship between `Author` and `Book` for now.
