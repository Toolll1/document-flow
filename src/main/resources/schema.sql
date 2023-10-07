drop table if exists organizations cascade;
drop table if exists passports cascade;
drop table if exists users cascade;

drop table if exists file_types cascade;
drop table if exists attributes_class cascade;
drop table if exists type_attributes cascade;
drop table if exists changes cascade;
drop table if exists attribute_values cascade;
drop table if exists changes cascade;
drop table if exists files cascade;


create table if not exists organizations (
org_id bigint not null generated by default as identity primary key,
name varchar(320) not null,
inn varchar(10)
);

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
post varchar (320) not null,
role varchar (5) not null,
passport_id bigint not null references passports(passport_id),
org_id bigint not null references organizations(org_id)
);




create table if not exists file_types (
type_id bigint not null generated by default as identity primary key,
name varchar(320) not null
);

create table if not exists attributes_class (
attribute_id bigint not null generated by default as identity primary key,
name varchar(320) not null,
type varchar(16) not null
);

create table if not exists type_attributes (
type_id bigint not null references file_types(type_id),
attribute_id bigint not null references attributes_class(attribute_id)
);

create table if not exists changes (
changes_id bigint not null generated by default as identity primary key,
date_of_change timestamp without time zone not null,
user_id bigint not null references users(user_id),
description varchar(1000) not null,
previous_version varchar(1000) not null,
file_id bigint
);

create table if not exists attribute_values (
value_id bigint not null generated by default as identity primary key,
attribute_name varchar(320) not null,
attribute_value varchar(1000) not null,
file_id bigint
);

create table if not exists file_process (
process_id bigint not null generated by default as identity primary key,
file_id bigint,
sender_id bigint not null references users(user_id),
recipient_id bigint not null references users(user_id),
status varchar(11) not null,
comment varchar(1000) not null
);

create table if not exists files (
file_id bigint not null generated by default as identity primary key,
title varchar(320) not null,
file_path varchar(1000) not null,
created_at timestamp without time zone not null,
creator_id bigint not null references users(user_id),
type_id bigint not null references file_types(type_id)
);
