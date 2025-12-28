-- init.sql

-- USERS / ROLES
create table if not exists users (
  id bigserial primary key,
  email varchar(255) not null unique,
  password_hash varchar(255) not null,
  enabled boolean not null default true,
  created_at timestamptz not null default now()
);

create table if not exists roles (
  id bigserial primary key,
  name varchar(50) not null unique
);

create table if not exists user_roles (
  user_id bigint not null references users(id) on delete cascade,
  role_id bigint not null references roles(id) on delete cascade,
  primary key (user_id, role_id)
);

-- CARS
create table if not exists cars (
  id bigserial primary key,
  plate_number varchar(32) not null unique,
  brand varchar(100) not null,
  model varchar(100) not null,
  year int not null,
  transmission varchar(20) not null, -- AUTOMATIC / MANUAL
  fuel_type varchar(20) not null,    -- PETROL / DIESEL / HYBRID / ELECTRIC
  seats int not null,
  daily_price numeric(12,2) not null,
  deposit numeric(12,2) not null default 0,
  status varchar(20) not null,       -- ACTIVE / INACTIVE
  image_url text
);

create index if not exists idx_cars_status on cars(status);

-- RENTAL REQUESTS
create table if not exists rental_requests (
  id bigserial primary key,
  user_id bigint not null references users(id) on delete restrict,
  car_id bigint not null references cars(id) on delete restrict,
  start_date date not null,
  end_date date not null,
  status varchar(20) not null,       -- PENDING / APPROVED / REJECTED / CANCELLED
  total_price numeric(12,2) not null,
  admin_note text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint chk_rental_dates check (start_date < end_date)
);

create index if not exists idx_rentals_status on rental_requests(status);
create index if not exists idx_rentals_car_id on rental_requests(car_id);
create index if not exists idx_rentals_user_id on rental_requests(user_id);

-- SEED ROLES
insert into roles(name) values ('ROLE_ADMIN') on conflict do nothing;
insert into roles(name) values ('ROLE_USER') on conflict do nothing;
