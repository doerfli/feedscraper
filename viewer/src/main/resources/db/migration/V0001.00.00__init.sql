create sequence hibernate_sequence start 1 increment 1;

create table feed (
    pkey int8 not null,
    id varchar(255),
    last_downloaded timestamp,
    link_alternate varchar(255),
    link_self varchar(255),
    subtitle varchar(255),
    title varchar(255),
    type varchar(64),
    updated timestamp,
    url varchar(255),
    primary key (pkey)
);

create table item (
    pkey int8 not null,
    content varchar(4096),
    id varchar(1024),
    link varchar(1024),
    published timestamp,
    summary varchar(4096),
    title varchar(1024),
    updated timestamp,
    feed_id int8,
    primary key (pkey)
);
alter table item
    add constraint FK_item_feed_id foreign key (feed_id) references feed;

