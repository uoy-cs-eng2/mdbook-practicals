# Creates the table for our publishers
create table publisher (
    id bigint primary key not null,
    name varchar(255) unique not null
);

# Adds the foreign key from a book to its publisher
alter table book
add publisher_id bigint references publisher (id);
