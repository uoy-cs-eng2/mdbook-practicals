create table windowed_area_checkin_stat (
    id bigint primary key not null,
    area int not null,
    window_start_at timestamp(3) not null,
    name varchar(255) not null,
    value bigint not null default 0,
    constraint uk_windowed_area_name unique (area, window_start_at, name)
);