ALTER TABLE `xcrm`.`salaryitem` 
ADD COLUMN `comment` VARCHAR(450)  AFTER `status`;

ALTER TABLE `xcrm`.`workitemallocation` 
CHANGE COLUMN `weight` `weight` DOUBLE NULL DEFAULT NULL ;

ALTER TABLE `xcrm`.`salary` 
ADD COLUMN `baseamount` DOUBLE NULL DEFAULT 0 AFTER `status`;

