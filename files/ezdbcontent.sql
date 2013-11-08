-- MySQL dump 10.11
--
-- Host: localhost    Database: ezdemodb
-- ------------------------------------------------------
-- Server version	5.0.95

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ezapprove_items`
--

DROP TABLE IF EXISTS `ezapprove_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezapprove_items` (
  `collaboration_id` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `workflow_process_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezapprove_items`
--

LOCK TABLES `ezapprove_items` WRITE;
/*!40000 ALTER TABLE `ezapprove_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezapprove_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezbasket`
--

DROP TABLE IF EXISTS `ezbasket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezbasket` (
  `id` int(11) NOT NULL auto_increment,
  `order_id` int(11) NOT NULL default '0',
  `productcollection_id` int(11) NOT NULL default '0',
  `session_id` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`id`),
  KEY `ezbasket_session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezbasket`
--

LOCK TABLES `ezbasket` WRITE;
/*!40000 ALTER TABLE `ezbasket` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezbasket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezbinaryfile`
--

DROP TABLE IF EXISTS `ezbinaryfile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezbinaryfile` (
  `contentobject_attribute_id` int(11) NOT NULL default '0',
  `download_count` int(11) NOT NULL default '0',
  `filename` varchar(255) NOT NULL default '',
  `mime_type` varchar(255) NOT NULL default '',
  `original_filename` varchar(255) NOT NULL default '',
  `version` int(11) NOT NULL default '0',
  PRIMARY KEY  (`contentobject_attribute_id`,`version`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezbinaryfile`
--

LOCK TABLES `ezbinaryfile` WRITE;
/*!40000 ALTER TABLE `ezbinaryfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezbinaryfile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcobj_state`
--

DROP TABLE IF EXISTS `ezcobj_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcobj_state` (
  `default_language_id` int(11) NOT NULL default '0',
  `group_id` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `identifier` varchar(45) NOT NULL default '',
  `language_mask` int(11) NOT NULL default '0',
  `priority` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `ezcobj_state_identifier` (`group_id`,`identifier`),
  KEY `ezcobj_state_lmask` (`language_mask`),
  KEY `ezcobj_state_priority` (`priority`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcobj_state`
--

LOCK TABLES `ezcobj_state` WRITE;
/*!40000 ALTER TABLE `ezcobj_state` DISABLE KEYS */;
INSERT INTO `ezcobj_state` VALUES (2,2,1,'not_locked',3,0),(2,2,2,'locked',3,1);
/*!40000 ALTER TABLE `ezcobj_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcobj_state_group`
--

DROP TABLE IF EXISTS `ezcobj_state_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcobj_state_group` (
  `default_language_id` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `identifier` varchar(45) NOT NULL default '',
  `language_mask` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `ezcobj_state_group_identifier` (`identifier`),
  KEY `ezcobj_state_group_lmask` (`language_mask`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcobj_state_group`
--

LOCK TABLES `ezcobj_state_group` WRITE;
/*!40000 ALTER TABLE `ezcobj_state_group` DISABLE KEYS */;
INSERT INTO `ezcobj_state_group` VALUES (2,2,'ez_lock',3);
/*!40000 ALTER TABLE `ezcobj_state_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcobj_state_group_language`
--

DROP TABLE IF EXISTS `ezcobj_state_group_language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcobj_state_group_language` (
  `contentobject_state_group_id` int(11) NOT NULL default '0',
  `description` longtext NOT NULL,
  `language_id` int(11) NOT NULL default '0',
  `name` varchar(45) NOT NULL default '',
  PRIMARY KEY  (`contentobject_state_group_id`,`language_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcobj_state_group_language`
--

LOCK TABLES `ezcobj_state_group_language` WRITE;
/*!40000 ALTER TABLE `ezcobj_state_group_language` DISABLE KEYS */;
INSERT INTO `ezcobj_state_group_language` VALUES (2,'',3,'Lock');
/*!40000 ALTER TABLE `ezcobj_state_group_language` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcobj_state_language`
--

DROP TABLE IF EXISTS `ezcobj_state_language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcobj_state_language` (
  `contentobject_state_id` int(11) NOT NULL default '0',
  `description` longtext NOT NULL,
  `language_id` int(11) NOT NULL default '0',
  `name` varchar(45) NOT NULL default '',
  PRIMARY KEY  (`contentobject_state_id`,`language_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcobj_state_language`
--

LOCK TABLES `ezcobj_state_language` WRITE;
/*!40000 ALTER TABLE `ezcobj_state_language` DISABLE KEYS */;
INSERT INTO `ezcobj_state_language` VALUES (1,'',3,'Not locked'),(2,'',3,'Locked');
/*!40000 ALTER TABLE `ezcobj_state_language` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcobj_state_link`
--

DROP TABLE IF EXISTS `ezcobj_state_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcobj_state_link` (
  `contentobject_id` int(11) NOT NULL default '0',
  `contentobject_state_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`contentobject_id`,`contentobject_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcobj_state_link`
--

LOCK TABLES `ezcobj_state_link` WRITE;
/*!40000 ALTER TABLE `ezcobj_state_link` DISABLE KEYS */;
INSERT INTO `ezcobj_state_link` VALUES (1,1),(4,1),(10,1),(11,1),(12,1),(13,1),(14,1),(41,1),(42,1),(45,1),(49,1),(50,1),(51,1),(52,1),(54,1),(56,1),(72,1),(73,1),(77,1),(82,1),(84,1),(85,1),(87,1),(90,1),(92,1),(93,1),(94,1),(95,1),(97,1),(99,1),(100,1),(101,1),(102,1),(103,1),(105,1),(106,1),(107,1),(118,1),(119,1),(120,1),(122,1),(134,1),(140,1),(141,1);
/*!40000 ALTER TABLE `ezcobj_state_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcollab_group`
--

DROP TABLE IF EXISTS `ezcollab_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcollab_group` (
  `created` int(11) NOT NULL default '0',
  `depth` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `is_open` int(11) NOT NULL default '1',
  `modified` int(11) NOT NULL default '0',
  `parent_group_id` int(11) NOT NULL default '0',
  `path_string` varchar(255) NOT NULL default '',
  `priority` int(11) NOT NULL default '0',
  `title` varchar(255) NOT NULL default '',
  `user_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `ezcollab_group_depth` (`depth`),
  KEY `ezcollab_group_path` (`path_string`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcollab_group`
--

LOCK TABLES `ezcollab_group` WRITE;
/*!40000 ALTER TABLE `ezcollab_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezcollab_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcollab_item`
--

DROP TABLE IF EXISTS `ezcollab_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcollab_item` (
  `created` int(11) NOT NULL default '0',
  `creator_id` int(11) NOT NULL default '0',
  `data_float1` float NOT NULL default '0',
  `data_float2` float NOT NULL default '0',
  `data_float3` float NOT NULL default '0',
  `data_int1` int(11) NOT NULL default '0',
  `data_int2` int(11) NOT NULL default '0',
  `data_int3` int(11) NOT NULL default '0',
  `data_text1` longtext NOT NULL,
  `data_text2` longtext NOT NULL,
  `data_text3` longtext NOT NULL,
  `id` int(11) NOT NULL auto_increment,
  `modified` int(11) NOT NULL default '0',
  `status` int(11) NOT NULL default '1',
  `type_identifier` varchar(40) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcollab_item`
--

LOCK TABLES `ezcollab_item` WRITE;
/*!40000 ALTER TABLE `ezcollab_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezcollab_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcollab_item_group_link`
--

DROP TABLE IF EXISTS `ezcollab_item_group_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcollab_item_group_link` (
  `collaboration_id` int(11) NOT NULL default '0',
  `created` int(11) NOT NULL default '0',
  `group_id` int(11) NOT NULL default '0',
  `is_active` int(11) NOT NULL default '1',
  `is_read` int(11) NOT NULL default '0',
  `last_read` int(11) NOT NULL default '0',
  `modified` int(11) NOT NULL default '0',
  `user_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`collaboration_id`,`group_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcollab_item_group_link`
--

LOCK TABLES `ezcollab_item_group_link` WRITE;
/*!40000 ALTER TABLE `ezcollab_item_group_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezcollab_item_group_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcollab_item_message_link`
--

DROP TABLE IF EXISTS `ezcollab_item_message_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcollab_item_message_link` (
  `collaboration_id` int(11) NOT NULL default '0',
  `created` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `message_id` int(11) NOT NULL default '0',
  `message_type` int(11) NOT NULL default '0',
  `modified` int(11) NOT NULL default '0',
  `participant_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcollab_item_message_link`
--

LOCK TABLES `ezcollab_item_message_link` WRITE;
/*!40000 ALTER TABLE `ezcollab_item_message_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezcollab_item_message_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcollab_item_participant_link`
--

DROP TABLE IF EXISTS `ezcollab_item_participant_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcollab_item_participant_link` (
  `collaboration_id` int(11) NOT NULL default '0',
  `created` int(11) NOT NULL default '0',
  `is_active` int(11) NOT NULL default '1',
  `is_read` int(11) NOT NULL default '0',
  `last_read` int(11) NOT NULL default '0',
  `modified` int(11) NOT NULL default '0',
  `participant_id` int(11) NOT NULL default '0',
  `participant_role` int(11) NOT NULL default '1',
  `participant_type` int(11) NOT NULL default '1',
  PRIMARY KEY  (`collaboration_id`,`participant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcollab_item_participant_link`
--

LOCK TABLES `ezcollab_item_participant_link` WRITE;
/*!40000 ALTER TABLE `ezcollab_item_participant_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezcollab_item_participant_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcollab_item_status`
--

DROP TABLE IF EXISTS `ezcollab_item_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcollab_item_status` (
  `collaboration_id` int(11) NOT NULL default '0',
  `is_active` int(11) NOT NULL default '1',
  `is_read` int(11) NOT NULL default '0',
  `last_read` int(11) NOT NULL default '0',
  `user_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`collaboration_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcollab_item_status`
--

LOCK TABLES `ezcollab_item_status` WRITE;
/*!40000 ALTER TABLE `ezcollab_item_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezcollab_item_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcollab_notification_rule`
--

DROP TABLE IF EXISTS `ezcollab_notification_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcollab_notification_rule` (
  `collab_identifier` varchar(255) NOT NULL default '',
  `id` int(11) NOT NULL auto_increment,
  `user_id` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcollab_notification_rule`
--

LOCK TABLES `ezcollab_notification_rule` WRITE;
/*!40000 ALTER TABLE `ezcollab_notification_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezcollab_notification_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcollab_profile`
--

DROP TABLE IF EXISTS `ezcollab_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcollab_profile` (
  `created` int(11) NOT NULL default '0',
  `data_text1` longtext NOT NULL,
  `id` int(11) NOT NULL auto_increment,
  `main_group` int(11) NOT NULL default '0',
  `modified` int(11) NOT NULL default '0',
  `user_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcollab_profile`
--

LOCK TABLES `ezcollab_profile` WRITE;
/*!40000 ALTER TABLE `ezcollab_profile` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezcollab_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcollab_simple_message`
--

DROP TABLE IF EXISTS `ezcollab_simple_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcollab_simple_message` (
  `created` int(11) NOT NULL default '0',
  `creator_id` int(11) NOT NULL default '0',
  `data_float1` float NOT NULL default '0',
  `data_float2` float NOT NULL default '0',
  `data_float3` float NOT NULL default '0',
  `data_int1` int(11) NOT NULL default '0',
  `data_int2` int(11) NOT NULL default '0',
  `data_int3` int(11) NOT NULL default '0',
  `data_text1` longtext NOT NULL,
  `data_text2` longtext NOT NULL,
  `data_text3` longtext NOT NULL,
  `id` int(11) NOT NULL auto_increment,
  `message_type` varchar(40) NOT NULL default '',
  `modified` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcollab_simple_message`
--

LOCK TABLES `ezcollab_simple_message` WRITE;
/*!40000 ALTER TABLE `ezcollab_simple_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezcollab_simple_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcontent_language`
--

DROP TABLE IF EXISTS `ezcontent_language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcontent_language` (
  `disabled` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL default '0',
  `locale` varchar(20) NOT NULL default '',
  `name` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`id`),
  KEY `ezcontent_language_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcontent_language`
--

LOCK TABLES `ezcontent_language` WRITE;
/*!40000 ALTER TABLE `ezcontent_language` DISABLE KEYS */;
INSERT INTO `ezcontent_language` VALUES (0,2,'eng-GB','English (United Kingdom)');
/*!40000 ALTER TABLE `ezcontent_language` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcontentbrowsebookmark`
--

DROP TABLE IF EXISTS `ezcontentbrowsebookmark`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcontentbrowsebookmark` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  `node_id` int(11) NOT NULL default '0',
  `user_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `ezcontentbrowsebookmark_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcontentbrowsebookmark`
--

LOCK TABLES `ezcontentbrowsebookmark` WRITE;
/*!40000 ALTER TABLE `ezcontentbrowsebookmark` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezcontentbrowsebookmark` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcontentbrowserecent`
--

DROP TABLE IF EXISTS `ezcontentbrowserecent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcontentbrowserecent` (
  `created` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  `node_id` int(11) NOT NULL default '0',
  `user_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `ezcontentbrowserecent_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcontentbrowserecent`
--

LOCK TABLES `ezcontentbrowserecent` WRITE;
/*!40000 ALTER TABLE `ezcontentbrowserecent` DISABLE KEYS */;
INSERT INTO `ezcontentbrowserecent` VALUES (1358425936,1,'Guest accounts',12,14),(1283761947,2,'Administrator users',13,14),(1283762130,3,'eZ Publish',2,14),(1316098847,4,'Users group',5,14),(1358503275,6,'Kursansvarlige',67,14),(1284034995,7,'Forsiden',2,64),(1291812896,8,'Kursansvarlige',67,64),(1283934766,9,'Troms fylkeskommune',75,64),(1283937756,10,'Images',51,64),(1283860930,11,'Help?',80,64),(1283934015,12,'RSS feed',71,64),(1359038200,13,'Ansatte',68,14),(1316109619,14,'Opplæringsansvarlige',14,14);
/*!40000 ALTER TABLE `ezcontentbrowserecent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcontentclass`
--

DROP TABLE IF EXISTS `ezcontentclass`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcontentclass` (
  `always_available` int(11) NOT NULL default '0',
  `contentobject_name` varchar(255) default NULL,
  `created` int(11) NOT NULL default '0',
  `creator_id` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `identifier` varchar(50) NOT NULL default '',
  `initial_language_id` int(11) NOT NULL default '0',
  `is_container` int(11) NOT NULL default '0',
  `language_mask` int(11) NOT NULL default '0',
  `modified` int(11) NOT NULL default '0',
  `modifier_id` int(11) NOT NULL default '0',
  `remote_id` varchar(100) NOT NULL default '',
  `serialized_description_list` longtext,
  `serialized_name_list` longtext,
  `sort_field` int(11) NOT NULL default '1',
  `sort_order` int(11) NOT NULL default '1',
  `url_alias_name` varchar(255) default NULL,
  `version` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`,`version`),
  KEY `ezcontentclass_version` (`version`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcontentclass`
--

LOCK TABLES `ezcontentclass` WRITE;
/*!40000 ALTER TABLE `ezcontentclass` DISABLE KEYS */;
INSERT INTO `ezcontentclass` VALUES (1,'<short_name|name>',1024392098,14,1,'folder',2,1,3,1082454875,14,'a3d405b81be900468eb153d774f4f0d2',NULL,'a:2:{s:6:\"eng-GB\";s:6:\"Folder\";s:16:\"always-available\";s:6:\"eng-GB\";}',1,1,NULL,0),(0,'<short_title|title>',1024392098,14,2,'article',2,1,3,1082454989,14,'c15b600eb9198b1924063b5a68758232',NULL,'a:2:{s:6:\"eng-GB\";s:7:\"Article\";s:16:\"always-available\";s:6:\"eng-GB\";}',1,1,NULL,0),(1,'<name>',1024392098,14,3,'user_group',2,1,3,1048494743,14,'25b4268cdcd01921b808a0d854b877ef',NULL,'a:2:{s:6:\"eng-GB\";s:10:\"User group\";s:16:\"always-available\";s:6:\"eng-GB\";}',1,1,NULL,0),(1,'<first_name> <last_name>',1024392098,14,4,'user',2,0,3,1082018364,14,'40faa822edc579b02c25f6bb7beec3ad',NULL,'a:2:{s:6:\"eng-GB\";s:4:\"User\";s:16:\"always-available\";s:6:\"eng-GB\";}',1,1,NULL,0),(1,'<name>',1031484992,8,5,'image',2,0,3,1048494784,14,'f6df12aa74e36230eb675f364fccd25a',NULL,'a:2:{s:6:\"eng-GB\";s:5:\"Image\";s:16:\"always-available\";s:6:\"eng-GB\";}',1,1,NULL,0),(0,'<name>',1052385361,14,11,'link',2,0,3,1082455072,14,'74ec6507063150bc813549b22534ad48',NULL,'a:2:{s:6:\"eng-GB\";s:4:\"Link\";s:16:\"always-available\";s:6:\"eng-GB\";}',1,1,NULL,0),(1,'<name>',1052385472,14,12,'file',2,0,3,1052385669,14,'637d58bfddf164627bdfd265733280a0',NULL,'a:2:{s:6:\"eng-GB\";s:4:\"File\";s:16:\"always-available\";s:6:\"eng-GB\";}',1,1,NULL,0),(0,'<subject>',1052385685,14,13,'comment',2,0,3,1082455144,14,'000c14f4f475e9f2955dedab72799941',NULL,'a:2:{s:6:\"eng-GB\";s:7:\"Comment\";s:16:\"always-available\";s:6:\"eng-GB\";}',1,1,NULL,0),(1,'<name>',1081858024,14,14,'common_ini_settings',2,0,3,1081858024,14,'ffedf2e73b1ea0c3e630e42e2db9c900',NULL,'a:2:{s:6:\"eng-GB\";s:19:\"Common ini settings\";s:16:\"always-available\";s:6:\"eng-GB\";}',1,1,NULL,0),(1,'<title>',1081858045,14,15,'template_look',2,0,3,1081858045,14,'59b43cd9feaaf0e45ac974fb4bbd3f92',NULL,'a:2:{s:6:\"eng-GB\";s:13:\"Template look\";s:16:\"always-available\";s:6:\"eng-GB\";}',1,1,NULL,0);
/*!40000 ALTER TABLE `ezcontentclass` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcontentclass_attribute`
--

DROP TABLE IF EXISTS `ezcontentclass_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcontentclass_attribute` (
  `can_translate` int(11) default '1',
  `category` varchar(25) NOT NULL default '',
  `contentclass_id` int(11) NOT NULL default '0',
  `data_float1` float default NULL,
  `data_float2` float default NULL,
  `data_float3` float default NULL,
  `data_float4` float default NULL,
  `data_int1` int(11) default NULL,
  `data_int2` int(11) default NULL,
  `data_int3` int(11) default NULL,
  `data_int4` int(11) default NULL,
  `data_text1` varchar(50) default NULL,
  `data_text2` varchar(50) default NULL,
  `data_text3` varchar(50) default NULL,
  `data_text4` varchar(255) default NULL,
  `data_text5` longtext,
  `data_type_string` varchar(50) NOT NULL default '',
  `id` int(11) NOT NULL auto_increment,
  `identifier` varchar(50) NOT NULL default '',
  `is_information_collector` int(11) NOT NULL default '0',
  `is_required` int(11) NOT NULL default '0',
  `is_searchable` int(11) NOT NULL default '0',
  `placement` int(11) NOT NULL default '0',
  `serialized_data_text` longtext,
  `serialized_description_list` longtext,
  `serialized_name_list` longtext NOT NULL,
  `version` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`,`version`),
  KEY `ezcontentclass_attr_ccid` (`contentclass_id`)
) ENGINE=InnoDB AUTO_INCREMENT=181 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcontentclass_attribute`
--

LOCK TABLES `ezcontentclass_attribute` WRITE;
/*!40000 ALTER TABLE `ezcontentclass_attribute` DISABLE KEYS */;
INSERT INTO `ezcontentclass_attribute` VALUES (1,'',2,0,0,0,0,255,0,0,0,'New article','','','','','ezstring',1,'title',0,1,1,1,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:5:\"Title\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',1,0,0,0,0,255,0,0,0,'Folder','','','','','ezstring',4,'name',0,1,1,1,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:4:\"Name\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',3,0,0,0,0,255,0,0,0,'','','','',NULL,'ezstring',6,'name',0,1,1,1,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:4:\"Name\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',3,0,0,0,0,255,0,0,0,'','','','',NULL,'ezstring',7,'description',0,0,1,2,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:11:\"Description\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',4,0,0,0,0,255,0,0,0,'','','','','','ezstring',8,'first_name',0,1,1,1,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:10:\"First name\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',4,0,0,0,0,255,0,0,0,'','','','','','ezstring',9,'last_name',0,1,1,2,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:9:\"Last name\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',4,0,0,0,0,0,0,0,0,'','','','','','ezuser',12,'user_account',0,1,1,3,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:12:\"User account\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',5,0,0,0,0,150,0,0,0,'','','','',NULL,'ezstring',116,'name',0,1,1,1,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:4:\"Name\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',5,0,0,0,0,10,0,0,0,'','','','',NULL,'ezxmltext',117,'caption',0,0,1,2,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:7:\"Caption\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',5,0,0,0,0,2,0,0,0,'','','','',NULL,'ezimage',118,'image',0,0,0,3,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:5:\"Image\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',1,0,0,0,0,5,0,0,0,'','','','','','ezxmltext',119,'short_description',0,0,1,3,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:17:\"Short description\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',2,0,0,0,0,10,0,0,0,'','','','','','ezxmltext',120,'intro',0,1,1,4,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:5:\"Intro\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',2,0,0,0,0,20,0,0,0,'','','','','','ezxmltext',121,'body',0,0,1,5,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:4:\"Body\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(0,'',2,0,0,0,0,0,0,0,0,'','','','','','ezboolean',123,'enable_comments',0,0,0,6,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:15:\"Enable comments\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',11,0,0,0,0,255,0,0,0,'','','','','','ezstring',143,'name',0,1,1,1,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:4:\"Name\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',11,0,0,0,0,20,0,0,0,'','','','','','ezxmltext',144,'description',0,0,1,2,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:11:\"Description\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',11,0,0,0,0,0,0,0,0,'','','','','','ezurl',145,'location',0,0,0,3,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:8:\"Location\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',12,0,0,0,0,0,0,0,0,'New file','','','',NULL,'ezstring',146,'name',0,1,1,1,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:4:\"Name\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',12,0,0,0,0,10,0,0,0,'','','','',NULL,'ezxmltext',147,'description',0,0,1,2,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:11:\"Description\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',12,0,0,0,0,0,0,0,0,'','','','',NULL,'ezbinaryfile',148,'file',0,1,0,3,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:4:\"File\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',13,0,0,0,0,100,0,0,0,'','','','','','ezstring',149,'subject',0,1,1,1,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:7:\"Subject\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',13,0,0,0,0,0,0,0,0,'','','','','','ezstring',150,'author',0,1,1,2,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:6:\"Author\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',13,0,0,0,0,20,0,0,0,'','','','','','eztext',151,'message',0,1,1,3,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:7:\"Message\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',2,0,0,0,0,255,0,0,0,'','','','','','ezstring',152,'short_title',0,0,1,2,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:11:\"Short title\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',2,0,0,0,0,0,0,0,0,'','','','','','ezauthor',153,'author',0,0,0,3,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:6:\"Author\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',2,0,0,0,0,0,0,0,0,'','','','','','ezobjectrelation',154,'image',0,0,1,7,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:5:\"Image\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',1,0,0,0,0,100,0,0,0,'','','','','','ezstring',155,'short_name',0,0,1,2,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:10:\"Short name\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',1,0,0,0,0,20,0,0,0,'','','','','','ezxmltext',156,'description',0,0,1,4,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:11:\"Description\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(0,'',1,0,0,0,0,0,0,1,0,'','','','','','ezboolean',158,'show_children',0,0,0,5,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:13:\"Show children\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',14,0,0,0,0,0,0,0,0,'','','','','','ezstring',159,'name',0,0,1,1,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:4:\"Name\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',14,0,0,0,0,1,0,0,0,'site.ini','SiteSettings','IndexPage','','override;user;admin;demo','ezinisetting',160,'indexpage',0,0,0,2,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:10:\"Index Page\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',14,0,0,0,0,1,0,0,0,'site.ini','SiteSettings','DefaultPage','','override;user;admin;demo','ezinisetting',161,'defaultpage',0,0,0,3,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:12:\"Default Page\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',14,0,0,0,0,2,0,0,0,'site.ini','DebugSettings','DebugOutput','','override;user;admin;demo','ezinisetting',162,'debugoutput',0,0,0,4,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:12:\"Debug Output\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',14,0,0,0,0,2,0,0,0,'site.ini','DebugSettings','DebugByIP','','override;user;admin;demo','ezinisetting',163,'debugbyip',0,0,0,5,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:11:\"Debug By IP\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',14,0,0,0,0,6,0,0,0,'site.ini','DebugSettings','DebugIPList','','override;user;admin;demo','ezinisetting',164,'debugiplist',0,0,0,6,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:13:\"Debug IP List\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',14,0,0,0,0,2,0,0,0,'site.ini','DebugSettings','DebugRedirection','','override;user;admin;demo','ezinisetting',165,'debugredirection',0,0,0,7,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:17:\"Debug Redirection\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',14,0,0,0,0,2,0,0,0,'site.ini','ContentSettings','ViewCaching','','override;user;admin;demo','ezinisetting',166,'viewcaching',0,0,0,8,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:12:\"View Caching\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',14,0,0,0,0,2,0,0,0,'site.ini','TemplateSettings','TemplateCache','','override;user;admin;demo','ezinisetting',167,'templatecache',0,0,0,9,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:14:\"Template Cache\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',14,0,0,0,0,2,0,0,0,'site.ini','TemplateSettings','TemplateCompile','','override;user;admin;demo','ezinisetting',168,'templatecompile',0,0,0,10,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:16:\"Template Compile\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',14,0,0,0,0,6,0,0,0,'image.ini','small','Filters','','override;user;admin;demo','ezinisetting',169,'imagesmall',0,0,0,11,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:16:\"Image Small Size\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',14,0,0,0,0,6,0,0,0,'image.ini','medium','Filters','','override;user;admin;demo','ezinisetting',170,'imagemedium',0,0,0,12,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:17:\"Image Medium Size\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',14,0,0,0,0,6,0,0,0,'image.ini','large','Filters','','override;user;admin;demo','ezinisetting',171,'imagelarge',0,0,0,13,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:16:\"Image Large Size\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',15,0,0,0,0,1,0,0,0,'site.ini','SiteSettings','SiteName','','override;user;admin;demo','ezinisetting',172,'title',0,0,0,1,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:5:\"Title\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',15,0,0,0,0,6,0,0,0,'site.ini','SiteSettings','MetaDataArray','','override;user;admin;demo','ezinisetting',173,'meta_data',0,0,0,2,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:9:\"Meta data\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',15,0,0,0,0,0,0,0,0,'','','','','','ezimage',174,'image',0,0,0,3,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:5:\"Image\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',15,0,0,0,0,0,0,0,0,'sitestyle','','','','','ezpackage',175,'sitestyle',0,0,0,4,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:9:\"Sitestyle\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',15,0,0,0,0,0,0,0,0,'','','','','','ezstring',176,'id',0,0,1,5,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:2:\"id\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',15,0,0,0,0,1,0,0,0,'site.ini','MailSettings','AdminEmail','','override;user;admin;demo','ezinisetting',177,'email',0,0,0,6,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:5:\"Email\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',15,0,0,0,0,1,0,0,0,'site.ini','SiteSettings','SiteURL','','override;user;admin;demo','ezinisetting',178,'siteurl',0,0,0,7,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:8:\"Site URL\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',4,0,0,0,0,10,0,0,0,'','','','','','eztext',179,'signature',0,0,1,4,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:9:\"Signature\";s:16:\"always-available\";s:6:\"eng-GB\";}',0),(1,'',4,0,0,0,0,1,0,0,0,'','','','','','ezimage',180,'image',0,0,0,5,NULL,NULL,'a:2:{s:6:\"eng-GB\";s:5:\"Image\";s:16:\"always-available\";s:6:\"eng-GB\";}',0);
/*!40000 ALTER TABLE `ezcontentclass_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcontentclass_classgroup`
--

DROP TABLE IF EXISTS `ezcontentclass_classgroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcontentclass_classgroup` (
  `contentclass_id` int(11) NOT NULL default '0',
  `contentclass_version` int(11) NOT NULL default '0',
  `group_id` int(11) NOT NULL default '0',
  `group_name` varchar(255) default NULL,
  PRIMARY KEY  (`contentclass_id`,`contentclass_version`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcontentclass_classgroup`
--

LOCK TABLES `ezcontentclass_classgroup` WRITE;
/*!40000 ALTER TABLE `ezcontentclass_classgroup` DISABLE KEYS */;
INSERT INTO `ezcontentclass_classgroup` VALUES (1,0,1,'Content'),(2,0,1,'Content'),(3,0,2,'Users'),(4,0,2,'Users'),(5,0,3,'Media'),(11,0,1,'Content'),(12,0,3,'Media'),(13,0,1,'Content'),(14,0,4,'Setup'),(15,0,4,'Setup');
/*!40000 ALTER TABLE `ezcontentclass_classgroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcontentclass_name`
--

DROP TABLE IF EXISTS `ezcontentclass_name`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcontentclass_name` (
  `contentclass_id` int(11) NOT NULL default '0',
  `contentclass_version` int(11) NOT NULL default '0',
  `language_id` int(11) NOT NULL default '0',
  `language_locale` varchar(20) NOT NULL default '',
  `name` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`contentclass_id`,`contentclass_version`,`language_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcontentclass_name`
--

LOCK TABLES `ezcontentclass_name` WRITE;
/*!40000 ALTER TABLE `ezcontentclass_name` DISABLE KEYS */;
INSERT INTO `ezcontentclass_name` VALUES (1,0,3,'eng-GB','Folder'),(2,0,3,'eng-GB','Article'),(3,0,3,'eng-GB','User group'),(4,0,3,'eng-GB','User'),(5,0,3,'eng-GB','Image'),(11,0,3,'eng-GB','Link'),(12,0,3,'eng-GB','File'),(13,0,3,'eng-GB','Comment'),(14,0,3,'eng-GB','Common ini settings'),(15,0,3,'eng-GB','Template look');
/*!40000 ALTER TABLE `ezcontentclass_name` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcontentclassgroup`
--

DROP TABLE IF EXISTS `ezcontentclassgroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcontentclassgroup` (
  `created` int(11) NOT NULL default '0',
  `creator_id` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `modified` int(11) NOT NULL default '0',
  `modifier_id` int(11) NOT NULL default '0',
  `name` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcontentclassgroup`
--

LOCK TABLES `ezcontentclassgroup` WRITE;
/*!40000 ALTER TABLE `ezcontentclassgroup` DISABLE KEYS */;
INSERT INTO `ezcontentclassgroup` VALUES (1031216928,14,1,1033922106,14,'Content'),(1031216941,14,2,1033922113,14,'Users'),(1032009743,14,3,1033922120,14,'Media'),(1081858024,14,4,1081858024,14,'Setup');
/*!40000 ALTER TABLE `ezcontentclassgroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcontentobject`
--

DROP TABLE IF EXISTS `ezcontentobject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcontentobject` (
  `contentclass_id` int(11) NOT NULL default '0',
  `current_version` int(11) default NULL,
  `id` int(11) NOT NULL auto_increment,
  `initial_language_id` int(11) NOT NULL default '0',
  `is_published` int(11) default NULL,
  `language_mask` int(11) NOT NULL default '0',
  `modified` int(11) NOT NULL default '0',
  `name` varchar(255) default NULL,
  `owner_id` int(11) NOT NULL default '0',
  `published` int(11) NOT NULL default '0',
  `remote_id` varchar(100) default NULL,
  `section_id` int(11) NOT NULL default '0',
  `status` int(11) default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `ezcontentobject_remote_id` (`remote_id`),
  KEY `ezcontentobject_classid` (`contentclass_id`),
  KEY `ezcontentobject_currentversion` (`current_version`),
  KEY `ezcontentobject_lmask` (`language_mask`),
  KEY `ezcontentobject_owner` (`owner_id`),
  KEY `ezcontentobject_pub` (`published`),
  KEY `ezcontentobject_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=142 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcontentobject`
--

LOCK TABLES `ezcontentobject` WRITE;
/*!40000 ALTER TABLE `ezcontentobject` DISABLE KEYS */;
INSERT INTO `ezcontentobject` VALUES (1,9,1,2,0,3,1295528043,'Forsiden',14,1033917596,'9459d3c29e15006e45197295722c7ade',1,1),(3,3,4,2,0,3,1282806074,'Users group',14,1033917596,'f5c88a2209584891056f987fd965b0ba',2,1),(4,2,10,2,0,3,1072180405,'Anonymous User',14,1033920665,'faaeb9be3bd98ed09f606fc16d144eca',2,1),(3,1,11,2,0,3,1033920746,'Guest accounts',14,1033920746,'5f7f0bdb3381d6a461d8c29ff53d908f',2,1),(3,1,12,2,0,3,1033920775,'Administrator users',14,1033920775,'9b47a45624b023b1a76c73b74d704acf',2,1),(3,2,13,2,0,3,1283764048,'Opplæringsansvarlige',14,1033920794,'3c160cca19fb135f83bd02d911f04db2',2,1),(4,1,14,2,0,3,1033920830,'Administrator User',14,1033920830,'1bb4fe25487f05527efa8bfd394cecc7',2,1),(1,1,41,2,0,3,1060695457,'Media',14,1060695457,'a6e35cbcb7cd6ae4b691f3eee30cd262',3,1),(3,1,42,2,0,3,1072180330,'Anonymous Users',14,1072180330,'15b256dbea2ae72418ff5facc999e8f9',2,1),(1,1,45,2,0,3,1079684190,'Setup',14,1079684190,'241d538ce310074e602f29f49e44e938',4,1),(1,1,49,2,0,3,1080220197,'Images',14,1080220197,'e7ff633c6b8e0fd3531e74c6e712bead',3,1),(1,1,50,2,0,3,1080220220,'Files',14,1080220220,'732a5acd01b51a6fe6eab448ad4138a9',3,1),(1,1,51,2,0,3,1080220233,'Multimedia',14,1080220233,'09082deb98662a104f325aaa8c4933d3',3,1),(14,1,52,2,0,2,1082016591,'Common INI settings',14,1082016591,'27437f3547db19cf81a33c92578b2c89',4,1),(15,1,54,2,0,2,1082016652,'eZ Publish',14,1082016652,'8b8b22fe3c6061ed500fbd2b377b885f',5,1),(1,1,56,2,0,3,1103023132,'Design',14,1103023132,'08799e609893f7aba22f10cb466d9cc8',5,1),(3,1,72,2,0,3,1283764063,'Kursansvarlige',14,1283764063,'7303af5a8e82f9ab5efc3eb4c03ac794',2,1),(3,1,73,2,0,3,1283764074,'Ansatte',14,1283764074,'736b7646bc945a78fe2c3e160062532d',2,1),(2,28,77,2,0,2,1295529432,'RSS feed',0,1283769648,'34faa7af0c2f477424d09f7be5b903c2',1,1),(1,19,82,2,0,3,1295528265,'Storholmen',0,1283857505,'56760d2a625669e539579142acb260c6',1,1),(5,1,84,2,0,3,1283858528,'FylkeshusetFront',0,1283858528,'932531e719ad979d5bc49c6a2f61b2a8',3,1),(5,1,85,2,0,3,1283859632,'FylkeshusetFront',0,1283859632,'f49c2e2a9213e4f44e5a50e14927b3c6',3,1),(1,5,87,2,0,3,1285150404,'Help?',0,1283860725,'951976a5560c05bfec15f0af9683c528',1,1),(5,1,90,2,0,3,1283864415,'128px-Feed-icon_svg',0,1283864415,'07d0391a600175102ea95510311cb77d',3,1),(5,1,92,2,0,3,1283930105,'FylkeshusetFront',0,1283930105,'5eb7e6650d073085ebd1e4fd7bbc3acf',3,1),(5,1,93,2,0,3,1283931318,'FylkeshusetFront',0,1283931318,'2717575c880f9be0c2c8bb565edc1588',3,1),(5,2,94,2,0,3,1283932330,'Fylkeshuset',0,1283931783,'5dacc376c742fe04d47523af900ba4ef',1,2),(5,2,95,2,0,3,1283932114,'RSS Feed',0,1283931925,'7a0d2c0f4ed9b6b24b4f5cee6b6cd906',1,1),(5,1,97,2,0,3,1283933210,'128px-Feed-icon',0,1283933210,'eac764514ca514e80a4f4d83fc56839c',1,1),(5,2,99,2,0,3,1283935394,'Følg oss på Flickr >>',0,1283934249,'4c1b64d789bc0d2b332f1f9447385ce7',3,1),(2,17,100,2,0,2,1289823010,'Følg oss på sosiale medier',0,1283934766,'83f6dd64624bd6659f70be8ee8769253',1,2),(5,1,101,2,0,3,1283935822,'flickr',0,1283935822,'81059a9d33748ecaa20cec4c90acfbfe',1,2),(5,1,102,2,0,3,1283936573,'Følg oss på Facebook >>',0,1283936573,'1b5500f865960b76730f1f5241beb667',3,1),(5,1,103,2,0,3,1283937017,'facebook',0,1283937017,'3a83ab1e68ba5e2037fff47a51ded884',1,2),(5,1,105,2,0,3,1283937709,'flickr',0,1283937709,'3091e04e4811e09527c10ba9580ef5c5',3,1),(5,1,106,2,0,3,1283937756,'wikipedia',0,1283937756,'4724b7bd4766ec1629f5f1da346006ef',3,1),(5,1,107,2,0,3,1283937904,'wikipedia',0,1283937904,'71a0a4353b3e45af1beddf605a044e3b',1,2),(4,2,118,2,0,3,1345724758,'Kurs Testbruker',14,1295525608,'84ed2b50d9e04555ca6bf5103fdce46a',2,1),(4,1,119,2,0,3,1295527921,'Kurt Kursansvarlig',14,1295527921,'018fdc65d486fb6c12ae7d3df8889994',2,1),(4,1,120,2,0,3,1309526167,'Test Bruker',14,1309526167,'641ad5c7e8cdf57b4573e4a0e8f3493d',2,1),(4,2,122,2,0,3,1316109807,'Rolle Tester',14,1316109619,'46f221c7288f2b49af85f191aa63f5bc',2,1),(4,1,134,2,0,3,1353415505,'Gunnar Velle',14,1353415505,'22ac5d8dd5ce2244b73ed64c26d72cc6',2,1),(4,1,140,2,0,3,1358503275,'Test Test',14,1358503275,'d7c2a6c7c21024af5855b637e0612a47',2,1),(4,1,141,2,0,3,1359038200,'Gunnar Velle',14,1359038200,'d10a6f323ebaa952b5c65b881ea632ad',2,1);
/*!40000 ALTER TABLE `ezcontentobject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcontentobject_attribute`
--

DROP TABLE IF EXISTS `ezcontentobject_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcontentobject_attribute` (
  `attribute_original_id` int(11) default '0',
  `contentclassattribute_id` int(11) NOT NULL default '0',
  `contentobject_id` int(11) NOT NULL default '0',
  `data_float` float default NULL,
  `data_int` int(11) default NULL,
  `data_text` longtext,
  `data_type_string` varchar(50) default '',
  `id` int(11) NOT NULL auto_increment,
  `language_code` varchar(20) NOT NULL default '',
  `language_id` int(11) NOT NULL default '0',
  `sort_key_int` int(11) NOT NULL default '0',
  `sort_key_string` varchar(255) NOT NULL default '',
  `version` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`,`version`),
  KEY `ezcontentobject_attr_id` (`id`),
  KEY `ezcontentobject_attribute_co_id_ver_lang_code` (`contentobject_id`,`version`,`language_code`),
  KEY `ezcontentobject_attribute_contentobject_id` (`contentobject_id`),
  KEY `ezcontentobject_attribute_language_code` (`language_code`),
  KEY `sort_key_int` (`sort_key_int`),
  KEY `sort_key_string` (`sort_key_string`)
) ENGINE=InnoDB AUTO_INCREMENT=548 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcontentobject_attribute`
--

LOCK TABLES `ezcontentobject_attribute` WRITE;
/*!40000 ALTER TABLE `ezcontentobject_attribute` DISABLE KEYS */;
INSERT INTO `ezcontentobject_attribute` VALUES (0,4,1,0,0,'Velkomen til kursportalen for Troms fylkeskommune','ezstring',1,'eng-GB',3,0,'velkomen til kursportalen for troms fylkeskommune',6),(0,4,1,0,0,'Velkomen til kursportalen for Troms fylkeskommune','ezstring',1,'eng-GB',3,0,'velkomen til kursportalen for troms fylkeskommune',7),(0,4,1,0,0,'Velkommen til kursportalen for Troms fylkeskommune','ezstring',1,'eng-GB',3,0,'velkommen til kursportalen for troms fylkeskommune',8),(0,4,1,0,0,'Velkommen til kursportalen for Storholmen kommune','ezstring',1,'eng-GB',3,0,'velkommen til kursportalen for storholmen kommune',9),(0,119,1,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph>Her kan du finne informasjon om kurs, seminarer og arrangementer i Troms fylkeskommune.</paragraph><paragraph>Du finner også brukerveiledninger, manualer og veiledningsvideoer.</paragraph><paragraph>Skal du arrangere et kurs eller annet arrangement, vil disse sidene gjøre det enkelt både å gi ut informasjon og ta imot påmeldinger.&amp;nbsp;</paragraph></section>\n','ezxmltext',2,'eng-GB',3,0,'',6),(0,119,1,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',2,'eng-GB',3,0,'',7),(0,119,1,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',2,'eng-GB',3,0,'',8),(0,119,1,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',2,'eng-GB',3,0,'',9),(0,7,4,NULL,NULL,'Main group','ezstring',7,'eng-GB',3,0,'',1),(0,7,4,0,NULL,'Main group','ezstring',7,'eng-GB',3,0,'main group',2),(0,7,4,0,NULL,'Main group','ezstring',7,'eng-GB',3,0,'main group',3),(0,7,4,0,NULL,'Main group','ezstring',7,'eng-GB',2,0,'main group',4),(0,6,4,NULL,NULL,'Users','ezstring',8,'eng-GB',3,0,'',1),(0,6,4,0,NULL,'khalil','ezstring',8,'eng-GB',3,0,'khalil',2),(0,6,4,0,NULL,'Users group','ezstring',8,'eng-GB',3,0,'users group',3),(0,6,4,0,NULL,'Users group','ezstring',8,'eng-GB',2,0,'users group',4),(0,8,10,0,0,'Anonymous','ezstring',19,'eng-GB',3,0,'anonymous',2),(0,9,10,0,0,'User','ezstring',20,'eng-GB',3,0,'user',2),(0,12,10,0,0,'','ezuser',21,'eng-GB',3,0,'',2),(0,6,11,0,0,'Guest accounts','ezstring',22,'eng-GB',3,0,'',1),(0,7,11,0,0,'','ezstring',23,'eng-GB',3,0,'',1),(0,6,12,0,0,'Administrator users','ezstring',24,'eng-GB',3,0,'',1),(0,6,12,0,0,'Administrator users','ezstring',24,'eng-GB',2,0,'administrator users',2),(0,7,12,0,0,'','ezstring',25,'eng-GB',3,0,'',1),(0,7,12,0,0,'','ezstring',25,'eng-GB',2,0,'',2),(0,6,13,0,0,'Editors','ezstring',26,'eng-GB',3,0,'',1),(0,6,13,0,0,'Opplæringsansvarlige','ezstring',26,'eng-GB',3,0,'opplæringsansvarlige',2),(0,7,13,0,0,'','ezstring',27,'eng-GB',3,0,'',1),(0,7,13,0,0,'','ezstring',27,'eng-GB',3,0,'',2),(0,8,14,0,0,'Administrator','ezstring',28,'eng-GB',3,0,'',1),(0,9,14,0,0,'User','ezstring',29,'eng-GB',3,0,'',1),(0,12,14,0,0,'','ezuser',30,'eng-GB',3,0,'',1),(0,4,41,0,0,'Media','ezstring',98,'eng-GB',3,0,'',1),(0,119,41,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\"\n         xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\"\n         xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\" />','ezxmltext',99,'eng-GB',3,0,'',1),(0,6,42,0,0,'Anonymous Users','ezstring',100,'eng-GB',3,0,'anonymous users',1),(0,7,42,0,0,'User group for the anonymous user','ezstring',101,'eng-GB',3,0,'user group for the anonymous user',1),(0,155,1,0,0,'Forsiden','ezstring',102,'eng-GB',3,0,'forsiden',6),(0,155,1,0,0,'Forsiden','ezstring',102,'eng-GB',3,0,'forsiden',7),(0,155,1,0,0,'Forsiden','ezstring',102,'eng-GB',3,0,'forsiden',8),(0,155,1,0,0,'Forsiden','ezstring',102,'eng-GB',3,0,'forsiden',9),(0,155,41,0,0,'','ezstring',103,'eng-GB',3,0,'',1),(0,156,1,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><section><header>Documentation and guidance</header><paragraph>The <link target=\"_blank\" url_id=\"1\">eZ Publish documentation</link>&amp;nbsp;covers common topics related to the setup and daily use of the eZ Publish content management system/framework. In addition, it also covers some advanced topics. People who are unfamiliar with eZ Publish should at least read the \"eZ Publish basics\" chapter.</paragraph><paragraph>If you\'re unable to find an answer/solution to a specific question/problem within the documentation pages, you should make use of the official <link target=\"_blank\" url_id=\"4\">eZ Publish forum</link>. People who need professional help should purchase <link target=\"_blank\" url_id=\"5\">support</link>&amp;nbsp;or <link target=\"_blank\" url_id=\"6\">consulting</link>&amp;nbsp;services. It is also possible to sign up for various <link target=\"_blank\" url_id=\"7\">training sessions</link>.</paragraph><paragraph><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\">&amp;nbsp;For more information about eZ Publish and other products/services from eZ Systems, please visit <link target=\"_blank\" url_id=\"9\">ez.no</link>.</line><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\">&amp;nbsp;</line></paragraph></section></section>\n','ezxmltext',104,'eng-GB',3,0,'',6),(0,156,1,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph>Her kan du finne informasjon om kurs, seminarer og arrangementer i Troms fylkeskommune.</paragraph><paragraph>Du finner også brukerveiledninger, manualer og veiledningsvideoer.</paragraph><paragraph>Skal du arrangere et kurs eller annet arrangement, vil disse sidene gjøre det enkelt både å gi ut informasjon og ta imot påmeldinger.&amp;nbsp;</paragraph></section>\n','ezxmltext',104,'eng-GB',3,0,'',7),(0,156,1,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph>Her kan du finne informasjon om kurs, seminarer og arrangementer i Troms fylkeskommune.</paragraph><paragraph>Du finner også brukerveiledninger, manualer og veiledningsvideoer.</paragraph><paragraph>Skal du arrangere et kurs eller annet arrangement, vil disse sidene gjøre det enkelt både å gi ut informasjon og ta imot påmeldinger.&amp;nbsp;</paragraph></section>\n','ezxmltext',104,'eng-GB',3,0,'',8),(0,156,1,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph>Her kan du finne informasjon om kurs, seminarer og arrangementer i Storholmen kommune.</paragraph><paragraph>Du kan du også etter hvert finne brukerveiledninger, manualer og veiledningsvideoer.</paragraph><paragraph>Skal du arrangere et kurs eller annet arrangement, vil disse sidene gjøre det enkelt både å gi ut informasjon og ta imot påmeldinger.&amp;nbsp;</paragraph></section>\n','ezxmltext',104,'eng-GB',3,0,'',9),(0,156,41,0,1045487555,'','ezxmltext',105,'eng-GB',3,0,'',1),(108,158,1,0,1,'','ezboolean',108,'eng-GB',3,1,'',6),(108,158,1,0,1,'','ezboolean',108,'eng-GB',3,1,'',7),(108,158,1,0,1,'','ezboolean',108,'eng-GB',3,1,'',8),(108,158,1,0,1,'','ezboolean',108,'eng-GB',3,1,'',9),(0,158,41,0,0,'','ezboolean',109,'eng-GB',3,0,'',1),(0,4,45,0,0,'Setup','ezstring',123,'eng-GB',3,0,'setup',1),(0,155,45,0,0,'','ezstring',124,'eng-GB',3,0,'',1),(0,119,45,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\"\n         xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\"\n         xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\" />','ezxmltext',125,'eng-GB',3,0,'',1),(0,156,45,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\"\n         xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\"\n         xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\" />','ezxmltext',126,'eng-GB',3,0,'',1),(0,158,45,0,0,'','ezboolean',128,'eng-GB',3,0,'',1),(0,4,49,0,0,'Images','ezstring',142,'eng-GB',3,0,'images',1),(0,155,49,0,0,'','ezstring',143,'eng-GB',3,0,'',1),(0,119,49,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\"\n         xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\"\n         xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\" />','ezxmltext',144,'eng-GB',3,0,'',1),(0,156,49,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\"\n         xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\"\n         xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\" />','ezxmltext',145,'eng-GB',3,0,'',1),(0,158,49,0,1,'','ezboolean',146,'eng-GB',3,1,'',1),(0,4,50,0,0,'Files','ezstring',147,'eng-GB',3,0,'files',1),(0,155,50,0,0,'','ezstring',148,'eng-GB',3,0,'',1),(0,119,50,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\"\n         xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\"\n         xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\" />','ezxmltext',149,'eng-GB',3,0,'',1),(0,156,50,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\"\n         xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\"\n         xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\" />','ezxmltext',150,'eng-GB',3,0,'',1),(0,158,50,0,1,'','ezboolean',151,'eng-GB',3,1,'',1),(0,4,51,0,0,'Multimedia','ezstring',152,'eng-GB',3,0,'multimedia',1),(0,155,51,0,0,'','ezstring',153,'eng-GB',3,0,'',1),(0,119,51,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\"\n         xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\"\n         xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\" />','ezxmltext',154,'eng-GB',3,0,'',1),(0,156,51,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\"\n         xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\"\n         xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\" />','ezxmltext',155,'eng-GB',3,0,'',1),(0,158,51,0,1,'','ezboolean',156,'eng-GB',3,1,'',1),(0,159,52,0,0,'Common INI settings','ezstring',157,'eng-GB',2,0,'common ini settings',1),(0,160,52,0,0,'/content/view/full/2/','ezinisetting',158,'eng-GB',2,0,'',1),(0,161,52,0,0,'/content/view/full/2','ezinisetting',159,'eng-GB',2,0,'',1),(0,162,52,0,0,'disabled','ezinisetting',160,'eng-GB',2,0,'',1),(0,163,52,0,0,'disabled','ezinisetting',161,'eng-GB',2,0,'',1),(0,164,52,0,0,'','ezinisetting',162,'eng-GB',2,0,'',1),(0,165,52,0,0,'enabled','ezinisetting',163,'eng-GB',2,0,'',1),(0,166,52,0,0,'disabled','ezinisetting',164,'eng-GB',2,0,'',1),(0,167,52,0,0,'enabled','ezinisetting',165,'eng-GB',2,0,'',1),(0,168,52,0,0,'enabled','ezinisetting',166,'eng-GB',2,0,'',1),(0,169,52,0,0,'=geometry/scale=100;100','ezinisetting',167,'eng-GB',2,0,'',1),(0,170,52,0,0,'=geometry/scale=200;200','ezinisetting',168,'eng-GB',2,0,'',1),(0,171,52,0,0,'=geometry/scale=300;300','ezinisetting',169,'eng-GB',2,0,'',1),(0,172,54,0,0,'eZ Publish','ezinisetting',170,'eng-GB',2,0,'',1),(0,173,54,0,0,'author=eZ Systems\ncopyright=eZ Systems\ndescription=Content Management System\nkeywords=cms, publish, e-commerce, content management, development framework','ezinisetting',171,'eng-GB',2,0,'',1),(0,174,54,0,0,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<ezimage serial_number=\"1\"\n         is_valid=\"\"\n         filename=\"ez_publish.\"\n         suffix=\"\"\n         basename=\"ez_publish\"\n         dirpath=\"var/storage/images/setup/ez_publish/172-1-eng-GB\"\n         url=\"var/storage/images/setup/ez_publish/172-1-eng-GB/ez_publish.\"\n         original_filename=\"\"\n         mime_type=\"\"\n         width=\"\"\n         height=\"\"\n         alternative_text=\"\"\n         alias_key=\"1293033771\"\n         timestamp=\"1082016632\">\n  <original attribute_id=\"\"\n            attribute_version=\"\"\n            attribute_language=\"\" />\n</ezimage>','ezimage',172,'eng-GB',2,0,'',1),(0,175,54,0,0,'0','ezpackage',173,'eng-GB',2,0,'0',1),(0,176,54,0,0,'sitestyle_identifier','ezstring',174,'eng-GB',2,0,'sitestyle_identifier',1),(0,177,54,0,0,'nospam@ez.no','ezinisetting',175,'eng-GB',2,0,'',1),(0,178,54,0,0,'ez.no','ezinisetting',176,'eng-GB',2,0,'',1),(0,179,10,0,0,'','eztext',177,'eng-GB',3,0,'',2),(0,179,14,0,0,'','eztext',178,'eng-GB',3,0,'',1),(0,180,10,0,0,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"\" is_valid=\"\" filename=\"\" suffix=\"\" basename=\"\" dirpath=\"\" url=\"\" original_filename=\"\" mime_type=\"\" width=\"\" height=\"\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1286795607\"/>\n','ezimage',179,'eng-GB',3,0,'',2),(0,180,14,0,0,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"\" is_valid=\"\" filename=\"\" suffix=\"\" basename=\"\" dirpath=\"\" url=\"\" original_filename=\"\" mime_type=\"\" width=\"\" height=\"\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1282052024\"/>\n','ezimage',180,'eng-GB',3,0,'',1),(0,4,56,0,NULL,'Design','ezstring',181,'eng-GB',3,0,'design',1),(0,155,56,0,NULL,'','ezstring',182,'eng-GB',3,0,'',1),(0,119,56,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\"\n         xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\"\n         xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\" />','ezxmltext',183,'eng-GB',3,0,'',1),(0,156,56,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\"\n         xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\"\n         xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\" />','ezxmltext',184,'eng-GB',3,0,'',1),(0,158,56,0,1,'','ezboolean',185,'eng-GB',3,1,'',1),(0,6,72,0,NULL,'Kursansvarlige','ezstring',250,'eng-GB',3,0,'kursansvarlige',1),(0,7,72,0,NULL,'','ezstring',251,'eng-GB',3,0,'',1),(0,6,73,0,NULL,'Ansatte','ezstring',252,'eng-GB',3,0,'ansatte',1),(0,7,73,0,NULL,'','ezstring',253,'eng-GB',3,0,'',1),(0,1,77,0,NULL,'RSS feed','ezstring',267,'eng-GB',2,0,'rss feed',19),(0,1,77,0,NULL,'RSS feed','ezstring',267,'eng-GB',2,0,'rss feed',20),(0,1,77,0,NULL,'RSS feed','ezstring',267,'eng-GB',2,0,'rss feed',21),(0,1,77,0,NULL,'RSS feed','ezstring',267,'eng-GB',2,0,'rss feed',22),(0,1,77,0,NULL,'RSS feed','ezstring',267,'eng-GB',2,0,'rss feed',23),(0,1,77,0,NULL,'RSS feed','ezstring',267,'eng-GB',2,0,'rss feed',24),(0,1,77,0,NULL,'RSS feed fra Troms fylkeskommune','ezstring',267,'eng-GB',2,0,'rss feed fra troms fylkeskommune',25),(0,1,77,0,NULL,'RSS feed fra Troms fylkeskommune','ezstring',267,'eng-GB',2,0,'rss feed fra troms fylkeskommune',26),(0,1,77,0,NULL,'RSS feed fra Troms fylkeskommune','ezstring',267,'eng-GB',2,0,'rss feed fra troms fylkeskommune',27),(0,1,77,0,NULL,'RSS feed fra Troms fylkeskommune','ezstring',267,'eng-GB',2,0,'rss feed fra troms fylkeskommune',28),(0,152,77,0,NULL,'RSS feed','ezstring',268,'eng-GB',2,0,'rss feed',19),(0,152,77,0,NULL,'RSS feed','ezstring',268,'eng-GB',2,0,'rss feed',20),(0,152,77,0,NULL,'RSS feed','ezstring',268,'eng-GB',2,0,'rss feed',21),(0,152,77,0,NULL,'RSS feed','ezstring',268,'eng-GB',2,0,'rss feed',22),(0,152,77,0,NULL,'RSS feed','ezstring',268,'eng-GB',2,0,'rss feed',23),(0,152,77,0,NULL,'RSS feed','ezstring',268,'eng-GB',2,0,'rss feed',24),(0,152,77,0,NULL,'RSS feed','ezstring',268,'eng-GB',2,0,'rss feed',25),(0,152,77,0,NULL,'RSS feed','ezstring',268,'eng-GB',2,0,'rss feed',26),(0,152,77,0,NULL,'RSS feed','ezstring',268,'eng-GB',2,0,'rss feed',27),(0,152,77,0,NULL,'RSS feed','ezstring',268,'eng-GB',2,0,'rss feed',28),(0,153,77,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Khalil Dahbi\" email=\"khalil.dahbi@tromsfylke.no\"/></authors></ezauthor>\n','ezauthor',269,'eng-GB',2,0,'',19),(0,153,77,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Khalil Dahbi\" email=\"khalil.dahbi@tromsfylke.no\"/></authors></ezauthor>\n','ezauthor',269,'eng-GB',2,0,'',20),(0,153,77,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Khalil Dahbi\" email=\"khalil.dahbi@tromsfylke.no\"/></authors></ezauthor>\n','ezauthor',269,'eng-GB',2,0,'',21),(0,153,77,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Khalil Dahbi\" email=\"khalil.dahbi@tromsfylke.no\"/></authors></ezauthor>\n','ezauthor',269,'eng-GB',2,0,'',22),(0,153,77,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Khalil Dahbi\" email=\"khalil.dahbi@tromsfylke.no\"/></authors></ezauthor>\n','ezauthor',269,'eng-GB',2,0,'',23),(0,153,77,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Khalil Dahbi\" email=\"khalil.dahbi@tromsfylke.no\"/></authors></ezauthor>\n','ezauthor',269,'eng-GB',2,0,'',24),(0,153,77,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Khalil Dahbi\" email=\"khalil.dahbi@tromsfylke.no\"/></authors></ezauthor>\n','ezauthor',269,'eng-GB',2,0,'',25),(0,153,77,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Khalil Dahbi\" email=\"khalil.dahbi@tromsfylke.no\"/></authors></ezauthor>\n','ezauthor',269,'eng-GB',2,0,'',26),(0,153,77,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Vevredaktøren i Storholmen kommune\" email=\"nospam_storholmen@frikomport.no\"/></authors></ezauthor>\n','ezauthor',269,'eng-GB',2,0,'',27),(0,153,77,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Vevredaktøren i Storholmen\" email=\"storholmen@frikomport.no\"/></authors></ezauthor>\n','ezauthor',269,'eng-GB',2,0,'',28),(0,120,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><embed size=\"rss\" view=\"embed\" object_id=\"97\"/>&amp;nbsp;RSS feed fra <link url_id=\"29\">www.tromsfylke.no</link></paragraph></section>\n','ezxmltext',270,'eng-GB',2,0,'',19),(0,120,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><embed size=\"rss\" view=\"embed\" object_id=\"97\"/>&amp;nbsp;RSS feed fra <link url_id=\"29\">www.tromsfylke.no</link></paragraph><paragraph>&amp;nbsp;<link url_id=\"39\"><embed size=\"medium\" view=\"embed\" object_id=\"98\"/></link></paragraph></section>\n','ezxmltext',270,'eng-GB',2,0,'',20),(0,120,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><embed size=\"rss\" view=\"embed\" object_id=\"97\"/>&amp;nbsp;RSS feed fra <link url_id=\"29\">www.tromsfylke.no</link></paragraph><paragraph><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\">&amp;nbsp;<embed size=\"small\" view=\"embed\" object_id=\"98\"/></line><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\">&gt;\" href=\"http://www.flickr.com/photos/tromsfylke/\" mce_href=\"http://www.flickr.com/photos/tromsfylke/\"&gt;<embed size=\"small\" view=\"embed\" object_id=\"99\"/></line></paragraph></section>\n','ezxmltext',270,'eng-GB',2,0,'',21),(0,120,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><embed size=\"rss\" view=\"embed\" object_id=\"97\"/>&amp;nbsp;RSS feed fra <link url_id=\"29\">www.tromsfylke.no</link></paragraph><paragraph><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\">&amp;nbsp;<link url_id=\"40\"><embed size=\"small\" view=\"embed\" object_id=\"98\"/></link></line><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\"><link url_id=\"41\"><embed size=\"small\" view=\"embed\" object_id=\"99\"/></link></line></paragraph></section>\n','ezxmltext',270,'eng-GB',2,0,'',22),(0,120,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><embed size=\"rss\" view=\"embed\" object_id=\"97\"/>&amp;nbsp;RSS feed fra <link url_id=\"11\">www.tromsfylke.no</link></paragraph></section>\n','ezxmltext',270,'eng-GB',2,0,'',23),(0,120,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><embed size=\"rss\" view=\"embed\" object_id=\"97\"/>RSS feed fra <link url_id=\"29\">www.tromsfylke.no</link></paragraph></section>\n','ezxmltext',270,'eng-GB',2,0,'',24),(0,120,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><embed size=\"rss\" view=\"embed\" object_id=\"97\"/>RSS feed fra <link url_id=\"29\">www.tromsfylke.no</link></paragraph></section>\n','ezxmltext',270,'eng-GB',2,0,'',25),(0,120,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph>Eksempel på RSS feeds fra <link url_id=\"29\">www.tromsfylke.no</link></paragraph></section>\n','ezxmltext',270,'eng-GB',2,0,'',26),(0,120,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph>Eksempel på RSS feeds fra <link url_id=\"29\">www.tromsfylke.no</link></paragraph></section>\n','ezxmltext',270,'eng-GB',2,0,'',27),(0,120,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph>Eksempel på RSS feeds fra <link url_id=\"29\">www.tromsfylke.no</link></paragraph></section>\n','ezxmltext',270,'eng-GB',2,0,'',28),(0,121,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><table border=\"0\"><tr><td><paragraph>&amp;nbsp;<link url_id=\"30\">Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"31\">Utdanning</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"32\">Miljø og Samferdsel</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"33\">Næring</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"34\">Tannhelse</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"35\">Politikk</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"36\">Kultur</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"37\">Samiske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"38\">Engelske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link xhtml:id=\"__mce_tmp\" url_id=\"38\">FLK</link></paragraph></td></tr></table></paragraph></section>\n','ezxmltext',271,'eng-GB',2,0,'',19),(0,121,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><table border=\"0\"><tr><td><paragraph>&amp;nbsp;<link url_id=\"30\">Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"31\">Utdanning</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"32\">Miljø og Samferdsel</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"33\">Næring</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"34\">Tannhelse</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"35\">Politikk</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"36\">Kultur</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"37\">Samiske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"38\">Engelske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link xhtml:id=\"__mce_tmp\" url_id=\"38\">FLK</link></paragraph></td></tr></table></paragraph></section>\n','ezxmltext',271,'eng-GB',2,0,'',20),(0,121,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><table border=\"0\"><tr><td><paragraph>&amp;nbsp;<link url_id=\"30\">Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"31\">Utdanning</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"32\">Miljø og Samferdsel</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"33\">Næring</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"34\">Tannhelse</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"35\">Politikk</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"36\">Kultur</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"37\">Samiske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"38\">Engelske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link xhtml:id=\"__mce_tmp\" url_id=\"38\">FLK</link></paragraph></td></tr></table></paragraph></section>\n','ezxmltext',271,'eng-GB',2,0,'',21),(0,121,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><table border=\"0\"><tr><td><paragraph>&amp;nbsp;<link url_id=\"30\">Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"31\">Utdanning</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"32\">Miljø og Samferdsel</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"33\">Næring</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"34\">Tannhelse</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"35\">Politikk</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"36\">Kultur</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"37\">Samiske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"38\">Engelske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link xhtml:id=\"__mce_tmp\" url_id=\"38\">FLK</link></paragraph></td></tr></table></paragraph></section>\n','ezxmltext',271,'eng-GB',2,0,'',22),(0,121,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><table border=\"0\"><tr><td><paragraph>&amp;nbsp;<link url_id=\"30\">Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"31\">Utdanning</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"32\">Miljø og Samferdsel</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"33\">Næring</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"34\">Tannhelse</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"35\">Politikk</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"36\">Kultur</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"37\">Samiske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"38\">Engelske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link xhtml:id=\"__mce_tmp\" url_id=\"38\">FLK</link></paragraph></td></tr></table></paragraph></section>\n','ezxmltext',271,'eng-GB',2,0,'',23),(0,121,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><table border=\"0\"><tr><td><paragraph>&amp;nbsp;<link url_id=\"30\">Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"31\">Utdanning</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"32\">Miljø og Samferdsel</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"33\">Næring</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"34\">Tannhelse</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"35\">Politikk</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"36\">Kultur</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"37\">Samiske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"38\">Engelske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link xhtml:id=\"__mce_tmp\" url_id=\"38\">FLK</link></paragraph></td></tr></table></paragraph></section>\n','ezxmltext',271,'eng-GB',2,0,'',24),(0,121,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><table border=\"0\"><tr><td><paragraph>&amp;nbsp;<link url_id=\"30\">Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"31\">Utdanning</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"32\">Miljø og Samferdsel</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"33\">Næring</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"34\">Tannhelse</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"35\">Politikk</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"36\">Kultur</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"37\">Samiske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"38\">Engelske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link xhtml:id=\"__mce_tmp\" url_id=\"38\">FLK</link></paragraph></td></tr></table></paragraph></section>\n','ezxmltext',271,'eng-GB',2,0,'',25),(0,121,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><table border=\"0\"><tr><td><paragraph>&amp;nbsp;<link url_id=\"30\">Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"31\">Utdanning</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"32\">Miljø og Samferdsel</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"33\">Næring</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"34\">Tannhelse</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"35\">Politikk</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"36\">Kultur</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"37\">Samiske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"38\">Engelske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link xhtml:id=\"__mce_tmp\" url_id=\"38\">FLK</link></paragraph></td></tr></table></paragraph></section>\n','ezxmltext',271,'eng-GB',2,0,'',26),(0,121,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><table border=\"0\"><tr><td><paragraph>&amp;nbsp;<link url_id=\"30\">Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"31\">Utdanning</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"32\">Miljø og Samferdsel</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"33\">Næring</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"34\">Tannhelse</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"35\">Politikk</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"36\">Kultur</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"37\">Samiske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"38\">Engelske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link xhtml:id=\"__mce_tmp\" url_id=\"38\">FLK</link></paragraph></td></tr></table></paragraph></section>\n','ezxmltext',271,'eng-GB',2,0,'',27),(0,121,77,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><table border=\"0\"><tr><td><paragraph>&amp;nbsp;<link url_id=\"30\">Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"31\">Utdanning</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"32\">Miljø og Samferdsel</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"33\">Næring</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"34\">Tannhelse</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"35\">Politikk</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"36\">Kultur</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"37\">Samiske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link url_id=\"38\">Engelske Nyheter</link></paragraph></td></tr><tr><td><paragraph>&amp;nbsp;<link xhtml:id=\"__mce_tmp\" url_id=\"38\">FLK</link></paragraph></td></tr></table></paragraph></section>\n','ezxmltext',271,'eng-GB',2,0,'',28),(0,123,77,0,0,'','ezboolean',272,'eng-GB',2,0,'',19),(0,123,77,0,0,'','ezboolean',272,'eng-GB',2,0,'',20),(0,123,77,0,0,'','ezboolean',272,'eng-GB',2,0,'',21),(0,123,77,0,0,'','ezboolean',272,'eng-GB',2,0,'',22),(0,123,77,0,0,'','ezboolean',272,'eng-GB',2,0,'',23),(0,123,77,0,0,'','ezboolean',272,'eng-GB',2,0,'',24),(0,123,77,0,0,'','ezboolean',272,'eng-GB',2,0,'',25),(0,123,77,0,0,'','ezboolean',272,'eng-GB',2,0,'',26),(0,123,77,0,0,'','ezboolean',272,'eng-GB',2,0,'',27),(0,123,77,0,0,'','ezboolean',272,'eng-GB',2,0,'',28),(0,154,77,0,NULL,'','ezobjectrelation',273,'eng-GB',2,0,'',19),(0,154,77,0,NULL,'','ezobjectrelation',273,'eng-GB',2,0,'',20),(0,154,77,0,NULL,'','ezobjectrelation',273,'eng-GB',2,0,'',21),(0,154,77,0,NULL,'','ezobjectrelation',273,'eng-GB',2,0,'',22),(0,154,77,0,NULL,'','ezobjectrelation',273,'eng-GB',2,0,'',23),(0,154,77,0,NULL,'','ezobjectrelation',273,'eng-GB',2,0,'',24),(0,154,77,0,NULL,'','ezobjectrelation',273,'eng-GB',2,0,'',25),(0,154,77,0,NULL,'','ezobjectrelation',273,'eng-GB',2,0,'',26),(0,154,77,0,NULL,'','ezobjectrelation',273,'eng-GB',2,0,'',27),(0,154,77,0,NULL,'','ezobjectrelation',273,'eng-GB',2,0,'',28),(0,4,82,0,NULL,'Troms fylkeskommune','ezstring',288,'eng-GB',3,0,'troms fylkeskommune',15),(0,4,82,0,NULL,'Troms fylkeskommune','ezstring',288,'eng-GB',3,0,'troms fylkeskommune',16),(0,4,82,0,NULL,'Troms fylkeskommune','ezstring',288,'eng-GB',3,0,'troms fylkeskommune',17),(0,4,82,0,NULL,'Storholmen kommune','ezstring',288,'eng-GB',3,0,'storholmen kommune',18),(0,4,82,0,NULL,'Storholmen kommune','ezstring',288,'eng-GB',3,0,'storholmen kommune',19),(0,155,82,0,NULL,'Troms fylkeskommune','ezstring',289,'eng-GB',3,0,'troms fylkeskommune',15),(0,155,82,0,NULL,'Troms fylkeskommune','ezstring',289,'eng-GB',3,0,'troms fylkeskommune',16),(0,155,82,0,NULL,'Troms fylkeskommune','ezstring',289,'eng-GB',3,0,'troms fylkeskommune',17),(0,155,82,0,NULL,'storholmen','ezstring',289,'eng-GB',3,0,'storholmen',18),(0,155,82,0,NULL,'Storholmen','ezstring',289,'eng-GB',3,0,'storholmen',19),(0,119,82,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><strong>Fylkeskommunen tar seg av oppgaver som er for store til at hver kommune kan klare dem alene, eller saker som går på tvers av kommunene.</strong></paragraph><paragraph><link url_id=\"24\">Forside</link>&amp;nbsp; <link url_id=\"25\">Tjenester</link>&amp;nbsp; <link url_id=\"26\">Politikk</link>&amp;nbsp; <link url_id=\"27\">Om fylkeskommunen</link>&amp;nbsp; <link url_id=\"28\">Om Troms</link>&amp;nbsp; </paragraph></section>\n','ezxmltext',290,'eng-GB',3,0,'',15),(0,119,82,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><strong>Fylkeskommunen tar seg av oppgaver som er for store til at hver kommune kan klare dem alene, eller saker som går på tvers av kommunene.</strong></paragraph><paragraph><link url_id=\"24\">Forside</link>&amp;nbsp; <link url_id=\"25\">Tjenester</link>&amp;nbsp; <link url_id=\"26\">Politikk</link>&amp;nbsp; <link url_id=\"27\">Om fylkeskommunen</link>&amp;nbsp; <link url_id=\"28\">Om Troms</link>&amp;nbsp; </paragraph></section>\n','ezxmltext',290,'eng-GB',3,0,'',16),(0,119,82,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><strong>Fylkeskommunen tar seg av oppgaver som er for store til at hver kommune kan klare dem alene, eller saker som går på tvers av kommunene.</strong></paragraph><paragraph><link url_id=\"24\">Forside</link>&amp;nbsp; <link url_id=\"25\">Tjenester</link>&amp;nbsp; <link url_id=\"26\">Politikk</link>&amp;nbsp; <link url_id=\"27\">Om fylkeskommunen</link>&amp;nbsp; <link url_id=\"28\">Om Troms</link>&amp;nbsp; </paragraph></section>\n','ezxmltext',290,'eng-GB',3,0,'',17),(0,119,82,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><strong>Kommunen tar seg av mange viktige oppgaver for sine innbyggere.</strong></paragraph></section>\n','ezxmltext',290,'eng-GB',3,0,'',18),(0,119,82,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><strong>Kommunen tar seg av mange viktige oppgaver for sine innbyggere.</strong></paragraph></section>\n','ezxmltext',290,'eng-GB',3,0,'',19),(0,156,82,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><table width=\"0%\" border=\"0\"><tr><td><paragraph><embed size=\"large\" align=\"left\" view=\"embed\" object_id=\"93\"/></paragraph></td><td><paragraph>Fylkeskommunen arbeider for at hele regionen vil være et attraktivt og godt sted å bo, arbeide og leve i,&amp;nbsp;og at det også vil være et&amp;nbsp;trivelig og spennende reisemål.&amp;nbsp;Økt verdiskaping og bærekraftig utvikling er nøkkelord i denne sammenheng. </paragraph><paragraph>Fylkeskommunen har bl.a. ansvaret for regional utvikling, videregående utdanning, fylkesveier og offentlig transport inklusiv skoleskyss, &amp;nbsp;offentlig tannhelsetjeneste, kulturminnevern, fylkesbibliotek, friluftsliv, fordeling av tippemidler og støtte til næringslivet.</paragraph><paragraph>De største arbeidsområdene er videregående utdanning og offentlig tannhelsetjeneste. Troms fylkeskommune har ansvaret for og driver&amp;nbsp;16 videregående skoler&amp;nbsp;som har&amp;nbsp;ca. 5700 elever pr. år og 1400 tilsatte.</paragraph><paragraph>Tannhelsetjenesten har 25 fast betjente klinikker, 1 sykehusklinikk, og 4 ambuleringsklinikker i tillegg til tannhelsetjenestens kompetansesenter for Nord-Norge som ligger i Tromsø.</paragraph><paragraph>Fylkestinget vedtok 14. oktober 2004 at Troms fylkeskommune skal ha parlamentarisk styringsform, dvs. at administrasjonen ledes av fylkesrådet. Fylkesrådet utnevnes av fylkestinget og har en tilsvarende funksjon i forhold til fylkestinget som Regjeringen har i forhold til Stortinget. Fylkesrådslederen er den øverste administrative lederen i Fylkeskommunen.</paragraph><paragraph>Her er en grafisk presentasjon av den <link url_id=\"23\">administrative organiseringen i Troms fylkeskommune.</link></paragraph><section><section><section><header>&amp;nbsp;</header></section></section></section></td></tr></table><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\">&amp;nbsp;</line></paragraph></section>\n','ezxmltext',291,'eng-GB',3,0,'',15),(0,156,82,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><table width=\"0%\" border=\"0\"><tr><td><paragraph xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\"><embed size=\"medium\" view=\"embed\" object_id=\"94\"/></paragraph></td><td><paragraph>Fylkeskommunen arbeider for at hele regionen vil være et attraktivt og godt sted å bo, arbeide og leve i,&amp;nbsp;og at det også vil være et&amp;nbsp;trivelig og spennende reisemål.&amp;nbsp;Økt verdiskaping og bærekraftig utvikling er nøkkelord i denne sammenheng. </paragraph><paragraph>Fylkeskommunen har bl.a. ansvaret for regional utvikling, videregående utdanning, fylkesveier og offentlig transport inklusiv skoleskyss, &amp;nbsp;offentlig tannhelsetjeneste, kulturminnevern, fylkesbibliotek, friluftsliv, fordeling av tippemidler og støtte til næringslivet.</paragraph><paragraph>De største arbeidsområdene er videregående utdanning og offentlig tannhelsetjeneste. Troms fylkeskommune har ansvaret for og driver&amp;nbsp;16 videregående skoler&amp;nbsp;som har&amp;nbsp;ca. 5700 elever pr. år og 1400 tilsatte.</paragraph><paragraph>Tannhelsetjenesten har 25 fast betjente klinikker, 1 sykehusklinikk, og 4 ambuleringsklinikker i tillegg til tannhelsetjenestens kompetansesenter for Nord-Norge som ligger i Tromsø.</paragraph><paragraph>Fylkestinget vedtok 14. oktober 2004 at Troms fylkeskommune skal ha parlamentarisk styringsform, dvs. at administrasjonen ledes av fylkesrådet. Fylkesrådet utnevnes av fylkestinget og har en tilsvarende funksjon i forhold til fylkestinget som Regjeringen har i forhold til Stortinget. Fylkesrådslederen er den øverste administrative lederen i Fylkeskommunen.</paragraph><paragraph>Her er en grafisk presentasjon av den <link url_id=\"23\">administrative organiseringen i Troms fylkeskommune.</link></paragraph><section><section><section><header>&amp;nbsp;</header></section></section></section></td></tr></table><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\">&amp;nbsp;</line></paragraph></section>\n','ezxmltext',291,'eng-GB',3,0,'',16),(0,156,82,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><table width=\"0%\" border=\"0\"><tr><td><paragraph><embed size=\"medium\" view=\"embed\" object_id=\"94\"/></paragraph></td><td><paragraph>Fylkeskommunen arbeider for at hele regionen vil være et attraktivt og godt sted å bo, arbeide og leve i,&amp;nbsp;og at det også vil være et&amp;nbsp;trivelig og spennende reisemål.&amp;nbsp;Økt verdiskaping og bærekraftig utvikling er nøkkelord i denne sammenheng. </paragraph><paragraph>Fylkeskommunen har bl.a. ansvaret for regional utvikling, videregående utdanning, fylkesveier og offentlig transport inklusiv skoleskyss, &amp;nbsp;offentlig tannhelsetjeneste, kulturminnevern, fylkesbibliotek, friluftsliv, fordeling av tippemidler og støtte til næringslivet.</paragraph><paragraph>De største arbeidsområdene er videregående utdanning og offentlig tannhelsetjeneste. Troms fylkeskommune har ansvaret for og driver&amp;nbsp;16 videregående skoler&amp;nbsp;som har&amp;nbsp;ca. 5700 elever pr. år og 1400 tilsatte.</paragraph><paragraph>Tannhelsetjenesten har 25 fast betjente klinikker, 1 sykehusklinikk, og 4 ambuleringsklinikker i tillegg til tannhelsetjenestens kompetansesenter for Nord-Norge som ligger i Tromsø.</paragraph><paragraph>Fylkestinget vedtok 14. oktober 2004 at Troms fylkeskommune skal ha parlamentarisk styringsform, dvs. at administrasjonen ledes av fylkesrådet. Fylkesrådet utnevnes av fylkestinget og har en tilsvarende funksjon i forhold til fylkestinget som Regjeringen har i forhold til Stortinget. Fylkesrådslederen er den øverste administrative lederen i Fylkeskommunen.&amp;nbsp;</paragraph></td></tr></table><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\">&amp;nbsp;</line></paragraph></section>\n','ezxmltext',291,'eng-GB',3,0,'',17),(0,156,82,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><table width=\"0%\" border=\"0\"><tr><td><paragraph><embed size=\"medium\" view=\"embed\" object_id=\"94\"/></paragraph></td><td><paragraph>Kommunen arbeider for at Storholmen vil være et attraktivt og godt sted å bo, arbeide og leve i,&amp;nbsp;og at det også vil være et&amp;nbsp;trivelig og spennende reisemål.&amp;nbsp;Økt verdiskaping og bærekraftig utvikling er nøkkelord i denne sammenheng. </paragraph><paragraph>Vi har 2 skoler, i en fantastisk natur på vår unike holme.</paragraph></td></tr></table><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\">&amp;nbsp;</line></paragraph></section>\n','ezxmltext',291,'eng-GB',3,0,'',18),(0,156,82,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><table width=\"0%\" border=\"0\"><tr><td><paragraph><embed size=\"medium\" view=\"embed\" object_id=\"94\"/></paragraph></td><td><paragraph>Kommunen arbeider for at Storholmen vil være et attraktivt og godt sted å bo, arbeide og leve i,&amp;nbsp;og at det også vil være et&amp;nbsp;trivelig og spennende reisemål.&amp;nbsp;Økt verdiskaping og bærekraftig utvikling er nøkkelord i denne sammenheng. </paragraph><paragraph>Vi har 2 skoler, i en fantastisk natur på vår unike holme.</paragraph></td></tr></table><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\">&amp;nbsp;</line></paragraph></section>\n','ezxmltext',291,'eng-GB',3,0,'',19),(292,158,82,0,1,'','ezboolean',292,'eng-GB',3,1,'',15),(292,158,82,0,1,'','ezboolean',292,'eng-GB',3,1,'',16),(292,158,82,0,1,'','ezboolean',292,'eng-GB',3,1,'',17),(292,158,82,0,1,'','ezboolean',292,'eng-GB',3,1,'',18),(292,158,82,0,1,'','ezboolean',292,'eng-GB',3,1,'',19),(0,116,84,0,NULL,'FylkeshusetFront','ezstring',296,'eng-GB',3,0,'fylkeshusetfront',1),(0,117,84,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',297,'eng-GB',3,0,'',1),(0,118,84,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"1\" filename=\"FylkeshusetFront.jpg\" suffix=\"jpg\" basename=\"FylkeshusetFront\" dirpath=\"var/intranet/storage/images/media/images/fylkeshusetfront/298-1-eng-GB\" url=\"var/intranet/storage/images/media/images/fylkeshusetfront/298-1-eng-GB/FylkeshusetFront.jpg\" original_filename=\"FylkeshusetFront.jpg\" mime_type=\"image/jpeg\" width=\"200\" height=\"150\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1283858528\"><original attribute_id=\"298\" attribute_version=\"1\" attribute_language=\"eng-GB\"/><information Height=\"150\" Width=\"200\" IsColor=\"1\"/><alias name=\"reference\" filename=\"FylkeshusetFront_reference.jpg\" suffix=\"jpg\" dirpath=\"var/intranet/storage/images/media/images/fylkeshusetfront/298-1-eng-GB\" url=\"var/intranet/storage/images/media/images/fylkeshusetfront/298-1-eng-GB/FylkeshusetFront_reference.jpg\" mime_type=\"image/jpeg\" width=\"200\" height=\"150\" alias_key=\"2605465115\" timestamp=\"1283858529\" is_valid=\"1\"/><alias name=\"small\" filename=\"FylkeshusetFront_small.jpg\" suffix=\"jpg\" dirpath=\"var/intranet/storage/images/media/images/fylkeshusetfront/298-1-eng-GB\" url=\"var/intranet/storage/images/media/images/fylkeshusetfront/298-1-eng-GB/FylkeshusetFront_small.jpg\" mime_type=\"image/jpeg\" width=\"100\" height=\"75\" alias_key=\"2343348577\" timestamp=\"1283859203\" is_valid=\"1\"/><alias name=\"medium\" filename=\"FylkeshusetFront_medium.jpg\" suffix=\"jpg\" dirpath=\"var/intranet/storage/images/media/images/fylkeshusetfront/298-1-eng-GB\" url=\"var/intranet/storage/images/media/images/fylkeshusetfront/298-1-eng-GB/FylkeshusetFront_medium.jpg\" mime_type=\"image/jpeg\" width=\"200\" height=\"150\" alias_key=\"405413724\" timestamp=\"1283858530\" is_valid=\"1\"/><alias name=\"large\" filename=\"FylkeshusetFront_large.jpg\" suffix=\"jpg\" dirpath=\"var/intranet/storage/images/media/images/fylkeshusetfront/298-1-eng-GB\" url=\"var/intranet/storage/images/media/images/fylkeshusetfront/298-1-eng-GB/FylkeshusetFront_large.jpg\" mime_type=\"image/jpeg\" width=\"200\" height=\"150\" alias_key=\"1592566908\" timestamp=\"1283859434\" is_valid=\"1\"/></ezimage>\n','ezimage',298,'eng-GB',3,0,'',1),(0,116,85,0,NULL,'FylkeshusetFront','ezstring',299,'eng-GB',3,0,'fylkeshusetfront',1),(0,117,85,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',300,'eng-GB',3,0,'',1),(0,118,85,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"1\" filename=\"FylkeshusetFront.jpg\" suffix=\"jpg\" basename=\"FylkeshusetFront\" dirpath=\"var/intranet/storage/images/media/images/fylkeshusetfront2/301-1-eng-GB\" url=\"var/intranet/storage/images/media/images/fylkeshusetfront2/301-1-eng-GB/FylkeshusetFront.jpg\" original_filename=\"FylkeshusetFront.jpg\" mime_type=\"image/jpeg\" width=\"200\" height=\"150\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1283859632\"><original attribute_id=\"301\" attribute_version=\"1\" attribute_language=\"eng-GB\"/><information Height=\"150\" Width=\"200\" IsColor=\"1\"/><alias name=\"reference\" filename=\"FylkeshusetFront_reference.jpg\" suffix=\"jpg\" dirpath=\"var/intranet/storage/images/media/images/fylkeshusetfront2/301-1-eng-GB\" url=\"var/intranet/storage/images/media/images/fylkeshusetfront2/301-1-eng-GB/FylkeshusetFront_reference.jpg\" mime_type=\"image/jpeg\" width=\"200\" height=\"150\" alias_key=\"2605465115\" timestamp=\"1283859633\" is_valid=\"1\"/><alias name=\"small\" filename=\"FylkeshusetFront_small.jpg\" suffix=\"jpg\" dirpath=\"var/intranet/storage/images/media/images/fylkeshusetfront2/301-1-eng-GB\" url=\"var/intranet/storage/images/media/images/fylkeshusetfront2/301-1-eng-GB/FylkeshusetFront_small.jpg\" mime_type=\"image/jpeg\" width=\"100\" height=\"75\" alias_key=\"2343348577\" timestamp=\"1283859633\" is_valid=\"1\"/><alias name=\"medium\" filename=\"FylkeshusetFront_medium.jpg\" suffix=\"jpg\" dirpath=\"var/intranet/storage/images/media/images/fylkeshusetfront2/301-1-eng-GB\" url=\"var/intranet/storage/images/media/images/fylkeshusetfront2/301-1-eng-GB/FylkeshusetFront_medium.jpg\" mime_type=\"image/jpeg\" width=\"200\" height=\"150\" alias_key=\"405413724\" timestamp=\"1283859633\" is_valid=\"1\"/><alias name=\"large\" filename=\"FylkeshusetFront_large.jpg\" suffix=\"jpg\" dirpath=\"var/intranet/storage/images/media/images/fylkeshusetfront2/301-1-eng-GB\" url=\"var/intranet/storage/images/media/images/fylkeshusetfront2/301-1-eng-GB/FylkeshusetFront_large.jpg\" mime_type=\"image/jpeg\" width=\"200\" height=\"150\" alias_key=\"1592566908\" timestamp=\"1283929801\" is_valid=\"1\"/></ezimage>\n','ezimage',301,'eng-GB',3,0,'',1),(0,4,87,0,NULL,'Help?','ezstring',305,'eng-GB',3,0,'help?',1),(0,4,87,0,NULL,'Help?','ezstring',305,'eng-GB',3,0,'help?',2),(0,4,87,0,NULL,'Help?','ezstring',305,'eng-GB',3,0,'help?',3),(0,4,87,0,NULL,'Help?','ezstring',305,'eng-GB',3,0,'help?',4),(0,4,87,0,NULL,'Help?','ezstring',305,'eng-GB',3,0,'help?',5),(0,155,87,0,NULL,'Help?','ezstring',306,'eng-GB',3,0,'help?',1),(0,155,87,0,NULL,'Help?','ezstring',306,'eng-GB',3,0,'help?',2),(0,155,87,0,NULL,'Help?','ezstring',306,'eng-GB',3,0,'help?',3),(0,155,87,0,NULL,'Help?','ezstring',306,'eng-GB',3,0,'help?',4),(0,155,87,0,NULL,'Help?','ezstring',306,'eng-GB',3,0,'help?',5),(0,119,87,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph>For brukere /&amp;nbsp;kursansvarlige / opplæringsansvarlige eller administrator</paragraph></section>\n','ezxmltext',307,'eng-GB',3,0,'',1),(0,119,87,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph>For brukere /&amp;nbsp;kursansvarlige / opplæringsansvarlige eller administrator</paragraph></section>\n','ezxmltext',307,'eng-GB',3,0,'',2),(0,119,87,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><strong>For brukere /&amp;nbsp;kursansvarlige / opplæringsansvarlige eller administrator</strong></paragraph></section>\n','ezxmltext',307,'eng-GB',3,0,'',3),(0,119,87,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><strong>For brukere /&amp;nbsp;kursansvarlige / opplæringsansvarlige eller administrator</strong></paragraph></section>\n','ezxmltext',307,'eng-GB',3,0,'',4),(0,119,87,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><strong>For brukere /&amp;nbsp;kursansvarlige / opplæringsansvarlige eller administrator</strong></paragraph></section>\n','ezxmltext',307,'eng-GB',3,0,'',5),(0,156,87,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',308,'eng-GB',3,0,'',1),(0,156,87,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph>For kursansvarlige / opplæringsansvarlige eller administrator:</paragraph><paragraph><link xhtml:title=\"Hvordan publisere kurs?\" url_id=\"15\">Hvordan publisere kurs?</link></paragraph><paragraph><link url_id=\"16\">Hvordan administrere filer / legg ved dokumentasjon til kurs?</link></paragraph><paragraph><link url_id=\"17\">Hvordan endre opplysninger om kurset?</link></paragraph><paragraph><link url_id=\"18\">Hvordan melde av en som er påmeldt?</link></paragraph><paragraph><link url_id=\"19\">Hvordan avlyse et kurs?</link></paragraph><paragraph><link url_id=\"20\">Hvordan slette et kurs?</link>&amp;nbsp;</paragraph></section>\n','ezxmltext',308,'eng-GB',3,0,'',2),(0,156,87,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph>For brukere:</paragraph><paragraph><link xhtml:title=\"Hva inneholder portalen og hvordan virker den?\" url_id=\"12\">Hva inneholder portalen og hvordan virker den?</link></paragraph><paragraph><link url_id=\"13\">Hvordan melde seg på kurs?</link></paragraph><paragraph><link url_id=\"14\">Hvordan oppdatere i kalender at du har fått plass på kurs?</link>&amp;nbsp;(for Notes brukere)</paragraph><paragraph>For kursansvarlige / opplæringsansvarlige eller administrator:</paragraph><paragraph><link xhtml:title=\"Hvordan publisere kurs?\" url_id=\"15\">Hvordan publisere kurs?</link></paragraph><paragraph><link url_id=\"16\">Hvordan administrere filer / legg ved dokumentasjon til kurs?</link></paragraph><paragraph><link url_id=\"17\">Hvordan endre opplysninger om kurset?</link></paragraph><paragraph><link url_id=\"18\">Hvordan melde av en som er påmeldt?</link></paragraph><paragraph><link url_id=\"19\">Hvordan avlyse et kurs?</link></paragraph><paragraph><link url_id=\"20\">Hvordan slette et kurs?</link>&amp;nbsp;</paragraph></section>\n','ezxmltext',308,'eng-GB',3,0,'',3),(0,156,87,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph>For brukere:</paragraph><paragraph><link xhtml:title=\"Hva inneholder portalen og hvordan virker den?\" url_id=\"12\">Hva inneholder portalen og hvordan virker den?</link></paragraph><paragraph><link url_id=\"13\">Hvordan melde seg på kurs?</link></paragraph><paragraph><link url_id=\"14\">Hvordan oppdatere i kalender at du har fått plass på kurs?</link>&amp;nbsp;(for Notes brukere)</paragraph><paragraph>For kursansvarlige / opplæringsansvarlige eller administrator:</paragraph><paragraph><link xhtml:title=\"Hvordan publisere kurs?\" url_id=\"15\">Hvordan publisere kurs?</link></paragraph><paragraph><link url_id=\"16\">Hvordan administrere filer / legg ved dokumentasjon til kurs?</link></paragraph><paragraph><link url_id=\"17\">Hvordan endre opplysninger om kurset?</link></paragraph><paragraph><link url_id=\"18\">Hvordan melde av en som er påmeldt?</link></paragraph><paragraph><link url_id=\"19\">Hvordan avlyse et kurs?</link></paragraph><paragraph><link url_id=\"20\">Hvordan slette et kurs?</link>&amp;nbsp;</paragraph></section>\n','ezxmltext',308,'eng-GB',3,0,'',4),(0,156,87,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph>For brukere:</paragraph><paragraph><link xhtml:title=\"Hva inneholder portalen og hvordan virker den?\" url_id=\"12\">Hva inneholder portalen og hvordan virker den?</link></paragraph><paragraph><link url_id=\"13\">Hvordan melde seg på kurs?</link></paragraph><paragraph><link url_id=\"14\">Hvordan oppdatere i kalender at du har fått plass på kurs?</link></paragraph><paragraph>For kursansvarlige / opplæringsansvarlige eller administrator:</paragraph><paragraph><link xhtml:title=\"Hvordan publisere kurs?\" url_id=\"15\">Hvordan publisere kurs?</link></paragraph><paragraph><link url_id=\"16\">Hvordan administrere filer / legg ved dokumentasjon til kurs?</link></paragraph><paragraph><link url_id=\"17\">Hvordan endre opplysninger om kurset?</link></paragraph><paragraph><link url_id=\"18\">Hvordan melde av en som er påmeldt?</link></paragraph><paragraph><link url_id=\"19\">Hvordan avlyse et kurs?</link></paragraph><paragraph><link url_id=\"20\">Hvordan slette et kurs?</link>&amp;nbsp;</paragraph></section>\n','ezxmltext',308,'eng-GB',3,0,'',5),(309,158,87,0,1,'','ezboolean',309,'eng-GB',3,1,'',1),(309,158,87,0,1,'','ezboolean',309,'eng-GB',3,1,'',2),(309,158,87,0,1,'','ezboolean',309,'eng-GB',3,1,'',3),(309,158,87,0,1,'','ezboolean',309,'eng-GB',3,1,'',4),(309,158,87,0,1,'','ezboolean',309,'eng-GB',3,1,'',5),(0,116,90,0,NULL,'128px-Feed-icon_svg','ezstring',320,'eng-GB',3,0,'128px-feed-icon_svg',1),(0,117,90,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',321,'eng-GB',3,0,'',1),(0,118,90,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"1\" filename=\"128px-Feed-icon_svg.png\" suffix=\"png\" basename=\"128px-Feed-icon_svg\" dirpath=\"var/intranet/storage/images/media/images/128px-feed-icon_svg/322-1-eng-GB\" url=\"var/intranet/storage/images/media/images/128px-feed-icon_svg/322-1-eng-GB/128px-Feed-icon_svg.png\" original_filename=\"128px-Feed-icon_svg.png\" mime_type=\"image/png\" width=\"128\" height=\"128\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1283864415\"><original attribute_id=\"322\" attribute_version=\"1\" attribute_language=\"eng-GB\"/><alias name=\"reference\" filename=\"128px-Feed-icon_svg_reference.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/media/images/128px-feed-icon_svg/322-1-eng-GB\" url=\"var/intranet/storage/images/media/images/128px-feed-icon_svg/322-1-eng-GB/128px-Feed-icon_svg_reference.png\" mime_type=\"image/png\" width=\"128\" height=\"128\" alias_key=\"2605465115\" timestamp=\"1283864416\" is_valid=\"1\"/><alias name=\"small\" filename=\"128px-Feed-icon_svg_small.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/media/images/128px-feed-icon_svg/322-1-eng-GB\" url=\"var/intranet/storage/images/media/images/128px-feed-icon_svg/322-1-eng-GB/128px-Feed-icon_svg_small.png\" mime_type=\"image/png\" width=\"100\" height=\"100\" alias_key=\"2343348577\" timestamp=\"1283864416\" is_valid=\"1\"/><alias name=\"medium\" filename=\"128px-Feed-icon_svg_medium.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/media/images/128px-feed-icon_svg/322-1-eng-GB\" url=\"var/intranet/storage/images/media/images/128px-feed-icon_svg/322-1-eng-GB/128px-Feed-icon_svg_medium.png\" mime_type=\"image/png\" width=\"128\" height=\"128\" alias_key=\"405413724\" timestamp=\"1283933057\" is_valid=\"1\"/><alias name=\"tiny\" filename=\"128px-Feed-icon_svg_tiny.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/media/images/128px-feed-icon_svg/322-1-eng-GB\" url=\"var/intranet/storage/images/media/images/128px-feed-icon_svg/322-1-eng-GB/128px-Feed-icon_svg_tiny.png\" mime_type=\"image/png\" width=\"30\" height=\"30\" alias_key=\"2535682897\" timestamp=\"1283864482\" is_valid=\"1\"/><alias name=\"rss\" filename=\"128px-Feed-icon_svg_rss.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/media/images/128px-feed-icon_svg/322-1-eng-GB\" url=\"var/intranet/storage/images/media/images/128px-feed-icon_svg/322-1-eng-GB/128px-Feed-icon_svg_rss.png\" mime_type=\"image/png\" width=\"31\" height=\"31\" alias_key=\"1934726172\" timestamp=\"1283931869\" is_valid=\"1\"/></ezimage>\n','ezimage',322,'eng-GB',3,0,'',1),(0,116,92,0,NULL,'FylkeshusetFront','ezstring',326,'eng-GB',3,0,'fylkeshusetfront',1),(0,117,92,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',327,'eng-GB',3,0,'',1),(0,118,92,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"1\" filename=\"FylkeshusetFront.jpg\" suffix=\"jpg\" basename=\"FylkeshusetFront\" dirpath=\"var/intranet/storage/images/media/images/fylkeshusetfront3/328-1-eng-GB\" url=\"var/intranet/storage/images/media/images/fylkeshusetfront3/328-1-eng-GB/FylkeshusetFront.jpg\" original_filename=\"FylkeshusetFront.jpg\" mime_type=\"image/jpeg\" width=\"200\" height=\"150\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1283930105\"><original attribute_id=\"328\" attribute_version=\"1\" attribute_language=\"eng-GB\"/><information Height=\"150\" Width=\"200\" IsColor=\"1\"/><alias name=\"reference\" filename=\"FylkeshusetFront_reference.jpg\" suffix=\"jpg\" dirpath=\"var/intranet/storage/images/media/images/fylkeshusetfront3/328-1-eng-GB\" url=\"var/intranet/storage/images/media/images/fylkeshusetfront3/328-1-eng-GB/FylkeshusetFront_reference.jpg\" mime_type=\"image/jpeg\" width=\"200\" height=\"150\" alias_key=\"2605465115\" timestamp=\"1283930106\" is_valid=\"1\"/><alias name=\"small\" filename=\"FylkeshusetFront_small.jpg\" suffix=\"jpg\" dirpath=\"var/intranet/storage/images/media/images/fylkeshusetfront3/328-1-eng-GB\" url=\"var/intranet/storage/images/media/images/fylkeshusetfront3/328-1-eng-GB/FylkeshusetFront_small.jpg\" mime_type=\"image/jpeg\" width=\"100\" height=\"75\" alias_key=\"2343348577\" timestamp=\"1283930106\" is_valid=\"1\"/><alias name=\"large\" filename=\"FylkeshusetFront_large.jpg\" suffix=\"jpg\" dirpath=\"var/intranet/storage/images/media/images/fylkeshusetfront3/328-1-eng-GB\" url=\"var/intranet/storage/images/media/images/fylkeshusetfront3/328-1-eng-GB/FylkeshusetFront_large.jpg\" mime_type=\"image/jpeg\" width=\"200\" height=\"150\" alias_key=\"1592566908\" timestamp=\"1283930789\" is_valid=\"1\"/></ezimage>\n','ezimage',328,'eng-GB',3,0,'',1),(0,116,93,0,NULL,'FylkeshusetFront','ezstring',329,'eng-GB',3,0,'fylkeshusetfront',1),(0,117,93,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',330,'eng-GB',3,0,'',1),(0,118,93,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"1\" filename=\"FylkeshusetFront.jpg\" suffix=\"jpg\" basename=\"FylkeshusetFront\" dirpath=\"var/intranet/storage/images/media/images/fylkeshusetfront4/331-1-eng-GB\" url=\"var/intranet/storage/images/media/images/fylkeshusetfront4/331-1-eng-GB/FylkeshusetFront.jpg\" original_filename=\"FylkeshusetFront.jpg\" mime_type=\"image/jpeg\" width=\"200\" height=\"150\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1283931318\"><original attribute_id=\"331\" attribute_version=\"1\" attribute_language=\"eng-GB\"/><information Height=\"150\" Width=\"200\" IsColor=\"1\"/><alias name=\"reference\" filename=\"FylkeshusetFront_reference.jpg\" suffix=\"jpg\" dirpath=\"var/intranet/storage/images/media/images/fylkeshusetfront4/331-1-eng-GB\" url=\"var/intranet/storage/images/media/images/fylkeshusetfront4/331-1-eng-GB/FylkeshusetFront_reference.jpg\" mime_type=\"image/jpeg\" width=\"200\" height=\"150\" alias_key=\"2605465115\" timestamp=\"1283931319\" is_valid=\"1\"/><alias name=\"small\" filename=\"FylkeshusetFront_small.jpg\" suffix=\"jpg\" dirpath=\"var/intranet/storage/images/media/images/fylkeshusetfront4/331-1-eng-GB\" url=\"var/intranet/storage/images/media/images/fylkeshusetfront4/331-1-eng-GB/FylkeshusetFront_small.jpg\" mime_type=\"image/jpeg\" width=\"100\" height=\"75\" alias_key=\"2343348577\" timestamp=\"1283931319\" is_valid=\"1\"/><alias name=\"large\" filename=\"FylkeshusetFront_large.jpg\" suffix=\"jpg\" dirpath=\"var/intranet/storage/images/media/images/fylkeshusetfront4/331-1-eng-GB\" url=\"var/intranet/storage/images/media/images/fylkeshusetfront4/331-1-eng-GB/FylkeshusetFront_large.jpg\" mime_type=\"image/jpeg\" width=\"200\" height=\"150\" alias_key=\"1592566908\" timestamp=\"1283931411\" is_valid=\"1\"/></ezimage>\n','ezimage',331,'eng-GB',3,0,'',1),(0,116,94,0,NULL,'FylkeshusetFront','ezstring',332,'eng-GB',3,0,'fylkeshusetfront',1),(0,116,94,0,NULL,'Fylkeshuset','ezstring',332,'eng-GB',3,0,'fylkeshuset',2),(0,117,94,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',333,'eng-GB',3,0,'',1),(0,117,94,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',333,'eng-GB',3,0,'',2),(0,118,94,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"1\" filename=\"707b1a6f1e0deb7f3a573f12179a8c05.jpg\" suffix=\"jpg\" basename=\"707b1a6f1e0deb7f3a573f12179a8c05\" dirpath=\"var/intranet/storage/images/troms-fylkeskommune/fylkeshuset/334-1-eng-GB/trashed\" url=\"var/intranet/storage/images/troms-fylkeskommune/fylkeshuset/334-1-eng-GB/trashed/707b1a6f1e0deb7f3a573f12179a8c05.jpg\" original_filename=\"FylkeshusetFront.jpg\" mime_type=\"image/jpeg\" width=\"200\" height=\"150\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1283931783\"><original attribute_id=\"334\" attribute_version=\"1\" attribute_language=\"eng-GB\"/><information Height=\"150\" Width=\"200\" IsColor=\"1\"/></ezimage>\n','ezimage',334,'eng-GB',3,0,'',1),(0,118,94,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"1\" filename=\"7fc746873285caba37020cd68ddff84a.jpg\" suffix=\"jpg\" basename=\"7fc746873285caba37020cd68ddff84a\" dirpath=\"var/intranet/storage/images/troms-fylkeskommune/fylkeshuset/334-1-eng-GB/trashed/trashed\" url=\"var/intranet/storage/images/troms-fylkeskommune/fylkeshuset/334-1-eng-GB/trashed/trashed/7fc746873285caba37020cd68ddff84a.jpg\" original_filename=\"FylkeshusetFront.jpg\" mime_type=\"image/jpeg\" width=\"200\" height=\"150\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1283931783\"><original attribute_id=\"334\" attribute_version=\"1\" attribute_language=\"eng-GB\"/><information Height=\"150\" Width=\"200\" IsColor=\"1\"/></ezimage>\n','ezimage',334,'eng-GB',3,0,'',2),(0,116,95,0,NULL,'128px-Feed-icon_svg','ezstring',335,'eng-GB',3,0,'128px-feed-icon_svg',1),(0,116,95,0,NULL,'RSS Feed','ezstring',335,'eng-GB',3,0,'rss feed',2),(0,117,95,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',336,'eng-GB',3,0,'',1),(0,117,95,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',336,'eng-GB',3,0,'',2),(0,118,95,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"1\" filename=\"128px-Feed-icon_svg.png\" suffix=\"png\" basename=\"128px-Feed-icon_svg\" dirpath=\"var/intranet/storage/images/rss-feed/128px-feed-icon_svg/337-1-eng-GB\" url=\"var/intranet/storage/images/rss-feed/128px-feed-icon_svg/337-1-eng-GB/128px-Feed-icon_svg.png\" original_filename=\"128px-Feed-icon_svg.png\" mime_type=\"image/png\" width=\"128\" height=\"128\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1283931925\"><original attribute_id=\"337\" attribute_version=\"1\" attribute_language=\"eng-GB\"/><alias name=\"reference\" filename=\"128px-Feed-icon_svg_reference.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/rss-feed/128px-feed-icon_svg/337-1-eng-GB\" url=\"var/intranet/storage/images/rss-feed/128px-feed-icon_svg/337-1-eng-GB/128px-Feed-icon_svg_reference.png\" mime_type=\"image/png\" width=\"128\" height=\"128\" alias_key=\"2605465115\" timestamp=\"1283931926\" is_valid=\"1\"/><alias name=\"small\" filename=\"128px-Feed-icon_svg_small.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/rss-feed/128px-feed-icon_svg/337-1-eng-GB\" url=\"var/intranet/storage/images/rss-feed/128px-feed-icon_svg/337-1-eng-GB/128px-Feed-icon_svg_small.png\" mime_type=\"image/png\" width=\"100\" height=\"100\" alias_key=\"2343348577\" timestamp=\"1283931926\" is_valid=\"1\"/><alias name=\"medium\" filename=\"128px-Feed-icon_svg_medium.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/rss-feed/128px-feed-icon_svg/337-1-eng-GB\" url=\"var/intranet/storage/images/rss-feed/128px-feed-icon_svg/337-1-eng-GB/128px-Feed-icon_svg_medium.png\" mime_type=\"image/png\" width=\"128\" height=\"128\" alias_key=\"405413724\" timestamp=\"1283931926\" is_valid=\"1\"/></ezimage>\n','ezimage',337,'eng-GB',3,0,'',1),(0,118,95,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"1\" filename=\"RSS-Feed.png\" suffix=\"png\" basename=\"RSS-Feed\" dirpath=\"var/intranet/storage/images/rss-feed/rss-feed/337-1-eng-GB\" url=\"var/intranet/storage/images/rss-feed/rss-feed/337-1-eng-GB/RSS-Feed.png\" original_filename=\"128px-Feed-icon_svg.png\" mime_type=\"image/png\" width=\"128\" height=\"128\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1283931925\"><original attribute_id=\"337\" attribute_version=\"1\" attribute_language=\"eng-GB\"/><alias name=\"reference\" filename=\"RSS-Feed_reference.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/rss-feed/rss-feed/337-1-eng-GB\" url=\"var/intranet/storage/images/rss-feed/rss-feed/337-1-eng-GB/RSS-Feed_reference.png\" mime_type=\"image/png\" width=\"128\" height=\"128\" alias_key=\"2605465115\" timestamp=\"1283931926\" is_valid=\"1\"/><alias name=\"small\" filename=\"RSS-Feed_small.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/rss-feed/rss-feed/337-1-eng-GB\" url=\"var/intranet/storage/images/rss-feed/rss-feed/337-1-eng-GB/RSS-Feed_small.png\" mime_type=\"image/png\" width=\"100\" height=\"100\" alias_key=\"2343348577\" timestamp=\"1283931926\" is_valid=\"1\"/><alias name=\"medium\" filename=\"RSS-Feed_medium.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/rss-feed/rss-feed/337-1-eng-GB\" url=\"var/intranet/storage/images/rss-feed/rss-feed/337-1-eng-GB/RSS-Feed_medium.png\" mime_type=\"image/png\" width=\"128\" height=\"128\" alias_key=\"405413724\" timestamp=\"1283931926\" is_valid=\"1\"/></ezimage>\n','ezimage',337,'eng-GB',3,0,'',2),(0,116,97,0,NULL,'128px-Feed-icon','ezstring',341,'eng-GB',3,0,'128px-feed-icon',1),(0,117,97,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',342,'eng-GB',3,0,'',1),(0,118,97,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"1\" filename=\"128px-Feed-icon.png\" suffix=\"png\" basename=\"128px-Feed-icon\" dirpath=\"var/intranet/storage/images/rss-feed/128px-feed-icon/343-1-eng-GB\" url=\"var/intranet/storage/images/rss-feed/128px-feed-icon/343-1-eng-GB/128px-Feed-icon.png\" original_filename=\"128px-Feed-icon.png\" mime_type=\"image/png\" width=\"128\" height=\"128\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1283933210\"><original attribute_id=\"343\" attribute_version=\"1\" attribute_language=\"eng-GB\"/><alias name=\"reference\" filename=\"128px-Feed-icon_reference.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/rss-feed/128px-feed-icon/343-1-eng-GB\" url=\"var/intranet/storage/images/rss-feed/128px-feed-icon/343-1-eng-GB/128px-Feed-icon_reference.png\" mime_type=\"image/png\" width=\"128\" height=\"128\" alias_key=\"2605465115\" timestamp=\"1283933210\" is_valid=\"1\"/><alias name=\"small\" filename=\"128px-Feed-icon_small.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/rss-feed/128px-feed-icon/343-1-eng-GB\" url=\"var/intranet/storage/images/rss-feed/128px-feed-icon/343-1-eng-GB/128px-Feed-icon_small.png\" mime_type=\"image/png\" width=\"100\" height=\"100\" alias_key=\"2343348577\" timestamp=\"1283933684\" is_valid=\"1\"/><alias name=\"medium\" filename=\"128px-Feed-icon_medium.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/rss-feed/128px-feed-icon/343-1-eng-GB\" url=\"var/intranet/storage/images/rss-feed/128px-feed-icon/343-1-eng-GB/128px-Feed-icon_medium.png\" mime_type=\"image/png\" width=\"128\" height=\"128\" alias_key=\"3736024005\" timestamp=\"1288808382\" is_valid=\"1\"/><alias name=\"tiny\" filename=\"128px-Feed-icon_tiny.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/rss-feed/128px-feed-icon/343-1-eng-GB\" url=\"var/intranet/storage/images/rss-feed/128px-feed-icon/343-1-eng-GB/128px-Feed-icon_tiny.png\" mime_type=\"image/png\" width=\"30\" height=\"30\" alias_key=\"2535682897\" timestamp=\"1283933215\" is_valid=\"1\"/><alias name=\"rss\" filename=\"128px-Feed-icon_rss.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/rss-feed/128px-feed-icon/343-1-eng-GB\" url=\"var/intranet/storage/images/rss-feed/128px-feed-icon/343-1-eng-GB/128px-Feed-icon_rss.png\" mime_type=\"image/png\" width=\"31\" height=\"31\" alias_key=\"3240742506\" timestamp=\"1284015116\" is_valid=\"1\"/></ezimage>\n','ezimage',343,'eng-GB',3,0,'',1),(0,116,99,0,NULL,'flickr','ezstring',347,'eng-GB',3,0,'flickr',1),(0,116,99,0,NULL,'Følg oss på Flickr >>','ezstring',347,'eng-GB',3,0,'følg oss på flickr >>',2),(0,117,99,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',348,'eng-GB',3,0,'',1),(0,117,99,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',348,'eng-GB',3,0,'',2),(0,118,99,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"1\" filename=\"flickr.png\" suffix=\"png\" basename=\"flickr\" dirpath=\"var/intranet/storage/images/media/images/flickr/349-1-eng-GB\" url=\"var/intranet/storage/images/media/images/flickr/349-1-eng-GB/flickr.png\" original_filename=\"flickr.png\" mime_type=\"image/png\" width=\"100\" height=\"38\" alternative_text=\"Følg oss på Flicker\" alias_key=\"1293033771\" timestamp=\"1283934249\"><original attribute_id=\"349\" attribute_version=\"1\" attribute_language=\"eng-GB\"/><alias name=\"reference\" filename=\"flickr_reference.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/media/images/flickr/349-1-eng-GB\" url=\"var/intranet/storage/images/media/images/flickr/349-1-eng-GB/flickr_reference.png\" mime_type=\"image/png\" width=\"100\" height=\"38\" alias_key=\"2605465115\" timestamp=\"1283934249\" is_valid=\"1\"/><alias name=\"small\" filename=\"flickr_small.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/media/images/flickr/349-1-eng-GB\" url=\"var/intranet/storage/images/media/images/flickr/349-1-eng-GB/flickr_small.png\" mime_type=\"image/png\" width=\"100\" height=\"38\" alias_key=\"2343348577\" timestamp=\"1283935346\" is_valid=\"1\"/><alias name=\"medium\" filename=\"flickr_medium.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/media/images/flickr/349-1-eng-GB\" url=\"var/intranet/storage/images/media/images/flickr/349-1-eng-GB/flickr_medium.png\" mime_type=\"image/png\" width=\"100\" height=\"38\" alias_key=\"405413724\" timestamp=\"1283934250\" is_valid=\"1\"/></ezimage>\n','ezimage',349,'eng-GB',3,0,'',1),(0,118,99,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"1\" filename=\"Foelg-oss-paa-Flickr.png\" suffix=\"png\" basename=\"Foelg-oss-paa-Flickr\" dirpath=\"var/intranet/storage/images/media/images/foelg-oss-paa-flickr/349-1-eng-GB\" url=\"var/intranet/storage/images/media/images/foelg-oss-paa-flickr/349-1-eng-GB/Foelg-oss-paa-Flickr.png\" original_filename=\"flickr.png\" mime_type=\"image/png\" width=\"100\" height=\"38\" alternative_text=\"Følg oss på Flicker &gt;&gt;\" alias_key=\"1293033771\" timestamp=\"1283934249\"><original attribute_id=\"349\" attribute_version=\"1\" attribute_language=\"eng-GB\"/><alias name=\"reference\" filename=\"Foelg-oss-paa-Flickr_reference.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/media/images/foelg-oss-paa-flickr/349-1-eng-GB\" url=\"var/intranet/storage/images/media/images/foelg-oss-paa-flickr/349-1-eng-GB/Foelg-oss-paa-Flickr_reference.png\" mime_type=\"image/png\" width=\"100\" height=\"38\" alias_key=\"2605465115\" timestamp=\"1283934249\" is_valid=\"1\"/><alias name=\"small\" filename=\"Foelg-oss-paa-Flickr_small.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/media/images/foelg-oss-paa-flickr/349-1-eng-GB\" url=\"var/intranet/storage/images/media/images/foelg-oss-paa-flickr/349-1-eng-GB/Foelg-oss-paa-Flickr_small.png\" mime_type=\"image/png\" width=\"100\" height=\"38\" alias_key=\"2343348577\" timestamp=\"1283935494\" is_valid=\"1\"/><alias name=\"medium\" filename=\"Foelg-oss-paa-Flickr_medium.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/media/images/foelg-oss-paa-flickr/349-1-eng-GB\" url=\"var/intranet/storage/images/media/images/foelg-oss-paa-flickr/349-1-eng-GB/Foelg-oss-paa-Flickr_medium.png\" mime_type=\"image/png\" width=\"100\" height=\"38\" alias_key=\"405413724\" timestamp=\"1283934250\" is_valid=\"1\"/><alias name=\"large\" filename=\"Foelg-oss-paa-Flickr_large.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/media/images/foelg-oss-paa-flickr/349-1-eng-GB\" url=\"var/intranet/storage/images/media/images/foelg-oss-paa-flickr/349-1-eng-GB/Foelg-oss-paa-Flickr_large.png\" mime_type=\"image/png\" width=\"100\" height=\"38\" alias_key=\"1592566908\" timestamp=\"1283935429\" is_valid=\"1\"/></ezimage>\n','ezimage',349,'eng-GB',3,0,'',2),(0,1,100,0,NULL,'Følg oss på sosiale medier','ezstring',350,'eng-GB',2,0,'følg oss på sosiale medier',8),(0,1,100,0,NULL,'Følg oss på sosiale medier','ezstring',350,'eng-GB',2,0,'følg oss på sosiale medier',9),(0,1,100,0,NULL,'Følg oss på sosiale medier','ezstring',350,'eng-GB',2,0,'følg oss på sosiale medier',10),(0,1,100,0,NULL,'Følg oss på sosiale medier','ezstring',350,'eng-GB',2,0,'følg oss på sosiale medier',11),(0,1,100,0,NULL,'Følg oss på sosiale medier','ezstring',350,'eng-GB',2,0,'følg oss på sosiale medier',12),(0,1,100,0,NULL,'Følg oss på sosiale medier','ezstring',350,'eng-GB',2,0,'følg oss på sosiale medier',13),(0,1,100,0,NULL,'Følg oss på sosiale medier','ezstring',350,'eng-GB',2,0,'følg oss på sosiale medier',14),(0,1,100,0,NULL,'Følg oss på sosiale medier','ezstring',350,'eng-GB',2,0,'følg oss på sosiale medier',15),(0,1,100,0,NULL,'Følg oss på sosiale medier','ezstring',350,'eng-GB',2,0,'følg oss på sosiale medier',16),(0,1,100,0,NULL,'Følg oss på sosiale medier','ezstring',350,'eng-GB',2,0,'følg oss på sosiale medier',17),(0,152,100,0,NULL,'','ezstring',351,'eng-GB',2,0,'',8),(0,152,100,0,NULL,'','ezstring',351,'eng-GB',2,0,'',9),(0,152,100,0,NULL,'','ezstring',351,'eng-GB',2,0,'',10),(0,152,100,0,NULL,'','ezstring',351,'eng-GB',2,0,'',11),(0,152,100,0,NULL,'','ezstring',351,'eng-GB',2,0,'',12),(0,152,100,0,NULL,'','ezstring',351,'eng-GB',2,0,'',13),(0,152,100,0,NULL,'','ezstring',351,'eng-GB',2,0,'',14),(0,152,100,0,NULL,'','ezstring',351,'eng-GB',2,0,'',15),(0,152,100,0,NULL,'','ezstring',351,'eng-GB',2,0,'',16),(0,152,100,0,NULL,'','ezstring',351,'eng-GB',2,0,'',17),(0,153,100,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Khalil Dahbi\" email=\"khalil.dahbi@tromsfylke.no\"/></authors></ezauthor>\n','ezauthor',352,'eng-GB',2,0,'',8),(0,153,100,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Khalil Dahbi\" email=\"khalil.dahbi@tromsfylke.no\"/></authors></ezauthor>\n','ezauthor',352,'eng-GB',2,0,'',9),(0,153,100,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Khalil Dahbi\" email=\"khalil.dahbi@tromsfylke.no\"/></authors></ezauthor>\n','ezauthor',352,'eng-GB',2,0,'',10),(0,153,100,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Khalil Dahbi\" email=\"khalil.dahbi@tromsfylke.no\"/></authors></ezauthor>\n','ezauthor',352,'eng-GB',2,0,'',11),(0,153,100,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Khalil Dahbi\" email=\"khalil.dahbi@tromsfylke.no\"/></authors></ezauthor>\n','ezauthor',352,'eng-GB',2,0,'',12),(0,153,100,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Khalil Dahbi\" email=\"khalil.dahbi@tromsfylke.no\"/></authors></ezauthor>\n','ezauthor',352,'eng-GB',2,0,'',13),(0,153,100,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Khalil Dahbi\" email=\"khalil.dahbi@tromsfylke.no\"/></authors></ezauthor>\n','ezauthor',352,'eng-GB',2,0,'',14),(0,153,100,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Khalil Dahbi\" email=\"khalil.dahbi@tromsfylke.no\"/></authors></ezauthor>\n','ezauthor',352,'eng-GB',2,0,'',15),(0,153,100,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Khalil Dahbi\" email=\"khalil.dahbi@tromsfylke.no\"/></authors></ezauthor>\n','ezauthor',352,'eng-GB',2,0,'',16),(0,153,100,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezauthor><authors><author id=\"0\" name=\"Khalil Dahbi\" email=\"khalil.dahbi@tromsfylke.no\"/></authors></ezauthor>\n','ezauthor',352,'eng-GB',2,0,'',17),(0,120,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><link xhtml:title=\"Følg oss på Facebook\" url_id=\"40\"><embed object_id=\"98\"/>&gt;\" alt=medium src=\"http://kurs.tromsfylke.no/var/intranet/storage/images/rss-feed/foelg-oss-paa-facebook/346-1-eng-GB/Foelg-oss-paa-Facebook_medium.jpg\" width=100 height=38 view=\"embed\" inline=\"false\"&gt;</link></paragraph><paragraph><embed size=\"small\" view=\"embed\" object_id=\"101\"/></paragraph></section>\n','ezxmltext',353,'eng-GB',2,0,'',8),(0,120,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><embed object_id=\"98\"/>&gt;\" alt=medium src=\"http://kurs.tromsfylke.no/var/intranet/storage/images/rss-feed/foelg-oss-paa-facebook/346-1-eng-GB/Foelg-oss-paa-Facebook_medium.jpg\" width=100 height=38 view=\"embed\" inline=\"false\"&gt;<embed size=\"small\" view=\"embed\" object_id=\"101\"/></paragraph></section>\n','ezxmltext',353,'eng-GB',2,0,'',9),(0,120,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\"><embed size=\"medium\" view=\"embed\" object_id=\"98\"/></paragraph><paragraph xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\"><embed size=\"small\" view=\"embed\" object_id=\"101\"/></paragraph></section>\n','ezxmltext',353,'eng-GB',2,0,'',10),(0,120,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><embed object_id=\"102\"/>&gt;\" alt=small src=\"http://kurs.tromsfylke.no/var/intranet/storage/images/media/images/foelg-oss-paa-facebook/362-1-eng-GB/Foelg-oss-paa-Facebook_small.jpg\" width=100 height=38 view=\"embed\" inline=\"false\"&gt;</paragraph><paragraph><embed size=\"small\" view=\"embed\" object_id=\"101\"/></paragraph></section>\n','ezxmltext',353,'eng-GB',2,0,'',11),(0,120,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\"><link url_id=\"39\"><embed size=\"medium\" view=\"embed\" object_id=\"102\"/></link></line><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\"><link url_id=\"41\"><embed size=\"small\" view=\"embed\" object_id=\"101\"/></link></line></paragraph></section>\n','ezxmltext',353,'eng-GB',2,0,'',12),(0,120,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\"><link url_id=\"42\"><embed size=\"medium\" view=\"embed\" object_id=\"103\"/></link></line><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\"><link url_id=\"41\"><embed size=\"small\" view=\"embed\" object_id=\"101\"/></link></line></paragraph></section>\n','ezxmltext',353,'eng-GB',2,0,'',13),(0,120,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\"><link url_id=\"42\"><embed size=\"medium\" view=\"embed\" object_id=\"103\"/></link></line><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\"><link url_id=\"41\"><embed size=\"small\" view=\"embed\" object_id=\"101\"/></link></line><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\"><link url_id=\"43\"><embed size=\"small\" view=\"embed\" object_id=\"104\"/></link></line></paragraph></section>\n','ezxmltext',353,'eng-GB',2,0,'',14),(0,120,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\"><link url_id=\"42\"><embed size=\"medium\" view=\"embed\" object_id=\"103\"/></link></line><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\"><link url_id=\"41\"><embed size=\"small\" view=\"embed\" object_id=\"101\"/></link></line><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\"><link url_id=\"43\"><embed size=\"small\" view=\"embed\" object_id=\"106\"/></link></line></paragraph></section>\n','ezxmltext',353,'eng-GB',2,0,'',15),(0,120,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\"><link url_id=\"42\"><embed size=\"medium\" view=\"embed\" object_id=\"103\"/></link></line><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\"><link url_id=\"41\"><embed size=\"small\" view=\"embed\" object_id=\"101\"/></link></line><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\"><link url_id=\"43\"><embed size=\"small\" view=\"embed\" object_id=\"107\"/></link></line></paragraph></section>\n','ezxmltext',353,'eng-GB',2,0,'',16),(0,120,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\"><link url_id=\"42\"><embed size=\"medium\" view=\"embed\" object_id=\"103\"/></link></line><line xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\"><link url_id=\"41\"><embed size=\"small\" view=\"embed\" object_id=\"101\"/></link></line></paragraph></section>\n','ezxmltext',353,'eng-GB',2,0,'',17),(0,121,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',354,'eng-GB',2,0,'',8),(0,121,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',354,'eng-GB',2,0,'',9),(0,121,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',354,'eng-GB',2,0,'',10),(0,121,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',354,'eng-GB',2,0,'',11),(0,121,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',354,'eng-GB',2,0,'',12),(0,121,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',354,'eng-GB',2,0,'',13),(0,121,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',354,'eng-GB',2,0,'',14),(0,121,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',354,'eng-GB',2,0,'',15),(0,121,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',354,'eng-GB',2,0,'',16),(0,121,100,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',354,'eng-GB',2,0,'',17),(0,123,100,0,0,'','ezboolean',355,'eng-GB',2,0,'',8),(0,123,100,0,0,'','ezboolean',355,'eng-GB',2,0,'',9),(0,123,100,0,0,'','ezboolean',355,'eng-GB',2,0,'',10),(0,123,100,0,0,'','ezboolean',355,'eng-GB',2,0,'',11),(0,123,100,0,0,'','ezboolean',355,'eng-GB',2,0,'',12),(0,123,100,0,0,'','ezboolean',355,'eng-GB',2,0,'',13),(0,123,100,0,0,'','ezboolean',355,'eng-GB',2,0,'',14),(0,123,100,0,0,'','ezboolean',355,'eng-GB',2,0,'',15),(0,123,100,0,0,'','ezboolean',355,'eng-GB',2,0,'',16),(0,123,100,0,0,'','ezboolean',355,'eng-GB',2,0,'',17),(0,154,100,0,NULL,'','ezobjectrelation',356,'eng-GB',2,0,'',8),(0,154,100,0,NULL,'','ezobjectrelation',356,'eng-GB',2,0,'',9),(0,154,100,0,NULL,'','ezobjectrelation',356,'eng-GB',2,0,'',10),(0,154,100,0,NULL,'','ezobjectrelation',356,'eng-GB',2,0,'',11),(0,154,100,0,NULL,'','ezobjectrelation',356,'eng-GB',2,0,'',12),(0,154,100,0,NULL,'','ezobjectrelation',356,'eng-GB',2,0,'',13),(0,154,100,0,NULL,'','ezobjectrelation',356,'eng-GB',2,0,'',14),(0,154,100,0,NULL,'','ezobjectrelation',356,'eng-GB',2,0,'',15),(0,154,100,0,NULL,'','ezobjectrelation',356,'eng-GB',2,0,'',16),(0,154,100,0,NULL,'','ezobjectrelation',356,'eng-GB',2,0,'',17),(0,116,101,0,NULL,'flickr','ezstring',357,'eng-GB',3,0,'flickr',1),(0,117,101,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',358,'eng-GB',3,0,'',1),(0,118,101,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"1\" filename=\"ac9683863a5436332938f1763e8431b0.png\" suffix=\"png\" basename=\"ac9683863a5436332938f1763e8431b0\" dirpath=\"var/intranet/storage/images/troms-fylkeskommune/foelg-oss-paa-sosiale-medier/flickr/359-1-eng-GB/trashed\" url=\"var/intranet/storage/images/troms-fylkeskommune/foelg-oss-paa-sosiale-medier/flickr/359-1-eng-GB/trashed/ac9683863a5436332938f1763e8431b0.png\" original_filename=\"flickr.png\" mime_type=\"image/png\" width=\"100\" height=\"38\" alternative_text=\"Lølg oss på Flickr &gt;&gt;\" alias_key=\"1293033771\" timestamp=\"1283935821\"><original attribute_id=\"359\" attribute_version=\"1\" attribute_language=\"eng-GB\"/></ezimage>\n','ezimage',359,'eng-GB',3,0,'',1),(0,116,102,0,NULL,'Følg oss på Facebook >>','ezstring',360,'eng-GB',3,0,'følg oss på facebook >>',1),(0,117,102,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',361,'eng-GB',3,0,'',1),(0,118,102,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"1\" filename=\"Foelg-oss-paa-Facebook.jpg\" suffix=\"jpg\" basename=\"Foelg-oss-paa-Facebook\" dirpath=\"var/intranet/storage/images/media/images/foelg-oss-paa-facebook/362-1-eng-GB\" url=\"var/intranet/storage/images/media/images/foelg-oss-paa-facebook/362-1-eng-GB/Foelg-oss-paa-Facebook.jpg\" original_filename=\"facebook.jpg\" mime_type=\"image/jpeg\" width=\"100\" height=\"38\" alternative_text=\"Følg oss på Facebook &gt;&gt;\" alias_key=\"1293033771\" timestamp=\"1283936573\"><original attribute_id=\"362\" attribute_version=\"1\" attribute_language=\"eng-GB\"/><information Height=\"38\" Width=\"100\" IsColor=\"1\" ByteOrderMotorola=\"1\" Thumbnail.FileType=\"2\" Thumbnail.MimeType=\"image/jpeg\"><array name=\"ifd0\"><item key=\"Orientation\" base64=\"1\">MQ==</item><item key=\"XResolution\" base64=\"1\">MTE4MTEwLzEwMDA=</item><item key=\"YResolution\" base64=\"1\">MTE4MTEwLzEwMDA=</item><item key=\"ResolutionUnit\" base64=\"1\">Mw==</item><item key=\"Software\" base64=\"1\">UGFpbnQuTkVUIHYzLjUuNQ==</item><item key=\"DateTime\" base64=\"1\">MjAwNjowOTowNyAxNDo1NjowNQ==</item><item key=\"Exif_IFD_Pointer\" base64=\"1\">MTUy</item></array><array name=\"exif\"><item key=\"ExifImageWidth\" base64=\"1\">ODAw</item><item key=\"ExifImageLength\" base64=\"1\">MzAx</item></array></information><alias name=\"reference\" filename=\"Foelg-oss-paa-Facebook_reference.jpg\" suffix=\"jpg\" dirpath=\"var/intranet/storage/images/media/images/foelg-oss-paa-facebook/362-1-eng-GB\" url=\"var/intranet/storage/images/media/images/foelg-oss-paa-facebook/362-1-eng-GB/Foelg-oss-paa-Facebook_reference.jpg\" mime_type=\"image/jpeg\" width=\"100\" height=\"38\" alias_key=\"2605465115\" timestamp=\"1283936574\" is_valid=\"1\"/><alias name=\"small\" filename=\"Foelg-oss-paa-Facebook_small.jpg\" suffix=\"jpg\" dirpath=\"var/intranet/storage/images/media/images/foelg-oss-paa-facebook/362-1-eng-GB\" url=\"var/intranet/storage/images/media/images/foelg-oss-paa-facebook/362-1-eng-GB/Foelg-oss-paa-Facebook_small.jpg\" mime_type=\"image/jpeg\" width=\"100\" height=\"38\" alias_key=\"2343348577\" timestamp=\"1283936574\" is_valid=\"1\"/><alias name=\"medium\" filename=\"Foelg-oss-paa-Facebook_medium.jpg\" suffix=\"jpg\" dirpath=\"var/intranet/storage/images/media/images/foelg-oss-paa-facebook/362-1-eng-GB\" url=\"var/intranet/storage/images/media/images/foelg-oss-paa-facebook/362-1-eng-GB/Foelg-oss-paa-Facebook_medium.jpg\" mime_type=\"image/jpeg\" width=\"100\" height=\"38\" alias_key=\"405413724\" timestamp=\"1283936951\" is_valid=\"1\"/></ezimage>\n','ezimage',362,'eng-GB',3,0,'',1),(0,116,103,0,NULL,'facebook','ezstring',363,'eng-GB',3,0,'facebook',1),(0,117,103,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',364,'eng-GB',3,0,'',1),(0,118,103,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"1\" filename=\"0ada884b8cde994f49f6307fbfb0e598.jpg\" suffix=\"jpg\" basename=\"0ada884b8cde994f49f6307fbfb0e598\" dirpath=\"var/intranet/storage/images/troms-fylkeskommune/foelg-oss-paa-sosiale-medier/facebook/365-1-eng-GB/trashed\" url=\"var/intranet/storage/images/troms-fylkeskommune/foelg-oss-paa-sosiale-medier/facebook/365-1-eng-GB/trashed/0ada884b8cde994f49f6307fbfb0e598.jpg\" original_filename=\"facebook.jpg\" mime_type=\"image/jpeg\" width=\"100\" height=\"38\" alternative_text=\"Følg oss på Facebook &gt;&gt;\" alias_key=\"1293033771\" timestamp=\"1283937017\"><original attribute_id=\"365\" attribute_version=\"1\" attribute_language=\"eng-GB\"/><information Height=\"38\" Width=\"100\" IsColor=\"1\" ByteOrderMotorola=\"1\" Thumbnail.FileType=\"2\" Thumbnail.MimeType=\"image/jpeg\"><array name=\"ifd0\"><item key=\"Orientation\" base64=\"1\">MQ==</item><item key=\"XResolution\" base64=\"1\">MTE4MTEwLzEwMDA=</item><item key=\"YResolution\" base64=\"1\">MTE4MTEwLzEwMDA=</item><item key=\"ResolutionUnit\" base64=\"1\">Mw==</item><item key=\"Software\" base64=\"1\">UGFpbnQuTkVUIHYzLjUuNQ==</item><item key=\"DateTime\" base64=\"1\">MjAwNjowOTowNyAxNDo1NjowNQ==</item><item key=\"Exif_IFD_Pointer\" base64=\"1\">MTUy</item></array><array name=\"exif\"><item key=\"ExifImageWidth\" base64=\"1\">ODAw</item><item key=\"ExifImageLength\" base64=\"1\">MzAx</item></array></information></ezimage>\n','ezimage',365,'eng-GB',3,0,'',1),(0,116,105,0,NULL,'flickr','ezstring',369,'eng-GB',3,0,'flickr',1),(0,117,105,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',370,'eng-GB',3,0,'',1),(0,118,105,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"1\" filename=\"flickr.png\" suffix=\"png\" basename=\"flickr\" dirpath=\"var/intranet/storage/images/media/images/flickr/371-1-eng-GB\" url=\"var/intranet/storage/images/media/images/flickr/371-1-eng-GB/flickr.png\" original_filename=\"flickr.png\" mime_type=\"image/png\" width=\"100\" height=\"38\" alternative_text=\"Følg oss på Wikipedia &gt;&gt;\" alias_key=\"1293033771\" timestamp=\"1283937709\"><original attribute_id=\"371\" attribute_version=\"1\" attribute_language=\"eng-GB\"/><alias name=\"reference\" filename=\"flickr_reference.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/media/images/flickr/371-1-eng-GB\" url=\"var/intranet/storage/images/media/images/flickr/371-1-eng-GB/flickr_reference.png\" mime_type=\"image/png\" width=\"100\" height=\"38\" alias_key=\"2605465115\" timestamp=\"1283937710\" is_valid=\"1\"/><alias name=\"small\" filename=\"flickr_small.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/media/images/flickr/371-1-eng-GB\" url=\"var/intranet/storage/images/media/images/flickr/371-1-eng-GB/flickr_small.png\" mime_type=\"image/png\" width=\"100\" height=\"38\" alias_key=\"2343348577\" timestamp=\"1283937710\" is_valid=\"1\"/><alias name=\"medium\" filename=\"flickr_medium.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/media/images/flickr/371-1-eng-GB\" url=\"var/intranet/storage/images/media/images/flickr/371-1-eng-GB/flickr_medium.png\" mime_type=\"image/png\" width=\"100\" height=\"38\" alias_key=\"405413724\" timestamp=\"1283937710\" is_valid=\"1\"/></ezimage>\n','ezimage',371,'eng-GB',3,0,'',1),(0,116,106,0,NULL,'wikipedia','ezstring',372,'eng-GB',3,0,'wikipedia',1),(0,117,106,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',373,'eng-GB',3,0,'',1),(0,118,106,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"1\" filename=\"wikipedia.png\" suffix=\"png\" basename=\"wikipedia\" dirpath=\"var/intranet/storage/images/media/images/wikipedia/374-1-eng-GB\" url=\"var/intranet/storage/images/media/images/wikipedia/374-1-eng-GB/wikipedia.png\" original_filename=\"wikipedia.png\" mime_type=\"image/png\" width=\"100\" height=\"38\" alternative_text=\"Følg oss på Wikipedia &gt;&gt;\" alias_key=\"1293033771\" timestamp=\"1283937756\"><original attribute_id=\"374\" attribute_version=\"1\" attribute_language=\"eng-GB\"/><alias name=\"reference\" filename=\"wikipedia_reference.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/media/images/wikipedia/374-1-eng-GB\" url=\"var/intranet/storage/images/media/images/wikipedia/374-1-eng-GB/wikipedia_reference.png\" mime_type=\"image/png\" width=\"100\" height=\"38\" alias_key=\"2605465115\" timestamp=\"1283937757\" is_valid=\"1\"/><alias name=\"small\" filename=\"wikipedia_small.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/media/images/wikipedia/374-1-eng-GB\" url=\"var/intranet/storage/images/media/images/wikipedia/374-1-eng-GB/wikipedia_small.png\" mime_type=\"image/png\" width=\"100\" height=\"38\" alias_key=\"2343348577\" timestamp=\"1283937867\" is_valid=\"1\"/><alias name=\"medium\" filename=\"wikipedia_medium.png\" suffix=\"png\" dirpath=\"var/intranet/storage/images/media/images/wikipedia/374-1-eng-GB\" url=\"var/intranet/storage/images/media/images/wikipedia/374-1-eng-GB/wikipedia_medium.png\" mime_type=\"image/png\" width=\"100\" height=\"38\" alias_key=\"405413724\" timestamp=\"1283937758\" is_valid=\"1\"/></ezimage>\n','ezimage',374,'eng-GB',3,0,'',1),(0,116,107,0,NULL,'wikipedia','ezstring',375,'eng-GB',3,0,'wikipedia',1),(0,117,107,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',376,'eng-GB',3,0,'',1),(0,118,107,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"1\" filename=\"bfe4cb994e83efd48838887530c5480d.png\" suffix=\"png\" basename=\"bfe4cb994e83efd48838887530c5480d\" dirpath=\"var/intranet/storage/images/troms-fylkeskommune/foelg-oss-paa-sosiale-medier/wikipedia/377-1-eng-GB/trashed\" url=\"var/intranet/storage/images/troms-fylkeskommune/foelg-oss-paa-sosiale-medier/wikipedia/377-1-eng-GB/trashed/bfe4cb994e83efd48838887530c5480d.png\" original_filename=\"wikipedia.png\" mime_type=\"image/png\" width=\"100\" height=\"38\" alternative_text=\"Følg oss på Wikipedia &gt;&gt;\" alias_key=\"1293033771\" timestamp=\"1283937904\"><original attribute_id=\"377\" attribute_version=\"1\" attribute_language=\"eng-GB\"/></ezimage>\n','ezimage',377,'eng-GB',3,0,'',1),(0,8,118,0,NULL,'Kurs','ezstring',428,'eng-GB',3,0,'kurs',1),(0,8,118,0,NULL,'Kurs','ezstring',428,'eng-GB',3,0,'kurs',2),(0,9,118,0,NULL,'Testbruker','ezstring',429,'eng-GB',3,0,'testbruker',1),(0,9,118,0,NULL,'Testbruker','ezstring',429,'eng-GB',3,0,'testbruker',2),(0,12,118,0,NULL,'','ezuser',430,'eng-GB',3,0,'',1),(0,12,118,0,NULL,'','ezuser',430,'eng-GB',3,0,'',2),(0,179,118,0,NULL,'','eztext',431,'eng-GB',3,0,'',1),(0,179,118,0,NULL,'','eztext',431,'eng-GB',3,0,'',2),(0,180,118,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"\" filename=\"\" suffix=\"\" basename=\"\" dirpath=\"\" url=\"\" original_filename=\"\" mime_type=\"\" width=\"\" height=\"\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1295525579\"><original attribute_id=\"432\" attribute_version=\"1\" attribute_language=\"eng-GB\"/></ezimage>\n','ezimage',432,'eng-GB',3,0,'',1),(0,180,118,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"\" filename=\"\" suffix=\"\" basename=\"\" dirpath=\"\" url=\"\" original_filename=\"\" mime_type=\"\" width=\"\" height=\"\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1295525579\"><original attribute_id=\"432\" attribute_version=\"1\" attribute_language=\"eng-GB\"/></ezimage>\n','ezimage',432,'eng-GB',3,0,'',2),(0,8,119,0,NULL,'Kurt','ezstring',433,'eng-GB',3,0,'kurt',1),(0,9,119,0,NULL,'Kursansvarlig','ezstring',434,'eng-GB',3,0,'kursansvarlig',1),(0,12,119,0,NULL,'','ezuser',435,'eng-GB',3,0,'',1),(0,179,119,0,NULL,'','eztext',436,'eng-GB',3,0,'',1),(0,180,119,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"\" filename=\"\" suffix=\"\" basename=\"\" dirpath=\"\" url=\"\" original_filename=\"\" mime_type=\"\" width=\"\" height=\"\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1295527879\"><original attribute_id=\"437\" attribute_version=\"1\" attribute_language=\"eng-GB\"/></ezimage>\n','ezimage',437,'eng-GB',3,0,'',1),(0,8,120,0,NULL,'Test','ezstring',438,'eng-GB',3,0,'test',1),(0,9,120,0,NULL,'Bruker','ezstring',439,'eng-GB',3,0,'bruker',1),(0,12,120,0,NULL,'','ezuser',440,'eng-GB',3,0,'',1),(0,179,120,0,NULL,'','eztext',441,'eng-GB',3,0,'',1),(0,180,120,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"\" filename=\"\" suffix=\"\" basename=\"\" dirpath=\"\" url=\"\" original_filename=\"\" mime_type=\"\" width=\"\" height=\"\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1309526100\"><original attribute_id=\"442\" attribute_version=\"1\" attribute_language=\"eng-GB\"/></ezimage>\n','ezimage',442,'eng-GB',3,0,'',1),(0,8,122,0,NULL,'Rolle','ezstring',448,'eng-GB',3,0,'rolle',1),(0,8,122,0,NULL,'Rolle','ezstring',448,'eng-GB',3,0,'rolle',2),(0,9,122,0,NULL,'Tester','ezstring',449,'eng-GB',3,0,'tester',1),(0,9,122,0,NULL,'Tester','ezstring',449,'eng-GB',3,0,'tester',2),(0,12,122,0,NULL,'','ezuser',450,'eng-GB',3,0,'',1),(0,12,122,0,NULL,'','ezuser',450,'eng-GB',3,0,'',2),(0,179,122,0,NULL,'','eztext',451,'eng-GB',3,0,'',1),(0,179,122,0,NULL,'','eztext',451,'eng-GB',3,0,'',2),(0,180,122,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"\" filename=\"\" suffix=\"\" basename=\"\" dirpath=\"\" url=\"\" original_filename=\"\" mime_type=\"\" width=\"\" height=\"\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1316109526\"><original attribute_id=\"452\" attribute_version=\"1\" attribute_language=\"eng-GB\"/></ezimage>\n','ezimage',452,'eng-GB',3,0,'',1),(0,180,122,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"\" filename=\"\" suffix=\"\" basename=\"\" dirpath=\"\" url=\"\" original_filename=\"\" mime_type=\"\" width=\"\" height=\"\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1316109526\"><original attribute_id=\"452\" attribute_version=\"1\" attribute_language=\"eng-GB\"/></ezimage>\n','ezimage',452,'eng-GB',3,0,'',2),(0,8,134,0,NULL,'Gunnar','ezstring',508,'eng-GB',3,0,'gunnar',1),(0,9,134,0,NULL,'Velle','ezstring',509,'eng-GB',3,0,'velle',1),(0,12,134,0,NULL,'','ezuser',510,'eng-GB',3,0,'',1),(0,179,134,0,NULL,'','eztext',511,'eng-GB',3,0,'',1),(0,180,134,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"\" is_valid=\"\" filename=\"\" suffix=\"\" basename=\"\" dirpath=\"\" url=\"\" original_filename=\"\" mime_type=\"\" width=\"\" height=\"\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1353415505\"/>\n','ezimage',512,'eng-GB',3,0,'',1),(0,8,140,0,NULL,'Test','ezstring',538,'eng-GB',3,0,'test',1),(0,9,140,0,NULL,'Test','ezstring',539,'eng-GB',3,0,'test',1),(0,12,140,0,NULL,'','ezuser',540,'eng-GB',3,0,'',1),(0,179,140,0,NULL,'','eztext',541,'eng-GB',3,0,'',1),(0,180,140,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"\" filename=\"\" suffix=\"\" basename=\"\" dirpath=\"\" url=\"\" original_filename=\"\" mime_type=\"\" width=\"\" height=\"\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1358503247\"><original attribute_id=\"542\" attribute_version=\"1\" attribute_language=\"eng-GB\"/></ezimage>\n','ezimage',542,'eng-GB',3,0,'',1),(0,8,141,0,NULL,'Gunnar','ezstring',543,'eng-GB',3,0,'gunnar',1),(0,9,141,0,NULL,'Velle','ezstring',544,'eng-GB',3,0,'velle',1),(0,12,141,0,NULL,'','ezuser',545,'eng-GB',3,0,'',1),(0,179,141,0,NULL,'','eztext',546,'eng-GB',3,0,'',1),(0,180,141,0,NULL,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ezimage serial_number=\"1\" is_valid=\"\" filename=\"\" suffix=\"\" basename=\"\" dirpath=\"\" url=\"\" original_filename=\"\" mime_type=\"\" width=\"\" height=\"\" alternative_text=\"\" alias_key=\"1293033771\" timestamp=\"1359038176\"><original attribute_id=\"547\" attribute_version=\"1\" attribute_language=\"eng-GB\"/></ezimage>\n','ezimage',547,'eng-GB',3,0,'',1);
/*!40000 ALTER TABLE `ezcontentobject_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcontentobject_link`
--

DROP TABLE IF EXISTS `ezcontentobject_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcontentobject_link` (
  `contentclassattribute_id` int(11) NOT NULL default '0',
  `from_contentobject_id` int(11) NOT NULL default '0',
  `from_contentobject_version` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `op_code` int(11) NOT NULL default '0',
  `relation_type` int(11) NOT NULL default '1',
  `to_contentobject_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `ezco_link_from` (`from_contentobject_id`,`from_contentobject_version`,`contentclassattribute_id`),
  KEY `ezco_link_to_co_id` (`to_contentobject_id`)
) ENGINE=InnoDB AUTO_INCREMENT=307 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcontentobject_link`
--

LOCK TABLES `ezcontentobject_link` WRITE;
/*!40000 ALTER TABLE `ezcontentobject_link` DISABLE KEYS */;
INSERT INTO `ezcontentobject_link` VALUES (0,82,15,88,0,3,93),(0,82,16,96,0,2,94),(0,77,19,125,0,2,97),(0,77,20,129,0,2,97),(0,77,21,135,0,2,97),(0,77,21,137,0,2,99),(0,77,22,141,0,2,97),(0,77,22,143,0,2,99),(0,77,23,158,0,2,97),(0,100,8,201,0,0,97),(0,100,8,203,0,2,101),(0,100,9,207,0,0,97),(0,100,9,209,0,2,101),(0,100,10,213,0,0,97),(0,100,10,215,0,2,101),(0,100,11,231,0,0,97),(0,100,11,232,0,2,102),(0,100,11,233,0,2,101),(0,100,12,237,0,0,97),(0,100,12,238,0,2,102),(0,100,12,239,0,2,101),(0,100,13,246,0,0,97),(0,100,13,247,0,2,101),(0,100,13,248,0,2,103),(0,100,14,254,0,0,97),(0,100,14,255,0,2,101),(0,100,14,256,0,2,103),(0,100,15,268,0,0,97),(0,100,15,269,0,2,101),(0,100,15,270,0,2,103),(0,100,15,271,0,2,106),(0,100,16,279,0,0,97),(0,100,16,280,0,2,101),(0,100,16,281,0,2,103),(0,100,16,282,0,2,107),(0,77,24,284,0,2,97),(0,100,17,290,0,0,97),(0,100,17,291,0,2,101),(0,100,17,292,0,2,103),(0,82,17,296,0,2,94),(0,82,18,300,0,2,94),(0,82,19,304,0,2,94),(0,77,25,306,0,2,97);
/*!40000 ALTER TABLE `ezcontentobject_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcontentobject_name`
--

DROP TABLE IF EXISTS `ezcontentobject_name`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcontentobject_name` (
  `content_translation` varchar(20) NOT NULL default '',
  `content_version` int(11) NOT NULL default '0',
  `contentobject_id` int(11) NOT NULL default '0',
  `language_id` int(11) NOT NULL default '0',
  `name` varchar(255) default NULL,
  `real_translation` varchar(20) default NULL,
  PRIMARY KEY  (`contentobject_id`,`content_version`,`content_translation`),
  KEY `ezcontentobject_name_co_id` (`contentobject_id`),
  KEY `ezcontentobject_name_cov_id` (`content_version`),
  KEY `ezcontentobject_name_lang_id` (`language_id`),
  KEY `ezcontentobject_name_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcontentobject_name`
--

LOCK TABLES `ezcontentobject_name` WRITE;
/*!40000 ALTER TABLE `ezcontentobject_name` DISABLE KEYS */;
INSERT INTO `ezcontentobject_name` VALUES ('eng-GB',6,1,3,'Forsiden','eng-GB'),('eng-GB',7,1,3,'Forsiden','eng-GB'),('eng-GB',8,1,3,'Forsiden','eng-GB'),('eng-GB',9,1,3,'Forsiden','eng-GB'),('eng-GB',1,4,3,'Users','eng-GB'),('eng-GB',2,4,3,'khalil','eng-GB'),('eng-GB',3,4,3,'Users group','eng-GB'),('eng-GB',4,4,2,'Users group','eng-GB'),('eng-GB',2,10,3,'Anonymous User','eng-GB'),('eng-GB',1,11,3,'Guest accounts','eng-GB'),('eng-GB',1,12,3,'Administrator users','eng-GB'),('eng-GB',2,12,2,'Administrator users','eng-GB'),('eng-GB',1,13,3,'Editors','eng-GB'),('eng-GB',2,13,3,'Opplæringsansvarlige','eng-GB'),('eng-GB',1,14,3,'Administrator User','eng-GB'),('eng-GB',1,41,3,'Media','eng-GB'),('eng-GB',1,42,3,'Anonymous Users','eng-GB'),('eng-GB',1,45,3,'Setup','eng-GB'),('eng-GB',1,49,3,'Images','eng-GB'),('eng-GB',1,50,3,'Files','eng-GB'),('eng-GB',1,51,3,'Multimedia','eng-GB'),('eng-GB',1,52,2,'Common INI settings','eng-GB'),('eng-GB',1,54,2,'eZ Publish','eng-GB'),('eng-GB',1,56,3,'Design','eng-GB'),('eng-GB',1,72,3,'Kursansvarlige','eng-GB'),('eng-GB',1,73,3,'Ansatte','eng-GB'),('eng-GB',19,77,2,'RSS feed','eng-GB'),('eng-GB',20,77,2,'RSS feed','eng-GB'),('eng-GB',21,77,2,'RSS feed','eng-GB'),('eng-GB',22,77,2,'RSS feed','eng-GB'),('eng-GB',23,77,2,'RSS feed','eng-GB'),('eng-GB',24,77,2,'RSS feed','eng-GB'),('eng-GB',25,77,2,'RSS feed','eng-GB'),('eng-GB',26,77,2,'RSS feed','eng-GB'),('eng-GB',27,77,2,'RSS feed','eng-GB'),('eng-GB',28,77,2,'RSS feed','eng-GB'),('eng-GB',15,82,3,'Troms fylkeskommune','eng-GB'),('eng-GB',16,82,3,'Troms fylkeskommune','eng-GB'),('eng-GB',17,82,3,'Troms fylkeskommune','eng-GB'),('eng-GB',18,82,3,'storholmen','eng-GB'),('eng-GB',19,82,3,'Storholmen','eng-GB'),('eng-GB',1,84,3,'FylkeshusetFront','eng-GB'),('eng-GB',1,85,3,'FylkeshusetFront','eng-GB'),('eng-GB',1,87,3,'Help?','eng-GB'),('eng-GB',2,87,3,'Help?','eng-GB'),('eng-GB',3,87,3,'Help?','eng-GB'),('eng-GB',4,87,3,'Help?','eng-GB'),('eng-GB',5,87,3,'Help?','eng-GB'),('eng-GB',1,90,3,'128px-Feed-icon_svg','eng-GB'),('eng-GB',1,92,3,'FylkeshusetFront','eng-GB'),('eng-GB',1,93,3,'FylkeshusetFront','eng-GB'),('eng-GB',1,94,3,'FylkeshusetFront','eng-GB'),('eng-GB',2,94,3,'Fylkeshuset','eng-GB'),('eng-GB',1,95,3,'128px-Feed-icon_svg','eng-GB'),('eng-GB',2,95,3,'RSS Feed','eng-GB'),('eng-GB',1,97,3,'128px-Feed-icon','eng-GB'),('eng-GB',1,99,3,'flickr','eng-GB'),('eng-GB',2,99,3,'Følg oss på Flickr >>','eng-GB'),('eng-GB',8,100,2,'Følg oss på sosiale medier','eng-GB'),('eng-GB',9,100,2,'Følg oss på sosiale medier','eng-GB'),('eng-GB',10,100,2,'Følg oss på sosiale medier','eng-GB'),('eng-GB',11,100,2,'Følg oss på sosiale medier','eng-GB'),('eng-GB',12,100,2,'Følg oss på sosiale medier','eng-GB'),('eng-GB',13,100,2,'Følg oss på sosiale medier','eng-GB'),('eng-GB',14,100,2,'Følg oss på sosiale medier','eng-GB'),('eng-GB',15,100,2,'Følg oss på sosiale medier','eng-GB'),('eng-GB',16,100,2,'Følg oss på sosiale medier','eng-GB'),('eng-GB',17,100,2,'Følg oss på sosiale medier','eng-GB'),('eng-GB',1,101,3,'flickr','eng-GB'),('eng-GB',1,102,3,'Følg oss på Facebook >>','eng-GB'),('eng-GB',1,103,3,'facebook','eng-GB'),('eng-GB',1,105,3,'flickr','eng-GB'),('eng-GB',1,106,3,'wikipedia','eng-GB'),('eng-GB',1,107,3,'wikipedia','eng-GB'),('eng-GB',1,118,3,'Kurs Testbruker','eng-GB'),('eng-GB',2,118,3,'Kurs Testbruker','eng-GB'),('eng-GB',1,119,3,'Kurt Kursansvarlig','eng-GB'),('eng-GB',1,120,3,'Test Bruker','eng-GB'),('eng-GB',1,122,3,'Rolle Tester','eng-GB'),('eng-GB',2,122,3,'Rolle Tester','eng-GB'),('eng-GB',1,134,3,'Gunnar Velle','eng-GB'),('eng-GB',1,140,3,'Test Test','eng-GB'),('eng-GB',1,141,3,'Gunnar Velle','eng-GB');
/*!40000 ALTER TABLE `ezcontentobject_name` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcontentobject_trash`
--

DROP TABLE IF EXISTS `ezcontentobject_trash`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcontentobject_trash` (
  `contentobject_id` int(11) default NULL,
  `contentobject_version` int(11) default NULL,
  `depth` int(11) NOT NULL default '0',
  `is_hidden` int(11) NOT NULL default '0',
  `is_invisible` int(11) NOT NULL default '0',
  `main_node_id` int(11) default NULL,
  `modified_subnode` int(11) default '0',
  `node_id` int(11) NOT NULL default '0',
  `parent_node_id` int(11) NOT NULL default '0',
  `path_identification_string` longtext,
  `path_string` varchar(255) NOT NULL default '',
  `priority` int(11) NOT NULL default '0',
  `remote_id` varchar(100) NOT NULL default '',
  `sort_field` int(11) default '1',
  `sort_order` int(11) default '1',
  PRIMARY KEY  (`node_id`),
  KEY `ezcobj_trash_co_id` (`contentobject_id`),
  KEY `ezcobj_trash_depth` (`depth`),
  KEY `ezcobj_trash_modified_subnode` (`modified_subnode`),
  KEY `ezcobj_trash_p_node_id` (`parent_node_id`),
  KEY `ezcobj_trash_path` (`path_string`),
  KEY `ezcobj_trash_path_ident` (`path_identification_string`(50))
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcontentobject_trash`
--

LOCK TABLES `ezcontentobject_trash` WRITE;
/*!40000 ALTER TABLE `ezcontentobject_trash` DISABLE KEYS */;
INSERT INTO `ezcontentobject_trash` VALUES (94,2,3,1,1,85,1283933329,85,75,'troms_fylkeskommune/fylkeshuset','/1/2/75/85/',0,'1b15ead7a11aa2b9ee7f8c43e7893207',2,0),(100,17,3,0,0,91,1289823010,91,75,'troms_fylkeskommune/foelg_oss_paa_sosiale_medier','/1/2/75/91/',0,'7ba3dfea0907672efee94de3868afdae',1,1),(101,1,4,0,0,92,1283935822,92,91,'troms_fylkeskommune/foelg_oss_paa_sosiale_medier/flickr','/1/2/75/91/92/',0,'81ca4a16fb86086d44e6aa46cff1e899',2,0),(103,1,4,0,0,94,1283937017,94,91,'troms_fylkeskommune/foelg_oss_paa_sosiale_medier/facebook','/1/2/75/91/94/',0,'b7c8ac2e285fa4ece8ddaef2cb143d31',2,0),(107,1,4,0,0,98,1283937904,98,91,'troms_fylkeskommune/foelg_oss_paa_sosiale_medier/wikipedia','/1/2/75/91/98/',0,'9c16bcd1cae05bbae6f121b0c346d96b',2,0);
/*!40000 ALTER TABLE `ezcontentobject_trash` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcontentobject_tree`
--

DROP TABLE IF EXISTS `ezcontentobject_tree`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcontentobject_tree` (
  `contentobject_id` int(11) default NULL,
  `contentobject_is_published` int(11) default NULL,
  `contentobject_version` int(11) default NULL,
  `depth` int(11) NOT NULL default '0',
  `is_hidden` int(11) NOT NULL default '0',
  `is_invisible` int(11) NOT NULL default '0',
  `main_node_id` int(11) default NULL,
  `modified_subnode` int(11) default '0',
  `node_id` int(11) NOT NULL auto_increment,
  `parent_node_id` int(11) NOT NULL default '0',
  `path_identification_string` longtext,
  `path_string` varchar(255) NOT NULL default '',
  `priority` int(11) NOT NULL default '0',
  `remote_id` varchar(100) NOT NULL default '',
  `sort_field` int(11) default '1',
  `sort_order` int(11) default '1',
  PRIMARY KEY  (`node_id`),
  KEY `ezcontentobject_tree_co_id` (`contentobject_id`),
  KEY `ezcontentobject_tree_depth` (`depth`),
  KEY `ezcontentobject_tree_p_node_id` (`parent_node_id`),
  KEY `ezcontentobject_tree_path` (`path_string`),
  KEY `ezcontentobject_tree_path_ident` (`path_identification_string`(50)),
  KEY `modified_subnode` (`modified_subnode`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcontentobject_tree`
--

LOCK TABLES `ezcontentobject_tree` WRITE;
/*!40000 ALTER TABLE `ezcontentobject_tree` DISABLE KEYS */;
INSERT INTO `ezcontentobject_tree` VALUES (0,1,1,0,0,0,1,1359038200,1,1,'','/1/',0,'629709ba256fe317c3ddcee35453a96a',1,1),(1,1,9,1,0,0,2,1295529432,2,1,'','/1/2/',0,'f3e90596361e31d496d4026eb624c983',8,1),(4,1,3,1,0,0,5,1359038200,5,1,'users_group','/1/5/',0,'3f6d92f8044aed134f32153517850f5a',1,1),(11,1,1,2,0,0,12,1358425976,12,5,'users/guest_accounts','/1/5/12/',0,'602dcf84765e56b7f999eaafd3821dd3',1,1),(12,1,1,2,0,0,13,1357909239,13,5,'users/administrator_users','/1/5/13/',0,'769380b7aa94541679167eab817ca893',1,1),(13,1,2,2,0,0,14,1357910695,14,5,'users_group/opplaeringsansvarlige','/1/5/14/',0,'f7dda2854fc68f7c8455d9cb14bd04a9',1,1),(14,1,1,3,0,0,15,1282805988,15,13,'users/administrator_users/administrator_user','/1/5/13/15/',0,'e5161a99f733200b9ed4e80f9c16187b',1,1),(41,1,1,1,0,0,43,1283937757,43,1,'media','/1/43/',0,'75c715a51699d2d309a924eca6a95145',9,1),(42,1,1,2,0,0,44,1282805988,44,5,'users/anonymous_users','/1/5/44/',0,'4fdf0072da953bb276c0c7e0141c5c9b',9,1),(10,1,2,3,0,0,45,1282805988,45,44,'users/anonymous_users/anonymous_user','/1/5/44/45/',0,'2cf8343bee7b482bab82b269d8fecd76',9,1),(45,1,1,1,0,0,48,1184592117,48,1,'setup2','/1/48/',0,'182ce1b5af0c09fa378557c462ba2617',9,1),(49,1,1,2,0,0,51,1283937757,51,43,'media/images','/1/43/51/',0,'1b26c0454b09bb49dfb1b9190ffd67cb',9,1),(50,1,1,2,0,0,52,1081860720,52,43,'media/files','/1/43/52/',0,'0b113a208f7890f9ad3c24444ff5988c',9,1),(51,1,1,2,0,0,53,1081860720,53,43,'media/multimedia','/1/43/53/',0,'4f18b82c75f10aad476cae5adf98c11f',9,1),(52,1,1,2,0,0,54,1184592117,54,48,'setup2/common_ini_settings','/1/48/54/',0,'fa9f3cff9cf90ecfae335718dcbddfe2',1,1),(54,1,1,2,0,0,56,1082016653,56,58,'design/ez_publish','/1/58/56/',0,'772da20ecf88b3035d73cbdfcea0f119',1,1),(56,1,1,1,0,0,58,1103023133,58,1,'design','/1/58/',0,'79f2d67372ab56f59b5d65bb9e0ca3b9',2,0),(72,1,1,2,0,0,67,1358503275,67,5,'users_group/kursansvarlige','/1/5/67/',0,'032e41c6190720633648cd8775f3eabc',6,1),(73,1,1,2,0,0,68,1359038200,68,5,'users_group/ansatte','/1/5/68/',0,'4f4c926ec5f9aeb4dd8b72febcf3ce80',1,1),(77,1,28,2,0,0,71,1295529432,71,2,'rss_feed','/1/2/71/',2,'29a3fc2f40e5d7b8249e6a96a843ab61',1,1),(82,1,19,2,0,0,75,1295528707,75,2,'storholmen','/1/2/75/',1,'3193129f0be9f97faaaee46dfd67f3bd',1,1),(84,1,1,3,0,0,77,1283858528,77,51,'media/images/fylkeshusetfront','/1/43/51/77/',0,'b3eda58db9607ea367fce9ca09f1e659',2,0),(85,1,1,3,0,0,78,1283859632,78,51,'media/images/fylkeshusetfront2','/1/43/51/78/',0,'e756328fa0e79a6399c06baacf30729a',2,0),(87,1,5,2,0,0,80,1295529221,80,2,'help','/1/2/80/',3,'18be8481001c0ce11d368d43dba150bd',1,1),(90,1,1,3,0,0,82,1283864415,82,51,'media/images/128px_feed_icon_svg','/1/43/51/82/',0,'4a97281409de0c94694330d8d4f3a5f6',2,0),(92,1,1,3,0,0,83,1283930105,83,51,'media/images/fylkeshusetfront3','/1/43/51/83/',0,'06f11796416ed2b33ece9451b172b7bc',2,0),(93,1,1,3,0,0,84,1283931319,84,51,'media/images/fylkeshusetfront4','/1/43/51/84/',0,'1f9573d1a17891c6982b8a8755bb6a41',2,0),(95,1,2,3,0,0,86,1283934540,86,71,'rss_feed/rss_feed','/1/2/71/86/',0,'134f66dae96f24bf711d5a747191b1fe',2,0),(97,1,1,3,0,0,88,1283934540,88,71,'rss_feed/128px_feed_icon','/1/2/71/88/',0,'dc53b45d50299bcafa63bc0ac4774fe0',2,0),(99,1,2,3,0,0,90,1283935394,90,51,'media/images/foelg_oss_paa_flickr','/1/43/51/90/',0,'6600c1d47e611bce5c5605dcb4b64152',2,0),(102,1,1,3,0,0,93,1283936573,93,51,'media/images/foelg_oss_paa_facebook','/1/43/51/93/',0,'7190c5d650377385895e9ee885ed9e07',2,0),(105,1,1,3,0,0,96,1283937709,96,51,'media/images/flickr','/1/43/51/96/',0,'5248af7e3e31e805bd5ca1d99e25f558',2,0),(106,1,1,3,0,0,97,1283937757,97,51,'media/images/wikipedia','/1/43/51/97/',0,'a471763457eb06f08ee18b648a2affa9',2,0),(118,1,2,3,0,0,105,1345724758,105,67,'users_group/kursansvarlige/kurs_testbruker','/1/5/67/105/',0,'f1d64eca187bc49f7d309119c3d303bc',1,1),(119,1,1,3,0,0,106,1295527921,106,67,'users_group/kursansvarlige/kurt_kursansvarlig','/1/5/67/106/',0,'34f6f61a2a84662130aaced10e6c7aae',1,1),(120,1,1,3,0,0,107,1309526167,107,68,'users_group/ansatte/test_bruker','/1/5/68/107/',0,'a69b89fc2e5f6279742be15675d243ca',1,1),(122,1,2,3,0,0,109,1316109807,109,14,'users_group/opplaeringsansvarlige/rolle_tester','/1/5/14/109/',0,'356e696dc92ad41c21fd3dfa2c9dfa1c',1,1),(134,1,1,3,0,0,119,1353415505,119,67,'users_group/kursansvarlige/gunnar_velle','/1/5/67/119/',0,'LDAP_67',2,0),(140,1,1,3,0,0,125,1358503275,125,67,'users_group/kursansvarlige/test_test','/1/5/67/125/',0,'96e51d788c352d8e694a0da817c52be0',1,1),(141,1,1,3,0,0,126,1359038200,126,68,'users_group/ansatte/gunnar_velle','/1/5/68/126/',0,'dddbd79a076f4da69e6134e20c689465',1,1);
/*!40000 ALTER TABLE `ezcontentobject_tree` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcontentobject_version`
--

DROP TABLE IF EXISTS `ezcontentobject_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcontentobject_version` (
  `contentobject_id` int(11) default NULL,
  `created` int(11) NOT NULL default '0',
  `creator_id` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `initial_language_id` int(11) NOT NULL default '0',
  `language_mask` int(11) NOT NULL default '0',
  `modified` int(11) NOT NULL default '0',
  `status` int(11) NOT NULL default '0',
  `user_id` int(11) NOT NULL default '0',
  `version` int(11) NOT NULL default '0',
  `workflow_event_pos` int(11) default '0',
  PRIMARY KEY  (`id`),
  KEY `ezcobj_version_creator_id` (`creator_id`),
  KEY `ezcobj_version_status` (`status`),
  KEY `idx_object_version_objver` (`contentobject_id`,`version`)
) ENGINE=InnoDB AUTO_INCREMENT=720 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcontentobject_version`
--

LOCK TABLES `ezcontentobject_version` WRITE;
/*!40000 ALTER TABLE `ezcontentobject_version` DISABLE KEYS */;
INSERT INTO `ezcontentobject_version` VALUES (4,0,14,4,2,3,0,3,0,1,1),(11,1033920737,14,439,2,3,1033920746,1,0,1,0),(12,1033920760,14,440,2,3,1033920775,1,0,1,0),(13,1033920786,14,441,2,3,1033920794,3,0,1,0),(14,1033920808,14,442,2,3,1033920830,1,0,1,0),(41,1060695450,14,472,2,3,1060695457,1,0,1,0),(42,1072180278,14,473,2,3,1072180330,1,0,1,0),(10,1072180337,14,474,2,3,1072180405,1,0,2,0),(45,1079684084,14,477,2,3,1079684190,1,0,1,0),(49,1080220181,14,488,2,3,1080220197,1,0,1,0),(50,1080220211,14,489,2,3,1080220220,1,0,1,0),(51,1080220225,14,490,2,3,1080220233,1,0,1,0),(52,1082016497,14,491,2,3,1082016591,1,0,1,0),(54,1082016628,14,492,2,3,1082016652,1,0,1,0),(56,1103023120,14,495,2,3,1103023120,1,0,1,0),(4,1282805774,14,500,2,3,1282805830,3,0,2,1),(4,1282806017,14,502,2,3,1282806074,1,0,3,1),(4,1283510950,14,507,2,3,1283511038,0,0,4,1),(12,1283763639,14,518,2,3,1283763648,0,0,2,0),(13,1283764039,14,523,2,3,1283764048,1,0,2,0),(72,1283764058,14,524,2,3,1283764063,1,0,1,0),(73,1283764067,14,525,2,3,1283764074,1,0,1,0),(84,1283858528,0,550,2,3,1283858528,1,0,1,0),(85,1283859632,0,554,2,3,1283859632,1,0,1,0),(87,1283860663,0,557,2,3,1283860725,3,0,1,0),(87,1283860976,0,561,2,3,1283860998,3,0,2,0),(87,1283861035,0,563,2,3,1283861106,3,0,3,0),(87,1283861216,0,564,2,3,1283861245,3,0,4,0),(90,1283864415,0,573,2,3,1283864415,1,0,1,0),(92,1283930105,0,581,2,3,1283930105,1,0,1,0),(82,1283931260,0,585,2,3,1283931377,3,0,15,0),(93,1283931318,0,586,2,3,1283931318,1,0,1,0),(82,1283931414,0,587,2,3,1283931792,3,0,16,0),(94,1283931783,0,588,2,3,1283931783,3,0,1,0),(95,1283931925,0,590,2,3,1283931925,3,0,1,0),(95,1283932095,0,592,2,3,1283932114,1,0,2,0),(94,1283932237,0,594,2,3,1283932330,1,0,2,0),(97,1283933210,0,598,2,3,1283933210,1,0,1,0),(77,1283933687,0,602,2,3,1283933715,3,0,19,0),(77,1283933950,0,603,2,3,1283934097,3,0,20,0),(77,1283934171,0,605,2,3,1283934333,3,0,21,0),(99,1283934249,0,606,2,3,1283934249,3,0,1,0),(77,1283934378,0,607,2,3,1283934440,3,0,22,0),(77,1283934801,0,610,2,3,1283934813,3,0,23,0),(99,1283935365,0,616,2,3,1283935394,1,0,2,0),(101,1283935821,0,619,2,3,1283935821,1,0,1,0),(100,1283936047,0,622,2,3,1283936140,3,0,8,0),(100,1283936180,0,623,2,3,1283936202,3,0,9,0),(100,1283936237,0,624,2,3,1283936333,3,0,10,0),(100,1283936410,0,625,2,3,1283936638,3,0,11,0),(102,1283936573,0,626,2,3,1283936573,1,0,1,0),(100,1283936679,0,627,2,3,1283936890,3,0,12,0),(100,1283936955,0,629,2,3,1283937054,3,0,13,0),(103,1283937017,0,630,2,3,1283937017,1,0,1,0),(100,1283937433,0,631,2,3,1283937594,3,0,14,0),(100,1283937626,0,633,2,3,1283937793,3,0,15,0),(105,1283937709,0,634,2,3,1283937709,1,0,1,0),(106,1283937756,0,635,2,3,1283937756,1,0,1,0),(100,1283937873,0,636,2,3,1283937937,3,0,16,0),(107,1283937904,0,637,2,3,1283937904,1,0,1,0),(77,1283939769,0,638,2,3,1283939785,3,0,24,0),(1,1284976537,0,641,2,3,1284976575,3,0,6,1),(1,1284976601,0,642,2,3,1284976630,3,0,7,1),(87,1285150384,0,643,2,3,1285150403,1,0,5,0),(100,1289822987,0,657,2,3,1289823010,1,0,17,0),(82,1294404566,0,660,2,3,1294404671,3,0,17,0),(1,1294826463,0,661,2,3,1294826480,3,0,8,1),(118,1295525578,14,662,2,3,1295525608,3,0,1,0),(119,1295527879,14,663,2,3,1295527921,1,0,1,0),(1,1295527976,14,664,2,3,1295528043,1,0,9,1),(82,1295528074,14,665,2,3,1295528235,3,0,18,0),(82,1295528259,14,667,2,3,1295528264,1,0,19,0),(77,1295528734,14,669,2,3,1295528806,3,0,25,0),(77,1295528825,14,670,2,3,1295528839,3,0,26,0),(77,1295529336,14,672,2,3,1295529390,3,0,27,0),(77,1295529418,14,673,2,3,1295529432,1,0,28,0),(120,1309526099,14,674,2,3,1309526167,1,0,1,0),(122,1316109525,14,676,2,3,1316109619,3,0,1,0),(122,1316109689,14,677,2,3,1316109807,1,0,2,0),(118,1345724743,14,680,2,3,1345724758,1,0,2,0),(134,1353415505,14,691,2,3,1353415505,1,0,1,0),(140,1358503247,14,718,2,3,1358503274,1,0,1,0),(141,1359038175,14,719,2,3,1359038200,1,0,1,0);
/*!40000 ALTER TABLE `ezcontentobject_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezcurrencydata`
--

DROP TABLE IF EXISTS `ezcurrencydata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezcurrencydata` (
  `auto_rate_value` decimal(10,5) NOT NULL default '0.00000',
  `code` varchar(4) NOT NULL default '',
  `custom_rate_value` decimal(10,5) NOT NULL default '0.00000',
  `id` int(11) NOT NULL auto_increment,
  `locale` varchar(255) NOT NULL default '',
  `rate_factor` decimal(10,5) NOT NULL default '1.00000',
  `status` int(11) NOT NULL default '1',
  `symbol` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`id`),
  KEY `ezcurrencydata_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezcurrencydata`
--

LOCK TABLES `ezcurrencydata` WRITE;
/*!40000 ALTER TABLE `ezcurrencydata` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezcurrencydata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezdiscountrule`
--

DROP TABLE IF EXISTS `ezdiscountrule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezdiscountrule` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezdiscountrule`
--

LOCK TABLES `ezdiscountrule` WRITE;
/*!40000 ALTER TABLE `ezdiscountrule` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezdiscountrule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezdiscountsubrule`
--

DROP TABLE IF EXISTS `ezdiscountsubrule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezdiscountsubrule` (
  `discount_percent` float default NULL,
  `discountrule_id` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `limitation` char(1) default NULL,
  `name` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezdiscountsubrule`
--

LOCK TABLES `ezdiscountsubrule` WRITE;
/*!40000 ALTER TABLE `ezdiscountsubrule` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezdiscountsubrule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezdiscountsubrule_value`
--

DROP TABLE IF EXISTS `ezdiscountsubrule_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezdiscountsubrule_value` (
  `discountsubrule_id` int(11) NOT NULL default '0',
  `issection` int(11) NOT NULL default '0',
  `value` int(11) NOT NULL default '0',
  PRIMARY KEY  (`discountsubrule_id`,`value`,`issection`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezdiscountsubrule_value`
--

LOCK TABLES `ezdiscountsubrule_value` WRITE;
/*!40000 ALTER TABLE `ezdiscountsubrule_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezdiscountsubrule_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezenumobjectvalue`
--

DROP TABLE IF EXISTS `ezenumobjectvalue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezenumobjectvalue` (
  `contentobject_attribute_id` int(11) NOT NULL default '0',
  `contentobject_attribute_version` int(11) NOT NULL default '0',
  `enumelement` varchar(255) NOT NULL default '',
  `enumid` int(11) NOT NULL default '0',
  `enumvalue` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`contentobject_attribute_id`,`contentobject_attribute_version`,`enumid`),
  KEY `ezenumobjectvalue_co_attr_id_co_attr_ver` (`contentobject_attribute_id`,`contentobject_attribute_version`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezenumobjectvalue`
--

LOCK TABLES `ezenumobjectvalue` WRITE;
/*!40000 ALTER TABLE `ezenumobjectvalue` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezenumobjectvalue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezenumvalue`
--

DROP TABLE IF EXISTS `ezenumvalue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezenumvalue` (
  `contentclass_attribute_id` int(11) NOT NULL default '0',
  `contentclass_attribute_version` int(11) NOT NULL default '0',
  `enumelement` varchar(255) NOT NULL default '',
  `enumvalue` varchar(255) NOT NULL default '',
  `id` int(11) NOT NULL auto_increment,
  `placement` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`,`contentclass_attribute_id`,`contentclass_attribute_version`),
  KEY `ezenumvalue_co_cl_attr_id_co_class_att_ver` (`contentclass_attribute_id`,`contentclass_attribute_version`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezenumvalue`
--

LOCK TABLES `ezenumvalue` WRITE;
/*!40000 ALTER TABLE `ezenumvalue` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezenumvalue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezforgot_password`
--

DROP TABLE IF EXISTS `ezforgot_password`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezforgot_password` (
  `hash_key` varchar(32) NOT NULL default '',
  `id` int(11) NOT NULL auto_increment,
  `time` int(11) NOT NULL default '0',
  `user_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `ezforgot_password_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezforgot_password`
--

LOCK TABLES `ezforgot_password` WRITE;
/*!40000 ALTER TABLE `ezforgot_password` DISABLE KEYS */;
INSERT INTO `ezforgot_password` VALUES ('ebcabc3c6db7880d68844d5a9344c638',1,1367351958,141),('3746d9451d95cc14e2dcf1d288478c10',2,1367352056,141),('6c5694b94e996b237acb23b73ff7736e',3,1367352396,141),('e9a2cf98fefa24d64d09516eb56102d5',4,1367352421,141),('1c3f7fd570d61bc306ca624f8f082ce7',5,1367352428,141),('8a229ccd0b08f1a80124e0a4061a418f',6,1367352490,141),('8d65f026c6e1abbdd945f3935c4c5edb',7,1367352561,141),('c6cd2a03cc6610bd9d81f974c80faaf4',8,1367352704,141),('1cf623b2d6696638739f04456b582a3a',9,1367352705,141),('b6ae58dfce72b4e2a628506d13f2e572',10,1367352706,141),('7678c72ce7d135179b797f1434a64079',11,1367352707,141),('0136cda86b7ec1d04227ce64cf176cc4',12,1367352726,141),('188768fc21dd471dc60695cf98e49457',13,1367353098,141),('406f7183003c8be833b15620d07b9607',14,1367353284,141);
/*!40000 ALTER TABLE `ezforgot_password` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezgeneral_digest_user_settings`
--

DROP TABLE IF EXISTS `ezgeneral_digest_user_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezgeneral_digest_user_settings` (
  `address` varchar(255) NOT NULL default '',
  `day` varchar(255) NOT NULL default '',
  `digest_type` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `receive_digest` int(11) NOT NULL default '0',
  `time` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `ezgeneral_digest_user_settings_address` (`address`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezgeneral_digest_user_settings`
--

LOCK TABLES `ezgeneral_digest_user_settings` WRITE;
/*!40000 ALTER TABLE `ezgeneral_digest_user_settings` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezgeneral_digest_user_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezimagefile`
--

DROP TABLE IF EXISTS `ezimagefile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezimagefile` (
  `contentobject_attribute_id` int(11) NOT NULL default '0',
  `filepath` longtext NOT NULL,
  `id` int(11) NOT NULL auto_increment,
  PRIMARY KEY  (`id`),
  KEY `ezimagefile_coid` (`contentobject_attribute_id`),
  KEY `ezimagefile_file` (`filepath`(200))
) ENGINE=InnoDB AUTO_INCREMENT=170 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezimagefile`
--

LOCK TABLES `ezimagefile` WRITE;
/*!40000 ALTER TABLE `ezimagefile` DISABLE KEYS */;
INSERT INTO `ezimagefile` VALUES (172,'var/storage/images/setup/ez_publish/172-1-eng-GB/ez_publish.',1),(298,'var/intranet/storage/images/media/images/fylkeshusetfront/298-1-eng-GB/FylkeshusetFront.jpg',3),(298,'var/intranet/storage/images/media/images/fylkeshusetfront/298-1-eng-GB/FylkeshusetFront_reference.jpg',4),(298,'var/intranet/storage/images/media/images/fylkeshusetfront/298-1-eng-GB/FylkeshusetFront_small.jpg',5),(298,'var/intranet/storage/images/media/images/fylkeshusetfront/298-1-eng-GB/FylkeshusetFront_medium.jpg',6),(298,'var/intranet/storage/images/media/images/fylkeshusetfront/298-1-eng-GB/FylkeshusetFront_large.jpg',7),(301,'var/intranet/storage/images/media/images/fylkeshusetfront2/301-1-eng-GB/FylkeshusetFront.jpg',9),(301,'var/intranet/storage/images/media/images/fylkeshusetfront2/301-1-eng-GB/FylkeshusetFront_reference.jpg',10),(301,'var/intranet/storage/images/media/images/fylkeshusetfront2/301-1-eng-GB/FylkeshusetFront_small.jpg',11),(301,'var/intranet/storage/images/media/images/fylkeshusetfront2/301-1-eng-GB/FylkeshusetFront_medium.jpg',12),(301,'var/intranet/storage/images/media/images/fylkeshusetfront2/301-1-eng-GB/FylkeshusetFront_large.jpg',13),(322,'var/intranet/storage/images/media/images/128px-feed-icon_svg/322-1-eng-GB/128px-Feed-icon_svg.png',15),(322,'var/intranet/storage/images/media/images/128px-feed-icon_svg/322-1-eng-GB/128px-Feed-icon_svg_reference.png',16),(322,'var/intranet/storage/images/media/images/128px-feed-icon_svg/322-1-eng-GB/128px-Feed-icon_svg_small.png',17),(322,'var/intranet/storage/images/media/images/128px-feed-icon_svg/322-1-eng-GB/128px-Feed-icon_svg_medium.png',18),(322,'var/intranet/storage/images/media/images/128px-feed-icon_svg/322-1-eng-GB/128px-Feed-icon_svg_tiny.png',19),(322,'var/intranet/storage/images/media/images/128px-feed-icon_svg/322-1-eng-GB/128px-Feed-icon_svg_rss.png',20),(328,'var/intranet/storage/images/media/images/fylkeshusetfront3/328-1-eng-GB/FylkeshusetFront.jpg',22),(328,'var/intranet/storage/images/media/images/fylkeshusetfront3/328-1-eng-GB/FylkeshusetFront_reference.jpg',23),(328,'var/intranet/storage/images/media/images/fylkeshusetfront3/328-1-eng-GB/FylkeshusetFront_small.jpg',24),(328,'var/intranet/storage/images/media/images/fylkeshusetfront3/328-1-eng-GB/FylkeshusetFront_large.jpg',25),(331,'var/intranet/storage/images/media/images/fylkeshusetfront4/331-1-eng-GB/FylkeshusetFront.jpg',27),(331,'var/intranet/storage/images/media/images/fylkeshusetfront4/331-1-eng-GB/FylkeshusetFront_reference.jpg',28),(331,'var/intranet/storage/images/media/images/fylkeshusetfront4/331-1-eng-GB/FylkeshusetFront_small.jpg',29),(331,'var/intranet/storage/images/media/images/fylkeshusetfront4/331-1-eng-GB/FylkeshusetFront_large.jpg',30),(334,'var/intranet/storage/images/troms-fylkeskommune/fylkeshusetfront/334-1-eng-GB/FylkeshusetFront_reference.jpg',33),(334,'var/intranet/storage/images/troms-fylkeskommune/fylkeshusetfront/334-1-eng-GB/FylkeshusetFront_small.jpg',34),(334,'var/intranet/storage/images/troms-fylkeskommune/fylkeshusetfront/334-1-eng-GB/FylkeshusetFront_medium.jpg',35),(334,'var/intranet/storage/images/troms-fylkeskommune/fylkeshusetfront/334-1-eng-GB/FylkeshusetFront_large.jpg',36),(337,'var/intranet/storage/images/rss-feed/128px-feed-icon_svg/337-1-eng-GB/128px-Feed-icon_svg.png',38),(337,'var/intranet/storage/images/rss-feed/128px-feed-icon_svg/337-1-eng-GB/128px-Feed-icon_svg_reference.png',39),(337,'var/intranet/storage/images/rss-feed/128px-feed-icon_svg/337-1-eng-GB/128px-Feed-icon_svg_small.png',40),(337,'var/intranet/storage/images/rss-feed/128px-feed-icon_svg/337-1-eng-GB/128px-Feed-icon_svg_medium.png',41),(337,'var/intranet/storage/images/rss-feed/rss-feed/337-1-eng-GB/RSS-Feed.png',42),(337,'var/intranet/storage/images/rss-feed/rss-feed/337-1-eng-GB/RSS-Feed_reference.png',43),(337,'var/intranet/storage/images/rss-feed/rss-feed/337-1-eng-GB/RSS-Feed_small.png',44),(337,'var/intranet/storage/images/rss-feed/rss-feed/337-1-eng-GB/RSS-Feed_medium.png',45),(334,'var/intranet/storage/images/troms-fylkeskommune/fylkeshuset/334-1-eng-GB/Fylkeshuset.jpg',46),(334,'var/intranet/storage/images/troms-fylkeskommune/fylkeshuset/334-1-eng-GB/Fylkeshuset_reference.jpg',47),(334,'var/intranet/storage/images/troms-fylkeskommune/fylkeshuset/334-1-eng-GB/Fylkeshuset_medium.jpg',48),(334,'var/intranet/storage/images/troms-fylkeskommune/fylkeshuset/334-1-eng-GB/Fylkeshuset_small.jpg',49),(343,'var/intranet/storage/images/rss-feed/128px-feed-icon/343-1-eng-GB/128px-Feed-icon.png',57),(343,'var/intranet/storage/images/rss-feed/128px-feed-icon/343-1-eng-GB/128px-Feed-icon_reference.png',58),(343,'var/intranet/storage/images/rss-feed/128px-feed-icon/343-1-eng-GB/128px-Feed-icon_small.png',59),(343,'var/intranet/storage/images/rss-feed/128px-feed-icon/343-1-eng-GB/128px-Feed-icon_medium.png',60),(343,'var/intranet/storage/images/rss-feed/128px-feed-icon/343-1-eng-GB/128px-Feed-icon_tiny.png',61),(343,'var/intranet/storage/images/rss-feed/128px-feed-icon/343-1-eng-GB/128px-Feed-icon_rss.png',66),(349,'var/intranet/storage/images/media/images/flickr/349-1-eng-GB/flickr.png',74),(349,'var/intranet/storage/images/media/images/flickr/349-1-eng-GB/flickr_reference.png',75),(349,'var/intranet/storage/images/media/images/flickr/349-1-eng-GB/flickr_small.png',76),(349,'var/intranet/storage/images/media/images/flickr/349-1-eng-GB/flickr_medium.png',77),(349,'var/intranet/storage/images/media/images/foelg-oss-paa-flickr/349-1-eng-GB/Foelg-oss-paa-Flickr.png',83),(349,'var/intranet/storage/images/media/images/foelg-oss-paa-flickr/349-1-eng-GB/Foelg-oss-paa-Flickr_reference.png',84),(349,'var/intranet/storage/images/media/images/foelg-oss-paa-flickr/349-1-eng-GB/Foelg-oss-paa-Flickr_small.png',85),(349,'var/intranet/storage/images/media/images/foelg-oss-paa-flickr/349-1-eng-GB/Foelg-oss-paa-Flickr_medium.png',86),(349,'var/intranet/storage/images/media/images/foelg-oss-paa-flickr/349-1-eng-GB/Foelg-oss-paa-Flickr_large.png',87),(359,'var/intranet/storage/images/troms-fylkeskommune/foelg-oss-paa-sosiale-medier/flickr/359-1-eng-GB/flickr_reference.png',90),(359,'var/intranet/storage/images/troms-fylkeskommune/foelg-oss-paa-sosiale-medier/flickr/359-1-eng-GB/flickr_small.png',91),(359,'var/intranet/storage/images/troms-fylkeskommune/foelg-oss-paa-sosiale-medier/flickr/359-1-eng-GB/flickr_medium.png',92),(362,'var/intranet/storage/images/media/images/foelg-oss-paa-facebook/362-1-eng-GB/Foelg-oss-paa-Facebook.jpg',94),(362,'var/intranet/storage/images/media/images/foelg-oss-paa-facebook/362-1-eng-GB/Foelg-oss-paa-Facebook_reference.jpg',95),(362,'var/intranet/storage/images/media/images/foelg-oss-paa-facebook/362-1-eng-GB/Foelg-oss-paa-Facebook_small.jpg',96),(362,'var/intranet/storage/images/media/images/foelg-oss-paa-facebook/362-1-eng-GB/Foelg-oss-paa-Facebook_medium.jpg',97),(365,'var/intranet/storage/images/troms-fylkeskommune/foelg-oss-paa-sosiale-medier/facebook/365-1-eng-GB/facebook_reference.jpg',100),(365,'var/intranet/storage/images/troms-fylkeskommune/foelg-oss-paa-sosiale-medier/facebook/365-1-eng-GB/facebook_small.jpg',101),(365,'var/intranet/storage/images/troms-fylkeskommune/foelg-oss-paa-sosiale-medier/facebook/365-1-eng-GB/facebook_medium.jpg',102),(371,'var/intranet/storage/images/media/images/flickr/371-1-eng-GB/flickr.png',109),(371,'var/intranet/storage/images/media/images/flickr/371-1-eng-GB/flickr_reference.png',110),(371,'var/intranet/storage/images/media/images/flickr/371-1-eng-GB/flickr_small.png',111),(371,'var/intranet/storage/images/media/images/flickr/371-1-eng-GB/flickr_medium.png',112),(374,'var/intranet/storage/images/media/images/wikipedia/374-1-eng-GB/wikipedia.png',114),(374,'var/intranet/storage/images/media/images/wikipedia/374-1-eng-GB/wikipedia_reference.png',115),(374,'var/intranet/storage/images/media/images/wikipedia/374-1-eng-GB/wikipedia_small.png',116),(374,'var/intranet/storage/images/media/images/wikipedia/374-1-eng-GB/wikipedia_medium.png',117),(377,'var/intranet/storage/images/troms-fylkeskommune/foelg-oss-paa-sosiale-medier/wikipedia/377-1-eng-GB/wikipedia_reference.png',120),(377,'var/intranet/storage/images/troms-fylkeskommune/foelg-oss-paa-sosiale-medier/wikipedia/377-1-eng-GB/wikipedia_small.png',121),(377,'var/intranet/storage/images/troms-fylkeskommune/foelg-oss-paa-sosiale-medier/wikipedia/377-1-eng-GB/wikipedia_medium.png',122),(377,'var/intranet/storage/images/troms-fylkeskommune/foelg-oss-paa-sosiale-medier/wikipedia/377-1-eng-GB/trashed/bfe4cb994e83efd48838887530c5480d.png',162),(365,'var/intranet/storage/images/troms-fylkeskommune/foelg-oss-paa-sosiale-medier/facebook/365-1-eng-GB/trashed/0ada884b8cde994f49f6307fbfb0e598.jpg',163),(359,'var/intranet/storage/images/troms-fylkeskommune/foelg-oss-paa-sosiale-medier/flickr/359-1-eng-GB/trashed/ac9683863a5436332938f1763e8431b0.png',164),(334,'var/intranet/storage/images/troms-fylkeskommune/fylkeshuset/334-1-eng-GB/trashed/707b1a6f1e0deb7f3a573f12179a8c05.jpg',166),(334,'var/intranet/storage/images/troms-fylkeskommune/fylkeshusetfront/334-1-eng-GB/trashed/bf65a8d299007d4bf3d7f83841709c1f.jpg',167),(334,'var/intranet/storage/images/troms-fylkeskommune/fylkeshuset/334-1-eng-GB/trashed/821d7368aefa9382a463c4ee987674a4.jpg',168),(334,'var/intranet/storage/images/troms-fylkeskommune/fylkeshuset/334-1-eng-GB/trashed/trashed/7fc746873285caba37020cd68ddff84a.jpg',169);
/*!40000 ALTER TABLE `ezimagefile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezinfocollection`
--

DROP TABLE IF EXISTS `ezinfocollection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezinfocollection` (
  `contentobject_id` int(11) NOT NULL default '0',
  `created` int(11) NOT NULL default '0',
  `creator_id` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `modified` int(11) default '0',
  `user_identifier` varchar(34) default NULL,
  PRIMARY KEY  (`id`),
  KEY `ezinfocollection_co_id_created` (`contentobject_id`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezinfocollection`
--

LOCK TABLES `ezinfocollection` WRITE;
/*!40000 ALTER TABLE `ezinfocollection` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezinfocollection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezinfocollection_attribute`
--

DROP TABLE IF EXISTS `ezinfocollection_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezinfocollection_attribute` (
  `contentclass_attribute_id` int(11) NOT NULL default '0',
  `contentobject_attribute_id` int(11) default NULL,
  `contentobject_id` int(11) default NULL,
  `data_float` float default NULL,
  `data_int` int(11) default NULL,
  `data_text` longtext,
  `id` int(11) NOT NULL auto_increment,
  `informationcollection_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `ezinfocollection_attr_co_id` (`contentobject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezinfocollection_attribute`
--

LOCK TABLES `ezinfocollection_attribute` WRITE;
/*!40000 ALTER TABLE `ezinfocollection_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezinfocollection_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezisbn_group`
--

DROP TABLE IF EXISTS `ezisbn_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezisbn_group` (
  `description` varchar(255) NOT NULL default '',
  `group_number` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezisbn_group`
--

LOCK TABLES `ezisbn_group` WRITE;
/*!40000 ALTER TABLE `ezisbn_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezisbn_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezisbn_group_range`
--

DROP TABLE IF EXISTS `ezisbn_group_range`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezisbn_group_range` (
  `from_number` int(11) NOT NULL default '0',
  `group_from` varchar(32) NOT NULL default '',
  `group_length` int(11) NOT NULL default '0',
  `group_to` varchar(32) NOT NULL default '',
  `id` int(11) NOT NULL auto_increment,
  `to_number` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezisbn_group_range`
--

LOCK TABLES `ezisbn_group_range` WRITE;
/*!40000 ALTER TABLE `ezisbn_group_range` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezisbn_group_range` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezisbn_registrant_range`
--

DROP TABLE IF EXISTS `ezisbn_registrant_range`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezisbn_registrant_range` (
  `from_number` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `isbn_group_id` int(11) NOT NULL default '0',
  `registrant_from` varchar(32) NOT NULL default '',
  `registrant_length` int(11) NOT NULL default '0',
  `registrant_to` varchar(32) NOT NULL default '',
  `to_number` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezisbn_registrant_range`
--

LOCK TABLES `ezisbn_registrant_range` WRITE;
/*!40000 ALTER TABLE `ezisbn_registrant_range` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezisbn_registrant_range` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezkeyword`
--

DROP TABLE IF EXISTS `ezkeyword`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezkeyword` (
  `class_id` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `keyword` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `ezkeyword_keyword` (`keyword`),
  KEY `ezkeyword_keyword_id` (`keyword`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezkeyword`
--

LOCK TABLES `ezkeyword` WRITE;
/*!40000 ALTER TABLE `ezkeyword` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezkeyword` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezkeyword_attribute_link`
--

DROP TABLE IF EXISTS `ezkeyword_attribute_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezkeyword_attribute_link` (
  `id` int(11) NOT NULL auto_increment,
  `keyword_id` int(11) NOT NULL default '0',
  `objectattribute_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `ezkeyword_attr_link_keyword_id` (`keyword_id`),
  KEY `ezkeyword_attr_link_kid_oaid` (`keyword_id`,`objectattribute_id`),
  KEY `ezkeyword_attr_link_oaid` (`objectattribute_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezkeyword_attribute_link`
--

LOCK TABLES `ezkeyword_attribute_link` WRITE;
/*!40000 ALTER TABLE `ezkeyword_attribute_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezkeyword_attribute_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezmedia`
--

DROP TABLE IF EXISTS `ezmedia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezmedia` (
  `contentobject_attribute_id` int(11) NOT NULL default '0',
  `controls` varchar(50) default NULL,
  `filename` varchar(255) NOT NULL default '',
  `has_controller` int(11) default '0',
  `height` int(11) default NULL,
  `is_autoplay` int(11) default '0',
  `is_loop` int(11) default '0',
  `mime_type` varchar(50) NOT NULL default '',
  `original_filename` varchar(255) NOT NULL default '',
  `pluginspage` varchar(255) default NULL,
  `quality` varchar(50) default NULL,
  `version` int(11) NOT NULL default '0',
  `width` int(11) default NULL,
  PRIMARY KEY  (`contentobject_attribute_id`,`version`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezmedia`
--

LOCK TABLES `ezmedia` WRITE;
/*!40000 ALTER TABLE `ezmedia` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezmedia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezmessage`
--

DROP TABLE IF EXISTS `ezmessage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezmessage` (
  `body` longtext,
  `destination_address` varchar(50) NOT NULL default '',
  `id` int(11) NOT NULL auto_increment,
  `is_sent` int(11) NOT NULL default '0',
  `send_method` varchar(50) NOT NULL default '',
  `send_time` varchar(50) NOT NULL default '',
  `send_weekday` varchar(50) NOT NULL default '',
  `title` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezmessage`
--

LOCK TABLES `ezmessage` WRITE;
/*!40000 ALTER TABLE `ezmessage` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezmessage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezmodule_run`
--

DROP TABLE IF EXISTS `ezmodule_run`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezmodule_run` (
  `function_name` varchar(255) default NULL,
  `id` int(11) NOT NULL auto_increment,
  `module_data` longtext,
  `module_name` varchar(255) default NULL,
  `workflow_process_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `ezmodule_run_workflow_process_id_s` (`workflow_process_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezmodule_run`
--

LOCK TABLES `ezmodule_run` WRITE;
/*!40000 ALTER TABLE `ezmodule_run` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezmodule_run` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezmultipricedata`
--

DROP TABLE IF EXISTS `ezmultipricedata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezmultipricedata` (
  `contentobject_attr_id` int(11) NOT NULL default '0',
  `contentobject_attr_version` int(11) NOT NULL default '0',
  `currency_code` varchar(4) NOT NULL default '',
  `id` int(11) NOT NULL auto_increment,
  `type` int(11) NOT NULL default '0',
  `value` decimal(15,2) NOT NULL default '0.00',
  PRIMARY KEY  (`id`),
  KEY `ezmultipricedata_coa_id` (`contentobject_attr_id`),
  KEY `ezmultipricedata_coa_version` (`contentobject_attr_version`),
  KEY `ezmultipricedata_currency_code` (`currency_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezmultipricedata`
--

LOCK TABLES `ezmultipricedata` WRITE;
/*!40000 ALTER TABLE `ezmultipricedata` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezmultipricedata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eznode_assignment`
--

DROP TABLE IF EXISTS `eznode_assignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eznode_assignment` (
  `contentobject_id` int(11) default NULL,
  `contentobject_version` int(11) default NULL,
  `from_node_id` int(11) default '0',
  `id` int(11) NOT NULL auto_increment,
  `is_main` int(11) NOT NULL default '0',
  `op_code` int(11) NOT NULL default '0',
  `parent_node` int(11) default NULL,
  `parent_remote_id` varchar(100) NOT NULL default '',
  `remote_id` int(11) NOT NULL default '0',
  `sort_field` int(11) default '1',
  `sort_order` int(11) default '1',
  PRIMARY KEY  (`id`),
  KEY `eznode_assignment_co_id` (`contentobject_id`),
  KEY `eznode_assignment_co_version` (`contentobject_version`),
  KEY `eznode_assignment_coid_cov` (`contentobject_id`,`contentobject_version`),
  KEY `eznode_assignment_is_main` (`is_main`),
  KEY `eznode_assignment_parent_node` (`parent_node`)
) ENGINE=InnoDB AUTO_INCREMENT=259 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eznode_assignment`
--

LOCK TABLES `eznode_assignment` WRITE;
/*!40000 ALTER TABLE `eznode_assignment` DISABLE KEYS */;
INSERT INTO `eznode_assignment` VALUES (8,2,0,4,1,2,5,'',0,1,1),(42,1,0,5,1,2,5,'',0,9,1),(10,2,-1,6,1,2,44,'',0,9,1),(4,1,0,7,1,2,1,'',0,1,1),(12,1,0,8,1,2,5,'',0,1,1),(13,1,0,9,1,2,5,'',0,1,1),(14,1,0,10,1,2,13,'',0,1,1),(41,1,0,11,1,2,1,'',0,1,1),(11,1,0,12,1,2,5,'',0,1,1),(45,1,-1,16,1,2,1,'',0,9,1),(49,1,0,27,1,2,43,'',0,9,1),(50,1,0,28,1,2,43,'',0,9,1),(51,1,0,29,1,2,43,'',0,9,1),(52,1,0,30,1,2,48,'',0,1,1),(54,1,0,31,1,2,58,'',0,1,1),(56,1,0,34,1,2,1,'',0,2,0),(4,2,-1,39,1,2,1,'',0,1,1),(4,3,-1,41,1,2,1,'',0,1,1),(4,4,-1,46,1,2,1,'',0,1,1),(12,2,-1,57,1,2,5,'',0,1,1),(13,2,-1,62,1,2,5,'',0,1,1),(72,1,0,63,1,2,5,'',0,1,1),(73,1,0,64,1,2,5,'',0,1,1),(84,1,0,89,1,2,51,'',0,2,0),(85,1,0,93,1,2,51,'',0,2,0),(87,1,0,96,1,2,2,'',0,1,1),(87,2,-1,100,1,2,2,'',0,1,1),(87,3,-1,102,1,2,2,'',0,1,1),(87,4,-1,103,1,2,2,'',0,1,1),(90,1,0,112,1,2,51,'',0,2,0),(92,1,0,120,1,2,51,'',0,2,0),(82,15,-1,124,1,2,2,'',0,1,1),(93,1,0,125,1,2,51,'',0,2,0),(82,16,-1,126,1,2,2,'',0,1,1),(94,1,0,127,1,2,75,'',0,2,0),(95,1,0,129,1,2,71,'',0,2,0),(95,2,-1,131,1,2,71,'',0,2,0),(94,2,-1,133,1,2,75,'',0,2,0),(97,1,0,137,1,2,71,'',0,2,0),(77,19,-1,141,1,2,2,'',0,1,1),(77,20,-1,142,1,2,2,'',0,1,1),(77,21,-1,144,1,2,2,'',0,1,1),(99,1,0,145,1,2,51,'',0,2,0),(77,22,-1,146,1,2,2,'',0,1,1),(77,23,-1,149,1,2,2,'',0,1,1),(99,2,-1,155,1,2,51,'',0,2,0),(101,1,0,158,1,2,91,'',0,2,0),(100,8,-1,161,1,2,75,'',0,1,1),(100,9,-1,162,1,2,75,'',0,1,1),(100,10,-1,163,1,2,75,'',0,1,1),(100,11,-1,164,1,2,75,'',0,1,1),(102,1,0,165,1,2,51,'',0,2,0),(100,12,-1,166,1,2,75,'',0,1,1),(100,13,-1,168,1,2,75,'',0,1,1),(103,1,0,169,1,2,91,'',0,2,0),(100,14,-1,170,1,2,75,'',0,1,1),(100,15,-1,172,1,2,75,'',0,1,1),(105,1,0,173,1,2,51,'',0,2,0),(106,1,0,174,1,2,51,'',0,2,0),(100,16,-1,175,1,2,75,'',0,1,1),(107,1,0,176,1,2,91,'',0,2,0),(77,24,-1,177,1,2,2,'',0,1,1),(1,6,-1,180,1,2,1,'',0,8,1),(1,7,-1,181,1,2,1,'',0,8,1),(87,5,-1,182,1,2,2,'',0,1,1),(100,17,-1,196,1,2,75,'',0,1,1),(82,17,-1,199,1,2,2,'',0,1,1),(1,8,-1,200,1,2,1,'',0,8,1),(118,1,0,201,1,2,67,'',0,1,1),(119,1,0,202,1,2,67,'',0,1,1),(1,9,-1,203,1,2,1,'',0,8,1),(82,18,-1,204,1,2,2,'',0,1,1),(82,19,-1,206,1,2,2,'',0,1,1),(77,25,-1,208,1,2,2,'',0,1,1),(77,26,-1,209,1,2,2,'',0,1,1),(77,27,-1,211,1,2,2,'',0,1,1),(77,28,-1,212,1,2,2,'',0,1,1),(120,1,0,213,1,2,68,'',0,1,1),(122,1,0,215,1,2,14,'',0,1,1),(122,2,-1,216,1,2,14,'',0,1,1),(118,2,-1,219,1,2,67,'',0,1,1),(134,1,0,230,1,2,67,'LDAP_67',0,2,0),(140,1,0,257,1,2,67,'',0,1,1),(141,1,0,258,1,2,68,'',0,1,1);
/*!40000 ALTER TABLE `eznode_assignment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eznotificationcollection`
--

DROP TABLE IF EXISTS `eznotificationcollection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eznotificationcollection` (
  `data_subject` longtext NOT NULL,
  `data_text` longtext NOT NULL,
  `event_id` int(11) NOT NULL default '0',
  `handler` varchar(255) NOT NULL default '',
  `id` int(11) NOT NULL auto_increment,
  `transport` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eznotificationcollection`
--

LOCK TABLES `eznotificationcollection` WRITE;
/*!40000 ALTER TABLE `eznotificationcollection` DISABLE KEYS */;
/*!40000 ALTER TABLE `eznotificationcollection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eznotificationcollection_item`
--

DROP TABLE IF EXISTS `eznotificationcollection_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eznotificationcollection_item` (
  `address` varchar(255) NOT NULL default '',
  `collection_id` int(11) NOT NULL default '0',
  `event_id` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `send_date` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eznotificationcollection_item`
--

LOCK TABLES `eznotificationcollection_item` WRITE;
/*!40000 ALTER TABLE `eznotificationcollection_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `eznotificationcollection_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eznotificationevent`
--

DROP TABLE IF EXISTS `eznotificationevent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eznotificationevent` (
  `data_int1` int(11) NOT NULL default '0',
  `data_int2` int(11) NOT NULL default '0',
  `data_int3` int(11) NOT NULL default '0',
  `data_int4` int(11) NOT NULL default '0',
  `data_text1` longtext NOT NULL,
  `data_text2` longtext NOT NULL,
  `data_text3` longtext NOT NULL,
  `data_text4` longtext NOT NULL,
  `event_type_string` varchar(255) NOT NULL default '',
  `id` int(11) NOT NULL auto_increment,
  `status` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=177 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eznotificationevent`
--

LOCK TABLES `eznotificationevent` WRITE;
/*!40000 ALTER TABLE `eznotificationevent` DISABLE KEYS */;
INSERT INTO `eznotificationevent` VALUES (4,2,0,0,'','','','','ezpublish',1,0),(4,3,0,0,'','','','','ezpublish',2,0),(57,1,0,0,'','','','','ezpublish',3,0),(58,1,0,0,'','','','','ezpublish',4,0),(59,1,0,0,'','','','','ezpublish',5,0),(1,4,0,0,'','','','','ezpublish',6,0),(64,1,0,0,'','','','','ezpublish',7,0),(61,1,0,0,'','','','','ezpublish',8,0),(65,1,0,0,'','','','','ezpublish',9,0),(70,1,0,0,'','','','','ezpublish',10,0),(71,1,0,0,'','','','','ezpublish',11,0),(13,2,0,0,'','','','','ezpublish',12,0),(72,1,0,0,'','','','','ezpublish',13,0),(73,1,0,0,'','','','','ezpublish',14,0),(74,1,0,0,'','','','','ezpublish',15,0),(76,1,0,0,'','','','','ezpublish',16,0),(76,2,0,0,'','','','','ezpublish',17,0),(77,1,0,0,'','','','','ezpublish',18,0),(78,1,0,0,'','','','','ezpublish',19,0),(78,2,0,0,'','','','','ezpublish',20,0),(64,2,0,0,'','','','','ezpublish',21,0),(80,1,0,0,'','','','','ezpublish',22,0),(81,1,0,0,'','','','','ezpublish',23,0),(81,2,0,0,'','','','','ezpublish',24,0),(81,3,0,0,'','','','','ezpublish',25,0),(80,2,0,0,'','','','','ezpublish',26,0),(82,1,0,0,'','','','','ezpublish',27,0),(1,5,0,0,'','','','','ezpublish',28,0),(82,2,0,0,'','','','','ezpublish',29,0),(83,1,0,0,'','','','','ezpublish',30,0),(82,3,0,0,'','','','','ezpublish',31,0),(82,4,0,0,'','','','','ezpublish',32,0),(84,1,0,0,'','','','','ezpublish',33,0),(82,5,0,0,'','','','','ezpublish',34,0),(82,6,0,0,'','','','','ezpublish',35,0),(82,7,0,0,'','','','','ezpublish',36,0),(85,1,0,0,'','','','','ezpublish',37,0),(82,8,0,0,'','','','','ezpublish',38,0),(82,9,0,0,'','','','','ezpublish',39,0),(86,1,0,0,'','','','','ezpublish',40,0),(87,1,0,0,'','','','','ezpublish',41,0),(81,4,0,0,'','','','','ezpublish',42,0),(81,5,0,0,'','','','','ezpublish',43,0),(88,1,0,0,'','','','','ezpublish',44,0),(87,2,0,0,'','','','','ezpublish',45,0),(87,3,0,0,'','','','','ezpublish',46,0),(87,4,0,0,'','','','','ezpublish',47,0),(82,10,0,0,'','','','','ezpublish',48,0),(82,11,0,0,'','','','','ezpublish',49,0),(82,12,0,0,'','','','','ezpublish',50,0),(77,2,0,0,'','','','','ezpublish',51,0),(77,3,0,0,'','','','','ezpublish',52,0),(77,4,0,0,'','','','','ezpublish',53,0),(90,1,0,0,'','','','','ezpublish',54,0),(77,5,0,0,'','','','','ezpublish',55,0),(77,6,0,0,'','','','','ezpublish',56,0),(77,7,0,0,'','','','','ezpublish',57,0),(77,8,0,0,'','','','','ezpublish',58,0),(92,1,0,0,'','','','','ezpublish',59,0),(82,13,0,0,'','','','','ezpublish',60,0),(82,14,0,0,'','','','','ezpublish',61,0),(77,9,0,0,'','','','','ezpublish',62,0),(77,10,0,0,'','','','','ezpublish',63,0),(93,1,0,0,'','','','','ezpublish',64,0),(82,15,0,0,'','','','','ezpublish',65,0),(94,1,0,0,'','','','','ezpublish',66,0),(82,16,0,0,'','','','','ezpublish',67,0),(95,1,0,0,'','','','','ezpublish',68,0),(77,11,0,0,'','','','','ezpublish',69,0),(77,12,0,0,'','','','','ezpublish',70,0),(95,2,0,0,'','','','','ezpublish',71,0),(77,13,0,0,'','','','','ezpublish',72,0),(94,2,0,0,'','','','','ezpublish',73,0),(96,1,0,0,'','','','','ezpublish',74,0),(77,14,0,0,'','','','','ezpublish',75,0),(97,1,0,0,'','','','','ezpublish',76,0),(77,15,0,0,'','','','','ezpublish',77,0),(77,16,0,0,'','','','','ezpublish',78,0),(77,17,0,0,'','','','','ezpublish',79,0),(77,18,0,0,'','','','','ezpublish',80,0),(77,19,0,0,'','','','','ezpublish',81,0),(98,1,0,0,'','','','','ezpublish',82,0),(77,20,0,0,'','','','','ezpublish',83,0),(99,1,0,0,'','','','','ezpublish',84,0),(77,21,0,0,'','','','','ezpublish',85,0),(77,22,0,0,'','','','','ezpublish',86,0),(100,1,0,0,'','','','','ezpublish',87,0),(77,23,0,0,'','','','','ezpublish',88,0),(100,2,0,0,'','','','','ezpublish',89,0),(100,3,0,0,'','','','','ezpublish',90,0),(100,4,0,0,'','','','','ezpublish',91,0),(98,2,0,0,'','','','','ezpublish',92,0),(99,2,0,0,'','','','','ezpublish',93,0),(101,1,0,0,'','','','','ezpublish',94,0),(100,5,0,0,'','','','','ezpublish',95,0),(100,6,0,0,'','','','','ezpublish',96,0),(100,7,0,0,'','','','','ezpublish',97,0),(100,8,0,0,'','','','','ezpublish',98,0),(100,9,0,0,'','','','','ezpublish',99,0),(100,10,0,0,'','','','','ezpublish',100,0),(102,1,0,0,'','','','','ezpublish',101,0),(100,11,0,0,'','','','','ezpublish',102,0),(100,12,0,0,'','','','','ezpublish',103,0),(103,1,0,0,'','','','','ezpublish',104,0),(100,13,0,0,'','','','','ezpublish',105,0),(104,1,0,0,'','','','','ezpublish',106,0),(100,14,0,0,'','','','','ezpublish',107,0),(105,1,0,0,'','','','','ezpublish',108,0),(106,1,0,0,'','','','','ezpublish',109,0),(100,15,0,0,'','','','','ezpublish',110,0),(107,1,0,0,'','','','','ezpublish',111,0),(100,16,0,0,'','','','','ezpublish',112,0),(77,24,0,0,'','','','','ezpublish',113,0),(108,1,0,0,'','','','','ezpublish',114,0),(108,2,0,0,'','','','','ezpublish',115,0),(1,6,0,0,'','','','','ezpublish',116,0),(1,7,0,0,'','','','','ezpublish',117,0),(87,5,0,0,'','','','','ezpublish',118,0),(78,3,0,0,'','','','','ezpublish',119,0),(110,1,0,0,'','','','','ezpublish',120,0),(113,1,0,0,'','','','','ezpublish',121,0),(114,1,0,0,'','','','','ezpublish',122,0),(115,1,0,0,'','','','','ezpublish',123,0),(100,17,0,0,'','','','','ezpublish',124,0),(117,1,0,0,'','','','','ezpublish',125,0),(82,17,0,0,'','','','','ezpublish',126,0),(1,8,0,0,'','','','','ezpublish',127,0),(118,1,0,0,'','','','','ezpublish',128,0),(119,1,0,0,'','','','','ezpublish',129,0),(1,9,0,0,'','','','','ezpublish',130,0),(82,18,0,0,'','','','','ezpublish',131,0),(82,19,0,0,'','','','','ezpublish',132,0),(77,25,0,0,'','','','','ezpublish',133,0),(77,26,0,0,'','','','','ezpublish',134,0),(77,27,0,0,'','','','','ezpublish',135,0),(77,28,0,0,'','','','','ezpublish',136,0),(120,1,0,0,'','','','','ezpublish',137,0),(121,1,0,0,'','','','','ezpublish',138,0),(122,1,0,0,'','','','','ezpublish',139,0),(122,2,0,0,'','','','','ezpublish',140,0),(118,2,0,0,'','','','','ezpublish',141,0),(125,1,0,0,'','','','','ezpublish',142,0),(126,1,0,0,'','','','','ezpublish',143,0),(127,1,0,0,'','','','','ezpublish',144,0),(128,1,0,0,'','','','','ezpublish',145,0),(129,1,0,0,'','','','','ezpublish',146,0),(130,1,0,0,'','','','','ezpublish',147,0),(131,1,0,0,'','','','','ezpublish',148,0),(132,1,0,0,'','','','','ezpublish',149,0),(133,1,0,0,'','','','','ezpublish',150,0),(134,1,0,0,'','','','','ezpublish',151,0),(135,1,0,0,'','','','','ezpublish',152,0),(135,2,0,0,'','','','','ezpublish',153,0),(135,3,0,0,'','','','','ezpublish',154,0),(135,4,0,0,'','','','','ezpublish',155,0),(135,5,0,0,'','','','','ezpublish',156,0),(135,6,0,0,'','','','','ezpublish',157,0),(135,7,0,0,'','','','','ezpublish',158,0),(135,8,0,0,'','','','','ezpublish',159,0),(135,9,0,0,'','','','','ezpublish',160,0),(135,10,0,0,'','','','','ezpublish',161,0),(135,11,0,0,'','','','','ezpublish',162,0),(135,12,0,0,'','','','','ezpublish',163,0),(135,13,0,0,'','','','','ezpublish',164,0),(135,14,0,0,'','','','','ezpublish',165,0),(135,15,0,0,'','','','','ezpublish',166,0),(136,1,0,0,'','','','','ezpublish',167,0),(137,1,0,0,'','','','','ezpublish',168,0),(138,1,0,0,'','','','','ezpublish',169,0),(137,2,0,0,'','','','','ezpublish',170,0),(136,2,0,0,'','','','','ezpublish',171,0),(136,3,0,0,'','','','','ezpublish',172,0),(136,4,0,0,'','','','','ezpublish',173,0),(139,1,0,0,'','','','','ezpublish',174,0),(140,1,0,0,'','','','','ezpublish',175,0),(141,1,0,0,'','','','','ezpublish',176,0);
/*!40000 ALTER TABLE `eznotificationevent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezoperation_memento`
--

DROP TABLE IF EXISTS `ezoperation_memento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezoperation_memento` (
  `id` int(11) NOT NULL auto_increment,
  `main` int(11) NOT NULL default '0',
  `main_key` varchar(32) NOT NULL default '',
  `memento_data` longtext NOT NULL,
  `memento_key` varchar(32) NOT NULL default '',
  PRIMARY KEY  (`id`,`memento_key`),
  KEY `ezoperation_memento_memento_key_main` (`memento_key`,`main`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezoperation_memento`
--

LOCK TABLES `ezoperation_memento` WRITE;
/*!40000 ALTER TABLE `ezoperation_memento` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezoperation_memento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezorder`
--

DROP TABLE IF EXISTS `ezorder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezorder` (
  `account_identifier` varchar(100) NOT NULL default 'default',
  `created` int(11) NOT NULL default '0',
  `data_text_1` longtext,
  `data_text_2` longtext,
  `email` varchar(150) default '',
  `id` int(11) NOT NULL auto_increment,
  `ignore_vat` int(11) NOT NULL default '0',
  `is_archived` int(11) NOT NULL default '0',
  `is_temporary` int(11) NOT NULL default '1',
  `order_nr` int(11) NOT NULL default '0',
  `productcollection_id` int(11) NOT NULL default '0',
  `status_id` int(11) default '0',
  `status_modified` int(11) default '0',
  `status_modifier_id` int(11) default '0',
  `user_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `ezorder_is_archived` (`is_archived`),
  KEY `ezorder_is_tmp` (`is_temporary`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezorder`
--

LOCK TABLES `ezorder` WRITE;
/*!40000 ALTER TABLE `ezorder` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezorder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezorder_item`
--

DROP TABLE IF EXISTS `ezorder_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezorder_item` (
  `description` varchar(255) default NULL,
  `id` int(11) NOT NULL auto_increment,
  `is_vat_inc` int(11) NOT NULL default '0',
  `order_id` int(11) NOT NULL default '0',
  `price` float default NULL,
  `type` varchar(30) default NULL,
  `vat_value` float NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `ezorder_item_order_id` (`order_id`),
  KEY `ezorder_item_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezorder_item`
--

LOCK TABLES `ezorder_item` WRITE;
/*!40000 ALTER TABLE `ezorder_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezorder_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezorder_status`
--

DROP TABLE IF EXISTS `ezorder_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezorder_status` (
  `id` int(11) NOT NULL auto_increment,
  `is_active` int(11) NOT NULL default '1',
  `name` varchar(255) NOT NULL default '',
  `status_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `ezorder_status_active` (`is_active`),
  KEY `ezorder_status_name` (`name`),
  KEY `ezorder_status_sid` (`status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezorder_status`
--

LOCK TABLES `ezorder_status` WRITE;
/*!40000 ALTER TABLE `ezorder_status` DISABLE KEYS */;
INSERT INTO `ezorder_status` VALUES (1,1,'Pending',1),(2,1,'Processing',2),(3,1,'Delivered',3);
/*!40000 ALTER TABLE `ezorder_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezorder_status_history`
--

DROP TABLE IF EXISTS `ezorder_status_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezorder_status_history` (
  `id` int(11) NOT NULL auto_increment,
  `modified` int(11) NOT NULL default '0',
  `modifier_id` int(11) NOT NULL default '0',
  `order_id` int(11) NOT NULL default '0',
  `status_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `ezorder_status_history_mod` (`modified`),
  KEY `ezorder_status_history_oid` (`order_id`),
  KEY `ezorder_status_history_sid` (`status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezorder_status_history`
--

LOCK TABLES `ezorder_status_history` WRITE;
/*!40000 ALTER TABLE `ezorder_status_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezorder_status_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezpackage`
--

DROP TABLE IF EXISTS `ezpackage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezpackage` (
  `id` int(11) NOT NULL auto_increment,
  `install_date` int(11) NOT NULL default '0',
  `name` varchar(100) NOT NULL default '',
  `version` varchar(30) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezpackage`
--

LOCK TABLES `ezpackage` WRITE;
/*!40000 ALTER TABLE `ezpackage` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezpackage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezpaymentobject`
--

DROP TABLE IF EXISTS `ezpaymentobject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezpaymentobject` (
  `id` int(11) NOT NULL auto_increment,
  `order_id` int(11) NOT NULL default '0',
  `payment_string` varchar(255) NOT NULL default '',
  `status` int(11) NOT NULL default '0',
  `workflowprocess_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezpaymentobject`
--

LOCK TABLES `ezpaymentobject` WRITE;
/*!40000 ALTER TABLE `ezpaymentobject` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezpaymentobject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezpdf_export`
--

DROP TABLE IF EXISTS `ezpdf_export`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezpdf_export` (
  `created` int(11) default NULL,
  `creator_id` int(11) default NULL,
  `export_classes` varchar(255) default NULL,
  `export_structure` varchar(255) default NULL,
  `id` int(11) NOT NULL auto_increment,
  `intro_text` longtext,
  `modified` int(11) default NULL,
  `modifier_id` int(11) default NULL,
  `pdf_filename` varchar(255) default NULL,
  `show_frontpage` int(11) default NULL,
  `site_access` varchar(255) default NULL,
  `source_node_id` int(11) default NULL,
  `status` int(11) default NULL,
  `sub_text` longtext,
  `title` varchar(255) default NULL,
  `version` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`,`version`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezpdf_export`
--

LOCK TABLES `ezpdf_export` WRITE;
/*!40000 ALTER TABLE `ezpdf_export` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezpdf_export` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezpending_actions`
--

DROP TABLE IF EXISTS `ezpending_actions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezpending_actions` (
  `action` varchar(64) NOT NULL default '',
  `created` int(11) default NULL,
  `param` longtext,
  KEY `ezpending_actions_action` (`action`),
  KEY `ezpending_actions_created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezpending_actions`
--

LOCK TABLES `ezpending_actions` WRITE;
/*!40000 ALTER TABLE `ezpending_actions` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezpending_actions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezpolicy`
--

DROP TABLE IF EXISTS `ezpolicy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezpolicy` (
  `function_name` varchar(255) default NULL,
  `id` int(11) NOT NULL auto_increment,
  `module_name` varchar(255) default NULL,
  `role_id` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=367 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezpolicy`
--

LOCK TABLES `ezpolicy` WRITE;
/*!40000 ALTER TABLE `ezpolicy` DISABLE KEYS */;
INSERT INTO `ezpolicy` VALUES ('*',308,'*',2),('*',317,'content',3),('login',319,'user',3),('login',336,'user',5),('read',337,'content',5),('pdf',338,'content',5),('*',347,'content',9),('login',348,'user',9),('*',349,'content',11),('login',350,'user',11),('login',359,'user',7),('read',360,'content',7),('pdf',361,'content',7),('password',363,'user',7),('login',364,'user',1),('read',365,'content',1),('pdf',366,'content',1);
/*!40000 ALTER TABLE `ezpolicy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezpolicy_limitation`
--

DROP TABLE IF EXISTS `ezpolicy_limitation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezpolicy_limitation` (
  `id` int(11) NOT NULL auto_increment,
  `identifier` varchar(255) NOT NULL default '',
  `policy_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `policy_id` (`policy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=273 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezpolicy_limitation`
--

LOCK TABLES `ezpolicy_limitation` WRITE;
/*!40000 ALTER TABLE `ezpolicy_limitation` DISABLE KEYS */;
INSERT INTO `ezpolicy_limitation` VALUES (258,'Section',337),(259,'Section',338),(268,'Section',360),(269,'Section',361),(271,'Section',366),(272,'Section',365);
/*!40000 ALTER TABLE `ezpolicy_limitation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezpolicy_limitation_value`
--

DROP TABLE IF EXISTS `ezpolicy_limitation_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezpolicy_limitation_value` (
  `id` int(11) NOT NULL auto_increment,
  `limitation_id` int(11) default NULL,
  `value` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `ezpolicy_limitation_value_val` (`value`)
) ENGINE=InnoDB AUTO_INCREMENT=500 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezpolicy_limitation_value`
--

LOCK TABLES `ezpolicy_limitation_value` WRITE;
/*!40000 ALTER TABLE `ezpolicy_limitation_value` DISABLE KEYS */;
INSERT INTO `ezpolicy_limitation_value` VALUES (484,258,'1'),(485,259,'1'),(494,268,'1'),(495,269,'1'),(497,271,'1'),(498,272,'3'),(499,272,'1');
/*!40000 ALTER TABLE `ezpolicy_limitation_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezpreferences`
--

DROP TABLE IF EXISTS `ezpreferences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezpreferences` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(100) default NULL,
  `user_id` int(11) NOT NULL default '0',
  `value` varchar(100) default NULL,
  PRIMARY KEY  (`id`),
  KEY `ezpreferences_name` (`name`),
  KEY `ezpreferences_user_id_idx` (`user_id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezpreferences`
--

LOCK TABLES `ezpreferences` WRITE;
/*!40000 ALTER TABLE `ezpreferences` DISABLE KEYS */;
INSERT INTO `ezpreferences` VALUES (1,'admin_navigation_states',14,'1'),(2,'admin_navigation_content',14,'1'),(3,'admin_navigation_details',14,'0'),(4,'admin_navigation_locations',14,'0'),(5,'admin_navigation_relations',14,'0'),(6,'admin_left_menu_width',14,'medium'),(7,'admin_navigation_roles',14,'1'),(8,'admin_navigation_policies',14,'0'),(9,'admin_treemenu',14,'1'),(10,'admin_navigation_content',64,'1'),(11,'admin_navigation_details',64,'1'),(12,'admin_children_viewmode',64,'detailed'),(13,'admin_list_limit',64,'2'),(14,'admin_clearcache_menu',14,'1'),(15,'admin_clearcache_type',14,'All'),(16,'admin_list_limit',14,'3'),(17,'admin_quicksettings_menu',14,'1'),(18,'admin_quicksettings_siteaccess',14,'fkp_nob'),(19,'admin_navigation_class_temlates',14,'1'),(20,'admin_right_menu_show',14,'1');
/*!40000 ALTER TABLE `ezpreferences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezproductcategory`
--

DROP TABLE IF EXISTS `ezproductcategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezproductcategory` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezproductcategory`
--

LOCK TABLES `ezproductcategory` WRITE;
/*!40000 ALTER TABLE `ezproductcategory` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezproductcategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezproductcollection`
--

DROP TABLE IF EXISTS `ezproductcollection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezproductcollection` (
  `created` int(11) default NULL,
  `currency_code` varchar(4) NOT NULL default '',
  `id` int(11) NOT NULL auto_increment,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezproductcollection`
--

LOCK TABLES `ezproductcollection` WRITE;
/*!40000 ALTER TABLE `ezproductcollection` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezproductcollection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezproductcollection_item`
--

DROP TABLE IF EXISTS `ezproductcollection_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezproductcollection_item` (
  `contentobject_id` int(11) NOT NULL default '0',
  `discount` float default NULL,
  `id` int(11) NOT NULL auto_increment,
  `is_vat_inc` int(11) default NULL,
  `item_count` int(11) NOT NULL default '0',
  `name` varchar(255) NOT NULL default '',
  `price` float default '0',
  `productcollection_id` int(11) NOT NULL default '0',
  `vat_value` float default NULL,
  PRIMARY KEY  (`id`),
  KEY `ezproductcollection_item_contentobject_id` (`contentobject_id`),
  KEY `ezproductcollection_item_productcollection_id` (`productcollection_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezproductcollection_item`
--

LOCK TABLES `ezproductcollection_item` WRITE;
/*!40000 ALTER TABLE `ezproductcollection_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezproductcollection_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezproductcollection_item_opt`
--

DROP TABLE IF EXISTS `ezproductcollection_item_opt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezproductcollection_item_opt` (
  `id` int(11) NOT NULL auto_increment,
  `item_id` int(11) NOT NULL default '0',
  `name` varchar(255) NOT NULL default '',
  `object_attribute_id` int(11) default NULL,
  `option_item_id` int(11) NOT NULL default '0',
  `price` float NOT NULL default '0',
  `value` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`id`),
  KEY `ezproductcollection_item_opt_item_id` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezproductcollection_item_opt`
--

LOCK TABLES `ezproductcollection_item_opt` WRITE;
/*!40000 ALTER TABLE `ezproductcollection_item_opt` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezproductcollection_item_opt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezrole`
--

DROP TABLE IF EXISTS `ezrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezrole` (
  `id` int(11) NOT NULL auto_increment,
  `is_new` int(11) NOT NULL default '0',
  `name` varchar(255) NOT NULL default '',
  `value` char(1) default NULL,
  `version` int(11) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezrole`
--

LOCK TABLES `ezrole` WRITE;
/*!40000 ALTER TABLE `ezrole` DISABLE KEYS */;
INSERT INTO `ezrole` VALUES (1,0,'Anonymous','',0),(2,0,'Administrator','*',0),(3,0,'Editor','',0),(5,0,'Ansatt',NULL,0),(7,0,'Kursansvarlig',NULL,0),(9,0,'Opplaringsansvarlig',NULL,0),(11,1,'Editor',NULL,3);
/*!40000 ALTER TABLE `ezrole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezrss_export`
--

DROP TABLE IF EXISTS `ezrss_export`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezrss_export` (
  `access_url` varchar(255) default NULL,
  `active` int(11) default NULL,
  `created` int(11) default NULL,
  `creator_id` int(11) default NULL,
  `description` longtext,
  `id` int(11) NOT NULL auto_increment,
  `image_id` int(11) default NULL,
  `main_node_only` int(11) NOT NULL default '1',
  `modified` int(11) default NULL,
  `modifier_id` int(11) default NULL,
  `node_id` int(11) default NULL,
  `number_of_objects` int(11) NOT NULL default '0',
  `rss_version` varchar(255) default NULL,
  `site_access` varchar(255) default NULL,
  `status` int(11) NOT NULL default '0',
  `title` varchar(255) default NULL,
  `url` varchar(255) default NULL,
  PRIMARY KEY  (`id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezrss_export`
--

LOCK TABLES `ezrss_export` WRITE;
/*!40000 ALTER TABLE `ezrss_export` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezrss_export` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezrss_export_item`
--

DROP TABLE IF EXISTS `ezrss_export_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezrss_export_item` (
  `category` varchar(255) default NULL,
  `class_id` int(11) default NULL,
  `description` varchar(255) default NULL,
  `enclosure` varchar(255) default NULL,
  `id` int(11) NOT NULL auto_increment,
  `rssexport_id` int(11) default NULL,
  `source_node_id` int(11) default NULL,
  `status` int(11) NOT NULL default '0',
  `subnodes` int(11) NOT NULL default '0',
  `title` varchar(255) default NULL,
  PRIMARY KEY  (`id`,`status`),
  KEY `ezrss_export_rsseid` (`rssexport_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezrss_export_item`
--

LOCK TABLES `ezrss_export_item` WRITE;
/*!40000 ALTER TABLE `ezrss_export_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezrss_export_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezrss_import`
--

DROP TABLE IF EXISTS `ezrss_import`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezrss_import` (
  `active` int(11) default NULL,
  `class_description` varchar(255) default NULL,
  `class_id` int(11) default NULL,
  `class_title` varchar(255) default NULL,
  `class_url` varchar(255) default NULL,
  `created` int(11) default NULL,
  `creator_id` int(11) default NULL,
  `destination_node_id` int(11) default NULL,
  `id` int(11) NOT NULL auto_increment,
  `import_description` longtext NOT NULL,
  `modified` int(11) default NULL,
  `modifier_id` int(11) default NULL,
  `name` varchar(255) default NULL,
  `object_owner_id` int(11) default NULL,
  `status` int(11) NOT NULL default '0',
  `url` longtext,
  PRIMARY KEY  (`id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezrss_import`
--

LOCK TABLES `ezrss_import` WRITE;
/*!40000 ALTER TABLE `ezrss_import` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezrss_import` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezscheduled_script`
--

DROP TABLE IF EXISTS `ezscheduled_script`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezscheduled_script` (
  `command` varchar(255) NOT NULL default '',
  `id` int(11) NOT NULL auto_increment,
  `last_report_timestamp` int(11) NOT NULL default '0',
  `name` varchar(50) NOT NULL default '',
  `process_id` int(11) NOT NULL default '0',
  `progress` int(3) default '0',
  `user_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `ezscheduled_script_timestamp` (`last_report_timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezscheduled_script`
--

LOCK TABLES `ezscheduled_script` WRITE;
/*!40000 ALTER TABLE `ezscheduled_script` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezscheduled_script` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezsearch_object_word_link`
--

DROP TABLE IF EXISTS `ezsearch_object_word_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezsearch_object_word_link` (
  `contentclass_attribute_id` int(11) NOT NULL default '0',
  `contentclass_id` int(11) NOT NULL default '0',
  `contentobject_id` int(11) NOT NULL default '0',
  `frequency` float NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `identifier` varchar(255) NOT NULL default '',
  `integer_value` int(11) NOT NULL default '0',
  `next_word_id` int(11) NOT NULL default '0',
  `placement` int(11) NOT NULL default '0',
  `prev_word_id` int(11) NOT NULL default '0',
  `published` int(11) NOT NULL default '0',
  `section_id` int(11) NOT NULL default '0',
  `word_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `ezsearch_object_word_link_frequency` (`frequency`),
  KEY `ezsearch_object_word_link_identifier` (`identifier`),
  KEY `ezsearch_object_word_link_integer_value` (`integer_value`),
  KEY `ezsearch_object_word_link_object` (`contentobject_id`),
  KEY `ezsearch_object_word_link_word` (`word_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10744 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezsearch_object_word_link`
--

LOCK TABLES `ezsearch_object_word_link` WRITE;
/*!40000 ALTER TABLE `ezsearch_object_word_link` DISABLE KEYS */;
INSERT INTO `ezsearch_object_word_link` VALUES (6,3,11,0,201,'',0,58,0,0,1033920746,2,57),(6,3,11,0,202,'',0,57,1,57,1033920746,2,58),(6,3,11,0,203,'',0,58,2,58,1033920746,2,57),(6,3,11,0,204,'',0,0,3,57,1033920746,2,58),(6,3,12,0,213,'',0,62,0,0,1033920775,2,61),(6,3,12,0,214,'',0,61,1,61,1033920775,2,62),(6,3,12,0,215,'',0,62,2,62,1033920775,2,61),(6,3,12,0,216,'',0,0,3,61,1033920775,2,62),(8,4,14,0,217,'',0,61,0,0,1033920830,2,61),(8,4,14,0,218,'',0,63,1,61,1033920830,2,61),(9,4,14,0,219,'',0,63,2,61,1033920830,2,63),(9,4,14,0,220,'',0,64,3,63,1033920830,2,63),(12,4,14,0,221,'',0,65,4,63,1033920830,2,64),(12,4,14,0,222,'',0,64,5,64,1033920830,2,65),(12,4,14,0,223,'',0,65,6,65,1033920830,2,64),(12,4,14,0,224,'',0,0,7,64,1033920830,2,65),(6,3,42,0,227,'',0,62,0,0,1072180330,2,67),(6,3,42,0,228,'',0,67,1,67,1072180330,2,62),(6,3,42,0,229,'',0,62,2,62,1072180330,2,67),(6,3,42,0,230,'',0,63,3,67,1072180330,2,62),(7,3,42,0,231,'',0,68,4,62,1072180330,2,63),(7,3,42,0,232,'',0,69,5,63,1072180330,2,68),(7,3,42,0,233,'',0,70,6,68,1072180330,2,69),(7,3,42,0,234,'',0,67,7,69,1072180330,2,70),(7,3,42,0,235,'',0,63,8,70,1072180330,2,67),(7,3,42,0,236,'',0,63,9,67,1072180330,2,63),(7,3,42,0,237,'',0,68,10,63,1072180330,2,63),(7,3,42,0,238,'',0,69,11,63,1072180330,2,68),(7,3,42,0,239,'',0,70,12,68,1072180330,2,69),(7,3,42,0,240,'',0,67,13,69,1072180330,2,70),(7,3,42,0,241,'',0,63,14,70,1072180330,2,67),(7,3,42,0,242,'',0,0,15,67,1072180330,2,63),(8,4,10,0,243,'',0,67,0,0,1033920665,2,67),(8,4,10,0,244,'',0,63,1,67,1033920665,2,67),(9,4,10,0,245,'',0,63,2,67,1033920665,2,63),(9,4,10,0,246,'',0,67,3,63,1033920665,2,63),(12,4,10,0,247,'',0,65,4,63,1033920665,2,67),(12,4,10,0,248,'',0,67,5,67,1033920665,2,65),(12,4,10,0,249,'',0,65,6,65,1033920665,2,67),(12,4,10,0,250,'',0,0,7,67,1033920665,2,65),(4,1,45,0,1485,'',0,85,0,0,1079684190,4,85),(4,1,45,0,1486,'',0,86,1,85,1079684190,4,85),(158,1,45,0,1487,'',0,86,2,85,1079684190,4,86),(158,1,45,0,1488,'',0,0,3,86,1079684190,4,86),(4,1,49,0,2379,'',0,90,0,0,1080220197,3,90),(4,1,49,0,2380,'',0,79,1,90,1080220197,3,90),(158,1,49,0,2381,'',1,79,2,90,1080220197,3,79),(158,1,49,0,2382,'',1,0,3,79,1080220197,3,79),(4,1,50,0,2383,'',0,101,0,0,1080220220,3,101),(4,1,50,0,2384,'',0,79,1,101,1080220220,3,101),(158,1,50,0,2385,'',1,79,2,101,1080220220,3,79),(158,1,50,0,2386,'',1,0,3,79,1080220220,3,79),(4,1,51,0,2387,'',0,102,0,0,1080220233,3,102),(4,1,51,0,2388,'',0,79,1,102,1080220233,3,102),(158,1,51,0,2389,'',1,79,2,102,1080220233,3,79),(158,1,51,0,2390,'',1,0,3,79,1080220233,3,79),(159,14,52,0,2391,'',0,104,0,0,1082016591,4,103),(159,14,52,0,2392,'',0,105,1,103,1082016591,4,104),(159,14,52,0,2393,'',0,103,2,104,1082016591,4,105),(159,14,52,0,2394,'',0,104,3,105,1082016591,4,103),(159,14,52,0,2395,'',0,105,4,103,1082016591,4,104),(159,14,52,0,2396,'',0,0,5,104,1082016591,4,105),(176,15,54,0,2397,'',0,106,0,0,1082016652,4,106),(176,15,54,0,2398,'',0,0,1,106,1082016652,4,106),(6,3,4,0,3520,'name',0,68,0,0,1033917596,2,62),(6,3,4,0,3521,'name',0,393,1,62,1033917596,2,68),(7,3,4,0,3522,'description',0,68,2,68,1033917596,2,393),(7,3,4,0,3523,'description',0,0,3,393,1033917596,2,68),(6,3,13,0,3764,'name',0,0,0,0,1033920794,2,530),(6,3,72,0,3765,'name',0,0,0,0,1283764063,2,531),(6,3,73,0,3766,'name',0,0,0,0,1283764074,2,532),(116,5,84,0,5033,'name',0,0,0,0,1283858528,3,1192),(116,5,85,0,5700,'name',0,0,0,0,1283859632,3,1192),(116,5,90,0,7237,'name',0,2174,0,0,1283864415,3,2188),(116,5,90,0,7238,'name',0,2189,1,2188,1283864415,3,2174),(116,5,90,0,7239,'name',0,0,2,2174,1283864415,3,2189),(116,5,92,0,7432,'name',0,0,0,0,1283930105,3,1192),(116,5,93,0,8001,'name',0,0,0,0,1283931318,3,1192),(116,5,95,0,8574,'name',0,2174,0,0,1283931925,1,2730),(116,5,95,0,8575,'name',0,0,1,2730,1283931925,1,2174),(116,5,97,0,8665,'name',0,2174,0,0,1283933210,1,2188),(116,5,97,0,8666,'name',0,2765,1,2188,1283933210,1,2174),(116,5,97,0,8667,'name',0,0,2,2174,1283933210,1,2765),(116,5,99,0,9123,'name',0,2889,0,0,1283934249,3,2888),(116,5,99,0,9124,'name',0,601,1,2888,1283934249,3,2889),(116,5,99,0,9125,'name',0,2894,2,2889,1283934249,3,601),(116,5,99,0,9126,'name',0,2893,3,601,1283934249,3,2894),(116,5,99,0,9127,'name',0,2893,4,2894,1283934249,3,2893),(116,5,99,0,9128,'name',0,0,5,2893,1283934249,3,2893),(116,5,102,0,9325,'name',0,2889,0,0,1283936573,3,2888),(116,5,102,0,9326,'name',0,601,1,2888,1283936573,3,2889),(116,5,102,0,9327,'name',0,2892,2,2889,1283936573,3,601),(116,5,102,0,9328,'name',0,2893,3,601,1283936573,3,2892),(116,5,102,0,9329,'name',0,2893,4,2892,1283936573,3,2893),(116,5,102,0,9330,'name',0,0,5,2893,1283936573,3,2893),(116,5,105,0,9387,'name',0,0,0,0,1283937709,3,2894),(116,5,106,0,9388,'name',0,0,0,0,1283937756,3,3046),(4,1,87,0,9738,'name',0,3224,0,0,1283860725,1,3224),(155,1,87,0,9739,'short_name',0,69,1,3224,1283860725,1,3224),(119,1,87,0,9740,'short_description',0,3225,2,3224,1283860725,1,69),(119,1,87,0,9741,'short_description',0,463,3,69,1283860725,1,3225),(119,1,87,0,9742,'short_description',0,464,4,3225,1283860725,1,463),(119,1,87,0,9743,'short_description',0,531,5,463,1283860725,1,464),(119,1,87,0,9744,'short_description',0,530,6,464,1283860725,1,531),(119,1,87,0,9745,'short_description',0,611,7,531,1283860725,1,530),(119,1,87,0,9746,'short_description',0,61,8,530,1283860725,1,611),(119,1,87,0,9747,'short_description',0,69,9,611,1283860725,1,61),(156,1,87,0,9748,'description',0,3225,10,61,1283860725,1,69),(156,1,87,0,9749,'description',0,3226,11,69,1283860725,1,3225),(156,1,87,0,9750,'description',0,3227,12,3225,1283860725,1,3226),(156,1,87,0,9751,'description',0,3228,13,3226,1283860725,1,3227),(156,1,87,0,9752,'description',0,595,14,3227,1283860725,1,3228),(156,1,87,0,9753,'description',0,3229,15,3228,1283860725,1,595),(156,1,87,0,9754,'description',0,3230,16,595,1283860725,1,3229),(156,1,87,0,9755,'description',0,598,17,3229,1283860725,1,3230),(156,1,87,0,9756,'description',0,3229,18,3230,1283860725,1,598),(156,1,87,0,9757,'description',0,3231,19,598,1283860725,1,3229),(156,1,87,0,9758,'description',0,600,20,3229,1283860725,1,3231),(156,1,87,0,9759,'description',0,601,21,3231,1283860725,1,600),(156,1,87,0,9760,'description',0,1795,22,600,1283860725,1,601),(156,1,87,0,9761,'description',0,3229,23,601,1283860725,1,1795),(156,1,87,0,9762,'description',0,3232,24,1795,1283860725,1,3229),(156,1,87,0,9763,'description',0,604,25,3229,1283860725,1,3232),(156,1,87,0,9764,'description',0,3233,26,3232,1283860725,1,604),(156,1,87,0,9765,'description',0,477,27,604,1283860725,1,3233),(156,1,87,0,9766,'description',0,1798,28,3233,1283860725,1,477),(156,1,87,0,9767,'description',0,607,29,477,1283860725,1,1798),(156,1,87,0,9768,'description',0,3234,30,1798,1283860725,1,607),(156,1,87,0,9769,'description',0,3235,31,607,1283860725,1,3234),(156,1,87,0,9770,'description',0,601,32,3234,1283860725,1,3235),(156,1,87,0,9771,'description',0,1795,33,3235,1283860725,1,601),(156,1,87,0,9772,'description',0,69,34,601,1283860725,1,1795),(156,1,87,0,9773,'description',0,531,35,1795,1283860725,1,69),(156,1,87,0,9774,'description',0,530,36,69,1283860725,1,531),(156,1,87,0,9775,'description',0,611,37,531,1283860725,1,530),(156,1,87,0,9776,'description',0,61,38,530,1283860725,1,611),(156,1,87,0,9777,'description',0,3229,39,611,1283860725,1,61),(156,1,87,0,9778,'description',0,3236,40,61,1283860725,1,3229),(156,1,87,0,9779,'description',0,1795,41,3229,1283860725,1,3236),(156,1,87,0,9780,'description',0,3229,42,3236,1283860725,1,1795),(156,1,87,0,9781,'description',0,3237,43,1795,1283860725,1,3229),(156,1,87,0,9782,'description',0,3238,44,3229,1283860725,1,3237),(156,1,87,0,9783,'description',0,3239,45,3237,1283860725,1,3238),(156,1,87,0,9784,'description',0,3240,46,3238,1283860725,1,3239),(156,1,87,0,9785,'description',0,3241,47,3239,1283860725,1,3240),(156,1,87,0,9786,'description',0,618,48,3240,1283860725,1,3241),(156,1,87,0,9787,'description',0,1795,49,3241,1283860725,1,618),(156,1,87,0,9788,'description',0,3229,50,618,1283860725,1,1795),(156,1,87,0,9789,'description',0,3242,51,1795,1283860725,1,3229),(156,1,87,0,9790,'description',0,3243,52,3229,1283860725,1,3242),(156,1,87,0,9791,'description',0,1810,53,3242,1283860725,1,3243),(156,1,87,0,9792,'description',0,3244,54,3243,1283860725,1,1810),(156,1,87,0,9793,'description',0,3229,55,1810,1283860725,1,3244),(156,1,87,0,9794,'description',0,3231,56,3244,1283860725,1,3229),(156,1,87,0,9795,'description',0,623,57,3229,1283860725,1,3231),(156,1,87,0,9796,'description',0,624,58,3231,1283860725,1,623),(156,1,87,0,9797,'description',0,625,59,623,1283860725,1,624),(156,1,87,0,9798,'description',0,626,60,624,1283860725,1,625),(156,1,87,0,9799,'description',0,3245,61,625,1283860725,1,626),(156,1,87,0,9800,'description',0,3229,62,626,1283860725,1,3245),(156,1,87,0,9801,'description',0,3246,63,3245,1283860725,1,3229),(156,1,87,0,9802,'description',0,629,64,3229,1283860725,1,3246),(156,1,87,0,9803,'description',0,1795,65,3246,1283860725,1,629),(156,1,87,0,9804,'description',0,3229,66,629,1283860725,1,1795),(156,1,87,0,9805,'description',0,3247,67,1795,1283860725,1,3229),(156,1,87,0,9806,'description',0,629,68,3229,1283860725,1,3247),(156,1,87,0,9807,'description',0,1795,69,3247,1283860725,1,629),(156,1,87,0,9808,'description',0,463,70,629,1283860725,1,1795),(156,1,87,0,9809,'description',0,464,71,1795,1283860725,1,463),(156,1,87,0,9810,'description',0,0,72,463,1283860725,1,464),(8,4,119,0,10144,'first_name',0,3401,0,0,1295527921,2,3400),(9,4,119,0,10145,'last_name',0,3400,1,3400,1295527921,2,3401),(12,4,119,0,10146,'user_account',0,3402,2,3401,1295527921,2,3400),(12,4,119,0,10147,'user_account',0,3399,3,3400,1295527921,2,3402),(12,4,119,0,10148,'user_account',0,0,4,3402,1295527921,2,3399),(4,1,1,0,10149,'name',0,618,0,0,1033917596,1,3403),(4,1,1,0,10150,'name',0,3404,1,3403,1033917596,1,618),(4,1,1,0,10151,'name',0,69,2,618,1033917596,1,3404),(4,1,1,0,10152,'name',0,3405,3,3404,1033917596,1,69),(4,1,1,0,10153,'name',0,3277,4,69,1033917596,1,3405),(4,1,1,0,10154,'name',0,3406,5,3405,1033917596,1,3277),(155,1,1,0,10155,'short_name',0,3407,6,3277,1033917596,1,3406),(156,1,1,0,10156,'description',0,2612,7,3406,1033917596,1,3407),(156,1,1,0,10157,'description',0,1798,8,3407,1033917596,1,2612),(156,1,1,0,10158,'description',0,3408,9,2612,1033917596,1,1798),(156,1,1,0,10159,'description',0,3409,10,1798,1033917596,1,3408),(156,1,1,0,10160,'description',0,1810,11,3408,1033917596,1,3409),(156,1,1,0,10161,'description',0,1795,12,3409,1033917596,1,1810),(156,1,1,0,10162,'description',0,3410,13,1810,1033917596,1,1795),(156,1,1,0,10163,'description',0,595,14,1795,1033917596,1,3410),(156,1,1,0,10164,'description',0,3411,15,3410,1033917596,1,595),(156,1,1,0,10165,'description',0,604,16,595,1033917596,1,3411),(156,1,1,0,10166,'description',0,3405,17,3411,1033917596,1,604),(156,1,1,0,10167,'description',0,3277,18,604,1033917596,1,3405),(156,1,1,0,10168,'description',0,1798,19,3405,1033917596,1,3277),(156,1,1,0,10169,'description',0,2612,20,3277,1033917596,1,1798),(156,1,1,0,10170,'description',0,1798,21,1798,1033917596,1,2612),(156,1,1,0,10171,'description',0,2635,22,2612,1033917596,1,1798),(156,1,1,0,10172,'description',0,3412,23,1798,1033917596,1,2635),(156,1,1,0,10173,'description',0,3413,24,2635,1033917596,1,3412),(156,1,1,0,10174,'description',0,3408,25,3412,1033917596,1,3413),(156,1,1,0,10175,'description',0,3083,26,3413,1033917596,1,3408),(156,1,1,0,10176,'description',0,3414,27,3408,1033917596,1,3083),(156,1,1,0,10177,'description',0,595,28,3083,1033917596,1,3414),(156,1,1,0,10178,'description',0,3415,29,3414,1033917596,1,595),(156,1,1,0,10179,'description',0,2696,30,595,1033917596,1,3415),(156,1,1,0,10180,'description',0,1798,31,3415,1033917596,1,2696),(156,1,1,0,10181,'description',0,3416,32,2696,1033917596,1,1798),(156,1,1,0,10182,'description',0,629,33,1798,1033917596,1,3416),(156,1,1,0,10183,'description',0,1795,34,3416,1033917596,1,629),(156,1,1,0,10184,'description',0,611,35,629,1033917596,1,1795),(156,1,1,0,10185,'description',0,3417,36,1795,1033917596,1,611),(156,1,1,0,10186,'description',0,3418,37,611,1033917596,1,3417),(156,1,1,0,10187,'description',0,2625,38,3417,1033917596,1,3418),(156,1,1,0,10188,'description',0,3419,39,3418,1033917596,1,2625),(156,1,1,0,10189,'description',0,3420,40,2625,1033917596,1,3419),(156,1,1,0,10190,'description',0,3421,41,3419,1033917596,1,3420),(156,1,1,0,10191,'description',0,2634,42,3420,1033917596,1,3421),(156,1,1,0,10192,'description',0,3422,43,3421,1033917596,1,2634),(156,1,1,0,10193,'description',0,3423,44,2634,1033917596,1,3422),(156,1,1,0,10194,'description',0,2630,45,3422,1033917596,1,3423),(156,1,1,0,10195,'description',0,3424,46,3423,1033917596,1,2630),(156,1,1,0,10196,'description',0,3425,47,2630,1033917596,1,3424),(156,1,1,0,10197,'description',0,3409,48,3424,1033917596,1,3425),(156,1,1,0,10198,'description',0,595,49,3425,1033917596,1,3409),(156,1,1,0,10199,'description',0,3426,50,3409,1033917596,1,595),(156,1,1,0,10200,'description',0,3427,51,595,1033917596,1,3426),(156,1,1,0,10201,'description',0,3428,52,3426,1033917596,1,3427),(156,1,1,0,10202,'description',0,463,53,3427,1033917596,1,3428),(156,1,1,0,10203,'description',0,464,54,3428,1033917596,1,463),(156,1,1,0,10204,'description',0,0,55,463,1033917596,1,464),(4,1,82,0,10277,'name',0,3277,0,0,1283857505,1,3405),(4,1,82,0,10278,'name',0,3405,1,3405,1283857505,1,3277),(155,1,82,0,10279,'short_name',0,3462,2,3277,1283857505,1,3405),(119,1,82,0,10280,'short_description',0,3463,3,3405,1283857505,1,3462),(119,1,82,0,10281,'short_description',0,600,4,3462,1283857505,1,3463),(119,1,82,0,10282,'short_description',0,623,5,3463,1283857505,1,600),(119,1,82,0,10283,'short_description',0,3464,6,600,1283857505,1,623),(119,1,82,0,10284,'short_description',0,3465,7,623,1283857505,1,3464),(119,1,82,0,10285,'short_description',0,3466,8,3464,1283857505,1,3465),(119,1,82,0,10286,'short_description',0,69,9,3465,1283857505,1,3466),(119,1,82,0,10287,'short_description',0,3467,10,3466,1283857505,1,69),(119,1,82,0,10288,'short_description',0,3468,11,69,1283857505,1,3467),(119,1,82,0,10289,'short_description',0,3462,12,3467,1283857505,1,3468),(156,1,82,0,10290,'description',0,3469,13,3468,1283857505,1,3462),(156,1,82,0,10291,'description',0,69,14,3462,1283857505,1,3469),(156,1,82,0,10292,'description',0,477,15,3469,1283857505,1,69),(156,1,82,0,10293,'description',0,3405,16,69,1283857505,1,477),(156,1,82,0,10294,'description',0,2625,17,477,1283857505,1,3405),(156,1,82,0,10295,'description',0,3470,18,3405,1283857505,1,2625),(156,1,82,0,10296,'description',0,629,19,2625,1283857505,1,3470),(156,1,82,0,10297,'description',0,3471,20,3470,1283857505,1,629),(156,1,82,0,10298,'description',0,595,21,629,1283857505,1,3471),(156,1,82,0,10299,'description',0,3472,22,3471,1283857505,1,595),(156,1,82,0,10300,'description',0,3473,23,595,1283857505,1,3472),(156,1,82,0,10301,'description',0,2630,24,3472,1283857505,1,3473),(156,1,82,0,10302,'description',0,3474,25,3473,1283857505,1,2630),(156,1,82,0,10303,'description',0,3475,26,2630,1283857505,1,3474),(156,1,82,0,10304,'description',0,595,27,3474,1283857505,1,3475),(156,1,82,0,10305,'description',0,3476,28,3475,1283857505,1,595),(156,1,82,0,10306,'description',0,604,29,595,1283857505,1,3476),(156,1,82,0,10307,'description',0,463,30,3476,1283857505,1,604),(156,1,82,0,10308,'description',0,464,31,604,1283857505,1,463),(156,1,82,0,10309,'description',0,595,32,463,1283857505,1,464),(156,1,82,0,10310,'description',0,477,33,464,1283857505,1,595),(156,1,82,0,10311,'description',0,2634,34,595,1283857505,1,477),(156,1,82,0,10312,'description',0,2635,35,477,1283857505,1,2634),(156,1,82,0,10313,'description',0,2625,36,2634,1283857505,1,2635),(156,1,82,0,10314,'description',0,3470,37,2635,1283857505,1,2625),(156,1,82,0,10315,'description',0,629,38,2625,1283857505,1,3470),(156,1,82,0,10316,'description',0,463,39,3470,1283857505,1,629),(156,1,82,0,10317,'description',0,464,40,629,1283857505,1,463),(156,1,82,0,10318,'description',0,3477,41,463,1283857505,1,464),(156,1,82,0,10319,'description',0,595,42,464,1283857505,1,3477),(156,1,82,0,10320,'description',0,3478,43,3477,1283857505,1,595),(156,1,82,0,10321,'description',0,3479,44,595,1283857505,1,3478),(156,1,82,0,10322,'description',0,463,45,3478,1283857505,1,3479),(156,1,82,0,10323,'description',0,464,46,3479,1283857505,1,463),(156,1,82,0,10324,'description',0,3480,47,463,1283857505,1,464),(156,1,82,0,10325,'description',0,3481,48,464,1283857505,1,3480),(156,1,82,0,10326,'description',0,595,49,3480,1283857505,1,3481),(156,1,82,0,10327,'description',0,3482,50,3481,1283857505,1,595),(156,1,82,0,10328,'description',0,3483,51,595,1283857505,1,3482),(156,1,82,0,10329,'description',0,626,52,3482,1283857505,1,3483),(156,1,82,0,10330,'description',0,3484,53,3483,1283857505,1,626),(156,1,82,0,10331,'description',0,604,54,626,1283857505,1,3484),(156,1,82,0,10332,'description',0,3485,55,3484,1283857505,1,604),(156,1,82,0,10333,'description',0,3486,56,604,1283857505,1,3485),(156,1,82,0,10334,'description',0,3487,57,3485,1283857505,1,3486),(156,1,82,0,10335,'description',0,607,58,3486,1283857505,1,3487),(156,1,82,0,10336,'description',0,3488,59,3487,1283857505,1,607),(156,1,82,0,10337,'description',0,3489,60,607,1283857505,1,3488),(156,1,82,0,10338,'description',0,604,61,3488,1283857505,1,3489),(156,1,82,0,10339,'description',0,624,62,3489,1283857505,1,604),(156,1,82,0,10340,'description',0,3490,63,604,1283857505,1,624),(156,1,82,0,10341,'description',0,3491,64,624,1283857505,1,3490),(156,1,82,0,10342,'description',0,601,65,3490,1283857505,1,3491),(156,1,82,0,10343,'description',0,3492,66,3491,1283857505,1,601),(156,1,82,0,10344,'description',0,3493,67,601,1283857505,1,3492),(156,1,82,0,10345,'description',0,3494,68,3492,1283857505,1,3493),(156,1,82,0,10346,'description',0,463,69,3493,1283857505,1,3494),(156,1,82,0,10347,'description',0,464,70,3494,1283857505,1,463),(156,1,82,0,10348,'description',0,0,71,463,1283857505,1,464),(1,2,77,0,10491,'title',0,2174,0,0,1283769648,1,2730),(1,2,77,0,10492,'title',0,3547,1,2730,1283769648,1,2174),(1,2,77,0,10493,'title',0,3548,2,2174,1283769648,1,3547),(1,2,77,0,10494,'title',0,3549,3,3547,1283769648,1,3548),(1,2,77,0,10495,'title',0,2730,4,3548,1283769648,1,3549),(152,2,77,0,10496,'short_title',0,2174,5,3549,1283769648,1,2730),(152,2,77,0,10497,'short_title',0,3550,6,2730,1283769648,1,2174),(120,2,77,0,10498,'intro',0,601,7,2174,1283769648,1,3550),(120,2,77,0,10499,'intro',0,2730,8,3550,1283769648,1,601),(120,2,77,0,10500,'intro',0,3551,9,601,1283769648,1,2730),(120,2,77,0,10501,'intro',0,3547,10,2730,1283769648,1,3551),(120,2,77,0,10502,'intro',0,3552,11,3551,1283769648,1,3547),(120,2,77,0,10503,'intro',0,3553,12,3547,1283769648,1,3552),(120,2,77,0,10504,'intro',0,463,13,3552,1283769648,1,3553),(121,2,77,0,10505,'body',0,464,14,3553,1283769648,1,463),(121,2,77,0,10506,'body',0,3554,15,463,1283769648,1,464),(121,2,77,0,10507,'body',0,463,16,464,1283769648,1,3554),(121,2,77,0,10508,'body',0,464,17,3554,1283769648,1,463),(121,2,77,0,10509,'body',0,3555,18,463,1283769648,1,464),(121,2,77,0,10510,'body',0,463,19,464,1283769648,1,3555),(121,2,77,0,10511,'body',0,464,20,3555,1283769648,1,463),(121,2,77,0,10512,'body',0,3556,21,463,1283769648,1,464),(121,2,77,0,10513,'body',0,595,22,464,1283769648,1,3556),(121,2,77,0,10514,'body',0,3557,23,3556,1283769648,1,595),(121,2,77,0,10515,'body',0,463,24,595,1283769648,1,3557),(121,2,77,0,10516,'body',0,464,25,3557,1283769648,1,463),(121,2,77,0,10517,'body',0,3558,26,463,1283769648,1,464),(121,2,77,0,10518,'body',0,463,27,464,1283769648,1,3558),(121,2,77,0,10519,'body',0,464,28,3558,1283769648,1,463),(121,2,77,0,10520,'body',0,3559,29,463,1283769648,1,464),(121,2,77,0,10521,'body',0,463,30,464,1283769648,1,3559),(121,2,77,0,10522,'body',0,464,31,3559,1283769648,1,463),(121,2,77,0,10523,'body',0,3560,32,463,1283769648,1,464),(121,2,77,0,10524,'body',0,463,33,464,1283769648,1,3560),(121,2,77,0,10525,'body',0,464,34,3560,1283769648,1,463),(121,2,77,0,10526,'body',0,3561,35,463,1283769648,1,464),(121,2,77,0,10527,'body',0,463,36,464,1283769648,1,3561),(121,2,77,0,10528,'body',0,464,37,3561,1283769648,1,463),(121,2,77,0,10529,'body',0,3562,38,463,1283769648,1,464),(121,2,77,0,10530,'body',0,3554,39,464,1283769648,1,3562),(121,2,77,0,10531,'body',0,463,40,3562,1283769648,1,3554),(121,2,77,0,10532,'body',0,464,41,3554,1283769648,1,463),(121,2,77,0,10533,'body',0,3563,42,463,1283769648,1,464),(121,2,77,0,10534,'body',0,3554,43,464,1283769648,1,3563),(121,2,77,0,10535,'body',0,463,44,3563,1283769648,1,3554),(121,2,77,0,10536,'body',0,464,45,3554,1283769648,1,463),(121,2,77,0,10537,'body',0,3564,46,463,1283769648,1,464),(121,2,77,0,10538,'body',0,0,47,464,1283769648,1,3564),(8,4,120,0,10539,'first_name',0,3566,0,0,1309526167,2,3565),(9,4,120,0,10540,'last_name',0,3397,1,3565,1309526167,2,3566),(12,4,120,0,10541,'user_account',0,3567,2,3566,1309526167,2,3397),(12,4,120,0,10542,'user_account',0,3568,3,3397,1309526167,2,3567),(12,4,120,0,10543,'user_account',0,3569,4,3567,1309526167,2,3568),(12,4,120,0,10544,'user_account',0,0,5,3568,1309526167,2,3569),(8,4,122,0,10555,'first_name',0,3575,0,0,1316109619,2,3574),(9,4,122,0,10556,'last_name',0,3576,1,3574,1316109619,2,3575),(12,4,122,0,10557,'user_account',0,3576,2,3575,1316109619,2,3576),(12,4,122,0,10558,'user_account',0,3399,3,3576,1316109619,2,3576),(12,4,122,0,10559,'user_account',0,0,4,3576,1316109619,2,3399),(8,4,118,0,10560,'first_name',0,3397,0,0,1295525608,2,1795),(9,4,118,0,10561,'last_name',0,1795,1,1795,1295525608,2,3397),(12,4,118,0,10562,'user_account',0,3577,2,3397,1295525608,2,1795),(12,4,118,0,10563,'user_account',0,3399,3,1795,1295525608,2,3577),(12,4,118,0,10564,'user_account',0,0,4,3577,1295525608,2,3399),(8,4,134,0,10610,'first_name',0,3615,0,0,1353415505,2,3614),(9,4,134,0,10611,'last_name',0,3616,1,3614,1353415505,2,3615),(12,4,134,0,10612,'user_account',0,3616,2,3615,1353415505,2,3616),(12,4,134,0,10613,'user_account',0,3617,3,3616,1353415505,2,3616),(12,4,134,0,10614,'user_account',0,0,4,3616,1353415505,2,3617),(8,4,140,0,10734,'first_name',0,3565,0,0,1358503275,2,3565),(9,4,140,0,10735,'last_name',0,3628,1,3565,1358503275,2,3565),(12,4,140,0,10736,'user_account',0,3565,2,3565,1358503275,2,3628),(12,4,140,0,10737,'user_account',0,3399,3,3628,1358503275,2,3565),(12,4,140,0,10738,'user_account',0,0,4,3565,1358503275,2,3399),(8,4,141,0,10739,'first_name',0,3615,0,0,1359038200,2,3614),(9,4,141,0,10740,'last_name',0,3614,1,3614,1359038200,2,3615),(12,4,141,0,10741,'user_account',0,3629,2,3615,1359038200,2,3614),(12,4,141,0,10742,'user_account',0,3630,3,3614,1359038200,2,3629),(12,4,141,0,10743,'user_account',0,0,4,3629,1359038200,2,3630);
/*!40000 ALTER TABLE `ezsearch_object_word_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezsearch_return_count`
--

DROP TABLE IF EXISTS `ezsearch_return_count`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezsearch_return_count` (
  `count` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `phrase_id` int(11) NOT NULL default '0',
  `time` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `ezsearch_return_cnt_ph_id_cnt` (`phrase_id`,`count`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezsearch_return_count`
--

LOCK TABLES `ezsearch_return_count` WRITE;
/*!40000 ALTER TABLE `ezsearch_return_count` DISABLE KEYS */;
INSERT INTO `ezsearch_return_count` VALUES (1,1,1,1282808089),(1,2,2,1283762653),(0,3,2,1283763066),(0,4,3,1286964199),(0,5,4,1312897957),(0,6,5,1315900513),(0,7,6,1315900526),(0,8,7,1334060935),(0,9,8,1349434484);
/*!40000 ALTER TABLE `ezsearch_return_count` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezsearch_search_phrase`
--

DROP TABLE IF EXISTS `ezsearch_search_phrase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezsearch_search_phrase` (
  `id` int(11) NOT NULL auto_increment,
  `phrase` varchar(250) default NULL,
  `phrase_count` int(11) default '0',
  `result_count` int(11) default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `ezsearch_search_phrase_phrase` (`phrase`),
  KEY `ezsearch_search_phrase_count` (`phrase_count`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezsearch_search_phrase`
--

LOCK TABLES `ezsearch_search_phrase` WRITE;
/*!40000 ALTER TABLE `ezsearch_search_phrase` DISABLE KEYS */;
INSERT INTO `ezsearch_search_phrase` VALUES (1,'ez',1,1),(2,'aida',2,1),(3,'ansatte',1,0),(4,'drammen',1,0),(5,'turnus',1,0),(6,'turnus-det glade vanvidd',1,0),(7,'lillesand',1,0),(8,'hadsel',1,0);
/*!40000 ALTER TABLE `ezsearch_search_phrase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezsearch_word`
--

DROP TABLE IF EXISTS `ezsearch_word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezsearch_word` (
  `id` int(11) NOT NULL auto_increment,
  `object_count` int(11) NOT NULL default '0',
  `word` varchar(150) default NULL,
  PRIMARY KEY  (`id`),
  KEY `ezsearch_word_obj_count` (`object_count`),
  KEY `ezsearch_word_word_i` (`word`)
) ENGINE=InnoDB AUTO_INCREMENT=3631 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezsearch_word`
--

LOCK TABLES `ezsearch_word` WRITE;
/*!40000 ALTER TABLE `ezsearch_word` DISABLE KEYS */;
INSERT INTO `ezsearch_word` VALUES (57,1,'guest'),(58,1,'accounts'),(61,3,'administrator'),(62,3,'users'),(63,3,'user'),(64,1,'admin'),(65,2,'nospam@ez.no'),(67,2,'anonymous'),(68,2,'group'),(69,4,'for'),(70,1,'the'),(79,3,'1'),(85,1,'setup'),(86,1,'0'),(90,1,'images'),(101,1,'files'),(102,1,'multimedia'),(103,1,'common'),(104,1,'ini'),(105,1,'settings'),(106,1,'sitestyle_identifier'),(393,1,'main'),(463,4,'amp'),(464,4,'nbsp'),(477,2,'at'),(530,2,'opplæringsansvarlige'),(531,2,'kursansvarlige'),(532,1,'ansatte'),(595,4,'og'),(598,1,'den'),(600,2,'seg'),(601,5,'på'),(604,3,'i'),(607,2,'har'),(611,2,'eller'),(618,2,'til'),(623,2,'av'),(624,2,'en'),(625,1,'som'),(626,2,'er'),(629,3,'et'),(1192,4,'fylkeshusetfront'),(1795,3,'kurs'),(1798,2,'du'),(1810,2,'om'),(2174,4,'feed'),(2188,2,'128px'),(2189,1,'icon_svg'),(2612,1,'kan'),(2625,2,'vil'),(2630,2,'å'),(2634,2,'det'),(2635,2,'også'),(2696,1,'skal'),(2730,2,'rss'),(2765,1,'icon'),(2888,2,'følg'),(2889,2,'oss'),(2892,1,'facebook'),(2893,2,'gt'),(2894,2,'flickr'),(3046,1,'wikipedia'),(3083,1,'brukerveiledninger'),(3224,1,'help'),(3225,1,'brukere'),(3226,1,'hva'),(3227,1,'inneholder'),(3228,1,'portalen'),(3229,1,'hvordan'),(3230,1,'virker'),(3231,1,'melde'),(3232,1,'oppdatere'),(3233,1,'kalender'),(3234,1,'fått'),(3235,1,'plass'),(3236,1,'publisere'),(3237,1,'administrere'),(3238,1,'filer'),(3239,1,'legg'),(3240,1,'ved'),(3241,1,'dokumentasjon'),(3242,1,'endre'),(3243,1,'opplysninger'),(3244,1,'kurset'),(3245,1,'påmeldt'),(3246,1,'avlyse'),(3247,1,'slette'),(3277,2,'kommune'),(3397,2,'testbruker'),(3399,4,'frikomport.no'),(3400,1,'kurt'),(3401,1,'kursansvarlig'),(3402,1,'nospam_kurt'),(3403,1,'velkommen'),(3404,1,'kursportalen'),(3405,2,'storholmen'),(3406,1,'forsiden'),(3407,1,'her'),(3408,1,'finne'),(3409,1,'informasjon'),(3410,1,'seminarer'),(3411,1,'arrangementer'),(3412,1,'etter'),(3413,1,'hvert'),(3414,1,'manualer'),(3415,1,'veiledningsvideoer'),(3416,1,'arrangere'),(3417,1,'annet'),(3418,1,'arrangement'),(3419,1,'disse'),(3420,1,'sidene'),(3421,1,'gjøre'),(3422,1,'enkelt'),(3423,1,'både'),(3424,1,'gi'),(3425,1,'ut'),(3426,1,'ta'),(3427,1,'imot'),(3428,1,'påmeldinger'),(3462,1,'kommunen'),(3463,1,'tar'),(3464,1,'mange'),(3465,1,'viktige'),(3466,1,'oppgaver'),(3467,1,'sine'),(3468,1,'innbyggere'),(3469,1,'arbeider'),(3470,1,'være'),(3471,1,'attraktivt'),(3472,1,'godt'),(3473,1,'sted'),(3474,1,'bo'),(3475,1,'arbeide'),(3476,1,'leve'),(3477,1,'trivelig'),(3478,1,'spennende'),(3479,1,'reisemål'),(3480,1,'økt'),(3481,1,'verdiskaping'),(3482,1,'bærekraftig'),(3483,1,'utvikling'),(3484,1,'nøkkelord'),(3485,1,'denne'),(3486,1,'sammenheng'),(3487,1,'vi'),(3488,1,'2'),(3489,1,'skoler'),(3490,1,'fantastisk'),(3491,1,'natur'),(3492,1,'vår'),(3493,1,'unike'),(3494,1,'holme'),(3547,1,'fra'),(3548,1,'troms'),(3549,1,'fylkeskommune'),(3550,1,'eksempel'),(3551,1,'feeds'),(3552,1,'www.tromsfylke.no'),(3553,1,'tromsfylke.no'),(3554,1,'nyheter'),(3555,1,'utdanning'),(3556,1,'miljø'),(3557,1,'samferdsel'),(3558,1,'næring'),(3559,1,'tannhelse'),(3560,1,'politikk'),(3561,1,'kultur'),(3562,1,'samiske'),(3563,1,'engelske'),(3564,1,'flk'),(3565,2,'test'),(3566,1,'bruker'),(3567,1,'britit'),(3568,1,'inger'),(3569,1,'kongsbergregionen.no'),(3574,1,'rolle'),(3575,1,'tester'),(3576,1,'rolletester'),(3577,1,'nospam'),(3614,2,'gunnar'),(3615,2,'velle'),(3616,1,'gv'),(3617,1,'knowit.no'),(3628,1,'test2'),(3629,1,'gunnar.velle'),(3630,1,'gmail.com');
/*!40000 ALTER TABLE `ezsearch_word` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezsection`
--

DROP TABLE IF EXISTS `ezsection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezsection` (
  `id` int(11) NOT NULL auto_increment,
  `locale` varchar(255) default NULL,
  `name` varchar(255) default NULL,
  `navigation_part_identifier` varchar(100) default 'ezcontentnavigationpart',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezsection`
--

LOCK TABLES `ezsection` WRITE;
/*!40000 ALTER TABLE `ezsection` DISABLE KEYS */;
INSERT INTO `ezsection` VALUES (1,'','Standard','ezcontentnavigationpart'),(2,'','Users','ezusernavigationpart'),(3,'','Media','ezmedianavigationpart'),(4,'','Setup','ezsetupnavigationpart'),(5,'','Design','ezvisualnavigationpart');
/*!40000 ALTER TABLE `ezsection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezsession`
--

DROP TABLE IF EXISTS `ezsession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezsession` (
  `data` longtext NOT NULL,
  `expiration_time` int(11) NOT NULL default '0',
  `session_key` varchar(32) NOT NULL default '',
  `user_hash` varchar(32) NOT NULL default '',
  `user_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`session_key`),
  KEY `expiration_time` (`expiration_time`),
  KEY `ezsession_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezsession`
--

LOCK TABLES `ezsession` WRITE;
/*!40000 ALTER TABLE `ezsession` DISABLE KEYS */;
INSERT INTO `ezsession` VALUES ('eZUserInfoCache_Timestamp|i:1381147723;AccessArrayTimestamp|i:1381147723;eZRoleIDList_Timestamp|i:1381147723;eZUserGroupsCache_Timestamp|i:1381147723;eZRoleLimitationValueList_Timestamp|i:1381147723;eZUserDiscountRulesTimestamp|i:1381146806;eZUserDiscountRules10|a:0:{}eZGlobalSection|a:1:{s:2:\"id\";s:1:\"1\";}LastAccessedModifyingURI|s:0:\"\";LastAccessesURI|s:0:\"\";eZUserDiscountRules14|a:0:{}force_logout|i:1;AccessArray|a:26:{s:5:\"class\";a:1:{s:1:\"*\";a:1:{s:1:\"*\";s:1:\"*\";}}s:13:\"collaboration\";a:1:{s:1:\"*\";a:1:{s:1:\"*\";s:1:\"*\";}}s:7:\"content\";a:22:{s:8:\"bookmark\";a:1:{s:1:\"*\";s:1:\"*\";}s:10:\"cleantrash\";a:1:{s:1:\"*\";s:1:\"*\";}s:6:\"create\";a:1:{s:1:\"*\";s:1:\"*\";}s:9:\"dashboard\";a:1:{s:1:\"*\";s:1:\"*\";}s:4:\"diff\";a:1:{s:1:\"*\";s:1:\"*\";}s:4:\"edit\";a:1:{s:1:\"*\";a:1:{s:18:\"StateGroup_ez_lock\";a:1:{i:0;s:1:\"1\";}}}s:4:\"hide\";a:1:{s:1:\"*\";s:1:\"*\";}s:16:\"manage_locations\";a:1:{s:1:\"*\";s:1:\"*\";}s:4:\"move\";a:1:{s:1:\"*\";s:1:\"*\";}s:3:\"pdf\";a:1:{s:1:\"*\";s:1:\"*\";}s:11:\"pendinglist\";a:1:{s:1:\"*\";s:1:\"*\";}s:4:\"read\";a:1:{s:1:\"*\";s:1:\"*\";}s:6:\"remove\";a:1:{s:1:\"*\";a:1:{s:18:\"StateGroup_ez_lock\";a:1:{i:0;s:1:\"1\";}}}s:7:\"restore\";a:1:{s:1:\"*\";s:1:\"*\";}s:18:\"reverserelatedlist\";a:1:{s:1:\"*\";s:1:\"*\";}s:10:\"tipafriend\";a:1:{s:1:\"*\";s:1:\"*\";}s:9:\"translate\";a:1:{s:1:\"*\";s:1:\"*\";}s:12:\"translations\";a:1:{s:1:\"*\";s:1:\"*\";}s:13:\"urltranslator\";a:1:{s:1:\"*\";s:1:\"*\";}s:11:\"versionread\";a:1:{s:1:\"*\";s:1:\"*\";}s:13:\"versionremove\";a:1:{s:1:\"*\";s:1:\"*\";}s:10:\"view_embed\";a:1:{s:1:\"*\";s:1:\"*\";}}s:5:\"error\";a:1:{s:1:\"*\";a:1:{s:1:\"*\";s:1:\"*\";}}s:6:\"ezinfo\";a:1:{s:4:\"read\";a:1:{s:1:\"*\";s:1:\"*\";}}s:13:\"infocollector\";a:1:{s:4:\"read\";a:1:{s:1:\"*\";s:1:\"*\";}}s:6:\"layout\";a:1:{s:1:\"*\";a:1:{s:1:\"*\";s:1:\"*\";}}s:12:\"notification\";a:2:{s:12:\"administrate\";a:1:{s:1:\"*\";s:1:\"*\";}s:3:\"use\";a:1:{s:1:\"*\";s:1:\"*\";}}s:7:\"package\";a:8:{s:6:\"create\";a:1:{s:1:\"*\";s:1:\"*\";}s:4:\"edit\";a:1:{s:1:\"*\";s:1:\"*\";}s:6:\"export\";a:1:{s:1:\"*\";s:1:\"*\";}s:6:\"import\";a:1:{s:1:\"*\";s:1:\"*\";}s:7:\"install\";a:1:{s:1:\"*\";s:1:\"*\";}s:4:\"list\";a:1:{s:1:\"*\";s:1:\"*\";}s:4:\"read\";a:1:{s:1:\"*\";s:1:\"*\";}s:6:\"remove\";a:1:{s:1:\"*\";s:1:\"*\";}}s:3:\"pdf\";a:2:{s:6:\"create\";a:1:{s:1:\"*\";s:1:\"*\";}s:4:\"edit\";a:1:{s:1:\"*\";s:1:\"*\";}}s:4:\"role\";a:1:{s:1:\"*\";a:1:{s:1:\"*\";s:1:\"*\";}}s:3:\"rss\";a:2:{s:4:\"edit\";a:1:{s:1:\"*\";s:1:\"*\";}s:4:\"feed\";a:1:{s:1:\"*\";s:1:\"*\";}}s:6:\"search\";a:1:{s:1:\"*\";a:1:{s:1:\"*\";s:1:\"*\";}}s:7:\"section\";a:3:{s:6:\"assign\";a:1:{s:1:\"*\";s:1:\"*\";}s:4:\"edit\";a:1:{s:1:\"*\";s:1:\"*\";}s:4:\"view\";a:1:{s:1:\"*\";s:1:\"*\";}}s:8:\"settings\";a:1:{s:1:\"*\";a:1:{s:1:\"*\";s:1:\"*\";}}s:5:\"setup\";a:5:{s:12:\"administrate\";a:1:{s:1:\"*\";s:1:\"*\";}s:7:\"install\";a:1:{s:1:\"*\";s:1:\"*\";}s:11:\"managecache\";a:1:{s:1:\"*\";s:1:\"*\";}s:5:\"setup\";a:1:{s:1:\"*\";s:1:\"*\";}s:11:\"system_info\";a:1:{s:1:\"*\";s:1:\"*\";}}s:4:\"shop\";a:5:{s:12:\"administrate\";a:1:{s:1:\"*\";s:1:\"*\";}s:3:\"buy\";a:1:{s:1:\"*\";s:1:\"*\";}s:11:\"edit_status\";a:1:{s:1:\"*\";s:1:\"*\";}s:9:\"setstatus\";a:1:{s:1:\"*\";s:1:\"*\";}s:5:\"setup\";a:1:{s:1:\"*\";s:1:\"*\";}}s:5:\"state\";a:2:{s:12:\"administrate\";a:1:{s:1:\"*\";s:1:\"*\";}s:6:\"assign\";a:1:{s:1:\"*\";s:1:\"*\";}}s:7:\"trigger\";a:1:{s:1:\"*\";a:1:{s:1:\"*\";s:1:\"*\";}}s:3:\"url\";a:1:{s:1:\"*\";a:1:{s:1:\"*\";s:1:\"*\";}}s:4:\"user\";a:5:{s:5:\"login\";a:1:{s:1:\"*\";s:1:\"*\";}s:8:\"password\";a:1:{s:1:\"*\";s:1:\"*\";}s:11:\"preferences\";a:1:{s:1:\"*\";s:1:\"*\";}s:8:\"register\";a:1:{s:1:\"*\";s:1:\"*\";}s:8:\"selfedit\";a:1:{s:1:\"*\";s:1:\"*\";}}s:6:\"visual\";a:1:{s:1:\"*\";a:1:{s:1:\"*\";s:1:\"*\";}}s:8:\"workflow\";a:1:{s:1:\"*\";a:1:{s:1:\"*\";s:1:\"*\";}}s:14:\"switchlanguage\";a:1:{s:1:\"*\";a:1:{s:1:\"*\";s:1:\"*\";}}s:8:\"ezjscore\";a:2:{s:4:\"call\";a:1:{s:1:\"*\";s:1:\"*\";}s:3:\"run\";a:1:{s:1:\"*\";s:1:\"*\";}}s:4:\"ezoe\";a:5:{s:6:\"browse\";a:1:{s:1:\"*\";s:1:\"*\";}s:14:\"disable_editor\";a:1:{s:1:\"*\";s:1:\"*\";}s:6:\"editor\";a:1:{s:1:\"*\";s:1:\"*\";}s:9:\"relations\";a:1:{s:1:\"*\";s:1:\"*\";}s:6:\"search\";a:1:{s:1:\"*\";s:1:\"*\";}}}eZUserLoggedInID|s:2:\"14\";eZUserInfoCache|a:1:{i:14;a:5:{s:16:\"contentobject_id\";s:2:\"14\";s:5:\"login\";s:5:\"admin\";s:5:\"email\";s:12:\"nospam@ez.no\";s:13:\"password_hash\";s:32:\"9f4c7295ca9cb322a33260c963c93269\";s:18:\"password_hash_type\";s:1:\"2\";}}eZUserGroupsCache|a:2:{i:0;s:2:\"12\";i:1;s:1:\"4\";}eZRoleIDList|a:1:{i:0;s:1:\"2\";}eZRoleLimitationValueList|a:1:{i:0;s:0:\"\";}eZPreferences|a:19:{s:21:\"admin_clearcache_type\";s:3:\"All\";s:23:\"admin_navigation_states\";s:1:\"1\";s:24:\"admin_navigation_content\";s:1:\"1\";s:24:\"admin_navigation_details\";s:1:\"0\";s:26:\"admin_navigation_locations\";s:1:\"0\";s:26:\"admin_navigation_relations\";s:1:\"0\";s:21:\"admin_left_menu_width\";s:6:\"medium\";s:22:\"admin_navigation_roles\";s:1:\"1\";s:25:\"admin_navigation_policies\";s:1:\"0\";s:14:\"admin_treemenu\";s:1:\"1\";s:21:\"admin_clearcache_menu\";s:1:\"1\";s:16:\"admin_list_limit\";s:1:\"3\";s:24:\"admin_quicksettings_menu\";s:1:\"1\";s:30:\"admin_quicksettings_siteaccess\";s:7:\"fkp_nob\";s:31:\"admin_navigation_class_temlates\";s:1:\"1\";s:21:\"admin_right_menu_show\";s:1:\"1\";s:23:\"admin_children_viewmode\";b:0;s:20:\"admin_left_menu_size\";b:0;s:11:\"admin_theme\";b:0;}',1381164363,'67smdvoggeguff3rea5jmllc50','a2e4822a98337283e39f7b60acf85ec9',14);
/*!40000 ALTER TABLE `ezsession` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezsite_data`
--

DROP TABLE IF EXISTS `ezsite_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezsite_data` (
  `name` varchar(60) NOT NULL default '',
  `value` longtext NOT NULL,
  PRIMARY KEY  (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezsite_data`
--

LOCK TABLES `ezsite_data` WRITE;
/*!40000 ALTER TABLE `ezsite_data` DISABLE KEYS */;
INSERT INTO `ezsite_data` VALUES ('ezpublish-release','1'),('ezpublish-version','4.3.0');
/*!40000 ALTER TABLE `ezsite_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezsubtree_notification_rule`
--

DROP TABLE IF EXISTS `ezsubtree_notification_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezsubtree_notification_rule` (
  `id` int(11) NOT NULL auto_increment,
  `node_id` int(11) NOT NULL default '0',
  `use_digest` int(11) default '0',
  `user_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `ezsubtree_notification_rule_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezsubtree_notification_rule`
--

LOCK TABLES `ezsubtree_notification_rule` WRITE;
/*!40000 ALTER TABLE `ezsubtree_notification_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezsubtree_notification_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eztipafriend_counter`
--

DROP TABLE IF EXISTS `eztipafriend_counter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eztipafriend_counter` (
  `count` int(11) NOT NULL default '0',
  `node_id` int(11) NOT NULL default '0',
  `requested` int(11) NOT NULL default '0',
  PRIMARY KEY  (`node_id`,`requested`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eztipafriend_counter`
--

LOCK TABLES `eztipafriend_counter` WRITE;
/*!40000 ALTER TABLE `eztipafriend_counter` DISABLE KEYS */;
/*!40000 ALTER TABLE `eztipafriend_counter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eztipafriend_request`
--

DROP TABLE IF EXISTS `eztipafriend_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eztipafriend_request` (
  `created` int(11) NOT NULL default '0',
  `email_receiver` varchar(100) NOT NULL default '',
  KEY `eztipafriend_request_created` (`created`),
  KEY `eztipafriend_request_email_rec` (`email_receiver`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eztipafriend_request`
--

LOCK TABLES `eztipafriend_request` WRITE;
/*!40000 ALTER TABLE `eztipafriend_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `eztipafriend_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eztrigger`
--

DROP TABLE IF EXISTS `eztrigger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eztrigger` (
  `connect_type` char(1) NOT NULL default '',
  `function_name` varchar(200) NOT NULL default '',
  `id` int(11) NOT NULL auto_increment,
  `module_name` varchar(200) NOT NULL default '',
  `name` varchar(255) default NULL,
  `workflow_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `eztrigger_def_id` (`module_name`(50),`function_name`(50),`connect_type`),
  KEY `eztrigger_fetch` (`name`(25),`module_name`(50),`function_name`(50))
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eztrigger`
--

LOCK TABLES `eztrigger` WRITE;
/*!40000 ALTER TABLE `eztrigger` DISABLE KEYS */;
/*!40000 ALTER TABLE `eztrigger` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezurl`
--

DROP TABLE IF EXISTS `ezurl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezurl` (
  `created` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `is_valid` int(11) NOT NULL default '1',
  `last_checked` int(11) NOT NULL default '0',
  `modified` int(11) NOT NULL default '0',
  `original_url_md5` varchar(32) NOT NULL default '',
  `url` longtext,
  PRIMARY KEY  (`id`),
  KEY `ezurl_url` (`url`(255))
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezurl`
--

LOCK TABLES `ezurl` WRITE;
/*!40000 ALTER TABLE `ezurl` DISABLE KEYS */;
INSERT INTO `ezurl` VALUES (1082368571,1,1,0,1082368571,'1c4c1d746fbd23350bcfa5e978841f23','http://ez.no/ez_publish/documentation'),(1082368571,4,1,0,1082368571,'41caff1d7f5ad51e70ad46abbcf28fb7','http://ez.no/community/forum'),(1082368571,5,1,0,1082368571,'7f0bed2dad9e69cc2c573d0868fe1a00','http://ez.no/services/support'),(1082368571,6,1,0,1082368571,'90c2b2894d43ee98fd5df8452dbfbfbd','http://ez.no/services/consulting'),(1082368571,7,1,0,1082368571,'23b22a1f1e566e15dead54b6d1b42706','http://ez.no/services/training'),(1283758772,9,1,0,1283758772,'5858d21c97ee3b5089c362546a3eefa4','http://ez.no/'),(1283762130,11,1,0,1283762130,'97fac2eb7a592a682293bca50379269d','http://www.tromsfylke.no'),(1283856634,12,1,0,1283856634,'8f9a5a38da1a93d21e032ae95ab158e2','https://projects.knowit.no/pages/viewpage.action?pageId=16319556'),(1283856634,13,1,0,1283856634,'7c2cc5445675ec85c52807cefebfd301','https://projects.knowit.no/pages/viewpage.action?pageId=15446640'),(1283856634,14,1,0,1283856634,'b657534f8b11e4429a14aac55766ddc4','https://projects.knowit.no/pages/viewpage.action?pageId=16318817'),(1283857144,15,1,0,1283857144,'602e8d7f378808591fa0d3c3b35305b7','https://projects.knowit.no/pages/viewpage.action?pageId=15446635'),(1283857144,16,1,0,1283857144,'aed64d8ee76a633edfa0024940ee15d5','https://projects.knowit.no/display/FRIKOMPORT/Hvordan+administrere+filer+-+legge+ved+dokumentasjon+til+kurset'),(1283857145,17,1,0,1283857145,'5526d5a4afe45066ef9fa4fff9723f9c','https://projects.knowit.no/display/FRIKOMPORT/Hvordan+endre+opplysninger+om+kurset'),(1283857145,18,1,0,1283857145,'d66b6fdf239950d0a8ece0587e16cd61','https://projects.knowit.no/pages/viewpage.action?pageId=15928064'),(1283857145,19,1,0,1283857145,'782420c4700a4d85a4b936310b1b2081','https://projects.knowit.no/display/FRIKOMPORT/Hvordan+avlyse+et+kurs'),(1283857145,20,1,0,1283857145,'fee55ddbfaece0814aa5aec5a64900c5','https://projects.knowit.no/display/FRIKOMPORT/Hvordan+slette+et+kurs'),(1283858368,23,1,0,1283858368,'66a189a1af2183e25ca591d5bf8456c4','http://www.tromsfylke.no/LinkClick.aspx?fileticket=6q2j2YWPgfU%3d&tabid=56'),(1283860427,24,1,0,1283860427,'58400d90e2967bc13aec0932059d489e','http://www.tromsfylke.no/Forside/tabid/38/Default.aspx'),(1283863581,25,1,0,1283863581,'06b336582aaaa6d5855fe9c56e207e9a','http://www.tromsfylke.no/Tjenester/tabid/55/Default.aspx'),(1283863581,26,1,0,1283863581,'cc6fffa88c1e95cc4a28e416251cb568','http://www.tromsfylke.no/Politikk/tabid/145/Default.aspx'),(1283863581,27,1,0,1283863581,'a9bc379585461523db487d933756ed6f','http://www.tromsfylke.no/Omfylkeskommunen/tabid/56/Default.aspx'),(1283863581,28,1,0,1283863581,'e146d7e257a260ec541459c93e2e8b84','http://www.tromsfylke.no/OmTroms/tabid/57/Default.aspx'),(1283864314,29,1,0,1283864314,'3f5416880a82b26f90695b178b7f7a22','http://www.tromsfylke.no/'),(1283930628,30,1,0,1283930628,'29f6f9b424b83c2665d6c3428397a7f0','http://www.tromsfylke.no/DesktopModules/DNNArticle/DNNArticleRSS.aspx?portalid=0&moduleid=454&tabid=135&categoryid=13&cp=False&uid=-1'),(1283930628,31,1,0,1283930628,'e1e56a1ea83534c4dad61bfc4d96c97f','http://www.tromsfylke.no/DesktopModules/DNNArticle/DNNArticleRSS.aspx?portalid=0&moduleid=454&tabid=135&categoryid=15&cp=False&uid=-1'),(1283930628,32,1,0,1283930628,'5ec7737347f11a499335f920cf3e6c21','http://www.tromsfylke.no/DesktopModules/DNNArticle/DNNArticleRSS.aspx?portalid=0&moduleid=454&tabid=135&categoryid=16&cp=False&uid=-1'),(1283931067,33,1,0,1283931067,'c9234e03505345ee50720aa8894c25d2','http://www.tromsfylke.no/DesktopModules/DNNArticle/DNNArticleRSS.aspx?portalid=0&moduleid=454&tabid=135&categoryid=17&cp=False&uid=-1'),(1283931067,34,1,0,1283931067,'07d96fc24548c8a119f6c23bb44821a9','http://www.tromsfylke.no/DesktopModules/DNNArticle/DNNArticleRSS.aspx?portalid=0&moduleid=454&tabid=135&categoryid=18&cp=False&uid=-1'),(1283931067,35,1,0,1283931067,'aed19ad3db8377ec5003f5749343a9fb','http://www.tromsfylke.no/DesktopModules/DNNArticle/DNNArticleRSS.aspx?portalid=0&moduleid=454&tabid=135&categoryid=20&cp=False&uid=-1'),(1283931067,36,1,0,1283931067,'dec58f26134971beb038a3862262d578','http://www.tromsfylke.no/DesktopModules/DNNArticle/DNNArticleRSS.aspx?portalid=0&moduleid=454&tabid=135&categoryid=21&cp=False&uid=-1'),(1283931067,37,1,0,1283931067,'10a94eeef6ddc06b11837a93244d63f3','http://www.tromsfylke.no/DesktopModules/DNNArticle/DNNArticleRSS.aspx?portalid=0&moduleid=454&tabid=135&categoryid=22&cp=False&uid=-1'),(1283931067,38,1,0,1283931067,'6b59b1112ecc25546175c8f3ec0f6e14','http://www.tromsfylke.no/DesktopModules/DNNArticle/DNNArticleRSS.aspx?portalid=0&moduleid=454&tabid=135&categoryid=23&cp=False&uid=-1'),(1283934097,39,1,0,1283934097,'c4e253e1606e9af006e7b1e5718e754d','http://www.facebook.com/tromsfylke'),(1283934440,40,1,0,1283934440,'66c0cbb57f7d6b0668d516d6cd77992f','http://wwww.facebook.com/tromsfylke'),(1283934440,41,1,0,1283934440,'35bc4590c39d4ecad6c5f3caf72bff6d','http://www.flickr.com/photos/tromsfylke/'),(1283937054,42,1,0,1283937054,'032296b6e88007bd4c02482b4cafabab','http://www.facebook.com/tromsfylke/'),(1283937594,43,1,0,1283937594,'f632fb198819b1522ed2e01dad6515d5','http://no.wikipedia.org/wiki/Troms_fylke');
/*!40000 ALTER TABLE `ezurl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezurl_object_link`
--

DROP TABLE IF EXISTS `ezurl_object_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezurl_object_link` (
  `contentobject_attribute_id` int(11) NOT NULL default '0',
  `contentobject_attribute_version` int(11) NOT NULL default '0',
  `url_id` int(11) NOT NULL default '0',
  KEY `ezurl_ol_coa_id` (`contentobject_attribute_id`),
  KEY `ezurl_ol_coa_version` (`contentobject_attribute_version`),
  KEY `ezurl_ol_url_id` (`url_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezurl_object_link`
--

LOCK TABLES `ezurl_object_link` WRITE;
/*!40000 ALTER TABLE `ezurl_object_link` DISABLE KEYS */;
INSERT INTO `ezurl_object_link` VALUES (308,2,15),(308,2,16),(308,2,17),(308,2,18),(308,2,19),(308,2,20),(308,3,12),(308,3,13),(308,3,14),(308,3,15),(308,3,16),(308,3,17),(308,3,18),(308,3,19),(308,3,20),(308,4,12),(308,4,13),(308,4,14),(308,4,15),(308,4,16),(308,4,17),(308,4,18),(308,4,19),(308,4,20),(290,15,24),(290,15,25),(290,15,26),(290,15,27),(290,15,28),(291,15,23),(290,16,24),(290,16,25),(290,16,26),(290,16,27),(290,16,28),(291,16,23),(270,19,29),(271,19,30),(271,19,31),(271,19,32),(271,19,33),(271,19,34),(271,19,35),(271,19,36),(271,19,37),(271,19,38),(270,20,29),(270,20,39),(271,20,30),(271,20,31),(271,20,32),(271,20,33),(271,20,34),(271,20,35),(271,20,36),(271,20,37),(271,20,38),(270,21,29),(271,21,30),(271,21,31),(271,21,32),(271,21,33),(271,21,34),(271,21,35),(271,21,36),(271,21,37),(271,21,38),(270,22,29),(270,22,40),(270,22,41),(271,22,30),(271,22,31),(271,22,32),(271,22,33),(271,22,34),(271,22,35),(271,22,36),(271,22,37),(271,22,38),(270,23,11),(271,23,30),(271,23,31),(271,23,32),(271,23,33),(271,23,34),(271,23,35),(271,23,36),(271,23,37),(271,23,38),(353,8,40),(353,12,39),(353,12,41),(353,13,41),(353,13,42),(353,14,42),(353,14,41),(353,14,43),(353,15,42),(353,15,41),(353,15,43),(353,16,42),(353,16,41),(353,16,43),(270,24,29),(271,24,30),(271,24,31),(271,24,32),(271,24,33),(271,24,34),(271,24,35),(271,24,36),(271,24,37),(271,24,38),(104,6,1),(104,6,4),(104,6,5),(104,6,6),(104,6,7),(104,6,9),(308,5,12),(308,5,13),(308,5,14),(308,5,15),(308,5,16),(308,5,17),(308,5,18),(308,5,19),(308,5,20),(353,17,42),(353,17,41),(290,17,24),(290,17,25),(290,17,26),(290,17,27),(290,17,28),(290,18,24),(290,18,25),(290,18,26),(290,18,27),(290,18,28),(270,25,29),(271,25,30),(271,25,31),(271,25,32),(271,25,33),(271,25,34),(271,25,35),(271,25,36),(271,25,37),(271,25,38),(270,26,29),(271,26,30),(271,26,31),(271,26,32),(271,26,33),(271,26,34),(271,26,35),(271,26,36),(271,26,37),(271,26,38),(270,27,29),(271,27,30),(271,27,31),(271,27,32),(271,27,33),(271,27,34),(271,27,35),(271,27,36),(271,27,37),(271,27,38),(270,28,29),(271,28,30),(271,28,31),(271,28,32),(271,28,33),(271,28,34),(271,28,35),(271,28,36),(271,28,37),(271,28,38);
/*!40000 ALTER TABLE `ezurl_object_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezurlalias`
--

DROP TABLE IF EXISTS `ezurlalias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezurlalias` (
  `destination_url` longtext NOT NULL,
  `forward_to_id` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `is_imported` int(11) NOT NULL default '0',
  `is_internal` int(11) NOT NULL default '1',
  `is_wildcard` int(11) NOT NULL default '0',
  `source_md5` varchar(32) default NULL,
  `source_url` longtext NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `ezurlalias_desturl` (`destination_url`(200)),
  KEY `ezurlalias_forward_to_id` (`forward_to_id`),
  KEY `ezurlalias_imp_wcard_fwd` (`is_imported`,`is_wildcard`,`forward_to_id`),
  KEY `ezurlalias_source_md5` (`source_md5`),
  KEY `ezurlalias_source_url` (`source_url`(255)),
  KEY `ezurlalias_wcard_fwd` (`is_wildcard`,`forward_to_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezurlalias`
--

LOCK TABLES `ezurlalias` WRITE;
/*!40000 ALTER TABLE `ezurlalias` DISABLE KEYS */;
INSERT INTO `ezurlalias` VALUES ('content/view/full/2',0,12,1,1,0,'d41d8cd98f00b204e9800998ecf8427e',''),('content/view/full/5',0,13,1,1,0,'9bc65c2abec141778ffaa729489f3e87','users'),('content/view/full/12',0,15,1,1,0,'02d4e844e3a660857a3f81585995ffe1','users/guest_accounts'),('content/view/full/13',0,16,1,1,0,'1b1d79c16700fd6003ea7be233e754ba','users/administrator_users'),('content/view/full/14',0,17,1,1,0,'0bb9dd665c96bbc1cf36b79180786dea','users/editors'),('content/view/full/15',0,18,1,1,0,'f1305ac5f327a19b451d82719e0c3f5d','users/administrator_users/administrator_user'),('content/view/full/43',0,20,1,1,0,'62933a2951ef01f4eafd9bdf4d3cd2f0','media'),('content/view/full/44',0,21,1,1,0,'3ae1aac958e1c82013689d917d34967a','users/anonymous_users'),('content/view/full/45',0,22,1,1,0,'aad93975f09371695ba08292fd9698db','users/anonymous_users/anonymous_user'),('content/view/full/48',0,25,1,1,0,'a0f848942ce863cf53c0fa6cc684007d','setup'),('content/view/full/50',0,27,1,1,0,'c60212835de76414f9bfd21eecb8f221','foo_bar_folder/images/vbanner'),('content/view/full/51',0,28,1,1,0,'38985339d4a5aadfc41ab292b4527046','media/images'),('content/view/full/52',0,29,1,1,0,'ad5a8c6f6aac3b1b9df267fe22e7aef6','media/files'),('content/view/full/53',0,30,1,1,0,'562a0ac498571c6c3529173184a2657c','media/multimedia'),('content/view/full/54',0,31,1,1,0,'e501fe6c81ed14a5af2b322d248102d8','setup/common_ini_settings'),('content/view/full/56',0,32,1,1,0,'2dd3db5dc7122ea5f3ee539bb18fe97d','design/ez_publish'),('content/view/full/58',0,33,1,1,0,'31c13f47ad87dd7baa2d558a91e0fbb9','design');
/*!40000 ALTER TABLE `ezurlalias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezurlalias_ml`
--

DROP TABLE IF EXISTS `ezurlalias_ml`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezurlalias_ml` (
  `action` longtext NOT NULL,
  `action_type` varchar(32) NOT NULL default '',
  `alias_redirects` int(11) NOT NULL default '1',
  `id` int(11) NOT NULL default '0',
  `is_alias` int(11) NOT NULL default '0',
  `is_original` int(11) NOT NULL default '0',
  `lang_mask` int(11) NOT NULL default '0',
  `link` int(11) NOT NULL default '0',
  `parent` int(11) NOT NULL default '0',
  `text` longtext NOT NULL,
  `text_md5` varchar(32) NOT NULL default '',
  PRIMARY KEY  (`parent`,`text_md5`),
  KEY `ezurlalias_ml_act_org` (`action`(32),`is_original`),
  KEY `ezurlalias_ml_action` (`action`(32),`id`,`link`),
  KEY `ezurlalias_ml_actt` (`action_type`),
  KEY `ezurlalias_ml_actt_org_al` (`action_type`,`is_original`,`is_alias`),
  KEY `ezurlalias_ml_id` (`id`),
  KEY `ezurlalias_ml_par_act_id_lnk` (`parent`,`action`(32),`id`,`link`),
  KEY `ezurlalias_ml_par_lnk_txt` (`parent`,`link`,`text`(32)),
  KEY `ezurlalias_ml_par_txt` (`parent`,`text`(32)),
  KEY `ezurlalias_ml_text` (`text`(32),`id`,`link`),
  KEY `ezurlalias_ml_text_lang` (`text`(32),`lang_mask`,`parent`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezurlalias_ml`
--

LOCK TABLES `ezurlalias_ml` WRITE;
/*!40000 ALTER TABLE `ezurlalias_ml` DISABLE KEYS */;
INSERT INTO `ezurlalias_ml` VALUES ('nop:','nop',1,14,0,0,1,14,0,'foo_bar_folder','0288b6883046492fa92e4a84eb67acc9'),('eznode:58','eznode',1,25,0,1,3,25,0,'Design','31c13f47ad87dd7baa2d558a91e0fbb9'),('eznode:71','eznode',1,68,0,0,2,51,0,'RSS-feed-fre-Tromsfylke','35928436802a20776ba9ce97f32835d8'),('eznode:71','eznode',1,51,0,1,2,51,0,'RSS-feed','3dc15fb0a4e72897ec8aeb6ee005767c'),('eznode:48','eznode',1,13,0,1,3,13,0,'Setup2','475e97c0146bfb1c490339546d9e72ee'),('nop:','nop',1,17,0,0,1,17,0,'media2','50e2736330de124f6edea9b008556fe6'),('eznode:75','eznode',1,99,0,0,3,56,0,'Troms-fylkeskommune','557228da5c3cbac844680e420846d91a'),('eznode:43','eznode',1,9,0,1,3,9,0,'Media','62933a2951ef01f4eafd9bdf4d3cd2f0'),('eznode:80','eznode',1,62,0,1,3,62,0,'Help','657f8b8da628ef83cf69101b6817150a'),('nop:','nop',1,21,0,0,1,21,0,'setup3','732cefcf28bf4547540609fb1a786a30'),('nop:','nop',1,3,0,0,1,3,0,'users2','86425c35a33507d479f71ade53a669aa'),('eznode:75','eznode',1,56,0,1,3,56,0,'Storholmen','95bbd5de5aa1242bfa6b0e9211b25528'),('eznode:71','eznode',1,67,0,0,2,51,0,'New-article-test','98009d1117caf2117528e7436b91d4e7'),('eznode:5','eznode',1,36,0,0,3,2,0,'Users','9bc65c2abec141778ffaa729489f3e87'),('eznode:80','eznode',1,66,0,0,3,62,0,'Help2','9c54453ae18900f1fe1e454e9bb696ee'),('eznode:75','eznode',1,57,0,0,3,56,0,'Troms-fylkeskommune2','a4c5f24be3a7a9402b64abfce9fe1ae2'),('eznode:5','eznode',1,2,0,1,3,2,0,'Users-group','b2ed519775cc8a2b1e4a104c341dd64c'),('eznode:2','eznode',1,1,0,1,3,1,0,'','d41d8cd98f00b204e9800998ecf8427e'),('eznode:5','eznode',1,37,0,0,3,2,0,'khalil','ffadd3bb28d3086971fc23cad4d8eab1'),('eznode:14','eznode',1,6,0,1,3,6,2,'Opplaeringsansvarlige','5dd17201769a34c6c87ef77f3f615e4b'),('eznode:67','eznode',1,47,0,1,3,47,2,'Kursansvarlige','8b3f8e1ef60b3b7d18f2c0bf37f9630e'),('eznode:14','eznode',1,46,0,0,3,6,2,'Editors','a147e136bfa717592f2bd70bd4b53b17'),('eznode:68','eznode',1,48,0,1,3,48,2,'Ansatte','a6db1f314f4ac4fcdd7c91c1a670200a'),('eznode:44','eznode',1,10,0,1,3,10,2,'Anonymous-Users','c2803c3fa1b0b5423237b4e018cae755'),('eznode:12','eznode',1,4,0,1,3,4,2,'Guest-accounts','e57843d836e3af8ab611fde9e2139b3a'),('eznode:13','eznode',1,5,0,1,3,5,2,'Administrator-users','f89fad7f8a3abc8c09e1deb46a420007'),('nop:','nop',1,11,0,0,1,11,3,'anonymous_users2','505e93077a6dde9034ad97a14ab022b1'),('eznode:12','eznode',1,26,0,0,1,4,3,'guest_accounts','70bb992820e73638731aa8de79b3329e'),('eznode:14','eznode',1,29,0,0,0,6,3,'editors','a147e136bfa717592f2bd70bd4b53b17'),('nop:','nop',1,7,0,0,1,7,3,'administrator_users2','a7da338c20bf65f9f789c87296379c2a'),('eznode:13','eznode',1,27,0,0,1,5,3,'administrator_users','aeb8609aa933b0899aa012c71139c58c'),('eznode:44','eznode',1,30,0,0,1,10,3,'anonymous_users','e9e5ad0c05ee1a43715572e5cc545926'),('eznode:15','eznode',1,8,0,1,3,8,5,'Administrator-User','5a9d7b0ec93173ef4fedee023209cb61'),('eznode:109','eznode',1,102,0,1,3,102,6,'Rolle-Tester','c39f54148db52ddcd1de06ee3adaef50'),('eznode:15','eznode',1,28,0,0,1,8,7,'administrator_user','a3cca2de936df1e2f805710399989971'),('eznode:53','eznode',1,20,0,1,3,20,9,'Multimedia','2e5bc8831f7ae6a29530e7f1bbf2de9c'),('eznode:52','eznode',1,19,0,1,3,19,9,'Files','45b963397aa40d4a0063e0d85e4fe7a1'),('eznode:51','eznode',1,18,0,1,3,18,9,'Images','59b514174bffe4ae402b3d63aad79fe0'),('eznode:45','eznode',1,12,0,1,3,12,10,'Anonymous-User','ccb62ebca03a31272430bc414bd5cd5b'),('eznode:45','eznode',1,31,0,0,1,12,11,'anonymous_user','c593ec85293ecb0e02d50d4c5c6c20eb'),('eznode:54','eznode',1,22,0,1,2,22,13,'Common-INI-settings','4434993ac013ae4d54bb1f51034d6401'),('nop:','nop',1,15,0,0,1,15,14,'images','59b514174bffe4ae402b3d63aad79fe0'),('eznode:50','eznode',1,16,0,1,2,16,15,'vbanner','c54e2d1b93642e280bdc5d99eab2827d'),('eznode:53','eznode',1,34,0,0,1,20,17,'multimedia','2e5bc8831f7ae6a29530e7f1bbf2de9c'),('eznode:52','eznode',1,33,0,0,1,19,17,'files','45b963397aa40d4a0063e0d85e4fe7a1'),('eznode:51','eznode',1,32,0,0,1,18,17,'images','59b514174bffe4ae402b3d63aad79fe0'),('eznode:84','eznode',1,71,0,1,3,71,18,'FylkeshusetFront4','11ec4e05931c2fbe2462c2fad6aaf64e'),('eznode:97','eznode',1,88,0,1,3,88,18,'wikipedia','12672e79b01e9ca7018105efb0ef871c'),('eznode:82','eznode',1,69,0,1,3,69,18,'128px-Feed-icon_svg','707e4f6fcc4b2a8b348b30ffd9fcb2f5'),('eznode:77','eznode',1,59,0,1,3,59,18,'FylkeshusetFront','7c136ee3f7db8c808643d22cb0d7c490'),('eznode:78','eznode',1,60,0,1,3,60,18,'FylkeshusetFront2','8aae8811238c4fc6589e3b83a09a1887'),('eznode:93','eznode',1,85,0,1,3,85,18,'Foelg-oss-paa-Facebook','9569c1b490a2022082740897ae0d408c'),('eznode:90','eznode',1,79,0,1,3,79,18,'Foelg-oss-paa-Flickr','9c7863598bfa96c5d97ad73311b2bd5d'),('eznode:96','eznode',1,83,0,1,3,83,18,'flickr','e1f53f2df5c7a2bdc9fb8a57291044c9'),('eznode:83','eznode',1,70,0,1,3,70,18,'FylkeshusetFront3','e27db493983e6678fc7427c2427dd1d7'),('eznode:54','eznode',1,35,0,0,1,22,21,'common_ini_settings','e59d6834e86cee752ed841f9cd8d5baf'),('eznode:56','eznode',1,24,0,1,2,24,25,'eZ-publish','10e4c3cb527fb9963258469986c16240'),('eznode:105','eznode',1,97,0,1,3,97,47,'Kurs-Testbruker','0edab65fd19e0e1d7d346a9178c3fed9'),('eznode:125','eznode',1,123,0,1,3,123,47,'Test-Test','1af1a690b81b7cabc2f5fde0b00d15ad'),('eznode:119','eznode',1,112,0,1,3,112,47,'Gunnar-Velle','4d0a5253f419ad75e2d885582f0ddec2'),('eznode:106','eznode',1,98,0,1,3,98,47,'Kurt-Kursansvarlig','abd9fa00874c5f2f34d5a7e80df03ad1'),('eznode:126','eznode',1,124,0,1,3,124,48,'Gunnar-Velle','4d0a5253f419ad75e2d885582f0ddec2'),('eznode:107','eznode',1,100,0,1,3,100,48,'Test-Bruker','556ad26772965da9310115b4d3138879'),('eznode:86','eznode',1,73,0,1,3,73,51,'RSS-Feed','3dc15fb0a4e72897ec8aeb6ee005767c'),('eznode:88','eznode',1,77,0,1,3,77,51,'128px-Feed-icon','5fc31d891e4cf3d025ee279422c3783a'),('eznode:86','eznode',1,74,0,0,3,73,51,'128px-Feed-icon_svg','707e4f6fcc4b2a8b348b30ffd9fcb2f5');
/*!40000 ALTER TABLE `ezurlalias_ml` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezurlalias_ml_incr`
--

DROP TABLE IF EXISTS `ezurlalias_ml_incr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezurlalias_ml_incr` (
  `id` int(11) NOT NULL auto_increment,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezurlalias_ml_incr`
--

LOCK TABLES `ezurlalias_ml_incr` WRITE;
/*!40000 ALTER TABLE `ezurlalias_ml_incr` DISABLE KEYS */;
INSERT INTO `ezurlalias_ml_incr` VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10),(11),(12),(13),(14),(15),(16),(17),(18),(19),(20),(21),(22),(24),(25),(26),(27),(28),(29),(30),(31),(32),(33),(34),(35),(36),(37),(38),(39),(40),(41),(42),(43),(44),(45),(46),(47),(48),(49),(50),(51),(52),(53),(54),(55),(56),(57),(58),(59),(60),(61),(62),(63),(64),(65),(66),(67),(68),(69),(70),(71),(72),(73),(74),(75),(76),(77),(78),(79),(80),(81),(82),(83),(84),(85),(86),(87),(88),(89),(90),(91),(92),(93),(94),(95),(96),(97),(98),(99),(100),(101),(102),(103),(104),(105),(106),(107),(108),(109),(110),(111),(112),(113),(114),(115),(116),(117),(118),(119),(120),(121),(122),(123),(124);
/*!40000 ALTER TABLE `ezurlalias_ml_incr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezurlwildcard`
--

DROP TABLE IF EXISTS `ezurlwildcard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezurlwildcard` (
  `destination_url` longtext NOT NULL,
  `id` int(11) NOT NULL auto_increment,
  `source_url` longtext NOT NULL,
  `type` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezurlwildcard`
--

LOCK TABLES `ezurlwildcard` WRITE;
/*!40000 ALTER TABLE `ezurlwildcard` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezurlwildcard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezuser`
--

DROP TABLE IF EXISTS `ezuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezuser` (
  `contentobject_id` int(11) NOT NULL default '0',
  `email` varchar(150) NOT NULL default '',
  `login` varchar(150) NOT NULL default '',
  `password_hash` varchar(50) default NULL,
  `password_hash_type` int(11) NOT NULL default '1',
  PRIMARY KEY  (`contentobject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezuser`
--

LOCK TABLES `ezuser` WRITE;
/*!40000 ALTER TABLE `ezuser` DISABLE KEYS */;
INSERT INTO `ezuser` VALUES (10,'frikomport@gmail.com','anonymous','4e6f6184135228ccd45f8233d72a0363',2),(14,'nospam@ez.no','admin','9f4c7295ca9cb322a33260c963c93269',2),(118,'nospam@frikomport.no','kurs','bafe9757a0c22572ec3cff6466e1f6d0',2),(119,'nospam_kurt@frikomport.no','kurt','219c47110c914a92106703f1b39d15ea',2),(120,'britit-inger@kongsbergregionen.no','testbruker','c764daa909b5068ddb0b12034f8c110e',2),(122,'rolletester@frikomport.no','rolletester','d767b0acda8baebbc1fddc0b74950e2d',2),(134,'gv@knowit.no','gv','',0),(140,'test@frikomport.no','test2','c7a8bd25460ffbd5de8b5f81102caf48',2),(141,'gunnar.velle@gmail.com','gunnar','3aa4ba145a744785c2293540e2776611',2);
/*!40000 ALTER TABLE `ezuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezuser_accountkey`
--

DROP TABLE IF EXISTS `ezuser_accountkey`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezuser_accountkey` (
  `hash_key` varchar(32) NOT NULL default '',
  `id` int(11) NOT NULL auto_increment,
  `time` int(11) NOT NULL default '0',
  `user_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `hash_key` (`hash_key`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezuser_accountkey`
--

LOCK TABLES `ezuser_accountkey` WRITE;
/*!40000 ALTER TABLE `ezuser_accountkey` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezuser_accountkey` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezuser_discountrule`
--

DROP TABLE IF EXISTS `ezuser_discountrule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezuser_discountrule` (
  `contentobject_id` int(11) default NULL,
  `discountrule_id` int(11) default NULL,
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezuser_discountrule`
--

LOCK TABLES `ezuser_discountrule` WRITE;
/*!40000 ALTER TABLE `ezuser_discountrule` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezuser_discountrule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezuser_role`
--

DROP TABLE IF EXISTS `ezuser_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezuser_role` (
  `contentobject_id` int(11) default NULL,
  `id` int(11) NOT NULL auto_increment,
  `limit_identifier` varchar(255) default '',
  `limit_value` varchar(255) default '',
  `role_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `ezuser_role_contentobject_id` (`contentobject_id`),
  KEY `ezuser_role_role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezuser_role`
--

LOCK TABLES `ezuser_role` WRITE;
/*!40000 ALTER TABLE `ezuser_role` DISABLE KEYS */;
INSERT INTO `ezuser_role` VALUES (12,25,'','',2),(42,31,'','',1),(13,32,'Subtree','/1/2/',3),(13,33,'Subtree','/1/43/',3),(13,34,'','',5),(72,35,'','',5),(73,36,'','',5),(13,37,'','',7),(72,38,'','',7),(13,39,'','',9),(13,40,'','',1),(73,42,'','',1),(122,43,'','',3),(72,44,'','',1);
/*!40000 ALTER TABLE `ezuser_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezuser_setting`
--

DROP TABLE IF EXISTS `ezuser_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezuser_setting` (
  `is_enabled` int(11) NOT NULL default '0',
  `max_login` int(11) default NULL,
  `user_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezuser_setting`
--

LOCK TABLES `ezuser_setting` WRITE;
/*!40000 ALTER TABLE `ezuser_setting` DISABLE KEYS */;
INSERT INTO `ezuser_setting` VALUES (1,1000,10),(1,10,14),(1,0,118),(1,0,119),(1,0,120),(1,0,122),(1,0,134),(1,0,140),(1,0,141);
/*!40000 ALTER TABLE `ezuser_setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezuservisit`
--

DROP TABLE IF EXISTS `ezuservisit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezuservisit` (
  `current_visit_timestamp` int(11) NOT NULL default '0',
  `failed_login_attempts` int(11) NOT NULL default '0',
  `last_visit_timestamp` int(11) NOT NULL default '0',
  `login_count` int(11) NOT NULL default '0',
  `user_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`user_id`),
  KEY `ezuservisit_co_visit_count` (`current_visit_timestamp`,`login_count`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezuservisit`
--

LOCK TABLES `ezuservisit` WRITE;
/*!40000 ALTER TABLE `ezuservisit` DISABLE KEYS */;
INSERT INTO `ezuservisit` VALUES (1381156853,0,1381151768,96,14),(1360012156,0,1359455161,60,118),(1309526185,0,1309526185,1,120),(1316122629,0,1316111201,4,122),(1353506444,0,1353506443,0,134),(1358503383,0,1358503383,1,140),(1359038215,0,1359038215,1,141);
/*!40000 ALTER TABLE `ezuservisit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezvatrule`
--

DROP TABLE IF EXISTS `ezvatrule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezvatrule` (
  `country_code` varchar(255) NOT NULL default '',
  `id` int(11) NOT NULL auto_increment,
  `vat_type` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezvatrule`
--

LOCK TABLES `ezvatrule` WRITE;
/*!40000 ALTER TABLE `ezvatrule` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezvatrule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezvatrule_product_category`
--

DROP TABLE IF EXISTS `ezvatrule_product_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezvatrule_product_category` (
  `product_category_id` int(11) NOT NULL default '0',
  `vatrule_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`vatrule_id`,`product_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezvatrule_product_category`
--

LOCK TABLES `ezvatrule_product_category` WRITE;
/*!40000 ALTER TABLE `ezvatrule_product_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezvatrule_product_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezvattype`
--

DROP TABLE IF EXISTS `ezvattype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezvattype` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  `percentage` float default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezvattype`
--

LOCK TABLES `ezvattype` WRITE;
/*!40000 ALTER TABLE `ezvattype` DISABLE KEYS */;
INSERT INTO `ezvattype` VALUES (1,'Std',0);
/*!40000 ALTER TABLE `ezvattype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezview_counter`
--

DROP TABLE IF EXISTS `ezview_counter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezview_counter` (
  `count` int(11) NOT NULL default '0',
  `node_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezview_counter`
--

LOCK TABLES `ezview_counter` WRITE;
/*!40000 ALTER TABLE `ezview_counter` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezview_counter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezwaituntildatevalue`
--

DROP TABLE IF EXISTS `ezwaituntildatevalue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezwaituntildatevalue` (
  `contentclass_attribute_id` int(11) NOT NULL default '0',
  `contentclass_id` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `workflow_event_id` int(11) NOT NULL default '0',
  `workflow_event_version` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`,`workflow_event_id`,`workflow_event_version`),
  KEY `ezwaituntildateevalue_wf_ev_id_wf_ver` (`workflow_event_id`,`workflow_event_version`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezwaituntildatevalue`
--

LOCK TABLES `ezwaituntildatevalue` WRITE;
/*!40000 ALTER TABLE `ezwaituntildatevalue` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezwaituntildatevalue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezwishlist`
--

DROP TABLE IF EXISTS `ezwishlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezwishlist` (
  `id` int(11) NOT NULL auto_increment,
  `productcollection_id` int(11) NOT NULL default '0',
  `user_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezwishlist`
--

LOCK TABLES `ezwishlist` WRITE;
/*!40000 ALTER TABLE `ezwishlist` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezwishlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezworkflow`
--

DROP TABLE IF EXISTS `ezworkflow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezworkflow` (
  `created` int(11) NOT NULL default '0',
  `creator_id` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `is_enabled` int(11) NOT NULL default '0',
  `modified` int(11) NOT NULL default '0',
  `modifier_id` int(11) NOT NULL default '0',
  `name` varchar(255) NOT NULL default '',
  `version` int(11) NOT NULL default '0',
  `workflow_type_string` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`id`,`version`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezworkflow`
--

LOCK TABLES `ezworkflow` WRITE;
/*!40000 ALTER TABLE `ezworkflow` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezworkflow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezworkflow_assign`
--

DROP TABLE IF EXISTS `ezworkflow_assign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezworkflow_assign` (
  `access_type` int(11) NOT NULL default '0',
  `as_tree` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `node_id` int(11) NOT NULL default '0',
  `workflow_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezworkflow_assign`
--

LOCK TABLES `ezworkflow_assign` WRITE;
/*!40000 ALTER TABLE `ezworkflow_assign` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezworkflow_assign` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezworkflow_event`
--

DROP TABLE IF EXISTS `ezworkflow_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezworkflow_event` (
  `data_int1` int(11) default NULL,
  `data_int2` int(11) default NULL,
  `data_int3` int(11) default NULL,
  `data_int4` int(11) default NULL,
  `data_text1` varchar(255) default NULL,
  `data_text2` varchar(255) default NULL,
  `data_text3` varchar(255) default NULL,
  `data_text4` varchar(255) default NULL,
  `data_text5` longtext,
  `description` varchar(50) NOT NULL default '',
  `id` int(11) NOT NULL auto_increment,
  `placement` int(11) NOT NULL default '0',
  `version` int(11) NOT NULL default '0',
  `workflow_id` int(11) NOT NULL default '0',
  `workflow_type_string` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`id`,`version`),
  KEY `wid_version_placement` (`workflow_id`,`version`,`placement`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezworkflow_event`
--

LOCK TABLES `ezworkflow_event` WRITE;
/*!40000 ALTER TABLE `ezworkflow_event` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezworkflow_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezworkflow_group`
--

DROP TABLE IF EXISTS `ezworkflow_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezworkflow_group` (
  `created` int(11) NOT NULL default '0',
  `creator_id` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `modified` int(11) NOT NULL default '0',
  `modifier_id` int(11) NOT NULL default '0',
  `name` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezworkflow_group`
--

LOCK TABLES `ezworkflow_group` WRITE;
/*!40000 ALTER TABLE `ezworkflow_group` DISABLE KEYS */;
INSERT INTO `ezworkflow_group` VALUES (1024392098,14,1,1024392098,14,'Standard');
/*!40000 ALTER TABLE `ezworkflow_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezworkflow_group_link`
--

DROP TABLE IF EXISTS `ezworkflow_group_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezworkflow_group_link` (
  `group_id` int(11) NOT NULL default '0',
  `group_name` varchar(255) default NULL,
  `workflow_id` int(11) NOT NULL default '0',
  `workflow_version` int(11) NOT NULL default '0',
  PRIMARY KEY  (`workflow_id`,`group_id`,`workflow_version`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezworkflow_group_link`
--

LOCK TABLES `ezworkflow_group_link` WRITE;
/*!40000 ALTER TABLE `ezworkflow_group_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezworkflow_group_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ezworkflow_process`
--

DROP TABLE IF EXISTS `ezworkflow_process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ezworkflow_process` (
  `activation_date` int(11) default NULL,
  `content_id` int(11) NOT NULL default '0',
  `content_version` int(11) NOT NULL default '0',
  `created` int(11) NOT NULL default '0',
  `event_id` int(11) NOT NULL default '0',
  `event_position` int(11) NOT NULL default '0',
  `event_state` int(11) default NULL,
  `event_status` int(11) NOT NULL default '0',
  `id` int(11) NOT NULL auto_increment,
  `last_event_id` int(11) NOT NULL default '0',
  `last_event_position` int(11) NOT NULL default '0',
  `last_event_status` int(11) NOT NULL default '0',
  `memento_key` varchar(32) default NULL,
  `modified` int(11) NOT NULL default '0',
  `node_id` int(11) NOT NULL default '0',
  `parameters` longtext,
  `process_key` varchar(32) NOT NULL default '',
  `session_key` varchar(32) NOT NULL default '0',
  `status` int(11) default NULL,
  `user_id` int(11) NOT NULL default '0',
  `workflow_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `ezworkflow_process_process_key` (`process_key`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezworkflow_process`
--

LOCK TABLES `ezworkflow_process` WRITE;
/*!40000 ALTER TABLE `ezworkflow_process` DISABLE KEYS */;
/*!40000 ALTER TABLE `ezworkflow_process` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-10-07 17:06:05
