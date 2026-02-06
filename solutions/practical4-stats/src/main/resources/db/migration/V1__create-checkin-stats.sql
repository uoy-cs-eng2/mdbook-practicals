create sequence hibernate_sequence;

create table partitioned_checkin_stat (
    id bigint primary key not null,
    name varchar(255) not null,
    partition_id int not null,
    value bigint not null default 0,
    constraint uk_stat_partition unique (partition_id, name)
);