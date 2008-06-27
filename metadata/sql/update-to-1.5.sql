﻿ALTER TABLE organization ADD COLUMN address varchar(150);
ALTER TABLE organization ADD COLUMN city varchar(50);
ALTER TABLE organization ADD COLUMN province varchar(100);
ALTER TABLE organization ADD COLUMN country varchar(100);
ALTER TABLE organization ADD COLUMN postal_code varchar(15);
ALTER TABLE organization ADD COLUMN invoicename varchar(100);

