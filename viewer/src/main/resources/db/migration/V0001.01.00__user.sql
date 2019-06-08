create table appluser (
    pkey int8 not null,
    password varchar(255),
    username varchar(255),
    primary key (pkey)
);

create unique index
    idx_appluser_username ON appluser(username);


create table user_roles (
    user_pkey int8 not null,
    roles varchar(255)
);

alter table user_roles
    add constraint fk_user_roles_appluser foreign key (user_pkey) references appluser;

