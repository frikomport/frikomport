ALTER TABLE course ADD COLUMN chargeoverdue tinyint(1) default 0;
ALTER TABLE course MODIFY COLUMN freezeAttendance datetime;

ALTER TABLE configuration add COLUMN active tinyint(1) default 0;
ALTER TABLE configuration MODIFY COLUMN value varchar(100);
