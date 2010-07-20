-- Script for upgrading the frikob db from version 1.2 to 1.2.5
 
ALTER TABLE COURSE ADD COLUMN ROLE VARCHAR(50) NOT NULL;
ALTER TABLE COURSE ALTER COLUMN ROLE SET DEFAULT 'Anonymous';
UPDATE COURSE SET ROLE = 'Anonymous';
