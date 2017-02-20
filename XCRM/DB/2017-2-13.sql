ALTER TABLE `xcrm`.`order` 
ADD COLUMN `paymenttype` INT NULL;

ALTER TABLE `xcrm`.`order` 
ADD COLUMN `paid` FLOAT NULL;

ALTER TABLE `xcrm`.`order` 
ADD COLUMN `deliverytime` DATETIME NULL DEFAULT NULL;


ALTER TABLE `xcrm`.`order` 
ADD COLUMN `paymentcomments` VARCHAR(2000) NULL AFTER `status`;

CREATE TABLE `payment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customerid` int(11) DEFAULT NULL,
  `paymenttype` int(11) DEFAULT NULL,
  `paid` float DEFAULT NULL,
  `comments` varchar(450) DEFAULT NULL,
  `paymenttime` datetime DEFAULT NULL,
  `orderno` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


