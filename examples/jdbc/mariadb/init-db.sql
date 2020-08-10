create table PROPERTIES(
    application varchar(256),
    profile varchar(256),
    label varchar(256),
    `key` varchar(256),
    value varchar(256)
);

insert into PROPERTIES values ('jdbc-app', 'dev', 'latest', 'sample key', 'a value');