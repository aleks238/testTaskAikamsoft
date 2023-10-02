create table products(
    id              bigserial primary key,
    title           varchar(255),
    price           int
);

create table customers(
    id          bigserial primary key,
    name        varchar (30) not null ,
    last_name   varchar(80) not null
);

create table purchases(
    id              bigserial primary key,
    customer_id     bigint not null references customers(id),
    product_id      bigint not null references products(id),
    purchase_date   date not null default current_date
);

insert into products (title, price) values ('bread', 45), ('apple', 110), ('milk', 75);
insert into customers (name, last_name) values ('Jack', 'Jackson'),('Bill', 'Jackson'), ('Bill', 'Wilson'), ('Bob', 'Murphy');
insert into purchases (customer_id, product_id, purchase_date) values (1, 1, '2023-09-20'),(1, 2),(1, 1),(1, 1),(1,3),(2, 1),(2, 2),(2, 3),(2, 3),(3, 1),(3, 2),(3,3);
