create table accounts
(
    id           int primary key generated always as identity,
    name         varchar(100) not null,
    email        varchar(100) not null unique,
    phone        varchar(20)  not null unique,
    account_type varchar(100) not null,
    address      varchar(100),
    created_at   timestamp not null default current_timestamp,
    created_by   varchar(20) not null,
    updated_at   timestamp,
    updated_by   varchar(20)
);
