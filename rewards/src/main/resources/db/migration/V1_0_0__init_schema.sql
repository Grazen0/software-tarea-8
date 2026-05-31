create table client_rewards (
    client_id uuid primary key,
    email varchar,
    points bigint not null default 0,
    cashback decimal not null default 0,
    created_at timestamptz default current_timestamp
);
