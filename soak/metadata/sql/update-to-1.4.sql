delete from user_role;
delete from role;
delete from app_user;

insert into role values ('anonymous',1,'Anonymous');
insert into role values ('employee',1,'Ansatt');
insert into role values ('instructor',1,'Kursansvarlig');
insert into role values ('editor',1,'Opplaringsansvarlig');
insert into role values ('admin',1,'Administrator');

ALTER TABLE user_role MODIFY COLUMN username varchar(100);
ALTER TABLE user_role MODIFY COLUMN role_name varchar(100);

-- update user table.
ALTER TABLE app_user ADD COLUMN organizationid BIGINT(20) DEFAULT 0;
ALTER TABLE app_user ADD COLUMN serviceareaid BIGINT(20) DEFAULT 0;
ALTER TABLE app_user ADD COLUMN employeenumber BIGINT(20) DEFAULT 0;
ALTER TABLE app_user ADD COLUMN jobtitle varchar(50);
ALTER TABLE app_user ADD COLUMN workplace varchar(100);
ALTER TABLE app_user ADD COLUMN id INTEGER DEFAULT 0;
ALTER TABLE app_user ADD COLUMN mobilephone VARCHAR(30);
ALTER TABLE app_user ADD COLUMN hash VARCHAR(255);


ALTER TABLE app_user MODIFY COLUMN username VARCHAR(100);

-- set fields to not required
ALTER TABLE app_user MODIFY COLUMN version INTEGER;
ALTER TABLE app_user MODIFY COLUMN password VARCHAR(255);
ALTER TABLE app_user MODIFY COLUMN city VARCHAR(50);
ALTER TABLE app_user MODIFY COLUMN postal_code VARCHAR(15);

ALTER TABLE registration ADD COLUMN username VARCHAR(100);
ALTER TABLE registration MODIFY COLUMN organizationid BIGINT(20);
ALTER TABLE registration MODIFY COLUMN serviceareaid BIGINT(20);

-- add status field and set default values
ALTER TABLE course ADD COLUMN status INTEGER DEFAULT 0;
ALTER TABLE course ADD COLUMN responsibleusername VARCHAR(50);
update course set status = 2;