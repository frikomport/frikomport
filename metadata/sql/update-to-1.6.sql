ALTER TABLE registration MODIFY COLUMN jobtitle varchar(100);
ALTER TABLE registration MODIFY COLUMN firstname varchar(100);
ALTER TABLE registration MODIFY COLUMN lastname varchar(100);
ALTER TABLE registration MODIFY COLUMN comment varchar(255);

ALTER TABLE app_user MODIFY COLUMN jobtitle varchar(100);
ALTER TABLE app_user MODIFY COLUMN first_name varchar(100);
ALTER TABLE app_user MODIFY COLUMN last_name varchar(100);

INSERT INTO configuration VALUES ('show.menu','false');
INSERT INTO configuration VALUES ('access.registration.userdefaults','false');
INSERT INTO configuration VALUES ('access.registration.candelete','false');
INSERT INTO configuration VALUES ('access.course.filterlocation','false');
INSERT INTO configuration VALUES ('access.course.singleprice','false');


