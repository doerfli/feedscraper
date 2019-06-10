create table item_user_property (
    pkey int8 not null,
    is_read boolean not null,
    feed_id int8,
    item_id int8,
    user_id int8,
    primary key (pkey)
);
alter table item_user_property
    add constraint FKow2smr45dyqmprkuthyb6s8qy foreign key (feed_id) references feed;
alter table item_user_property
    add constraint FK7gmxkuvy14oyxe6su3w54xht9 foreign key (item_id) references item;
alter table item_user_property
    add constraint FK50cc03hjdn2ma7vl2j4gj7anq foreign key (user_id) references appluser;

