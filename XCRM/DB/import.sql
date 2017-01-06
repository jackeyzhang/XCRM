DROP DATABASE IF EXISTS xcrm;
CREATE DATABASE IF NOT EXISTS xcrm;
use xcrm;

DROP TABLE  IF EXISTS store;
CREATE TABLE `store` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `city` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='门店表';

DROP TABLE  IF EXISTS user;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contact` varchar(45) DEFAULT NULL,
  `department` varchar(45) DEFAULT NULL,
  `title` varchar(45) DEFAULT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `isenable` bit(1) DEFAULT b'1',
  `email` varchar(45) NOT NULL,
  `storeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `storeFk_idx` (`storeid`),
  CONSTRAINT `storeFk` FOREIGN KEY (`storeid`) REFERENCES `store` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;


DROP TABLE  IF EXISTS customer;
CREATE TABLE `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
   `regionId` int(11) DEFAULT NULL,
  `custAddr` varchar(45) NOT NULL,
  `contact` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `cellphone` varchar(45) DEFAULT NULL,
  `fax` varchar(45) DEFAULT NULL,
  `shiptoAddr` varchar(45) DEFAULT NULL,
  `custType` int(11) NOT NULL COMMENT '      <option value="1">先生</option>\n      <option value="2">女士</option>\n      <option value="3">长子</option>\n      <option value="4">次子</option>\n      <option value="5">长女</option>\n      <option value="6">次女</option>\n      <option v',
  `heartlevel` int(2) DEFAULT '5',
  `heartinfo` varchar(200) DEFAULT NULL,
  `createUser` int(11) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `updateUser` int(11) DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

DROP TABLE  IF EXISTS product;
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `barcode` varchar(45) DEFAULT NULL,
  `level1category` int(11) DEFAULT NULL,
  `level2category` int(11) DEFAULT NULL,
  `count` int(11) DEFAULT NULL COMMENT '数量',
  `period` int(11) DEFAULT NULL COMMENT '交货期',
  `remark` longtext,
  `createdate` datetime DEFAULT NULL,
  `editdate` datetime DEFAULT NULL,
  `createuser` int(11) DEFAULT NULL,
  `edituser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_category_idx` (`level1category`,`level2category`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;



DROP TABLE  IF EXISTS productpic;
CREATE TABLE `productpic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fielname` varchar(45) NOT NULL,
  `productid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `productfk_idx` (`productid`),
  CONSTRAINT `productfk` FOREIGN KEY (`productid`) REFERENCES `product` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE  IF EXISTS productcategory;
CREATE TABLE `productcategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `level` int(11) NOT NULL COMMENT '1  产品大类\n2  产品小类',
  `pid` int(11) DEFAULT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS attributeid;
CREATE TABLE `attributeid` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `type` int(11) NOT NULL COMMENT '1 number\n2 list-single\n3 list-multiple',
  `value` varchar(5000) DEFAULT NULL COMMENT 'if it''s list, split by comma',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE  IF EXISTS attribute;
CREATE TABLE `attribute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `attributeid` int(11) NOT NULL,
  `displayname` varchar(500) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `category` int(11) NOT NULL COMMENT '1 product\n2 customer\n3 user\n4 store\n5...',
  `value` varchar(5000) NOT NULL,
  `scopetype` int(11) NOT NULL COMMENT '1 site\n2 store\n3 mutiple stores',
  `scopevalue` varchar(5000) DEFAULT 'null' COMMENT 'store id and combin with comma. if scope is site, should be null',
  `visiable` tinyint(2) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;


DROP TABLE  IF EXISTS attributevalue;
CREATE TABLE `attributevalue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` int(11) NOT NULL,
  `attributeid` int(11) NOT NULL,
  `objectid` int(11) NOT NULL,
  `value` varchar(5000) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_attributeid_idx` (`attributeid`),
  CONSTRAINT `fk_attributeid` FOREIGN KEY (`attributeid`) REFERENCES `attributeid` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

DROP TABLE  IF EXISTS price;
CREATE TABLE `price` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `product` int(11) DEFAULT NULL,
  `store` int(11) DEFAULT NULL,
  `price` float DEFAULT NULL,
  `default` int(2) DEFAULT '1' COMMENT '0 false\n1 true',
  `enable` int(2) DEFAULT NULL COMMENT '0 false\n1 true',
  `createtime` datetime DEFAULT NULL,
  `createuser` int(11) DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  `updateuser` int(11) DEFAULT NULL,
  `count` int(11) DEFAULT NULL COMMENT '库存',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

DROP TABLE  IF EXISTS priceinventoryvalue;
CREATE TABLE `priceinventoryvalue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `priceid` int(11) NOT NULL,
  `pricekey` varchar(100) NOT NULL COMMENT 'key=priceid+valueKey\nvaluekey=value1+value2+value3+...',
  `price` float NOT NULL,
  `inventory` int(11) NOT NULL COMMENT '初始库存',
  `count` int(11) NOT NULL COMMENT '剩余数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8;



