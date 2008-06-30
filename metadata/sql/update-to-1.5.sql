﻿ALTER TABLE organization CHANGE COLUMN `address` `invoice_address` varchar(150);
ALTER TABLE organization CHANGE COLUMN `city` `invoice_city` varchar(50);
ALTER TABLE organization CHANGE COLUMN `province` `invoice_province` varchar(100);
ALTER TABLE organization CHANGE COLUMN `country` `invoice_country` varchar(100);
ALTER TABLE organization CHANGE COLUMN `postal_code` `invoice_postal_code` varchar(15);
ALTER TABLE organization CHANGE COLUMN `invoicename` `invoice_name` varchar(100);

ALTER TABLE registration ADD COLUMN invoice_address varchar(150);
ALTER TABLE registration ADD COLUMN invoice_city varchar(50);
ALTER TABLE registration ADD COLUMN invoice_province varchar(100);
ALTER TABLE registration ADD COLUMN invoice_country varchar(100);
ALTER TABLE registration ADD COLUMN invoice_postal_code varchar(15);
ALTER TABLE registration ADD COLUMN invoice_name varchar(100);

ALTER TABLE app_user ADD COLUMN invoice_address varchar(150);
ALTER TABLE app_user ADD COLUMN invoice_city varchar(50);
ALTER TABLE app_user ADD COLUMN invoice_province varchar(100);
ALTER TABLE app_user ADD COLUMN invoice_country varchar(100);
ALTER TABLE app_user ADD COLUMN invoice_postal_code varchar(15);
ALTER TABLE app_user ADD COLUMN invoice_name varchar(100);

