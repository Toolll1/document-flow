drop table if exists passports cascade;
drop table if exists users cascade;
drop table if exists file_types cascade;
drop table if exists attributes cascade;
drop table if exists files cascade;
drop table if exists type_attributes cascade;

create table if not exists passports (
passport_id bigint not null generated by default as identity primary key,
series varchar(4) not null,
number varchar(6) not null,
issued varchar(320) not null,
date timestamp without time zone not null,
kp varchar(6) not null
);

create table if not exists users (
user_id bigint not null generated by default as identity primary key,
last_name varchar(100) not null,
first_name varchar(100) not null,
patronymic varchar(100),
date_of_birth timestamp without time zone not null,
email varchar(320) UNIQUE not null,
phone varchar(11) UNIQUE not null,
user_password varchar (20),
passport_id integer not null references passports(passport_id)
);

create table if not exists file_types (
type_id bigint not null generated by default as identity primary key,
name varchar(320) not null
);

create table if not exists attributes (
attribute_id bigint not null generated by default as identity primary key,
name varchar(320) not null
);

create table if not exists type_attributes (
type_id integer not null references file_types(type_id),
attribute_id integer not null references attributes(attribute_id)
);

create table if not exists files (
file_id bigint not null generated by default as identity primary key,
title varchar(320) not null,
file_path varchar(320) not null,
created_at timestamp without time zone not null,
user_id integer not null references users(user_id),
type_id integer not null references file_types(type_id)
);