DROP TABLE  IF EXISTS contract;
CREATE TABLE `contract` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
   `path` varchar(200) ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='合同表';


DROP TABLE  IF EXISTS bookitem;
CREATE TABLE `bookitem` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  product int(11) NOT NULL,
  price int(11) NOT NULL,
  num int(11) NOT NULL,
  user int(11) NOT NULL,
  `date` datetime NOT NULL,
  status tinyint(1) NOT NULL,
  prdattrs varchar(2000),
  contract int(11),
  contact int(11),
  `comments` varchar(2000) DEFAULT NULL COMMENT '购买时备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

DROP TABLE  IF EXISTS `order`;
CREATE TABLE `order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  orderno bigint(20) NOT NULL,
  `date` datetime NOT NULL,
  `totalprice` float NOT NULL COMMENT '总价',
  `price` float NOT NULL COMMENT '成交价',
  `comments` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

DROP TABLE  IF EXISTS orderitem;
CREATE TABLE `orderitem` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order` int(11) NOT NULL,
   bookitem int(11) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--default script in

INSERT INTO `user` (`id`, `contact`, `department`, `title`, `username`, `password`, `isenable`, `email`) VALUES  (1, 'root', 'it', 'dev', 'root', 'root', b'1', 'lewis@gmail.com');

INSERT INTO `productcategory` VALUES (1,1,NULL,'商曼'),(2,2,1,'婚纱'),(3,2,1,'礼服');

INSERT INTO `attributeid` (`id`,`name`,`type`,`value`) VALUES (101,'颜色',2,'红色,蓝色,绿色,白色,紫色,卡其色');
INSERT INTO `attributeid` (`id`,`name`,`type`,`value`) VALUES (102,'型号',2,'XXL,XL,L,M,S');
INSERT INTO `attributeid` (`id`,`name`,`type`,`value`) VALUES (103,'数量单位',2,'件,个');
INSERT INTO `attributeid` (`id`,`name`,`type`,`value`) VALUES (104,'时间单位',2,'天,小时');
INSERT INTO `attributeid` (`id`,`name`,`type`,`value`) VALUES (105,'数量',1,NULL);
INSERT INTO `attributeid` (`id`,`name`,`type`,`value`) VALUES (106,'交货期',1,NULL);
INSERT INTO `attributeid` (`id`,`name`,`type`,`value`) VALUES (201,'部门',2,'礼服部,摄像部,化妆部,选片部,设计部,取件部,客服部');
INSERT INTO `attributeid` (`id`,`name`,`type`,`value`) VALUES (202,'工作部门',3,'礼服部,摄像部,化妆部,选片部,设计部,取件部,客服部');
INSERT INTO `attributeid` (`id`,`name`,`type`,`value`) VALUES (203,'产品类型',2,'婚纱,礼服');
INSERT INTO `attributeid` (`id`,`name`,`type`,`value`) VALUES (204,'产品面料',2,'白沙,蕾丝');
INSERT INTO `attributeid` (`id`,`name`,`type`,`value`) VALUES (205,'产品颜色',3,'红色,黄色,蓝色,白色,粉色,紫色');
INSERT INTO `attributeid` (`id`,`name`,`type`,`value`) VALUES (206,'产品尺码',2,'S,M,L,XL,XXL');

INSERT INTO `attribute` VALUES (17,204,'产品面料',3,1,'白沙,蕾丝',1,NULL,1),(18,205,'产品颜色',3,1,'红色,黄色,蓝色,白色,粉色,紫色',1,NULL,1),(19,206,'产品尺码',3,1,'S,M,L,XL,XXL',1,NULL,1),(20,204,'产品面料',3,6,'白沙,蕾丝',1,NULL,1),(21,205,'产品颜色',3,6,'红色,黄色,蓝色,白色,粉色,紫色',1,NULL,1),(22,206,'产品尺码',3,6,'S,M,L,XL,XXL',1,NULL,1);

INSERT INTO `attributevalue` VALUES (28,1,206,13,'S,M,L,XL,XXL'),(29,1,205,13,'红色,黄色,蓝色,白色,粉色,紫色'),(30,1,204,13,'白沙,蕾丝'),(31,1,206,14,'S,M,L'),(32,1,205,14,'红色,黄色,蓝色,白色'),(33,1,204,14,'白沙,蕾丝');

