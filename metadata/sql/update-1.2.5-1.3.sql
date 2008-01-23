-- Script for upgrading the frikob db from version 1.2.5 to 1.3
 
ALTER TABLE REGISTRATION ADD COLUMN COMMENT VARCHAR(100);
ALTER TABLE REGISTRATION ADD COLUMN ATTENDED BOOL NOT NULL;
ALTER TABLE REGISTRATION ALTER COLUMN ATTENDED SET DEFAULT 0;
-- Set attended = 1 for all already registered courses
UPDATE REGISTRATION SET ATTENDED = 1;
