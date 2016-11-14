CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contact` varchar(45) DEFAULT NULL,
  `department` varchar(45) DEFAULT NULL,
  `title` varchar(45) DEFAULT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `isenable` bit(1) DEFAULT b'1',
  `email` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

INSERT INTO `user` (`id`, `contact`, `department`, `title`, `username`, `password`, `isenable`, `email`) VALUES
	(1, 'root', 'it', 'dev', 'root', 'root', b'1', 'lewis@gmail.com');

CREATE TABLE `store` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `city` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='门店表';

CREATE TABLE `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
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

CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `barcode` varchar(45) DEFAULT NULL,
  `count` int(11) DEFAULT NULL COMMENT '数量',
  `period` int(11) DEFAULT NULL COMMENT '交货期',
  `remark` longtext,
  `createdate` datetime DEFAULT NULL,
  `editdate` datetime DEFAULT NULL,
  `createuser` int(11) DEFAULT NULL,
  `edituser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;



CREATE TABLE `productpic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fielname` varchar(45) NOT NULL,
  `productid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `productfk_idx` (`productid`),
  CONSTRAINT `productfk` FOREIGN KEY (`productid`) REFERENCES `product` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `attributeid` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `type` int(11) NOT NULL COMMENT '1 number\n2 list-single\n3 list-multiple',
  `value` varchar(5000) DEFAULT NULL COMMENT 'if it''s list, split by comma',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `attributeid`(`id`,`name`,`type`,`value`) VALUES(101,'颜色',2,'红色,蓝色,绿色,白色,紫色,卡其色');

CREATE TABLE `attribute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `attributeid` int(11) NOT NULL,
  `displayname` varchar(500) NOT NULL,
  `category` int(11) NOT NULL COMMENT '1 product\n2 customer\n3 user\n4 store\n5...',
  `value` varchar(5000) NOT NULL,
  `scopetype` int(11) NOT NULL COMMENT '1 site\n2 store\n3 mutiple stores',
  `scopevalue` varchar(5000) DEFAULT 'null' COMMENT 'store id and combin with comma. if scope is site, should be null',
  `visiable` tinyint(2) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

CREATE TABLE `attributevalue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` int(11) NOT NULL,
  `attributeid` int(11) NOT NULL,
  `objectid` int(11) NOT NULL,
  `value` varchar(5000) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_attributeid_idx` (`attributeid`),
  CONSTRAINT `fk_attributeid` FOREIGN KEY (`attributeid`) REFERENCES `attributeid` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;





