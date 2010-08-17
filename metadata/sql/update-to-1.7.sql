ALTER TABLE course ADD COLUMN chargeoverdue tinyint(1) default 0;
ALTER TABLE course MODIFY COLUMN freezeAttendance datetime;

ALTER TABLE configuration add COLUMN active tinyint(1) default 0;
ALTER TABLE configuration MODIFY COLUMN value varchar(100);

ALTER TABLE configuration DROP PRIMARY KEY;
ALTER TABLE configuration CHANGE COLUMN cfg_key name varchar(100) not null;
ALTER TABLE configuration ADD column id INT UNSIGNED NOT NULL AUTO_INCREMENT first, add PRIMARY KEY (id);
INSERT INTO configuration (name) values ('mail.registration.notifyResponsible');
INSERT INTO configuration (name, active) values ('mail.course.sendSummary', 1);
