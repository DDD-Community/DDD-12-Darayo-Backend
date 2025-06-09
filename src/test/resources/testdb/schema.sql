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

create table performance
(
    end_date            date         not null,
    start_date          date         not null,
    id                  bigint auto_increment
        primary key,
    place_address       varchar(512) not null,
    poster_url          varchar(512) not null,
    name                varchar(255) not null,
    place_name          varchar(255) not null,
    ban_goods           tinytext     null,
    remark              tinytext     null,
    transportation_info tinytext     null
);

create table performance_artist
(
    performance_date date   not null,
    artist_id        bigint null,
    id               bigint auto_increment
        primary key,
    performance_id   bigint null,
    constraint FKh1bst8qj2kv492baghm3u0xhw
        foreign key (performance_id) references performance (id),
    constraint FKkmkl0vu98xlbeau4swy7ksaxh
        foreign key (artist_id) references artist (id)
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
    end_time         time(6)      not null,
    performance_date date         not null,
    start_time       time(6)      not null,
    id               bigint auto_increment
        primary key,
    performance_id   bigint       null,
    performance_hall varchar(512) null,
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

