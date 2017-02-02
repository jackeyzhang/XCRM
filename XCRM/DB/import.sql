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
  customer int(11),
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


-- from Jan 25, 2017  add company for customer
ALTER TABLE customer 
ADD COLUMN `company` VARCHAR(100) NULL COMMENT '公司名称' AFTER `updateDate`;

-- Feb 2, 2017  add default value for customer table
ALTER TABLE customer 
CHANGE COLUMN `custType` `custType` INT(11) NOT NULL DEFAULT 1 COMMENT '      <option value=\"1\">先生</option>\n      <option value=\"2\">女士</option>\n      <option value=\"3\">长子</option>\n      <option value=\"4\">次子</option>\n      <option value=\"5\">长女</option>\n      <option value=\"6\">次女</option>\n      <option v' ;



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


-- insert customer records - number is 451
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  'INSHINE婚纱之王' , '上海市长宁区长宁路1027号兆丰广场802室'  , '张晴'  , 18516093493 , null  , '上海市长宁区长宁路1027号兆丰广场802室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '马克.蝶丽奥娜' , '长宁区长宁路1027号兆丰广场8楼802'  , '张晴'  , 15821149983 , null  , '长宁区长宁路1027号兆丰广场8楼802'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '孟锦慧' , '长宁区华山路1520弄55号14G' , '孟锦慧' , 13641827129 , null  , '长宁区华山路1520弄55号14G' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海LG'  , '上海市徐汇区襄阳南路450弄2号La Grace'  , '朱玲'  , 13564730416 , null  , '上海市徐汇区襄阳南路450弄2号La Grace'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海玛丽'  , '上海市中山西路1350号中星美华村18号楼601室' , '玛丽'  , 13818968020 , null  , '上海市中山西路1350号中星美华村18号楼601室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海AM'  , '上海市宝山区松兴西路258号5C201' , '唐唐'  , 13501786229 , null  , '上海市宝山区松兴西路258号5C201' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海DS-Weedding' , '上海市静安区新丰路518弄6号1102室'  , '罗小姐' , 15821482852 , null  , '上海市静安区新丰路518弄6号1102室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海江苏路GK' , '上海市长宁区江苏路40号4楼'  , 'IVIN'  , 18621071731 , null  , '上海市长宁区江苏路40号4楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海茂名GK'  , '上海市静安区茂名北路39号Grace Kelly ' , 'IVIN'  , 13564110633 , null  , '上海市静安区茂名北路39号Grace Kelly ' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海GK 苏菲雅'  , '上海建国西路379号'  , '冯琦'  , 13661837265 , null  , '上海建国西路379号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海GK 秋林' , '上海市凯旋南路31号9B栋采购部[秋林]'  , '秋林'  , 13918428862 , null  , '上海市凯旋南路31号9B栋采购部[秋林]'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海GK小木桥店'  , '上海市徐汇区小木桥路2号2楼礼服部' , '唐唐'  , 13501786229 , null  , '上海市徐汇区小木桥路2号2楼礼服部' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海牛亚辉' , '上海市长宁区长宁天山路1718号2号楼D-212'  , '牛亚辉' , 13818424300 , null  , '上海市长宁区长宁天山路1718号2号楼D-212'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海LAMOON'  , '上海淮海中路461号4楼59室' , '于先生' , 18964717668 , null  , '上海淮海中路461号4楼59室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海MY Wedding'  , '上海市建国西路379号' , '黄雅'  , 13621628893 , null  , '上海市建国西路379号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海peter' , '上海市普陀区金沙江路2348弄21号楼101'  , 'peter' , 18918259381 , null  , '上海市普陀区金沙江路2348弄21号楼101'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海思韵服饰有限公司'  , '上海市肇嘉浜路356号' , 'Ada' , 021-54043987  , null  , '上海市肇嘉浜路356号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海唐唐'  , '上海市建国西路379号' , '唐唐'  , 13621611981 , null  , '上海市建国西路379号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海SNM' , '上海市徐汇区小木桥路2号SUM店礼服部' , '唐唐'  , 13501786229 , null  , '上海市徐汇区小木桥路2号SUM店礼服部' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海爱铤堡' , '上海市徐汇区淮海中路1200弄4号2楼' , '黄亭亭' , 13611902703 , null  , '上海市徐汇区淮海中路1200弄4号2楼' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海巴黎左岸'  , '上海市浦东新区川河路4629弄21-33号' , '李建鸿' , 15921279018 , 021-58986080  , '上海市浦东新区川河路4629弄21-33号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海陈翔翔' , '上海浦东新区丁香路1399弄仁恒河滨城27幢2301室' , '陈翔翔' , 15921917387 , null  , '上海浦东新区丁香路1399弄仁恒河滨城27幢2301室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海丹尼'  , '上海市徐汇区南丹东路300弄亚都国际5号楼503室' , '丹尼'  , 18863599888 , null  , '上海市徐汇区南丹东路300弄亚都国际5号楼503室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海芳芳姐' , '上海市浦东新区红枫路358弄晓园8号楼902室' , '芳芳姐 '  , 17721277536 , null  , '上海市浦东新区红枫路358弄晓园8号楼902室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海公主婚纱'  , '上海市黄埔区成都南路158号'  , '谢小姐' , 021-53062238  , null  , '上海市黄埔区成都南路158号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海贵阁'  , '上海市长宁区愚园路1221弄1号201' , '吴小姐' , 18964378143 , null  , '上海市长宁区愚园路1221弄1号201' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海花嫁丽舍'  , '上海市闵行红松东路1000号四楼'  , '徐菊'  , 18017841311 , null  , '上海市闵行红松东路1000号四楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海黄老师' , '上海古美路377弄2号' , '黄老师' , 18101866117 , null  , '上海古美路377弄2号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海黎幽佑' , '上海澳门路58弄26号1701室'  , '黎幽佑' , 13817061889 , null  , '上海澳门路58弄26号1701室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海李春艳' , '上海市徐汇区南丹东路100弄10号202'  , '李春艳' , 13829631618 , null  , '上海市徐汇区南丹东路100弄10号202'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海刘晔'  , '上海南京西路651号广电大厦8楼'  , '刘晔'  , 18616686373 , null  , '上海南京西路651号广电大厦8楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海马丽'  , '上海市徐汇区建国西路253号B1栋1701室'  , '马丽'  , 13818968020 , null  , '上海市徐汇区建国西路253号B1栋1701室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海马沅娜' , '上海建国西路379号'  , '马沅娜' , 13701997630 , null  , '上海建国西路379号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海孟老师' , '上海市长宁区华山路1520弄55号14G'  , '孟老师' , 136-4182-7129 , null  , '上海市长宁区华山路1520弄55号14G'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海孟梓华' , '上海市徐汇区淮海中路1200弄4号2楼ZSABELLABRIDE'  , '孟梓华' , 13918730466 , null  , '上海市徐汇区淮海中路1200弄4号2楼ZSABELLABRIDE'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海母其丹' , '上海静安区延安中路1135弄西一楼1号201室' , '母其丹' , 15000011501 , null  , '上海静安区延安中路1135弄西一楼1号201室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海唐慧珍' , '上海市人民路757号101室'  , '唐慧珍' , 15921097737 , null  , '上海市人民路757号101室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海天使嫁衣'  , '上海长宁区长宁路1027号兆丰广场802'  , '张宇涵' , null  , null  , '上海长宁区长宁路1027号兆丰广场802'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海汪英姿' , '上海市闸北区彭江路602路E座226室' , '汪英姿' , 18116263885 , null  , '上海市闸北区彭江路602路E座226室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海王娇'  , '上海市杨浦区大学路307号202室' , '王娇'  , 18001980116 , null  , '上海市杨浦区大学路307号202室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海唯一视觉摄影'  , '上海杨浦区军工路1436号18栋唯一视觉2楼'  , '唐燕娟' , 13651654227 , null  , '上海杨浦区军工路1436号18栋唯一视觉2楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海徐百姿     '  , '黄浦区九江路501号外滩WE'  , '徐百姿' , 13636561293 , null  , '黄浦区九江路501号外滩WE'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海严钰'  , '上海市黄浦区南京东路378号5楼礼服馆' , '严钰'  , 15121188559 , null  , '上海市黄浦区南京东路378号5楼礼服馆' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海伊甸园' , '上海长宁区长宁路969号兆丰花苑1605室' , '范小姐' , 13916946550 , null  , '上海长宁区长宁路969号兆丰花苑1605室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海伊甸缘婚纱' , '上海市延安西路1228号嘉利大厦2号35AB'  , '范丽丽' , 13916946550 , 021-52373272  , '上海市延安西路1228号嘉利大厦2号35AB'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '珍妮花婚纱摄影' , '上海静安区万航渡路50号2F ' , '黄老师' , 13301723351 , 62557811  , '上海静安区万航渡路50号2F ' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海郑涵文' , ' 上海长宁剑河路2000号55栋'  , '郑涵文' , 18521539898 , null  , ' 上海长宁剑河路2000号55栋'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海朱玲'  , '上海徐汇区襄阳南路450弄2号' , '朱玲'  , 13564730416 , null  , '上海徐汇区襄阳南路450弄2号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '上海甘璐'  , '上海徐汇区龙吴路11弄10号302' , '张凯'  , 18658571569 , null  , '上海徐汇区龙吴路11弄10号302' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '艾尔文视觉' , '厦门市湖里区五缘湾道路文化展览苑B区13号' , '欧普'  , 18650909300 , 0592-5288257  , '厦门市湖里区五缘湾道路文化展览苑B区13号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '艾米丽          ' , '湖北省武汉市汉口沿江大道江景大厦228号B栋5楼D室'  , '吴丹'  , 13986001532 , null  , '湖北省武汉市汉口沿江大道江景大厦228号B栋5楼D室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '安徽爱唯一婚纱礼服会馆' , '安徽省铜陵市财富广场B座506室'  , '王彩霞' , 18656226313 , null  , '安徽省铜陵市财富广场B座506室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '安徽浪漫巴黎'  , '安徽省濉溪县闸河路32号508室'  , '赵春华' , 15205615879 , 15205615879 , '安徽省濉溪县闸河路32号508室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '安徽刘晓蕊' , '安徽马鞍山市朝辉首府2栋2单元1103室'  , '刘晓蕊' , 13865557525 , null  , '安徽马鞍山市朝辉首府2栋2单元1103室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '安徽马鞍山蒂凡嫁衣' , '安徽省马鞍山市花山区中央花园16栋底商' , '唐莹'  , 18609612333 , 18609612333 , '安徽省马鞍山市花山区中央花园16栋底商' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '安徽宋娇娇' , '安徽省宿州市捅桥区西昌南路大花园北200米路东花火' , '宋娇娇' , 13225777777 , null  , '安徽省宿州市捅桥区西昌南路大花园北200米路东花火' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '安徽王道美' , '安徽省合肥市政务区绿地内森庄园D-14-1邮编230000' , '王道美' , 13855108333 , null  , '安徽省合肥市政务区绿地内森庄园D-14-1邮编230000' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '安徽薇薇新娘'  , '安徽铜陵市淮河路中段799号'  , '王彩霞' , 18656226313 , null  , '安徽铜陵市淮河路中段799号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '鞍山金夫人店'  , '鞍山市铁西区兴盛路20号金夫人婚纱摄影' , '高胜平' , 15141291117 , null  , '鞍山市铁西区兴盛路20号金夫人婚纱摄影' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京CD视觉摄影'  , '北京市朝阳区建外SOHO丘旦楼商铺0104C'  , '经理'  , 18610195677 , null  , '北京市朝阳区建外SOHO丘旦楼商铺0104C'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京GK店' , '北京朝阳区东直门外大街甲26号院1号韩国艺匠 （北京GK） '  , '黄店'  , 13911828784 , null  , '北京朝阳区东直门外大街甲26号院1号韩国艺匠 （北京GK） '  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京GK 韩国艺匠' , '北京市朝阳区东直门外大街甲26号院1号2层  韩国艺匠' , '韩念'  , 13911828784 , null  , '北京市朝阳区东直门外大街甲26号院1号2层  韩国艺匠' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京Only You'  , '北京市朝阳区东三环北路55号世茂宫园2号楼1105' , '靳靳'  , 18801150587 , null  , '北京市朝阳区东三环北路55号世茂宫园2号楼1105' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京阿兰'  , '北京市朝阳区四季河中街星河湾朗园1-1-2202'  , '阿兰'  , 13550296688 , null  , '北京市朝阳区四季河中街星河湾朗园1-1-2202'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京白色凯乐婚纱'  , '北京市朝阳区新东路8号首开铂群北区三号楼3-053' , '绍总'  , 13311560223 , null  , '北京市朝阳区新东路8号首开铂群北区三号楼3-053' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京贝可尼        ' , '北京市朝阳区建外SOHO东区2号楼三层306室' , '谢云'  , 13141117684 , null  , '北京市朝阳区建外SOHO东区2号楼三层306室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京MY Weeding'  , '北京市朝阳区东三环中路建外SOHO东区8号楼502室'  , '戴小钊' , 18701488786 , 010-57136371  , '北京市朝阳区东三环中路建外SOHO东区8号楼502室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京冯印'  , '北京市通州区新华联南区1号楼3单元401'  , '冯印'  , 18611162251 , null  , '北京市通州区新华联南区1号楼3单元401'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京郜先生' , '北京市海淀区远大路金源商务中心A座7C' , '郜先生' , 18500135890 , null  , '北京市海淀区远大路金源商务中心A座7C' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京公主嫁到'  , '北京市东城区东方广场W2座1003室'  , '潘一鹏' , 18601189872 , null  , '北京市东城区东方广场W2座1003室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京李楠'  , '北京市朝阳区双井东柏街9号院天之骄子5-2-701' , '李楠'  , 13466568695 , null  , '北京市朝阳区双井东柏街9号院天之骄子5-2-701' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  ' 北京李天棚 ' , '北京市朝阳区北苑路32号院平安嘉苑2号楼1409'  , '李天棚 '  , 13810423616 , null  , '北京市朝阳区北苑路32号院平安嘉苑2号楼1409'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京李宇航' , '北京市朝阳区建国路万达广场1号楼501双瑞世纪圣皇金诗' , '李宇航' , 18668078968 , null  , '北京市朝阳区建国路万达广场1号楼501双瑞世纪圣皇金诗' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京里翌锴' , '北京市朝阳区南磨房路16号院禧福汇5号楼802' , '里翌锴' , 18701586288 , null  , '北京市朝阳区南磨房路16号院禧福汇5号楼802' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京林珍'  , '北京市昌平区市北七家镇北亚花园小区东门对面八仙宾馆5号楼4007房' , '北京林珍'  , 15601942299 , null  , '北京市昌平区市北七家镇北亚花园小区东门对面八仙宾馆5号楼4007房' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京刘女士-李斯羽' , ' 北京市朝阳区东四环阳光上东安徒生花园33号楼1单元502' , '刘女士' , 13651081796 , null  , ' 北京市朝阳区东四环阳光上东安徒生花园33号楼1单元502' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京刘姝婧' , '北京市朝阳区建国路88号SOHO现代城3-105'  , '刘姝婧' , 13901014926 , null  , '北京市朝阳区建国路88号SOHO现代城3-105'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京刘依娜' , '北京市朝阳区八里庄西里远洋天地64号楼1002' , '刘依娜' ,   00000000000,  null  , '北京市朝阳区八里庄西里远洋天地64号楼1002' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京蒙娜丽莎'  , '北京市东城区东四南大街143号 蒙娜丽莎礼服部' , '周思思' , 15311490713 , null  , '北京市东城区东四南大街143号 蒙娜丽莎礼服部' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京邵思'  , '北京市朝阳区工体北路永利国际1-1627'  , '邵思'  , 13501235068 , 13311560223 , '北京市朝阳区工体北路永利国际1-1627'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京刘丹'  , '北京市朝阳区东三环中路39号建外SOho5号别墅0140C CD视觉'  , '刘丹'  , 18611056882 , null  , '北京市朝阳区东三环中路39号建外SOho5号别墅0140C CD视觉'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京孙倩倩' , '北京市朝阳区建外SOHO2号楼2层1001室'  , '孙倩倩' , 13141117684 , null  , '北京市朝阳区建外SOHO2号楼2层1001室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京唐芳芳' , '北京市朝阳区建外SOHO8号楼502室' , '唐芳芳' , 18817377366 , null  , '北京市朝阳区建外SOHO8号楼502室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京唐小姐' , '北京市朝阳区朝阳路139号禧瑞都3号楼106'  , '唐小姐' , 13581687064 , null  , '北京市朝阳区朝阳路139号禧瑞都3号楼106'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京天帛婚纱逢增梅' , '北京市朝阳区99号大悦城[天帛婚纱]'  , '逢增梅' , 13701143073 , null  , '北京市朝阳区99号大悦城[天帛婚纱]'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京薇薇新娘'  , '北京市西城区新街口北大街59号地下三层薇薇新娘婚纱摄影' , '凌小姐' , 15910816996 , null  , '北京市西城区新街口北大街59号地下三层薇薇新娘婚纱摄影' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京吴秀佳' , '北京市朝阳区建国西路88号SOHO现代城3-105' , '吴秀佳' , 13901014926 , null  , '北京市朝阳区建国西路88号SOHO现代城3-105' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京黄莎莎-小宝'  , '北京市海淀区五棵松路40号摄影器材城2楼237室'  , '黄莎莎' , 18501641323 , null  , '北京市海淀区五棵松路40号摄影器材城2楼237室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京闫丹'  , '京市朝阳区东三环北路55号院世民宫园2号楼1105' , '闫丹'  , 18611029339 , null  , '京市朝阳区东三环北路55号院世民宫园2号楼1105' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京杨骏韬' , '北京市朝阳区高碑店金家村中街8号花园里文创园C3009' , '杨骏韬' , 15010314507 , null  , '北京市朝阳区高碑店金家村中街8号花园里文创园C3009' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京悦林纱' , '北京市朝阳区郎家园6号郎园Lintage'  , '石小姐' , 13070155609 , null  , '北京市朝阳区郎家园6号郎园Lintage'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京赵雨桐' , '北京市朝阳区十八里店乡六道口村256号' , '李女士转赵雨桐' , 13269220991 , null  , '北京市朝阳区十八里店乡六道口村256号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '北京梓航'  , '北京市通州区永顺镇朝阳北路与温榆河西路' , '梓航'  , 13621071576 , null  , '北京市通州区永顺镇朝阳北路与温榆河西路' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '没有值' , '交汇路口北两公里金融街园中园6号园37栋'  , '没有值' , 0 , null  , '交汇路口北两公里金融街园中园6号园37栋'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '天后嫁衣'  , '温州苍南灵溪镇金乡路鑫和锦园2幢1602'  , '娜娜'  , 13857185321 , null  , '温州苍南灵溪镇金乡路鑫和锦园2幢1602'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '常州米拉嫁衣'  , '常州市天宁区嘉宏世纪大厦1922'  , '张小姐' , 15851931449 , null  , '常州市天宁区嘉宏世纪大厦1922'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '常州杨洁'  , '常州市晋陵中路388号南方大厦3楼' , '杨洁'  , 13915098699 , null  , '常州市晋陵中路388号南方大厦3楼' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州陈宣羽' , '浙江省温州市鹿城区南大门联合广场1幢16E' , '陈宣羽' , 13968879406 , null  , '浙江省温州市鹿城区南大门联合广场1幢16E' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '成都GK'  , '四川省成都市武侯区人民路南路四段12号' , '丹尼'  , 18610070014 , null  , '四川省成都市武侯区人民路南路四段12号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '华蓉国府', '华蓉国府韩国艺匠2楼Grace kelly婚纱礼服馆'  ,   '',   00000000000,  null  , '华蓉国府韩国艺匠2楼Grace kelly婚纱礼服馆'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '成都LG'  , '成都市锦江区紫东大街东楼段35号明宇金融广场901室'  , '屈梦蝶' , 15821778660 , null  , '成都市锦江区紫东大街东楼段35号明宇金融广场901室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '成都李汶蔚' , '四川省成都市高新区天府大道中段666号希顿国际广场B座701'  , '李汶蔚' , 13880332121 , null  , '四川省成都市高新区天府大道中段666号希顿国际广场B座701'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '成都斐丽婚纱'  , '四川省成都市一环路西二段18#斐丽婚纱' , '雯雯'  , 13518179313 , 13518179313 , '四川省成都市一环路西二段18#斐丽婚纱' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '成都茜思'  , '四川成都天府二街蜀都中心二期1栋4单元1101' , '居弓轶' , 18523568518 , null  , '四川成都天府二街蜀都中心二期1栋4单元1101' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '大连刁玲玲' , '大连市沙河口区西安路福佳新天地地下一层81052'  , '刁玲玲' , 13998580986 , null  , '大连市沙河口区西安路福佳新天地地下一层81052'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '大连董雪梅' , '辽宁省大连市沙河口区西安路天兴罗斯福A座1906室' , '董雪梅' , 13842695527 , null  , '辽宁省大连市沙河口区西安路天兴罗斯福A座1906室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '大连韩园'  , '大连市中山区时代广场A座11-9'  , '韩园'  , 18018918477 , null  , '大连市中山区时代广场A座11-9'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '大连金夫人店'  , '大连市西岗区新开路21号金夫人婚纱摄影' , '张鑫'  , 15840460699 , null  , '大连市西岗区新开路21号金夫人婚纱摄影' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '大连李洁'  , '大连市西尚区黄河路356号 reone  ' , '李洁'  , 18642662230 , null  , '大连市西尚区黄河路356号 reone  ' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '大连李文'  , '大连市沙河口区西安路60号2049室'  , '李文'  , 13614094060 , null  , '大连市沙河口区西安路60号2049室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '大连罗曼蒂克婚纱'  , '大连市金川安盛购物广场五楼' , '伊治敏' , 13478517476 , 13478517476 , '大连市金川安盛购物广场五楼' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '大连玛芮莎' , '大连中山区时代广场A座610'  , '王丽萍' , 18640950678 , 0411-84305120 , '大连中山区时代广场A座610'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '大连金丹富丽婚纱'  , '大连市金州区胜利路781号' , '杨玉荣' , 1880420568  , 13909848756 , '大连市金州区胜利路781号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '大连天使在人间' , '大连市沙河口区西安路139号天兴罗斯福西座2506室'  , '李爽'  , 13898669884 , null  , '大连市沙河口区西安路139号天兴罗斯福西座2506室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '大连新青年摄影' , '大连市中山区中山路124号新青年婚纱摄影'  , '李小姐' , 18624371282 , null  , '大连市中山区中山路124号新青年婚纱摄影'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '黑龙江大庆新潮购物中心' , '黑龙江省大庆市让胡路区长春路1号新潮国际购物中心二楼A2-11' , '白煌贵' , 13945059065 , null  , '黑龙江省大庆市让胡路区长春路1号新潮国际购物中心二楼A2-11' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '黑龙江大庆张丽丽'  , '黑龙江大庆市让胡路区世纪家园SC02-3-302'  , '张丽丽' , 18945999510 , null  , '黑龙江大庆市让胡路区世纪家园SC02-3-302'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '台北法国巴黎婚纱'  , '台湾台北市中山北路2段114号1F[邮编104]'  , '刘罗伶' , 886911021999  , null  , '台湾台北市中山北路2段114号1F[邮编104]'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '佛山黄淑梅' , '广东佛山顺德北窖碧桂园西六路12A' , '黄小姐' , 1850375126  , null  , '广东佛山顺德北窖碧桂园西六路12A' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '福建贝贝'  , '福建省厦门市湖里区五缘湾文化展览苑B区13号 艾尔文视觉'  , '贝贝'  , 18650160832 , null  , '福建省厦门市湖里区五缘湾文化展览苑B区13号 艾尔文视觉'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '福建方远'  , '福建省福州市鼓楼区八一七北路268号冠亚广场7-42号' , '徐小姐' , 13960822348 , null  , '福建省福州市鼓楼区八一七北路268号冠亚广场7-42号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '福州GK'  , '福州市台江区金钻大厦4楼603室'  , '徐波'  , 13661498604 , null  , '福州市台江区金钻大厦4楼603室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '福州程若'  , '福州市金山区金工路1号海峡创业园9号102' , '程若'  , 13960771877 , null  , '福州市金山区金工路1号海峡创业园9号102' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '福州明华'  , '福州市台江区万达路群升国际御园J区2号3001' , '明华'  , 13489001152 , null  , '福州市台江区万达路群升国际御园J区2号3001' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '广东简晓彤' , '广东省珠海市香洲区拱北口岸地下商场新来魅力女人世界F003A铺' , '简晓彤' , 85366110880 , null  , '广东省珠海市香洲区拱北口岸地下商场新来魅力女人世界F003A铺' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '广东李春艳' , '广东省汕头市龙湖区华山路丹霞西区32栋401'  , '李春艳' , 13829631618 , null  , '广东省汕头市龙湖区华山路丹霞西区32栋401'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '广东张洋'  , '广东省东莞市凤岗镇永盛大街永盛一号富康阁1单元703室' , '张洋'  , 18813510888 , null  , '广东省东莞市凤岗镇永盛大街永盛一号富康阁1单元703室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '广西邓淋姗' , '广西省南宁市青秀东盟商务区印尼园东2-03商铺婚纱女皇' , '邓淋姗' , 18677068750 , null  , '广西省南宁市青秀东盟商务区印尼园东2-03商铺婚纱女皇' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '广西邓小姐' , '广西南宁市青秀东盟商务区盟四街印尼园东2-03[婚纱皇后]' , '邓小姐' , 18677068750 , null  , '广西南宁市青秀东盟商务区盟四街印尼园东2-03[婚纱皇后]' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '广西甘璐'  , '广西崇左市龙州县县委大院'  , '甘璐'  , 18775959274 , null  , '广西崇左市龙州县县委大院'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '广州GK'  , '广州市天河区东莞庄路2号鸿德国际酒店3楼韩国艺匠礼服部' , '邸亚男' , 15026976801 , null  , '广州市天河区东莞庄路2号鸿德国际酒店3楼韩国艺匠礼服部' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '广州My Weding' , '广州天河区东莞庄路2号财润国际大厦3楼' , '徐先生' , 13661498604 , null  , '广州天河区东莞庄路2号财润国际大厦3楼' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '广州郭莹'  , '广州市滨江东路931号B栋903'  , '郭莹'  , 13602857198 , null  , '广州市滨江东路931号B栋903'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '广州金夫人' , '广州市越秀区小北路200号 金夫人婚纱' , '金夫人' , 13826231308 , null  , '广州市越秀区小北路200号 金夫人婚纱' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '广州Luna'  , '广州市天河区黄埔大道西126号云来斯堡酒店201礼服部' , '李小娟' , 13802834169 , null  , '广州市天河区黄埔大道西126号云来斯堡酒店201礼服部' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '闺秀堂婚纱' , '南京湖北路36号'  , '经理'  , 18105184761 , 83225390  , '南京湖北路36号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '贵州省张微' , '贵州省贵阳市富水中路22号[小十字]新菀7楼'  , '张微'  , 13985498989 , null  , '贵州省贵阳市富水中路22号[小十字]新菀7楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '哈尔滨V.V婚纱'  , '哈尔滨市道里区经纬街46号' , '王莹'  , 18686784974 , 84617839  , '哈尔滨市道里区经纬街46号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '哈尔滨花样年华' , '哈尔滨道里区经纬街38号'  , '权红梅' , 13704509998 , null  , '哈尔滨道里区经纬街38号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '哈尔滨金夫人影城'  , '哈尔滨市江北区浦西路1号'  , '谷艳虹' , 13845044383 , null  , '哈尔滨市江北区浦西路1号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '哈尔滨刘佳' , '黑龙江哈尔滨市道里区工农大街上和园著12栋' , '刘佳'  , 18545593690 , 18646338292 , '黑龙江哈尔滨市道里区工农大街上和园著12栋' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '哈尔滨王欢' , '哈尔滨市南岗区汉水路333号'  , '王欢'  , 13766818342 , null  , '哈尔滨市南岗区汉水路333号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '哈尔滨王瞻' , '哈尔滨市东直路309-6号' , '王瞻'  , 18686860758 , null  , '哈尔滨市东直路309-6号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '哈尔滨迎宾婚纱' , '哈尔滨市南岗区红军街108号'  , '孙义新' , 13089996776 , 13089996776 , '哈尔滨市南岗区红军街108号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州AK婚纱'  , '杭州市拱墅区长乐路29号元谷创意园5-202'  , '张月'  , 15088775005 , null  , '杭州市拱墅区长乐路29号元谷创意园5-202'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州GK         ' , '杭州市西湖区文三路工专路黄龙万科中心H座裙楼三楼'  , '艾琳'  , 13788929651 , null  , '杭州市西湖区文三路工专路黄龙万科中心H座裙楼三楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州PINKY双色婚纱' , '杭州拱墅区长乐路29号元谷创艺园区5幢206'  , '范佳'  , 13606625521 , null  , '杭州拱墅区长乐路29号元谷创艺园区5幢206'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州阿布'  , '杭州市和睦路2号元谷创意园2-204A' , '阿布'  , 18658868379 , null  , '杭州市和睦路2号元谷创意园2-204A' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州爱渡时尚婚纱会所'  , '杭州市下城区文晖路22号现代置业大厦东楼105室'  , '云云'  , 13958130383 , null  , '杭州市下城区文晖路22号现代置业大厦东楼105室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州百合婚纱礼服部' , '杭州余杭正五常自庙工业园区11幢百合新娘礼服部' , '范佳'  , 13606625521 , null  , '杭州余杭正五常自庙工业园区11幢百合新娘礼服部' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州陈斌'  , '浙江省杭州市下城区凤起路367号国都公寓十幢7楼'  , '陈斌'  , 15067121314 , null  , '浙江省杭州市下城区凤起路367号国都公寓十幢7楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州郭超'  , '浙江省杭州市萧山区衙前镇浙江东南网架股份有限公司'  , '郭超'  , 13588278899 , null  , '浙江省杭州市萧山区衙前镇浙江东南网架股份有限公司'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州嫁日新娘'  , '杭州市下城区体育场路292号金夫人婚纱摄影二楼嫁日新娘' , '陈莉娟' , 13018987511 , null  , '杭州市下城区体育场路292号金夫人婚纱摄影二楼嫁日新娘' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州久悦     ' , '杭州市下城区上塘路97号'  , '思思'  , 15868857120 , null  , '杭州市下城区上塘路97号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州孔沪萍' , '杭州市西湖区黄龙国际万科中心H座MissLuna婚纱摄影3楼礼服部' , '孔沪萍' , 13764041787 , null  , '杭州市西湖区黄龙国际万科中心H座MissLuna婚纱摄影3楼礼服部' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州刘晓如' , '杭州市下城区文晖路33号1楼'  , '刘晓如' , 13958046419 , 13588102131 , '杭州市下城区文晖路33号1楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州MELODY曼莉婚纱'  , '杭州市下城区凤起路507号MANLI婚纱【武林路交叉口】'  , '邱泽'  , 18657199681 , null  , '杭州市下城区凤起路507号MANLI婚纱【武林路交叉口】'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州木悦婚纱'  , '杭州市拱罡区嘉墅街25号商铺 木悦婚纱' , '妍妍'  , 13221824605 , null  , '杭州市拱罡区嘉墅街25号商铺 木悦婚纱' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州段玉香' , '杭州市江干区钱潮路9号东方润3-2-2101室' , '段玉香' , 13819196790 , null  , '杭州市江干区钱潮路9号东方润3-2-2101室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州杨小玉' , '杭州市上城区四条巷39号'  , '杨小玉' , 13957115930 , null  , '杭州市上城区四条巷39号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州索菲亚婚纱' , '杭州市拱墅区花园岗209号1楼  索菲亚婚纱'  , 'Tnile' , 13357113113 , null  , '杭州市拱墅区花园岗209号1楼  索菲亚婚纱'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州台尔 婚纱' , '杭州市风起路359号'  , '总经理' , 13606710243 , null  , '杭州市风起路359号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州汤于佳' , '杭州市下城区文晖路33号Amanda Queen婚纱一楼'  , '汤于佳' , 13588451312 , null  , '杭州市下城区文晖路33号Amanda Queen婚纱一楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州小月'  , '杭州市拱墅区长乐路29号元谷创意园5-202'  , '小月'  , 15088775005 , null  , '杭州市拱墅区长乐路29号元谷创意园5-202'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州怡人婚纱'  , '杭州上城区建国中路16号'  , '金子'  , 0571-87813199 , null  , '杭州上城区建国中路16号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州张杨'  , '杭州市上城区四条巷39号1-1-302' , '张杨'  , 13957115930 , null  , '杭州市上城区四条巷39号1-1-302' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '杭州AK婚纱'  , '杭州市拱墅区长乐路100号绿地集团1幢2楼AK婚纱' , '张月'  , 15088775005 , null  , '杭州市拱墅区长乐路100号绿地集团1幢2楼AK婚纱' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '河北毕海传' , '河北省秦皇岛市海港区渤海明珠10-5-503'  , '毕海传' , 13031878606 , null  , '河北省秦皇岛市海港区渤海明珠10-5-503'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '河北秦皇岛梦巴黎婚纱'  , '河北秦皇岛市海港区海阳路95号 梦巴黎婚纱摄影' , '宇宏桥' , 1380335468  , null  , '河北秦皇岛市海港区海阳路95号 梦巴黎婚纱摄影' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '河南安静'  , '河南省平顶山市优越路万家南门对面红颜对面'  , '安静'  , 15837521688 , 0375-6169521  , '河南省平顶山市优越路万家南门对面红颜对面'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '洛阳蒙娜丽莎'  , '河南省洛阳市西工区中州路天盛一百八楼'  , '陈晓红' , 18510974339 , null  , '河南省洛阳市西工区中州路天盛一百八楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '黑龙江爱琴海时尚摄影'  , '黑龙江省大庆市萨区' , '朱晓丽' , 0459-6650688  , null  , '黑龙江省大庆市萨区' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '黑龙江巴黎春天' , '黑龙江省双鸭山市新兴大街五马路温州商城一楼' , '马晓梅' , 13284692099 , null  , '黑龙江省双鸭山市新兴大街五马路温州商城一楼' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '黑龙江巴黎春天婚纱' , '黑龙江省佳木斯市西林路仿古楼对面'  , '郭建'  , 15046469345 , 0454-8825577  , '黑龙江省佳木斯市西林路仿古楼对面'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '黑龙江冯蕾冰'  , '黑龙江省哈尔滨市道里区群力第七大道上面和园著小区10号楼'  , '冯蕾冰' , 18645131818 , null  , '黑龙江省哈尔滨市道里区群力第七大道上面和园著小区10号楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '黑龙江佳木斯'  , '黑龙江省佳木斯市文化宫道南宜福都市公寓2号门'  , '李春玲' , 13803677515 , 0454-8759111  , '黑龙江省佳木斯市文化宫道南宜福都市公寓2号门'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '黑龙江齐威' , '黑龙江省大庆市让胡路区世奥中心薇尚彩妆婚纱馆写字楼2009室'  , '齐威'  , 13136802831 , null  , '黑龙江省大庆市让胡路区世奥中心薇尚彩妆婚纱馆写字楼2009室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '黑龙江张丽丽'  , '黑龙江省大庆市让胡路区世纪家园SC02-3-302' , '张丽丽' , 18945999510 , null  , '黑龙江省大庆市让胡路区世纪家园SC02-3-302' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '吉林苏瑞玲' , '吉林市昌邑区江湾路伊江丽景小区1号楼1单元302室' , '苏瑞玲' , 13844622078 , null  , '吉林市昌邑区江湾路伊江丽景小区1号楼1单元302室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '佳木斯李先生'  , '黑龙江富锦市中央大街玛雅婚纱'  , '李先生' , 15846383940 , null  , '黑龙江富锦市中央大街玛雅婚纱'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '嘉兴双色婚纱'  , '浙江省嘉兴市南湖区凌公塘路792号' , '王笑圆' , 13325737778 , null  , '浙江省嘉兴市南湖区凌公塘路792号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '江苏省靖江邓小莲'  , '江苏省靖江市康宁明珠A区3栋13号楼一单元602'  , '邓小莲' , 13852631022 , null  , '江苏省靖江市康宁明珠A区3栋13号楼一单元602'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '江苏金夫人婚纱摄影' , '江苏省盐城市亭湖区建军东路58号培友酒店蜗壁 金夫人婚纱'  , '李春斌' , 0515-88708999 , null  , '江苏省盐城市亭湖区建军东路58号培友酒店蜗壁 金夫人婚纱'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '庭园寝饰   ' , ' 江苏省海门市三星纺都大道庭园寝饰' , '邵素捷' , 15240577178 , null  , ' 江苏省海门市三星纺都大道庭园寝饰' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '江苏赵魏'  , '江苏省徐州市云龙区中山南路173-13号 微微新娘' , '赵魏'  , 18626038889 , null  , '江苏省徐州市云龙区中山南路173-13号 微微新娘' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '江西赖如勇' , '江西省赣州兴国汽车城四楼'  , '赖如勇' , 13807977046 , null  , '江西省赣州兴国汽车城四楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '江西罗桦'  , '江西省南昌市东湖区民德路时代广场写字楼1406号'  , '罗桦'  , 13576118817 , null  , '江西省南昌市东湖区民德路时代广场写字楼1406号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '昆明GK韩国艺匠礼服部' , '昆明市西山区南亚风情第壹城D5-123，25号商铺[星光苑后门]'  , '岳施晗' , 18636383322 , null  , '昆明市西山区南亚风情第壹城D5-123，25号商铺[星光苑后门]'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '乐清梵秀天香'  , '浙江省乐清市晨沐路318号梵秀天香' , '项洋洋' , 15888444999 , null  , '浙江省乐清市晨沐路318号梵秀天香' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '高如'  , '浙江省乐清市柳市镇大兴西路246号' , '高如'  , 13506551421 , null  , '浙江省乐清市柳市镇大兴西路246号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '乐清美人工坊'  , '乐清三环路新世纪花园南大门72-74号美人工坊' , '吴玲玲' , 61580505  , null  , '乐清三环路新世纪花园南大门72-74号美人工坊' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '乐清时代婚纱'  , '浙江省乐清市乐成镇清远路261号欣业大厦1单元1201室'  , '陈小姐' , 18657758797 , null  , '浙江省乐清市乐成镇清远路261号欣业大厦1单元1201室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '辽宁奥斯卡婚纱' , '辽宁省本溪市平山区平山路'  , '韩晓芳' , 13604145788 , null  , '辽宁省本溪市平山区平山路'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '辽宁巴黎风尚'  , '辽宁省营口市站前区深港西门6号' , '栾云'  , 15641746667 , null  , '辽宁省营口市站前区深港西门6号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '辽宁董雪梅' , '辽宁省营口市大石桥劳动小区1号楼409室'  , '董雪梅' , 13842695527 , null  , '辽宁省营口市大石桥劳动小区1号楼409室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '沈阳女王婚纱'  , '沈阳市和平区中山路21号新荣大厦B座'  , '胡雪'  , 15640504950 , null  , '沈阳市和平区中山路21号新荣大厦B座'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '辽宁虎妞精品冯卉'  , '辽宁省鞍山市台安县繁荣北街[同一首歌北]虎妞精品'  , '冯卉'  , 13842659903 , null  , '辽宁省鞍山市台安县繁荣北街[同一首歌北]虎妞精品'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '辽宁芭莎皇后'  , '辽宁省营口市西市区辽河大街辽河老街57-9芭莎皇后' , '赵辛彤' , 18341759553 , null  , '辽宁省营口市西市区辽河大街辽河老街57-9芭莎皇后' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '辽宁省盘锦市杨萍'  , '辽宁省盘锦市兴隆台区白特二楼摩登B06安比婚纱' , '杨萍'  , 13998788826 , null  , '辽宁省盘锦市兴隆台区白特二楼摩登B06安比婚纱' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '辽宁朴美玉' , '辽宁省鞍山市景区街东座二楼83号'  , '朴美玉' , 15040765423 , null  , '辽宁省鞍山市景区街东座二楼83号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '辽宁省小玉' , '辽宁省鞍山市铁东区对炉交通岗北走50米对炉大厦3楼情MS婚纱旗舰店' , '小玉'  , 15040765423 , null  , '辽宁省鞍山市铁东区对炉交通岗北走50米对炉大厦3楼情MS婚纱旗舰店' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '绚丽婚纱会馆'  , '辽宁省丹东市元宝区江景之都小区八号楼一单元2601' , '朱丽娜' , 15142376766 , null  , '辽宁省丹东市元宝区江景之都小区八号楼一单元2601' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '曼思嫁衣'  , '浙江文成中银大厦1106'  , '陈曼曼' , 15968780660 , null  , '浙江文成中银大厦1106'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '柳市亲密爱人'  , '浙江省乐清市柳市镇柳翁西路333号（新新雅大酒店斜对面）'  , '陈蕾'  , 15825666600 , null  , '浙江省乐清市柳市镇柳翁西路333号（新新雅大酒店斜对面）'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州黄小燕' , '温州市苍南县灵溪镇江湾路460号'  , '黄小燕' , 13958735968 , null  , '温州市苍南县灵溪镇江湾路460号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州林丽洁' , '温州市龙湾区状元龙飞东路103号'  , '林丽洁' , 15858858555 , null  , '温州市龙湾区状元龙飞东路103号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '南京GK'  , '南京市鼓楼区湖南路47号凤凰台饭店5楼' , '徐婷婷' , 18521061849 , null  , '南京市鼓楼区湖南路47号凤凰台饭店5楼' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '南京LG'  , '南京市秦淮区太平南路305号江苏饭店5楼 ' , '徐婷婷' , 18521061849 , null  , '南京市秦淮区太平南路305号江苏饭店5楼 ' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '宝娜嫁衣'  , '南京市玄武区中央路258-27号南京米兰尊荣婚纱摄影二楼'  , '郭家莉' , 13851750722 , null  , '南京市玄武区中央路258-27号南京米兰尊荣婚纱摄影二楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '宝娜嫁衣王玲'  , '南京市玄武区中央路258-27号新立基大厦四楼米兰统计部'  , '王玲'  , 13852288867 , null  , '南京市玄武区中央路258-27号新立基大厦四楼米兰统计部'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '南京陈荣'  , '南京市中山路18号国际大厦1903室'  , '陈荣'  , 13851929238 , null  , '南京市中山路18号国际大厦1903室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '南京淡香嫁衣'  , '南京市江宁区天元西路88号文化名园小区唯美居9栋501' , '张洁'  , 18061699476 , null  , '南京市江宁区天元西路88号文化名园小区唯美居9栋501' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '南京蒂凡婚纱'  , '南京市新街口金鹰国际商务城A座36楼'  , '李伟'  , 13913915515 , null  , '南京市新街口金鹰国际商务城A座36楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '南京都格婚纱摄影'  , '南京市新街口金陵饭店亚太商务楼负一层6号'  , '曹晶晶' , 15895881502 , null  , '南京市新街口金陵饭店亚太商务楼负一层6号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '南京闺秀堂' , '南京湖北路36号（乱世佳人路口）'  , '谢芳'  , 13805184761 , 83225390  , '南京湖北路36号（乱世佳人路口）'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '南京今生有约婚纱摄影'  , '南京市玄武区中央路224号红星家俱城内德汇广场3号楼 今生有约婚纱摄影' , '崔朋'  , 13951608043 , null  , '南京市玄武区中央路224号红星家俱城内德汇广场3号楼 今生有约婚纱摄影' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '南京丽莎皇宫'  , '南京市汉中路8号金轮国际广场1226'  , '张静雅' , 13813881891 , null  , '南京市汉中路8号金轮国际广场1226'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '南京梦幻公主'  , '南京市新街口中山东路18号国际贸易中心1903室'  , '陈蓉'  , 13851929238 , null  , '南京市新街口中山东路18号国际贸易中心1903室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '南京米兰'  , '南京市中央路258-27号南京米兰婚纱' , '谢店'  , 18251846660 , null  , '南京市中央路258-27号南京米兰婚纱' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '南京圣柏莎婚纱' , '南京市太平南路1号世纪广场B座727室' , '王云'  , 13951601112 , null  , '南京市太平南路1号世纪广场B座727室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '南京圣蒂娅婚纱摄影' , '南京市中山东路218号，外文书店旁，南京圣蒂娅婚纱摄影' , '曾巧红' , 18914482878 , null  , '南京市中山东路218号，外文书店旁，南京圣蒂娅婚纱摄影' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '南京十月公社'  , '江苏省南京市栖霞区广月路1号十月公社创业园米兰电影梦工厂'  , '王军霞 王雨'  , 13851647581 , null  , '江苏省南京市栖霞区广月路1号十月公社创业园米兰电影梦工厂'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '南京雪中彩影'  , '南京市白下区石鼓路73号金鹰国际花园对面'  , '梁惠'  , 13951670808 , null  , '南京市白下区石鼓路73号金鹰国际花园对面'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '宁波GK'  , '宁波市江北区钻石广场7号楼，韩国艺匠'  , '徐波'  , 13661498604 , null  , '宁波市江北区钻石广场7号楼，韩国艺匠'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '宁波罗丁摄影'  , '宁波江东甬港北路66号罗丁婚纱摄影，甬港饭店旁边'  , '敏姐'  , 15168158807 , null  , '宁波江东甬港北路66号罗丁婚纱摄影，甬港饭店旁边'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '宁波萝蔓朵' , '宁波市江北区人民路645弄16号'  , '姜雪萍' , 13685826898 , null  , '宁波市江北区人民路645弄16号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '宁波曼鱼画纱'  , '宁波市江北杨善路88号' , '周洁'  , 13967812580 , null  , '宁波市江北杨善路88号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '茉莉婚纱馆' , '宁波市钱湖北路399号钱湖天地TBD(B)318号茉莉婚纱馆'  , '潘小姐' , 13282223122 , null  , '宁波市钱湖北路399号钱湖天地TBD(B)318号茉莉婚纱馆'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '宁波索菲亚' , '宁波市鄞州区四明中路1008号，都市森林六楼，索菲亚'  , '岳苗'  , 18668867214 , null  , '宁波市鄞州区四明中路1008号，都市森林六楼，索菲亚'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '宁波唐唐'  , '宁波市海曙区镇明路514号 洛可可婚纱' , '唐唐'  , 13857886782 , null  , '宁波市海曙区镇明路514号 洛可可婚纱' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '宁波王琳'  , '浙江省宁波市江东区江东南路257弄1号France Bridal婚纱馆' , '王琳'  , 13738560025 , null  , '浙江省宁波市江东区江东南路257弄1号France Bridal婚纱馆' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '宁波张珍楠' , '浙江省宁波市海曙博府丽景湾5幢410'  , '张珍楠' , 13185748193 , null  , '浙江省宁波市海曙博府丽景湾5幢410'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '宁波周洁'  , '宁波市江北杨善路88号' , '周洁'  , 13967812580 , null  , '宁波市江北杨善路88号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '宁波朱朱'  , '浙江省宁波市鄞州匝天源巷127号南苑国际B-2幢2505'  , '朱朱'  , 18606617287 , null  , '浙江省宁波市鄞州匝天源巷127号南苑国际B-2幢2505'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '宁夏王文娟' , '宁夏银川市金凤区大世界商务广场A座1103室'  , '王文娟' , 13995472331 , null  , '宁夏银川市金凤区大世界商务广场A座1103室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '莆田薇薇新娘'  , '福建省莆田市荔城区文南大路 薇薇新娘'  , '张箐'  , 13959553299 , null  , '福建省莆田市荔城区文南大路 薇薇新娘'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '青岛名裳嫁衣'  , '山东省青岛市市南区香港西路裕源大厦C座708【新湛路11号】'  , '栾龙'  , 13255578889 , null  , '山东省青岛市市南区香港西路裕源大厦C座708【新湛路11号】'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '青岛圣特罗佩'  , '青岛市闽江路37号  ' , '王丽'  , 18669700061 , null  , '青岛市闽江路37号  ' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '青岛圣瓦伦丁'  , '山东省青岛市劲松三路187号圣瓦伦丁后期工厂'  , '张红'  , 18660291289 , 13655323322 , '山东省青岛市劲松三路187号圣瓦伦丁后期工厂'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '青岛微微新娘'  , '青岛市市北区延安二路5号'  , '吴姐'  , 13506390108 , 0532-82725091 , '青岛市市北区延安二路5号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '青岛张晓艳' , '青岛市宁夏路274号弘信山庄25号楼3单元801户' , '张晓艳' , 18669852875 , null  , '青岛市宁夏路274号弘信山庄25号楼3单元801户' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '浙江省OMOOMJ国际婚纱' , '浙江省瑞安市罗阳大道883-837号'  , '董事长' , 13958847448 , 0577-65918860 , '浙江省瑞安市罗阳大道883-837号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '瑞安TINA'  , '浙江省瑞安市拱瑞山路70-72号'  , 'TINA'  , 15058980830 , null  , '浙江省瑞安市拱瑞山路70-72号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '瑞安VINS婚纱'  , '浙江省瑞安市安阳望湖家园二期33幢' , '阿咪'  , 15158628809 , null  , '浙江省瑞安市安阳望湖家园二期33幢' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '瑞安爱的礼物'  , '浙江瑞安时代大厦C3--1006'  , '朱虹'  , 15968719077 , 15968719077 , '浙江瑞安时代大厦C3--1006'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '瑞安辰辰'  , '浙江省瑞安市海江东路华昌大厦2幢2902 ' , '辰辰'  , 18606679190 , null  , '浙江省瑞安市海江东路华昌大厦2幢2902 ' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '瑞安陈丽'  , '浙江省瑞安市东塔小区2幢2单元203'  , '陈丽'  , 18658761216 , null  , '浙江省瑞安市东塔小区2幢2单元203'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '瑞安丁雪洁     '  , '浙江省瑞安市万松西路锦湖大厦10B2'  , '丁雪洁' , 13967787225 , null  , '浙江省瑞安市万松西路锦湖大厦10B2'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '施华洛婚纱' , '温州瑞安市塘下塘川南街42-50'  , '冯显英' , 15158755272 , null  , '温州瑞安市塘下塘川南街42-50'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '瑞安红扣嫁衣'  , '浙江省瑞安市隆山东路462号 红扣嫁衣' , '董敏洁' , 18958906876 , null  , '浙江省瑞安市隆山东路462号 红扣嫁衣' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '美人工坊'  , '瑞安塘下塘西中心西路93到97号'  , '吴玲玲' , 15867777731 , null  , '瑞安塘下塘西中心西路93到97号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '梦之纱精品嫁衣馆'  , '浙江瑞安市滨江大道3340-3342号' , '刘婷婷' , 15958771119 , null  , '浙江瑞安市滨江大道3340-3342号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '瑞安名缓婚纱'  , '浙江省瑞安市塘下赵宅广场中路293-295号'  , '吴小芳' , 13957790193 , null  , '浙江省瑞安市塘下赵宅广场中路293-295号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '瑞安秦婷婷' , '浙江省瑞安市安阳佳欣华庭5幢1单元50B'  , '秦婷婷' , 13958892358 , null  , '浙江省瑞安市安阳佳欣华庭5幢1单元50B'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '瑞安姗姗国际婚纱'  , '浙江省瑞安市罗阳大道883-837号'  , '黄姗姗' , 13958847448 , 0577-65918860 , '浙江省瑞安市罗阳大道883-837号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '雪洁造型'  , '瑞安市莘阳大道润锦苑3幢101-102' , '王舟'  , 15057328000 , null  , '瑞安市莘阳大道润锦苑3幢101-102' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '瑞安薇丹婚纱'  , '浙江瑞安市安阳拱瑞山路139号' , '李伟丹' , 18815155509 , null  , '浙江瑞安市安阳拱瑞山路139号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '幸福留言嫁衣'  , '瑞安市长春花园3幢104-105室幸福留言嫁衣' , '张蕾'  , 13958968420 , null  , '瑞安市长春花园3幢104-105室幸福留言嫁衣' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '瑞安许程程' , '浙江省瑞安市安阳桥大厦19C'  , '许程程' , 18516034280 , null  , '浙江省瑞安市安阳桥大厦19C'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州叶其涛' , '温州瑞安外滩滨江大道3304-3306' , '叶其涛' , 15157798831 , null  , '温州瑞安外滩滨江大道3304-3306' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '瑞安郑欢乐' , '浙江省瑞安市滨江大道3422号' , '郑欢乐' , 13967737171 , null  , '浙江省瑞安市滨江大道3422号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '瑞安云裳嫁衣'  , '温州市江滨西路东泰大厦1501' , '郑灵丹' , 13867723238 , 0577-65902868 , '温州市江滨西路东泰大厦1501' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '厦门杨敏'  , '厦门市思明区西提南二路信隆城2号门二号楼'  , '杨敏'  , 15959255022 , 15959255022 , '厦门市思明区西提南二路信隆城2号门二号楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '山东TT造型'  , '山东省青岛市澳门路86号百丽广场西区2号楼253'  , '倪婷'  , 13964214191 , null  , '山东省青岛市澳门路86号百丽广场西区2号楼253'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '山东房宁'  , '山东省淄博华侨城8组团11号楼2单元1001'  , '房宁'  , 15315210667 , null  , '山东省淄博华侨城8组团11号楼2单元1001'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '山东高晓毅' , '山东省青岛市李沧区书院路218号1单元201'  , '高晓毅' , 13515328505 , null  , '山东省青岛市李沧区书院路218号1单元201'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '山东韩冰'  , '山东省东营市西城乐活城三楼TINA嫁衣' , '韩冰'  , 15288884128 , null  , '山东省东营市西城乐活城三楼TINA嫁衣' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '蓝月国际'  , '山东省潍坊市东风街新华路交叉口' , '田保莲' , 15153601862 , null  , '山东省潍坊市东风街新华路交叉口' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '山东名季婚纱彩妆'  , '山东省烟台市芝罘区阳光一百B座1709' , '娟子'  , 13864572309 , null  , '山东省烟台市芝罘区阳光一百B座1709' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '山东省ME婚纱定制' , '山东省烟台市芝罘区大马路滨海广场54号ME婚纱定制' , '暴丹丹' , 13275457777 , null  , '山东省烟台市芝罘区大马路滨海广场54号ME婚纱定制' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '山东省SWeet Stary'  , '山东省济南市泉城路111号世纪园珠宝'  , '井经理' , 13188941177 , null  , '山东省济南市泉城路111号世纪园珠宝'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '山东高琳美妆婚纱馆' , '山东省烟台市芝罘区大马路旅游大世界天海源酒店一楼'  , '高小姐' , 15553576776 , null  , '山东省烟台市芝罘区大马路旅游大世界天海源酒店一楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '山东省李亚娟'  , '山东省烟台市小川婚纱摄影芝罩区南大街267号'  , '李亚娟' , 13808907888 , null  , '山东省烟台市小川婚纱摄影芝罩区南大街267号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '新新娘婚纱摄影' , '山东省潍坊市奎文区胜利东街379号   新新娘婚纱' , '董事长' , 13953699698 , null  , '山东省潍坊市奎文区胜利东街379号   新新娘婚纱' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '山西我爱你婚纱' , '山西太原市后开化寺综合楼81号' , '康焱'  , 13834573773 , 15035188222 , '山西太原市后开化寺综合楼81号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '山东张敬华' , '山东省滨州市黄河六路529-12号' , '张敬华' , 13305439678 , 0543-3460233  , '山东省滨州市黄河六路529-12号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '山西省 I do'  , '山西省临汾市解放路42号I DO婚纱摄影'  , '王琪'  , 13546559997 , null  , '山西省临汾市解放路42号I DO婚纱摄影'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '山西省徐翔' , '山西省太原市小店区长凤街北美新天地2号楼1407室' , '徐翔'  , 13934601345 , null  , '山西省太原市小店区长凤街北美新天地2号楼1407室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '山西王治国' , '山西省介休市城隍庙广场B区9号  ' , '王治国' , 13935466276 , null  , '山西省介休市城隍庙广场B区9号  ' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '山西长治大台婚纱摄影'  , '山西省长治市府后西街119号'  , '罗霞'  , 13467040706 , null  , '山西省长治市府后西街119号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '陕西省西安东大街'  , '陕西省西安市碑林区端履门十字西北角蒙娜丽莎负一层'  , '礼服部海婕' , 029-62215083  , null  , '陕西省西安市碑林区端履门十字西北角蒙娜丽莎负一层'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '陕西新娘百分百婚纱' , '陕西省西安市东关解放广场'  , '许丽'  , 15592959000 , null  , '陕西省西安市东关解放广场'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '陕西西安张毅斌' , '陕西西安市新城区西一路175#品位' , '张毅斌' , 18681846366 , 029-87250619  , '陕西西安市新城区西一路175#品位' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '商丘金秋婚纱'  , '河南省商丘市睢阳区北门外50米' , '李爱英' , 13592320988 , 0370-3312352  , '河南省商丘市睢阳区北门外50米' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '深圳AM'  , '深圳市福田区深南路彩田路大中华国际金融中心C座2楼韩国AM礼服部 ' , '纪小雨' , 13265672007 , null  , '深圳市福田区深南路彩田路大中华国际金融中心C座2楼韩国AM礼服部 ' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '深圳GK'  , '深圳市福田區華強北紅荔路2010建設銀行2楼 ' , '孔沪萍' , 13764041787 ,  0755-66866972  , '深圳市福田區華強北紅荔路2010建設銀行2楼 ' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '深圳嫁日新娘'  , '深圳市福田区天安数码城天经大厦AB座2A2' , '郑艳枋' , 13714349888 , null  , '深圳市福田区天安数码城天经大厦AB座2A2' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '深圳麦海颖' , '深圳市罗湖区红荔路1008号荔景大厦1203室' , '麦海颖' , 13751159708 , 13751159708 , '深圳市罗湖区红荔路1008号荔景大厦1203室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '深圳衣纱礼服'  , '深圳市福田区中心六路星河中心一楼'  , '叶绣明' , 13510085661 , 13510085661 , '深圳市福田区中心六路星河中心一楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '深圳紫鹏'  , '深圳市福田区八卦一路617栋603室'  , '紫 鹏' , 13554805109 , null  , '深圳市福田区八卦一路617栋603室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '沈阳韩国艺匠礼服部' , '沈阳市沈河区市府大路262～6甲 韩国艺匠礼服部 ' , '代晶'  , 13998818887 , null  , '沈阳市沈河区市府大路262～6甲 韩国艺匠礼服部 ' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '沈阳韩浩哲' , '沈阳市沈河区朝阳街196号 米兰新娘'  , '韩浩哲' , 13998162288 , null  , '沈阳市沈河区朝阳街196号 米兰新娘'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '沈阳金夫人' , '沈阳市和平区中华路100号4楼' , '刘颂椒' , 13909880810 , 024-23401172  , '沈阳市和平区中华路100号4楼' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '沈阳玛芮莎' , '辽宁省沈阳市和平区青年大街华润大厦B座万象城3期2楼2F208' , '隋雅秋' , 13500771080 , 02431379068 , '辽宁省沈阳市和平区青年大街华润大厦B座万象城3期2楼2F208' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '沈阳牛宇'  , '沈阳市和平区中山路21号新荣大厦3楼3022'  , '牛宇'  , 15640504950 , null  , '沈阳市和平区中山路21号新荣大厦3楼3022'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '沈阳时尚经典'  , '沈阳市沈河区中街路21号时尚经典二楼'  , '周紫含 总经理' , 18040051115 , 18040051115 , '沈阳市沈河区中街路21号时尚经典二楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '沈阳巴黎'  , '辽宁省沈阳市和平区南大街118号巴黎卢浮宫影城' , '韩斯羽' , 18624086211 , null  , '辽宁省沈阳市和平区南大街118号巴黎卢浮宫影城' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '沈阳宋阳'  , '沈阳市沈河区朝阳街244号' , '宋阳'  , 13066681066 , null  , '沈阳市沈河区朝阳街244号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '沈阳唐秀丽' ,   '沈阳', '唐秀丽' , 13700014417 , null  ,   '沈阳');        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '沈阳辛会枫' , '沈阳市五爱市场' , '辛会枫' , 13700037849 , 13700037849 , '沈阳市五爱市场' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '四川陈小姐' , '四川省乐山市嘉定北路158号川港影楼'  , '陈小姐' , 15298088898 , 0833-2415571  , '四川省乐山市嘉定北路158号川港影楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '四川郭小姐' , '四川省成都市东大街黄金大厦1楼羽印象婚纱'  , '郭小利' , 18384668866 , 028-86669270  , '四川省成都市东大街黄金大厦1楼羽印象婚纱'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '苏州GK'  , '苏州园区国际博览中心璐娜摄影（月廊街南首）地铁一号线2号口出'  , '付云菲' , 15201018234 , 15201018234 , '苏州园区国际博览中心璐娜摄影（月廊街南首）地铁一号线2号口出'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '苏州今生有约婚纱摄影'  , '苏州市姑苏区景德路40号今生有约婚纱摄影'  , '朱峰'  , 18952003090 , 18952003090 , '苏州市姑苏区景德路40号今生有约婚纱摄影'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '苏州太郎花子'  , '苏州市平江区景德路193号' , '杨珺'  , 13812628666 , 13915579957 , '苏州市平江区景德路193号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '台北凯瑟琳婚纱' , '台北市中山北路2段91-2号'  , '唐坤玲' , null  , 88622571-1271 , '台北市中山北路2段91-2号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '台北林晟祥' , '台北高雄市中正3路154号' , '林晟祥' , 886952068838  , 88672367889 , '台北高雄市中正3路154号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '台北苏菲雅婚纱' , '新北市板桥区馆前东路82-84号'  , '高嘉伶' , null  , 022959-2123 , '新北市板桥区馆前东路82-84号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '台湾爱情万岁'  , '台北市中山区中山北路116巷25号1F' , '黄心怡' , null  , 8869-3275-8608  , '台北市中山区中山北路116巷25号1F' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '台湾第一婚礼'  , 'D-SWEDDING台湾省台北市大安区敦化南路一段294号2楼' , '邱纯玉 Ava wu'  , null  , 886-2-2755-1100 , 'D-SWEDDING台湾省台北市大安区敦化南路一段294号2楼' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '台北法国巴黎婚纱'  , '台湾台北市中山北路2段114号1F[邮编104]'  , '刘是伶' , null  , 886911021999  , '台湾台北市中山北路2段114号1F[邮编104]'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '台湾丽莎贝拉'  , '台中市中三民路二段114号' , '林小姐' , null  , 04-2222-3456  , '台中市中三民路二段114号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '高雄萝蔓朵' , '高雄市鼓山区美术东五路52号'  , '余明慧' , null  , 0932992203  , '高雄市鼓山区美术东五路52号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '台湾郑婷元' , '台湾省高雄市中山一路168号2F'  , '郑婷元' , null  , 0903627609  , '台湾省高雄市中山一路168号2F'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '台州DS'  , '台北市敦化南路一段294号2厅' , '邱总'  , null  , 0926372588  , '台北市敦化南路一段294号2厅' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '台州艾拉婚纱'  , '浙江省台州市路桥腾达路[338-4]号' , '徐晴'  , 15867663000 , 15867663000 , '浙江省台州市路桥腾达路[338-4]号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '台州吕小姐' , '上海市松江区三新北路900弄900号   精品馆地址'  , '吕小姐' , 136816660625  , 021-57769710  , '上海市松江区三新北路900弄900号   精品馆地址'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '台州何舟'  , '浙江省台州市路桥吉利大道22号' , '何舟'  , 13757635920 , 13757635920 , '浙江省台州市路桥吉利大道22号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '台州零零零' , '台州市椒江区白云山南路233号' , '林玲玲' , 13511444000 , null  , '台州市椒江区白云山南路233号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '台北Bear weddtng'  , '台北市八德路4段453栋7号1F'  , '吕小姐' , 932335845 , 932335845 , '台北市八德路4段453栋7号1F'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '浙江台州情婚纱' , '台州市路桥卖枝桥西路18号' , '王敏'  , 13957613888 , null  , '台州市路桥卖枝桥西路18号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '浙江台州芊樾'  , '台州市椒江区江城南路97号' , '王莎'  , 13857660275 , 13857660275 , '台州市椒江区江城南路97号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '台州许宁宁' , '浙江省台州市椒江区广场南路鑫泰广场E幢7楼706 宫主婚纱会馆' , '许宁宁' , 18057675333 , 18057675333 , '浙江省台州市椒江区广场南路鑫泰广场E幢7楼706 宫主婚纱会馆' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '太原郝睿'  , '太原市杏花岭区北大街112号'  , '郝睿'  , 13934235555 , 13934235555 , '太原市杏花岭区北大街112号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '唐山成远婚纱高级定制'  , '河北唐山市路北区友谊购物广场三层成远婚纱'  , '成远'  , 15033478777 , 15033478777 , '河北唐山市路北区友谊购物广场三层成远婚纱'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '天津Fancy婚纱' , '天津市和平区慎益大街6-4301'  , '景馨'  , 18602271255 , 18602271255 , '天津市和平区慎益大街6-4301'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '天津GK'  , '天津市红桥区南运河南路海河停车场'  , '段段'  , 18222826266 , 15712288027 , '天津市红桥区南运河南路海河停车场'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '天津LG'  , '天津市和平区大沽北路160号（与烟台道交口）璐娜婚纱摄影 礼服部'  , '李晔'  , 15922188080 , 15922188080 , '天津市和平区大沽北路160号（与烟台道交口）璐娜婚纱摄影 礼服部'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '天津My Weding' , '天津市红桥区南运河南路海河停车场'  , '段金爽' , 18222826266 , null  , '天津市红桥区南运河南路海河停车场'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '天津巴黎春天'  , '天津和平区和平路270号'  , '王喆'  , 13116064177 , null  , '天津和平区和平路270号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '天津花田喜铺'  , '天津市和平区南京路85号君隆广场B座4层'  , '宫紫凡' , 13132175793 , 13132175793 , '天津市和平区南京路85号君隆广场B座4层'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '天津姜鹏'  , '天津市红桥区丁字沽风采里20-303'  , '姜鹏'  , 13821000138 , 022-26373143  , '天津市红桥区丁字沽风采里20-303'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '天津李秋君' , '天津市和平区解放北路泰安园道五大院1号院2-1-202' , '李秋君' , 18622551886 , 13920393955 , '天津市和平区解放北路泰安园道五大院1号院2-1-202' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '天津罗亚婚纱摄影'  , '天津市南开区东马路新世界广场6楼'  , '杨浩元' , 13920398853 , null  , '天津市南开区东马路新世界广场6楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '天津钱华'  , '天津市和平区福安大街新文化花园新丽居C座802室'  , '钱华'  , 13820108898 , null  , '天津市和平区福安大街新文化花园新丽居C座802室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '天津三妮风尚'  , '天津市南开区长江道92号D107 三妮风尚' , '刘思勤' , 18622601810 , 18622601810 , '天津市南开区长江道92号D107 三妮风尚' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '天津张帅'  , '天津市南开区城厢中路檀园府59-2' , '张帅'  , 13821799999 , 13821799999 , '天津市南开区城厢中路檀园府59-2' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '天津赵洋'  , '天津市河西区友谊南路与俀江通交口海逸长洲瀚景园5-1-1002' , '赵洋'  , 15900280926 , 15900280926 , '天津市河西区友谊南路与俀江通交口海逸长洲瀚景园5-1-1002' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '宁波王开婚纱'  , '宁波市海曙区振明路514号' , '小唐'  , 13857886782 , null  , '宁波市海曙区振明路514号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '旺族婚纱'  , '哈市太平南二道街16号 龙祥旅店'  , '郑凯'  , 18249008000 , null  , '哈市太平南二道街16号 龙祥旅店'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '嫁日新娘'  , '浙江省温岭市三星大道18-38号'  , '陈英'  , 13867666168 , 13867666168 , '浙江省温岭市三星大道18-38号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州周女士' , ' 温州市瓯海区联众华庭东2206'  , '周女士' , 13868306153 , null  , ' 温州市瓯海区联众华庭东2206'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州1088婚纱'  , '浙江省温州市鹿城区环城东路208号' , '叶倚伶' , 13758701016 , null  , '浙江省温州市鹿城区环城东路208号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州GK'  , '温州市鹿城区锦绣路万川锦苑1幢202韩国艺匠礼服部 温州GK'  , '王菲'  , 13916796608 , 13916796608 , '温州市鹿城区锦绣路万川锦苑1幢202韩国艺匠礼服部 温州GK'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州爱丽斯婚纱' , '温州市人民中路环球大厦1305' , '陈小姐' , 13587696960 , 13587696960 , '温州市人民中路环球大厦1305' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州爱琴沙邸'  , '温州市望江东路42号一楼'  , '丁小姐' , 13989707100 , null  , '温州市望江东路42号一楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州冰冰'  , '温州市鹿城区吴桥路鸿盛锦园5栋1601室'  , '冰冰'  , 13819746388 , null  , '温州市鹿城区吴桥路鸿盛锦园5栋1601室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州市蔡凌之'  , '温州市鹿城区江滨西路九洲大厦589号'  , '蔡凌之' , 13676492992 , null  , '温州市鹿城区江滨西路九洲大厦589号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州苍南嫁衣'  , '温州市鹿城区望江东路39-1号' , '娜娜'  , 13857185321 , null  , '温州市鹿城区望江东路39-1号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州陈宣羽' , '浙江省温州市鹿城区南大门联合广场1幢16E' , '陈宣羽' , 13968879406 , null  , '浙江省温州市鹿城区南大门联合广场1幢16E' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州陈雪'  , '温州市鹿城区大南门路国正大厦四楼一层700平米形象馆'  , '陈雪'  , 13221176686 , 88688262  , '温州市鹿城区大南门路国正大厦四楼一层700平米形象馆'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州多奇维亚嫁衣馆       '  , '温州市鹿城区莲花路月光大厦1栋204室' , '胡蝶蝶' , 13957751022 , null  , '温州市鹿城区莲花路月光大厦1栋204室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '多奇维亚嫁衣馆' , '温州市鹿城区府前街胜利商厦二楼203多奇维亚嫁衣馆' , '王祥祥' , 15888456025 , null  , '温州市鹿城区府前街胜利商厦二楼203多奇维亚嫁衣馆' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州凤凰印象'  , '温州市西城路221号金汇广场2楼'  , '徐晨'  , 13968839972 , null  , '温州市西城路221号金汇广场2楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州好莱迪嫁衣宫坊' , '温州市龙湾区永中永康路2-6号' , '金健'  , 13738369009 , null  , '温州市龙湾区永中永康路2-6号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州花嫁'  , '浙江省温州市龙港镇百六街60-62' , '周晴'  , 13567707317 , null  , '浙江省温州市龙港镇百六街60-62' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州黄莹莹' , '温州市第一桥15号  为爱专一' , '黄莹莹' , 18857857809 , null  , '温州市第一桥15号  为爱专一' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州吉婚坊' , '浙江省温州市永嘉县鸥北镇罗浮大街113-115号'  , '吴琴香' , 13868886677 , null  , '浙江省温州市永嘉县鸥北镇罗浮大街113-115号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '嫁日新娘'  , '温州市欧洲城中心大楼北首1F(凯旋门进)  '  , '宣总'  , 13806888766 , null  , '温州市欧洲城中心大楼北首1F(凯旋门进)  '  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州嫁衣工坊'  , '温州市鹿城区江滨西路新业大厦5栋806室'  , '雷祖泽' , 13676462497 , null  , '温州市鹿城区江滨西路新业大厦5栋806室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州金珊珊' , '浙江省温州市鹿城区朔门街25号' , '金珊珊' , 15888401338 , null  , '浙江省温州市鹿城区朔门街25号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州卡卡公馆             ' , '温州市鹿城区江滨西路521号康华大厦D栋一层卡卡嫁衣公馆'  , '卡  卡'  , 13806688631 , null  , '温州市鹿城区江滨西路521号康华大厦D栋一层卡卡嫁衣公馆'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州卡洛尔.时尚婚纱造型  '  , '温州龙湾万达1号写字楼807-810号' , ' 张海霞'  , 13567791808 , null  , '温州龙湾万达1号写字楼807-810号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州李步贵 丁潇潇' , '温州市鹿城区望江东路42号' , '丁潇潇' , 13989707100 , null  , '温州市鹿城区望江东路42号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州李大唯' , '浙江省温州市瓯海区娄桥街道华鸿中央城5幢701' , '李大唯' , 13736755918 , null  , '浙江省温州市瓯海区娄桥街道华鸿中央城5幢701' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州林彩琴' , '温州市龙湾区万达金街4号门2-118皇后镇' , '林彩琴' , 1367679880  , null  , '温州市龙湾区万达金街4号门2-118皇后镇' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州罗门婚纱'  , '浙江省温州市平阳县鳌江镇新河路274号 罗门婚纱'  , '邱海碧' , 13958929566 , null  , '浙江省温州市平阳县鳌江镇新河路274号 罗门婚纱'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州洛唯思婚纱' , '温州鹿城区大南门国正大厦4楼  '  , '陈雪'  , 0577-88688262 , null  , '温州鹿城区大南门国正大厦4楼  '  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州曼思嫁衣'  , '温州市文成路中银大厦1106'  , '陈小姐' , 15968780660 , null  , '温州市文成路中银大厦1106'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州米房创意园' , '温州市鹿城区瓯江路5255米房创意园D栋201.202.301室'  , 'Erica' , 15888278883 , null  , '温州市鹿城区瓯江路5255米房创意园D栋201.202.301室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州潘多拉礼服' , '温州市人民中路金煌大楼205室' , '冯茜'  , 13758457887 , null  , '温州市人民中路金煌大楼205室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州倾晨嫁衣'  , '温州市鹿区南方大厦4C倾晨嫁衣' , '邹玉'  , 18668710831 , null  , '温州市鹿区南方大厦4C倾晨嫁衣' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '时尚新娘嫁衣馆' , '温州市鹿城区人民中路鹿开大楼二楼'  , '蒋玮'  , 13968880991 , null  , '温州市鹿城区人民中路鹿开大楼二楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州丁一凌' , '温州市勤奋路月湖小区2A-304室' , '丁一凌' , 13989707100 , null  , '温州市勤奋路月湖小区2A-304室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州周琦'  , '温州市鹿城区黎明工业区东鸥智库20号楼2楼' , '周琦'  , 13738716686 , null  , '温州市鹿城区黎明工业区东鸥智库20号楼2楼' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州我的嫁衣'  , '温州市第一桥126号2F'  , '张聪聪' , 13857763007 , 13587401664 , '温州市第一桥126号2F'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州为爱专一'  , '温州市六虹桥路钢材市场A幢三楼' , '陈凤华' , 0577-56609955 , null  , '温州市六虹桥路钢材市场A幢三楼' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州薇安嫁衣'  , '温州市鹿城区南塘4组团1-501'  , '潘丽丽' , 13757721430 , 0577-88866515 , '温州市鹿城区南塘4组团1-501'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州为爱专一'  , '温州市钢材市厂A座四楼' , '杨月节' , 13566295060 , null  , '温州市钢材市厂A座四楼' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州维纳斯' , '温州鳌江镇新河南路40-42号' , '陈佳佳' , 13587530903 , 0577-23895666 , '温州鳌江镇新河南路40-42号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州项海晓' , '浙江省温州市鹿城区龙泉巷亦美大厦四幢一单元601'  , '项海晓' , 13957736076 , null  , '浙江省温州市鹿城区龙泉巷亦美大厦四幢一单元601'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州杨小姐' , '浙江省温州市荷花路荷花大厦2栋407室 '  , '杨小姐' , 18072106099 , null  , '浙江省温州市荷花路荷花大厦2栋407室 '  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '溢彩造型'  , '温州市鹿城区黎明工业区东欧支库20号楼2楼' , '周琦'  , 13758701630 , null  , '温州市鹿城区黎明工业区东欧支库20号楼2楼' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州 张蓓蕾'  , '温州市车站大道292号喜来登3楼'  , '张蓓蕾' , 13777799119 , null  , '温州市车站大道292号喜来登3楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州张赛君' , '浙江省温州市鹿城区联合广场1栋16E 铭造型'  , '张赛君' , 15958790757 , null  , '浙江省温州市鹿城区联合广场1栋16E 铭造型'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州朱千千' , '浙江省温州市鹿城区划龙桥金色家园北区3幢101' , '朱千千' , 18858822577 , null  , '浙江省温州市鹿城区划龙桥金色家园北区3幢101' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '温州朱小飞' , '温州市龙湾区永中永康路2-6号' , '朱小飞' , 13506647312 , null  , '温州市龙湾区永中永康路2-6号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '江苏无锡情缘坊' , '无锡市塘南招南城5#1楼-177#情缘坊'  , '邵静秀' , 18915322535 , null  , '无锡市塘南招南城5#1楼-177#情缘坊'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '武汉GK'  , '湖北省武汉市武昌区楚河汉街外街商铺号【松竹路万达百货2号门对面】J4-2-1-J4-2-4' , '唐唐'  , 13501786229 , null  , '湖北省武汉市武昌区楚河汉街外街商铺号【松竹路万达百货2号门对面】J4-2-1-J4-2-4' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '武汉MY WEDDING'  , '武汉市武昌区楚河汉街外街J4-2-1-J4-2-4内街药圣广场礼仪部'  , '陈小姐' , 18507186046 , null  , '武汉市武昌区楚河汉街外街J4-2-1-J4-2-4内街药圣广场礼仪部'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '武汉贝拉维拉'  , '武汉市汉阳区龟北路1号创意园区20号'  , '张箐'  , 15207162665 , null  , '武汉市汉阳区龟北路1号创意园区20号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '武汉甘娜'  , '武汉市汉阳区古秦台龟山水路汉阳造17号' , '甘娜'  , 15072345471 , null  , '武汉市汉阳区古秦台龟山水路汉阳造17号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '武汉  吴丹丹' , '   武汉市洪山区楚河汉街北岸婚庆文化街1楼A4'  , '吴丹丹' , 13986001532 , null  , '   武汉市洪山区楚河汉街北岸婚庆文化街1楼A4'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '武汉李丹'  , '湖北省武汉市江岸区沿江大道兰陵路口江滩公园内 薇拉上善造型' , '李丹'  , 18672561816 , null  , '湖北省武汉市江岸区沿江大道兰陵路口江滩公园内 薇拉上善造型' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '武汉商铺'  , '武汉市武昌区楚河汉街外街商铺号J4-2-1  J4-2-4' , '佳佳'  , 13621611981 , null  , '武汉市武昌区楚河汉街外街商铺号J4-2-1  J4-2-4' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '武汉邵静萍' , '武汉市扬子街8#'  , '邵静萍' , 13018099078 , null  , '武汉市扬子街8#'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '武汉薇拉皇后'  , '武汉市江岸区芦沟桥路2号武汉天地12-4-1'  , '张箐'  , 13986269103 , null  , '武汉市江岸区芦沟桥路2号武汉天地12-4-1'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '陕西高静'  , '陕西省西安市碑林区南门外珠江时代广场B座901室'  , '高静'  , 18629262699 , null  , '陕西省西安市碑林区南门外珠江时代广场B座901室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '西安绝对人气'  , '陕西省西安市长安中路237号'  , '张小斌' , 18681828000 , null  , '陕西省西安市长安中路237号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '陕西西安蒙娜丽莎'  , '西安天地华宇物流航天大道'  , '宋红艳' , 15529629916 , null  , '西安天地华宇物流航天大道'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '陕西西安蒙娜丽莎东大街' , '西安市碑林路东大街端履门十字西北角西安蒙娜丽莎' , '海婕'  , 15693806611 , null  , '西安市碑林路东大街端履门十字西北角西安蒙娜丽莎' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '西安叶文'  , '西安市碑林区南大街真爱粉巷里大厦607室'  , '叶文'  , 17791007777 , 17791007777 , '西安市碑林区南大街真爱粉巷里大厦607室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '香港唯美主义'  , '香港九龙广东道1155号日异广场1201室' , '曹小姐' , null  , 852-63326603  , '香港九龙广东道1155号日异广场1201室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '何詠珊' , '香港九龙尖沙咀彌敦道132-134号美丽华大厦8号楼801-5室'  , '何詠珊' , 85298120303 , null  , '香港九龙尖沙咀彌敦道132-134号美丽华大厦8号楼801-5室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '香港李小姐' , '香港上环文咸东街135号文咸东135商业中心19楼1902室'  , '李小姐' , null  , 97503262  , '香港上环文咸东街135号文咸东135商业中心19楼1902室'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '香港梁小姐' , '香港尖沙咀漆咸围顺丰门市（自取）'  , '梁小姐' , null  , 0852-92891818 , '香港尖沙咀漆咸围顺丰门市（自取）'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '香港林小姐' , '香港九龙了雨墩道383号平安大楼16号地铺' , '林小姐' , 13672599997 , 852-21100186  , '香港九龙了雨墩道383号平安大楼16号地铺' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '新疆艾美国际婚纱'  , '新疆乌市新华北路190号（小西门中信银行大厦对面）' , '王欣'  , 13609976230 , null  , '新疆乌市新华北路190号（小西门中信银行大厦对面）' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '新疆黄丽霞' , '新疆乌鲁木齐大西门宏大购物中心5楼13号'  , '黄丽霞' , null  , 0991-7767913  , '新疆乌鲁木齐大西门宏大购物中心5楼13号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '徐州微微新娘'  , '江苏省徐州市云龙区中山南路173-13号'  , '赵魏'  , 18626038889 , 18626038889 , '江苏省徐州市云龙区中山南路173-13号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '烟台情中情' , '烟台市西南河路232号' , '赵小梅' , 13883552288 , 0535-6208170  , '烟台市西南河路232号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '长春GK ' , '长春市 绿园区西安大路与清溪路交汇 金地梧桐华府8号楼 '  , '张丽娜' , 18710113029 , 18710113029 , '长春市 绿园区西安大路与清溪路交汇 金地梧桐华府8号楼 '  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '长春金夫人' , '长春市绿园区正阳街4008号[万达车城后身]金夫人婚纱摄影' , '杨雪梅' , 15043034211 , null  , '长春市绿园区正阳街4008号[万达车城后身]金夫人婚纱摄影' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '长春施华洛' , '长春市大经路448号世华洛主流摄影' , '刘迪'  , 18604435511 , null  , '长春市大经路448号世华洛主流摄影' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '长春燕子'  , '长春市同志街与隆礼胡同交汇西50米Me时尚彩妆.婚纱馆' , '燕子'  , 13644407655 , 0431-81857949 , '长春市同志街与隆礼胡同交汇西50米Me时尚彩妆.婚纱馆' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '长纱艾特婚纱'  , '湖南省长纱市五一大道90号' , '李斯'  , 13618461994 , null  , '湖南省长纱市五一大道90号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '长沙GK'  , '湖南省长沙市芙蓉区长岛路8号燕山小学对面 ' , '张晓萌' , 18514733100 , 18514733100 , '湖南省长沙市芙蓉区长岛路8号燕山小学对面 ' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '艾特婚纱'  , '长沙市曙光北路119号798城市体验中心 艾特婚纱' , '李斯'  , 13618461994 , null  , '长沙市曙光北路119号798城市体验中心 艾特婚纱' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '浙江艾米'  , '浙江省慈溪市浒山水门路215号' , '艾米'  , 18006846866 , null  , '浙江省慈溪市浒山水门路215号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '浙江胡先生' , '浙江东阳市城南东路190号' , '胡先生' , 15305792505 , null  , '浙江东阳市城南东路190号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '浙江欢颜婚纱摄影'  , '浙江省金华市欢颜婚纱摄影'  , '经理'  , 15657996584 , 15657996584 , '浙江省金华市欢颜婚纱摄影'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '浙江皇嘉嫁衣'  , '浙江省温州市永嘉县鸥北四季酒店西门河边' , '孙小姐' , 13868886677 , 0577-67189989 , '浙江省温州市永嘉县鸥北四季酒店西门河边' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '浙江麦田视觉'  , '浙江宁波中心东路455号曙光大厦4F'  , '黄霞'  , 13967871380 , 13967871380 , '浙江宁波中心东路455号曙光大厦4F'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '浙江青田浓浓'  , '温州市瑞安市塘下镇塘川南街48-50号' , '冯大姐' , 15158755272 , null  , '温州市瑞安市塘下镇塘川南街48-50号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '浙江省毕丽丽'  , '浙江省义乌市工人北路138号3楼'  , '毕丽丽' , 13738948288 , null  , '浙江省义乌市工人北路138号3楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '浙江省慈溪市徐小姐' , '浙江省慈溪市虞波北园东区14号102室' , '徐佳瑜' , 15990245431 , 15990245431 , '浙江省慈溪市虞波北园东区14号102室' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '浙江台州杜娟'  , '浙江台州椒江区春潮路153' , '杜娟'  , 13606684346 , 13606684346 , '浙江台州椒江区春潮路153' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '浙江台州芊樾'  , '台州市椒江区江城南路97号' , '王莎'  , 13857660275 , 0576-88855075 , '台州市椒江区江城南路97号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '浙江台州情婚纱' , '台州市路桥卖枝桥西路18号' , '王敏'  , 13957613888 , null  , '台州市路桥卖枝桥西路18号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '天使嫁衣'  , '浙江省洞头县中心街38号'  , '叶丽娜' , 13750905300 , null  , '浙江省洞头县中心街38号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '浙江杨杨'  , '浙江省金华市义乌宾王商贸区五街85号2楼'  , '杨杨'  , 13777921018 , null  , '浙江省金华市义乌宾王商贸区五街85号2楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '浙江舟山丽人汇婚纱摄影' , '浙江定海解放西路191-197号'  , '毛红卫' , 15606800680 , 4000580968  , '浙江定海解放西路191-197号'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '郑州GK'  , '郑州市郑东新区东风南路如意西路建业总部港B座韩国艺匠礼服部' , '邸亚男' , 15026976801 , 15026976801 , '郑州市郑东新区东风南路如意西路建业总部港B座韩国艺匠礼服部' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '河南高源'  , '河南省郑州市金水东路与心怡路交叉口绿地新都会B座12B层1041-1412' , '高源'  , 13838333111 , null  , '河南省郑州市金水东路与心怡路交叉口绿地新都会B座12B层1041-1412' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '郑州金夫人' , '河南省郑州市惠济区迎宾路街道东西会隔壁金夫人婚纱摄影'  , '金夫人' , 13811033166 , null  , '河南省郑州市惠济区迎宾路街道东西会隔壁金夫人婚纱摄影'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '郑州范金芝' , '河南郑州金水区西里路46号' , '王晴'  , 13949131226 , null  , '河南郑州金水区西里路46号' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '郑州唯妮嫁衣'  , '河南省郑州市金水区国贸新领地4号楼1单元2605'  , '香香'  , 15038158660 , null  , '河南省郑州市金水区国贸新领地4号楼1单元2605'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '郑州伊丽莎白'  , '郑州市中原区桐柏路棉纺路锦艺一期9#2#3103'  , '张先生' , 18638537676 , null  , '郑州市中原区桐柏路棉纺路锦艺一期9#2#3103'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '中恒钻石有限公司'  , '义乌市国际商贸城一区[东]2楼4单元1街102572店面'  , '许伟雄' , 18877441166 , 18867574499 , '义乌市国际商贸城一区[东]2楼4单元1街102572店面'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '重庆GK'  , '重庆渝北区金开大道88号棕榈岛2号楼 重庆GK' , '黄娇娇' , 15730628286 , null  , '重庆渝北区金开大道88号棕榈岛2号楼 重庆GK' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '重庆LG'  , '重庆江北区江北嘴聚贤岩广场力帆中心1号楼7层 璐娜礼服部 ' , '黄巧林' , 13764434893 , null  , '重庆江北区江北嘴聚贤岩广场力帆中心1号楼7层 璐娜礼服部 ' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '重庆My Wedding'  , '重庆市渝中区民生路235号海航保利国际中心第三楼'  , '屈梦蝶' , 15821778660 , null  , '重庆市渝中区民生路235号海航保利国际中心第三楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '重庆柏纱礼服'  , '重庆市渝中解放碑民权路26号英利购物中心3楼'  , '陈爱云' , 15922523693 , 15922523693 , '重庆市渝中解放碑民权路26号英利购物中心3楼'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '巴蜀中学冯书'  , '重庆市渝中区黄花园巴蜀大厦23-5巴蜀中学内'  , '冯书'  , 18623423344 , null  , '重庆市渝中区黄花园巴蜀大厦23-5巴蜀中学内'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '重庆金夫人' , '重庆渝中区民族路169号纽约大厦18楼进夫人采购部陈丽' , '陈丽'  , 13617613777 , null  , '重庆渝中区民族路169号纽约大厦18楼进夫人采购部陈丽' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '重庆李爱琴' , '重庆市沙坪坝区凤天路升伟新时店5栋' , '李爱琴' , 18696606234 , 18696606234 , '重庆市沙坪坝区凤天路升伟新时店5栋' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '重庆李姣'  , '重庆市渝北区北部新区经开园留云路1号保利高尔夫花园155栋' , '李姣'  , 18696540806 , 18696540806 , '重庆市渝北区北部新区经开园留云路1号保利高尔夫花园155栋' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '重庆李小姐' , '重庆市渝中区解放碑美美时代豪宛D座26-2' , '李小姐' , 18008333330 , 18008333330 , '重庆市渝中区解放碑美美时代豪宛D座26-2' );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '重庆罗亚陈爱云' , '重庆市渝中区民权路26号英利商场3楼L301A-7号商铺[柏纱礼服]'  , '陈爱云' , 15922523693 , 15922523693 , '重庆市渝中区民权路26号英利商场3楼L301A-7号商铺[柏纱礼服]'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '重庆王鑫'  , '重庆市江北区南桥寺天下城4-13-7'  , '王鑫'  , 13308366973 , null  , '重庆市江北区南桥寺天下城4-13-7'  );        
INSERT INTO `xcrm`.`customer`(`company`,`custAddr`,`name`,`phone`,`cellphone`,`shiptoAddr`)VALUES(  '陕西省周山林'  , '陕西省西安市碑林区李家村万达广场2栋1单元24楼'  , '周山林' , 18291445712 , null  , '陕西省西安市碑林区李家村万达广场2栋1单元24楼'  );        

