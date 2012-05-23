INSERT INTO `ezcobj_state_link` VALUES 
(57,1),
(58,1),
(59,1);

INSERT INTO `ezcontentobject` VALUES 
(3,1,57,4,0,5,1337165924,'Ansatte',14,1337165924,'e547686e6118e08ebc496c9d7d888eff',2,1),
(3,1,58,4,0,5,1337165944,'Kursansvarlige',14,1337165944,'c45e3301b4fbafcc8eacffbe67529f1c',2,1),
(3,1,59,4,0,5,1337166010,'Opplæringsansvarlige',14,1337166010,'f8537afc685c6bb8bc34413971014178',2,1);
UPDATE ezcontentobject SET `name` = 'FriKomPort' WHERE `id` = 1;
UPDATE ezcontentobject SET `status` = 2 WHERE `id` = 13;

INSERT INTO `ezcontentobject_attribute` VALUES
(108,158,1,0,1,'','ezboolean',108,'eng-GB',3,1,'',4),
(0,6,57,0,NULL,'Ansatte','ezstring',186,'nor-NO',5,0,'ansatte',1),
(0,7,57,0,NULL,'','ezstring',187,'nor-NO',5,0,'',1),
(0,6,58,0,NULL,'Kursansvarlige','ezstring',188,'nor-NO',5,0,'kursansvarlige',1),
(0,7,58,0,NULL,'','ezstring',189,'nor-NO',5,0,'',1),
(0,6,59,0,NULL,'Opplæringsansvarlige','ezstring',190,'nor-NO',5,0,'opplæringsansvarlige',1),
(0,7,59,0,NULL,'','ezstring',191,'nor-NO',5,0,'',1),
(0,4,1,0,NULL,'FriKomPort','ezstring',192,'eng-GB',3,0,'frikomport',4),
(0,155,1,0,NULL,'FriKomPort','ezstring',193,'eng-GB',3,0,'frikomport',4),
(0,119,1,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"><paragraph xmlns:tmp=\"http://ez.no/namespaces/ezpublish3/temporary/\">Velkommen til FriKomPort</paragraph></section>\n','ezxmltext',194,'eng-GB',3,0,'',4),
(0,156,1,0,1045487555,'<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<section xmlns:image=\"http://ez.no/namespaces/ezpublish3/image/\" xmlns:xhtml=\"http://ez.no/namespaces/ezpublish3/xhtml/\" xmlns:custom=\"http://ez.no/namespaces/ezpublish3/custom/\"/>\n','ezxmltext',195,'eng-GB',3,0,'',4);
UPDATE `ezcontentobject_attribute` SET `data_text` = 'FriKomPort' where `id` = 1;
UPDATE `ezcontentobject_attribute` SET `data_text` = 'FriKomPort' where `id` = 2;

INSERT INTO `ezcontentobject_name` VALUES 
('eng-GB',4,1,3,'FriKomPort','eng-GB'),
('nor-NO',1,57,5,'Ansatte','nor-NO'),
('nor-NO',1,58,5,'Kursansvarlige','nor-NO'),
('nor-NO',1,59,5,'Opplæringsansvarlige','nor-NO');

INSERT INTO `ezcontentobject_trash` VALUES 
(13,1,2,0,0,14,1081860719,14,5,'users/editors','/1/5/14/',0,'f7dda2854fc68f7c8455d9cb14bd04a9',1,1);

INSERT INTO `ezcontentobject_tree` VALUES 
(57,1,1,2,0,0,59,1337165925,59,5,'users/ansatte','/1/5/59/',0,'c5e11c4aa2a4f9e678b98170d6993f6c',1,1),
(58,1,1,2,0,0,60,1337165944,60,5,'users/kursansvarlige','/1/5/60/',0,'71751efa6ad8a92a4a3b3bb037efded8',1,1),
(59,1,1,2,0,0,61,1337166010,61,5,'users/opplaeringsansvarlige','/1/5/61/',0,'ddfe2bd10bc778203d5f4ea603c2ae85',1,1);
DELETE FROM `ezcontentobject_tree` where `contentobject_id` = 13;
UPDATE `ezcontentobject_tree` SET `modified_subnode` = 1337166295 WHERE `contentobject_id` = 0;
UPDATE `ezcontentobject_tree` SET `modified_subnode` = 1337166295 WHERE `contentobject_id` = 1;
UPDATE `ezcontentobject_tree` SET `modified_subnode` = 1337166134 WHERE `contentobject_id` = 0;

INSERT INTO `ezcontentobject_version` VALUES 
(57,1337165881,14,496,4,5,1337165924,1,0,1,0),
(58,1337165937,14,497,4,5,1337165944,1,0,1,0),
(59,1337166000,14,498,4,5,1337166010,1,0,1,0),
(1,1337166267,14,499,2,3,1337166295,1,0,4,1);
UPDATE `ezcontentobject_version` set `status` = 3 WHERE `contentobject_id` = 1;

INSERT INTO `eznode_assignment` VALUES 
(57,1,0,35,1,2,5,'',0,1,1),
(58,1,0,36,1,2,5,'',0,1,1),
(59,1,0,37,1,2,5,'',0,1,1),
(1,4,-1,38,1,2,1,'',0,8,1);

INSERT INTO `eznotificationevent` VALUES 
(57,1,0,0,'','','','','ezpublish',1,0),
(58,1,0,0,'','','','','ezpublish',2,0),
(59,1,0,0,'','','','','ezpublish',3,0),
(1,4,0,0,'','','','','ezpublish',4,0);

INSERT INTO `ezpolicy` VALUES 
('login',333,'user',6),
('read',334,'content',6),
('pdf',335,'content',6),
('login',339,'user',8),
('read',340,'content',8),
('pdf',341,'content',8),
('*',344,'content',10),
('login',345,'user',10);

INSERT INTO `ezpolicy_limitation` VALUES 
(255,'Section',334),
(256,'Section',335),
(259,'Section',340),
(260,'Section',341);

INSERT INTO `ezpolicy_limitation_value` VALUES 
(481,255,'1'),
(482,256,'1'),
(485,259,'1'),
(486,260,'1');

INSERT INTO `ezpreferences` VALUES 
(8,'admin_navigation_translations',14,'1');

INSERT INTO `ezrole` VALUES 
(6,0,'Ansatt',NULL,0),
(8,0,'Kursansvarlig',NULL,0),
(10,0,'Opplaringsansvarlig',NULL,0);

INSERT INTO `ezsearch_object_word_link` VALUES 
(6,3,57,0,3517,'name',0,0,0,0,1337165924,2,391),
(6,3,58,0,3518,'name',0,0,0,0,1337165944,2,392),
(6,3,59,0,3519,'name',0,0,0,0,1337166010,2,393),
(4,1,1,0,3520,'name',0,394,0,0,1033917596,1,394),
(155,1,1,0,3521,'short_name',0,395,1,394,1033917596,1,394),
(119,1,1,0,3522,'short_description',0,396,2,394,1033917596,1,395),
(119,1,1,0,3523,'short_description',0,394,3,395,1033917596,1,396),
(119,1,1,0,3524,'short_description',0,0,4,396,1033917596,1,394);
DELETE FROM `ezsearch_object_word_link` WHERE `id` = 225;
DELETE FROM `ezsearch_object_word_link` WHERE `id` = 226;
DELETE FROM `ezsearch_object_word_link` WHERE `id` between 3127 and 3516;

INSERT INTO `ezsearch_word` VALUES 
(391,1,'ansatte'),
(392,1,'kursansvarlige'),
(393,1,'opplæringsansvarlige'),
(394,1,'frikomport'),
(395,1,'velkommen'),
(396,1,'til');
DELETE FROM `ezsearch_word` WHERE `id` = 66;
DELETE FROM `ezsearch_word` WHERE `id` between 278 and 390;

INSERT INTO `ezurlalias_ml` VALUES 
('eznode:61','eznode',1,38,0,1,5,38,2,'Opplaeringsansvarlige','5dd17201769a34c6c87ef77f3f615e4b'),
('eznode:60','eznode',1,37,0,1,5,37,2,'Kursansvarlige','8b3f8e1ef60b3b7d18f2c0bf37f9630e'),
('eznode:59','eznode',1,36,0,1,5,36,2,'Ansatte','a6db1f314f4ac4fcdd7c91c1a670200a');
DELETE FROM `ezurlalias_ml` WHERE `action` = 'eznode:14';

INSERT INTO `ezurlalias_ml_incr` VALUES 
(36),
(37),
(38);

INSERT INTO `ezuser_role` VALUES 
(57,34,'','',6),
(58,35,'','',6),
(59,36,'','',6),
(58,37,'','',8),
(59,38,'','',8),
(59,39,'','',10);

UPDATE `ezuser` SET `email` = 'anonymous@ez.no' WHERE `login`= 'anonymous';
