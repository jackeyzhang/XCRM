ALTER TABLE `xcrm`.`order` 
ADD COLUMN `paymenttype` INT NULL;

ALTER TABLE `xcrm`.`order` 
ADD COLUMN `paid` FLOAT NULL;

ALTER TABLE `xcrm`.`order` 
ADD COLUMN `deliverytime` DATETIME NULL DEFAULT NULL;

ALTER TABLE `xcrm`.`order` 
ADD COLUMN `status` INT NULL DEFAULT NULL COMMENT '1 已付款\n2 未付款\n3 部分付款';


ALTER TABLE `xcrm`.`order` 
ADD COLUMN `paymentcomments` VARCHAR(2000) NULL AFTER `status`;

