create database if not exists soakdb;
grant all privileges on soakdb.* to test@"%" identified by "test";
grant all privileges on soakdb.* to test@localhost identified by "test";

-- You may have to explicitly define your hostname in order for things
-- to work correctly.  For example:
-- grant all privileges on soakdb.* to test@host.domain.com identified by "test";