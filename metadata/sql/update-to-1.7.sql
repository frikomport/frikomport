ALTER TABLE course ADD COLUMN chargeoverdue tinyint(1) default 0;

ALTER TABLE course MODIFY COLUMN freezeAttendance not null;

