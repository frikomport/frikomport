ALTER TABLE registration MODIFY COLUMN jobtitle varchar(100);
ALTER TABLE registration MODIFY COLUMN firstname varchar(100);
ALTER TABLE registration MODIFY COLUMN lastname varchar(100);
ALTER TABLE registration MODIFY COLUMN comment varchar(255);
ALTER TABLE registration MODIFY COLUMN email varchar(255);

ALTER TABLE app_user MODIFY COLUMN jobtitle varchar(100);
ALTER TABLE app_user MODIFY COLUMN first_name varchar(100);
ALTER TABLE app_user MODIFY COLUMN last_name varchar(100);

ALTER TABLE course MODIFY COLUMN responsibleusername VARCHAR(100) NOT NULL;
ALTER TABLE course MODIFY COLUMN responsibleid bigint(20);

CREATE TABLE configuration (cfg_key VARCHAR(100) NOT NULL, value VARCHAR(100) NOT NULL, PRIMARY KEY (cfg_key));
INSERT INTO configuration VALUES ('show.menu','false');
INSERT INTO configuration VALUES ('access.registration.userdefaults','false');
INSERT INTO configuration VALUES ('access.registration.candelete','false');
INSERT INTO configuration VALUES ('access.registration.emailrepeat','false');
INSERT INTO configuration VALUES ('access.course.filterlocation','false');
INSERT INTO configuration VALUES ('access.course.singleprice','false');

create table category (id integer not null default 0, name varchar(100) not null, selectable tinyint(1) default 1);
ALTER TABLE course ADD COLUMN categoryid BIGINT(20)


