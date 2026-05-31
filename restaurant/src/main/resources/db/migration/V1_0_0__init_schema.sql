create table orders (
    id uuid primary key default uuidv7(),
    client_id uuid not null,
    restaurant_code varchar not null,
    total decimal not null,
    created_at timestamptz not null default current_timestamp
);
