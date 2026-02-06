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