-- Script for upgrading the frikob db from version 1.0 to 1.1
 
ALTER TABLE REGISTRATION ADD COLUMN LOCALE VARCHAR(10);

DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
   `id` bigint(20) NOT NULL auto_increment,
   `registrationid` bigint(20) NOT NULL,
   `reminderSent` bit(1) NOT NULL,
   PRIMARY KEY  (`id`),
   KEY `FK237A88EB5F05D1F1` (`registrationid`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;