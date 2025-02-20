create table check_in_desk (
    -- internal key (autoincremented)
    id bigint primary key not null,
    -- business key (from Kafka)
    desk_id bigint unique not null,
    -- if not null, a check-in is currently undergoing in this desk
    checkin_started_at timestamp(3),
    -- true iff out of order
    out_of_order bool not null default false
);