INSERT INTO `product` VALUES (13,'婚纱-XF100','8d08159886413679a76ee01d3aea1345',1,2,NULL,NULL,NULL,'2016-12-17 16:29:48',NULL,1,NULL),(14,'礼服-LF200','5ded9b1cc86dfc4b3059ebfe9108ab31',1,3,NULL,NULL,NULL,'2016-12-17 16:30:31',NULL,1,NULL);

INSERT INTO `productpic` VALUES (1,'20120312142458_FSyvu.jpg',13),(2,'lifu1.jpg',14),(3,'lifu2.jpg',14);

INSERT INTO `price` VALUES (3,'婚纱-XF100',13,NULL,5000,1,1,NULL,1,'2016-12-17 16:37:09',1,100);

INSERT INTO `priceinventoryvalue` VALUES (92,3,'白沙-L-粉色',5000,100,100),(93,3,'蕾丝-S-白色',5000,100,100),(94,3,'蕾丝-L-黄色',5000,100,100),(95,3,'蕾丝-XL-蓝色',5000,100,100),(96,3,'白沙-L-红色',5000,100,100),(97,3,'白沙-M-紫色',5000,100,100),(98,3,'白沙-M-白色',5000,100,100),(99,3,'白沙-XXL-蓝色',5000,100,100),(100,3,'蕾丝-L-红色',5000,100,100),(101,3,'白沙-S-白色',5000,100,100),(102,3,'白沙-L-黄色',5000,100,100),(103,3,'蕾丝-M-紫色',5000,100,100),(104,3,'蕾丝-L-粉色',5000,100,100),(105,3,'蕾丝-M-白色',5000,100,100),(106,3,'蕾丝-XL-粉色',5000,100,100),(107,3,'白沙-S-紫色',5000,100,100),(108,3,'蕾丝-XL-红色',5000,100,100),(109,3,'蕾丝-XXL-红色',5000,100,100),(110,3,'蕾丝-XXL-粉色',5000,100,100),(111,3,'白沙-L-蓝色',5000,100,100),(112,3,'白沙-M-红色',5000,100,100),(113,3,'蕾丝-S-蓝色',5000,100,100),(114,3,'蕾丝-XXL-黄色',5000,100,100),(115,3,'白沙-XL-紫色',5000,100,100),(116,3,'白沙-M-粉色',5000,100,100),(117,3,'白沙-XXL-紫色',5000,100,100),(118,3,'蕾丝-XL-黄色',5000,100,100),(119,3,'白沙-XXL-白色',5000,100,100),(120,3,'白沙-XL-白色',5000,100,100),(121,3,'白沙-XL-红色',5000,100,100),(122,3,'白沙-M-黄色',5000,100,100),(123,3,'蕾丝-L-紫色',5000,100,100),(124,3,'白沙-XL-粉色',5000,100,100),(125,3,'蕾丝-S-红色',5000,100,100),(126,3,'蕾丝-XXL-蓝色',5000,100,100),(127,3,'蕾丝-S-黄色',5000,100,100),(128,3,'蕾丝-S-粉色',5000,100,100),(129,3,'白沙-XL-黄色',5000,100,100),(130,3,'蕾丝-L-白色',5000,100,100),(131,3,'白沙-XL-蓝色',5000,100,100),(132,3,'白沙-L-紫色',5000,100,100),(133,3,'蕾丝-M-蓝色',5000,100,100),(134,3,'蕾丝-M-黄色',5000,100,100),(135,3,'白沙-S-粉色',5000,100,100),(136,3,'白沙-S-红色',5000,100,100),(137,3,'白沙-L-白色',5000,100,100),(138,3,'白沙-M-蓝色',5000,100,100),(139,3,'蕾丝-M-红色',5000,100,100),(140,3,'白沙-XXL-红色',5000,100,100),(141,3,'白沙-S-蓝色',5000,100,100),(142,3,'蕾丝-M-粉色',5000,100,100),(143,3,'白沙-S-黄色',5000,100,100),(144,3,'白沙-XXL-粉色',5000,100,100),(145,3,'白沙-XXL-黄色',5000,100,100),(146,3,'蕾丝-XXL-白色',5000,100,100),(147,3,'蕾丝-XL-白色',5000,100,100),(148,3,'蕾丝-S-紫色',5000,100,100),(149,3,'蕾丝-L-蓝色',5000,100,100),(150,3,'蕾丝-XL-紫色',5000,100,100),(151,3,'蕾丝-XXL-紫色',5000,100,100);

INSERT INTO `contract` VALUES (1,'贝可曼尼合同',NULL),(2,'尚曼合同',NULL),(3,'王峰合同',NULL);
