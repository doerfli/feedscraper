create table item_state (
    pkey int8 not null,
    is_read boolean not null,
    feed_id int8,
    item_id int8,
    user_id int8,
    primary key (pkey)
);
alter table item_state
    add constraint fk_item_state_feed foreign key (feed_id) references feed;
alter table item_state
    add constraint fk_item_state_item foreign key (item_id) references item;
alter table item_state
    add constraint fk_item_state_appluser foreign key (user_id) references appluser;

