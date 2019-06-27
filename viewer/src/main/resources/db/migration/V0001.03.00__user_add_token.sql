alter table appluser
    add column state varchar(255),
    add column token varchar(255),
    add column token_expiration timestamp;
