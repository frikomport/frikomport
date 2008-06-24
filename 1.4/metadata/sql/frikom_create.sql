--
-- Definition of table `app_user`
--

DROP TABLE IF EXISTS `app_user`;
CREATE TABLE `app_user` (
  `username` varchar(20) NOT NULL default '',
  `version` int(11) NOT NULL default '0',
  `password` varchar(255) NOT NULL default '',
  `first_name` varchar(50) NOT NULL default '',
  `last_name` varchar(50) NOT NULL default '',
  `address` varchar(150) default NULL,
  `city` varchar(50) NOT NULL default '',
  `province` varchar(100) default NULL,
  `country` varchar(100) default NULL,
  `postal_code` varchar(15) NOT NULL default '',
  `email` varchar(255) NOT NULL default '',
  `phone_number` varchar(255) default NULL,
  `website` varchar(255) default NULL,
  `password_hint` varchar(255) default NULL,
  `enabled` tinyint(1) default NULL,
  PRIMARY KEY  (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table `user_cookie`
--

DROP TABLE IF EXISTS `user_cookie`;
CREATE TABLE `user_cookie` (
  `id` bigint(20) NOT NULL default '0',
  `username` varchar(30) NOT NULL default '',
  `cookie_id` varchar(100) NOT NULL default '',
  `date_created` datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (`id`),
  KEY `user_cookie_username_cookie_id` (`username`,`cookie_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table `role`
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `name` varchar(20) NOT NULL default '',
  `version` int(11) NOT NULL default '0',
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `username` varchar(20) NOT NULL default '',
  `role_name` varchar(20) NOT NULL default '',
  PRIMARY KEY  (`username`,`role_name`),
  KEY `FK143BF46A928F9645` (`username`),
  KEY `FK143BF46AB6692ECE` (`role_name`),
  CONSTRAINT `FK143BF46A928F9645` FOREIGN KEY (`username`) REFERENCES `app_user` (`username`),
  CONSTRAINT `FK143BF46AB6692ECE` FOREIGN KEY (`role_name`) REFERENCES `role` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Definition of table `person`
--

DROP TABLE IF EXISTS `person`;
CREATE TABLE `person` (
  `id` bigint(20) NOT NULL auto_increment,
  `selectable` tinyint(1) NOT NULL default '0',
  `detailurl` varchar(200) default NULL,
  `email` varchar(50) NOT NULL default '',
  `mailaddress` varchar(100) default NULL,
  `name` varchar(50) NOT NULL default '',
  `phone` varchar(30) default NULL,
  `mobilephone` varchar(30) default NULL,
  `description` text,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table `organization`
--

DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization` (
  `id` bigint(20) NOT NULL auto_increment,
  `selectable` tinyint(1) NOT NULL default '0',
  `name` varchar(50) NOT NULL default '',
  `number` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Definition of table `servicearea`
--

DROP TABLE IF EXISTS `servicearea`;
CREATE TABLE `servicearea` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(30) NOT NULL default '',
  `selectable` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table `location`
--

DROP TABLE IF EXISTS `location`;
CREATE TABLE `location` (
  `id` bigint(20) NOT NULL auto_increment,
  `selectable` tinyint(1) NOT NULL default '0',
  `address` varchar(50) NOT NULL default '',
  `mailaddress` varchar(100) default NULL,
  `contactname` varchar(50) default NULL,
  `email` varchar(50) default NULL,
  `detailurl` varchar(200) default NULL,
  `mapurl` varchar(200) default NULL,
  `name` varchar(50) NOT NULL default '',
  `phone` varchar(30) default NULL,
  `owner` varchar(50) default NULL,
  `maxattendants` int(11) default NULL,
  `feeperday` double default NULL,
  `description` text,
  `organizationid` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `FK714F9FB5F4E66D55` (`organizationid`),
  CONSTRAINT `FK714F9FB5F4E66D55` FOREIGN KEY (`organizationid`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table `course`
--

DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` bigint(20) NOT NULL auto_increment,
  `instructorid` bigint(20) NOT NULL default '0',
  `responsibleid` bigint(20) NOT NULL default '0',
  `serviceareaid` bigint(20) NOT NULL default '0',
  `detailurl` varchar(200) default NULL,
  `duration` varchar(100) NOT NULL default '',
  `feexternal` double NOT NULL default '0',
  `feeinternal` double NOT NULL default '0',
  `freezeAttendance` datetime NOT NULL default '0000-00-00 00:00:00',
  `maxattendants` int(11) NOT NULL default '0',
  `name` varchar(100) NOT NULL default '',
  `registerby` datetime NOT NULL default '0000-00-00 00:00:00',
  `registerstart` datetime default NULL,
  `reminder` datetime default NULL,
  `reservedinternal` int(11) NOT NULL default '0',
  `starttime` datetime NOT NULL default '0000-00-00 00:00:00',
  `stoptime` datetime NOT NULL default '0000-00-00 00:00:00',
  `type` varchar(50) default NULL,
  `description` text,
  `locationid` bigint(20) NOT NULL default '0',
  `organizationid` bigint(20) NOT NULL default '0',
  `role` varchar(50) NOT NULL default 'Anonymous',
  PRIMARY KEY  (`id`),
  KEY `FKAF42E01B179C401B` (`serviceareaid`),
  KEY `FKAF42E01BC4F84E71` (`instructorid`),
  KEY `FKAF42E01B99F3E2E9` (`locationid`),
  KEY `FKAF42E01BF4E66D55` (`organizationid`),
  CONSTRAINT `FKAF42E01B179C401B` FOREIGN KEY (`serviceareaid`) REFERENCES `servicearea` (`id`),
  CONSTRAINT `FKAF42E01B99F3E2E9` FOREIGN KEY (`locationid`) REFERENCES `location` (`id`),
  CONSTRAINT `FKAF42E01BC4F84E71` FOREIGN KEY (`instructorid`) REFERENCES `person` (`id`),
  CONSTRAINT `FKAF42E01BF4E66D55` FOREIGN KEY (`organizationid`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table `attachment`
--

DROP TABLE IF EXISTS `attachment`;
CREATE TABLE `attachment` (
  `id` bigint(20) NOT NULL auto_increment,
  `contenttype` varchar(100) default NULL,
  `courseid` bigint(20) default NULL,
  `filename` varchar(100) default NULL,
  `locationid` bigint(20) default NULL,
  `size` bigint(20) default NULL,
  `storedname` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK8AF7592399F3E2E9` (`locationid`),
  KEY `FK8AF759236C3B7B35` (`courseid`),
  CONSTRAINT `FK8AF759236C3B7B35` FOREIGN KEY (`courseid`) REFERENCES `course` (`id`),
  CONSTRAINT `FK8AF7592399F3E2E9` FOREIGN KEY (`locationid`) REFERENCES `location` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table `notification`
--

DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
  `id` bigint(20) NOT NULL auto_increment,
  `registrationid` bigint(20) NOT NULL,
  `reminderSent` bit(1) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK237A88EB5F05D1F1` (`registrationid`)
) ENGINE=MyISAM AUTO_INCREMENT=513 DEFAULT CHARSET=utf8;

--
-- Definition of table `registration`
--

DROP TABLE IF EXISTS `registration`;
CREATE TABLE `registration` (
  `id` bigint(20) NOT NULL auto_increment,
  `courseid` bigint(20) NOT NULL default '0',
  `email` varchar(50) NOT NULL default '',
  `employeenumber` int(11) default NULL,
  `ezuserid` int(11) default NULL,
  `firstname` varchar(30) NOT NULL default '',
  `invoiced` tinyint(1) NOT NULL default '0',
  `jobtitle` varchar(30) default NULL,
  `lastname` varchar(30) NOT NULL default '',
  `mobilephone` varchar(30) default NULL,
  `organizationid` bigint(20) NOT NULL default '0',
  `phone` varchar(30) default NULL,
  `registered` datetime NOT NULL default '0000-00-00 00:00:00',
  `reserved` tinyint(1) NOT NULL default '0',
  `serviceareaid` bigint(20) NOT NULL default '0',
  `usemailaddress` varchar(100) default NULL,
  `locale` varchar(10) default NULL,
  `comment` varchar(100) default NULL,
  `workplace` varchar(100) default NULL,
  `attended` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `FKAF83E8B9179C401B` (`serviceareaid`),
  KEY `FKAF83E8B9F4E66D55` (`organizationid`),
  KEY `FKAF83E8B96C3B7B35` (`courseid`),
  CONSTRAINT `FKAF83E8B9179C401B` FOREIGN KEY (`serviceareaid`) REFERENCES `servicearea` (`id`),
  CONSTRAINT `FKAF83E8B96C3B7B35` FOREIGN KEY (`courseid`) REFERENCES `course` (`id`),
  CONSTRAINT `FKAF83E8B9F4E66D55` FOREIGN KEY (`organizationid`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;