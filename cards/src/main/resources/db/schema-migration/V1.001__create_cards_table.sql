create table cards
(
    id               int primary key generated always as identity,
    phone            varchar(20)  not null unique,
    card_number      varchar(100) not null,
    card_type        varchar(100) not null,
    total_limit      int          not null,
    amount_used      int          not null,
    available_amount int          not null,
    created_at       timestamp    not null default current_timestamp,
    created_by       varchar(20)  not null,
    updated_at       timestamp,
    updated_by       varchar(20)
);
