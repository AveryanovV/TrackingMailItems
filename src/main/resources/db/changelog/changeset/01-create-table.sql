-- liquibase formatted sql

-- changeset AveryanovV:1.0

create table delivery_movement
(
    delivery_delivery_id bigint,
    delivery_movement_id bigint not null,
    delivery_movements   bigint,
    movement_date        timestamp(6),
    movement_location    varchar(255),
    movement_status      varchar(255),
    primary key (delivery_movement_id)
);
create table postal_delivery
(
    recipient_index   integer,
    delivery_id       bigint not null,
    post_office_id    bigint,
    delivery_type     varchar(255),
    recipient_address varchar(255),
    recipient_name    varchar(255),
    recipient_status  varchar(255),
    primary key (delivery_id)
);
create table post_office
(
    post_office_index   integer,
    post_office_id      bigint not null,
    post_office_address varchar(255),
    post_office_name    varchar(255),
    primary key (post_office_id)
);



alter table delivery_movement add constraint FK3s31r7u0hud3sgjxoa8oxo92a foreign key (delivery_delivery_id) references postal_delivery;

alter table delivery_movement add constraint FKpa0cscyytspvs5px56j1i1fk1 foreign key (delivery_movements) references postal_delivery;

alter table postal_delivery add constraint FKkkqr3g1qd97vv8e48aysoqciv foreign key (post_office_id) references post_office;

create sequence delivery_movement_seq start with 1 increment by 50;

create sequence postal_delivery_seq start with 1 increment by 50;

create sequence post_office_seq start with 1 increment by 50;

