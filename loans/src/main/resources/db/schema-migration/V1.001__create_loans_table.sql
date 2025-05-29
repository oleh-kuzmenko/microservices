create table loans
(
    id          int primary key generated always as identity,
    phone       varchar(20)  not null unique,
    loan_number varchar(100) not null,
    loan_type   varchar(100) not null,
    total_loan  int          not null,
    amount_paid int          not null,
    address     varchar(100),
    created_at  timestamp    not null default current_timestamp,
    created_by  varchar(20)  not null,
    updated_at  timestamp,
    updated_by  varchar(20)
);
