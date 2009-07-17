# MySQL-Front 5.1  (Build 1.48)

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;


# Host: localhost    Database: estore
# ------------------------------------------------------
# Server version 5.0.22-community-nt

#
# Source for table enm_menu
#

use estore;

DROP TABLE IF EXISTS `enm_menu`;
CREATE TABLE `enm_menu` (
  `Id` bigint(20) NOT NULL auto_increment,
  `enm_menu_parent_id` bigint(20) NOT NULL,
  `enm_menu_name` varchar(255) default NULL,
  `enm_menu_href` varchar(255) default NULL,
  `enm_menu_ico` varchar(255) default NULL,
  `enm_menu_type` bigint(20) default NULL,
  `enm_menu_disable` varchar(1) default NULL,
  `enm_menu_display_no` bigint(20) default NULL,
  `entexd1` varchar(255) default NULL,
  PRIMARY KEY  (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table enm_menu
#
LOCK TABLES `enm_menu` WRITE;
/*!40000 ALTER TABLE `enm_menu` DISABLE KEYS */;

INSERT INTO `enm_menu` VALUES (1,-1,'参数管理','','add',1,'Y',10,'');
INSERT INTO `enm_menu` VALUES (2,1,'资源管理',NULL,'add',1,'Y',1,NULL);
INSERT INTO `enm_menu` VALUES (3,2,'菜单管理','','add',1,'Y',1,NULL);
INSERT INTO `enm_menu` VALUES (4,3,'菜单管理','/boot/menu/menu.jsp','remove',2,'Y',1,NULL);
INSERT INTO `enm_menu` VALUES (5,-1,'经营管理','','edit',1,'Y',1,NULL);
INSERT INTO `enm_menu` VALUES (6,-1,'客户管理','',' ',1,'Y',2,NULL);
/*!40000 ALTER TABLE `enm_menu` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table enm_role
#

DROP TABLE IF EXISTS `enm_role`;
CREATE TABLE `enm_role` (
  `Id` bigint(20) NOT NULL auto_increment,
  `enm_role_name` varchar(255) default NULL,
  `extend1` varchar(255) default NULL,
  PRIMARY KEY  (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table enm_role
#
LOCK TABLES `enm_role` WRITE;
/*!40000 ALTER TABLE `enm_role` DISABLE KEYS */;

/*!40000 ALTER TABLE `enm_role` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table enm_user
#

DROP TABLE IF EXISTS `enm_user`;
CREATE TABLE `enm_user` (
  `Id` bigint(20) NOT NULL auto_increment,
  `enm_user_name` varchar(255) default NULL,
  `enm_user_password` varchar(255) default NULL,
  `enm_role_id` bigint(20) NOT NULL,
  `extend1` varchar(255) default NULL,
  PRIMARY KEY  (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table enm_user
#
LOCK TABLES `enm_user` WRITE;
/*!40000 ALTER TABLE `enm_user` DISABLE KEYS */;

INSERT INTO `enm_user` VALUES (1,'xvxv','3742591',1,NULL);
/*!40000 ALTER TABLE `enm_user` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table es_test
#

DROP TABLE IF EXISTS `es_test`;
CREATE TABLE `es_test` (
  `Id` bigint(20) NOT NULL auto_increment,
  `es_test_name` varchar(255) default NULL,
  PRIMARY KEY  (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table es_test
#
LOCK TABLES `es_test` WRITE;
/*!40000 ALTER TABLE `es_test` DISABLE KEYS */;

/*!40000 ALTER TABLE `es_test` ENABLE KEYS */;
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
