create table orders (
    id uuid primary key,
    client_id uuid not null,
    restaurant_code varchar not null,
    created_at timestamptz not null
);
