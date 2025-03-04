create sequence hibernate_sequence;

create table book (
  id bigint primary key not null,
  title varchar(255) not null,
  author varchar(255) not null
);
