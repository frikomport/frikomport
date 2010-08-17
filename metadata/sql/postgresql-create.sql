-- drop the existing database
drop database soakdb;

-- create the test user
create user test password 'test';

-- create the database
create database soakdb owner test;
