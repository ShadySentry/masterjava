drop table if exists users_groups;
drop table if exists groups;
drop table if exists project;
DROP TABLE IF EXISTS users;
drop table if exists city;
DROP SEQUENCE IF EXISTS common_seq;

DROP TYPE IF EXISTS user_flag;
DROP TYPE IF EXISTS group_type;

CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');
CREATE TYPE group_type AS ENUM ('registering', 'current', 'finished');

CREATE SEQUENCE common_seq START 100000;

create table city
(
    ref  text not null PRIMARY KEY,
    name text not null
);

CREATE TABLE users
(
    id        INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
    full_name TEXT      NOT NULL,
    email     TEXT      NOT NULL,
    flag      user_flag NOT NULL,
    city_ref  text references city (ref) on update cascade
);

CREATE UNIQUE INDEX email_idx ON users (email);

create table project
(
    id integer primary key default nextval('common_seq'),
    name        text not null unique,
    description text not null
);

create table groups
(
    id                      INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
    name                    TEXT       NOT NULL,
    type group_type   NOT NULL,
    project_id              integer references project(id)
);


create table users_groups(
                             user_id integer not null references users(id),
                             group_id integer not null references groups(id),
                             constraint users_group_idx unique (user_id,group_id)
)