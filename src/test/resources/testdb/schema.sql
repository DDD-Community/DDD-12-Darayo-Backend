use test;

create table artist
(
    id           bigint auto_increment
        primary key,
    description  varchar(512) null,
    display_name varchar(255) not null
);

create table artist_alias
(
    artist_id bigint       null,
    id        bigint auto_increment
        primary key,
    name      varchar(255) not null,
    constraint UKmssac0k9kr9ldw8njs6avn90d
        unique (name),
    constraint FK5x7ca9pl4tu7xlr9x02c2guxn
        foreign key (artist_id) references artist (id)
);

create table performance_place
(
    id      bigint auto_increment
        primary key,
    address varchar(512) not null,
    name    varchar(255) not null
);

create table performance
(
    end_date            date         null,
    start_date          date         null,
    id                  bigint auto_increment
        primary key,
    place_id            bigint       null,
    poster_url          varchar(512) null,
    name                varchar(255) not null,
    ban_goods           tinytext     null,
    remark              tinytext     null,
    transportation_info tinytext     null,
    constraint FKedb352whbcddiscv836vi5y8n
        foreign key (place_id) references performance_place (id)
);

create table performance_hall
(
    id       bigint auto_increment
        primary key,
    place_id bigint       null,
    name     varchar(255) not null,
    constraint FK2ly74kw29jqgu596ex1t5xvp6
        foreign key (place_id) references performance_place (id)
);

create table performanceurl
(
    type           tinyint      not null,
    id             bigint auto_increment
        primary key,
    performance_id bigint       null,
    url            varchar(512) not null,
    constraint FKpe0yx24po9utacc9p4fnr6r95
        foreign key (performance_id) references performance (id)
);

create table reservation_info
(
    close_date_time datetime(6)                    not null,
    id              bigint auto_increment
        primary key,
    open_date_time  datetime(6)                    not null,
    performance_id  bigint                         null,
    ticketurl       varchar(512)                   null,
    remark          tinytext                       null,
    type            enum ('EARLY_BIRD', 'GENERAL') not null,
    constraint FKkhsfcgblfvv2awwxrabgt04uo
        foreign key (performance_id) references performance (id)
);

create table timetable
(
    end_time         time(6) null,
    performance_date date    not null,
    start_time       time(6) null,
    hall_id          bigint  null,
    id               bigint auto_increment
        primary key,
    performance_id   bigint  null,
    constraint FKd90gmllbgmc3jcitqydv4iq5x
        foreign key (hall_id) references performance_hall (id),
    constraint FKsmu2k8sab15tpr35civcmkxi3
        foreign key (performance_id) references performance (id)
);

create table timetable_artist
(
    artist_id          bigint               null,
    id                 bigint auto_increment
        primary key,
    timetable_id       bigint               null,
    participation_type enum ('MAIN', 'SUB') not null,
    constraint FK879axnngp2ukeobtbcs2ddwvc
        foreign key (artist_id) references artist (id),
    constraint FKo7rtp5ptlo3hvej9h38yqo01m
        foreign key (timetable_id) references timetable (id)
);

create table user_alarm_token
(
    id          bigint auto_increment
        primary key,
    alarm_token varchar(512) not null,
    expired_at  datetime(6)  null,
    is_valid    bit          null,
    user_id     bigint       not null
);

create table user_performance_alarm
(
    id        bigint auto_increment
        primary key,
    target_id bigint  not null,
    type      tinyint not null,
    user_id   bigint  null
);

create table user
(
    id               bigint auto_increment
        primary key,
    is_alarm_allowed bit             not null,
    last_login_at    datetime(6)     null,
    provider         enum ('DEVICE') null,
    provider_user_id varchar(255)    null
);

alter table user_alarm_token add column updated_at datetime(6) null;
alter table reservation_info add column open_time_modified_at datetime(6) null;