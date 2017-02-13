ALTER TABLE `xcrm`.`order` 
ADD COLUMN `paymenttype` INT NULL AFTER `comments`;

ALTER TABLE `xcrm`.`order` 
ADD COLUMN `paid` FLOAT NULL AFTER `paymenttype`;

ALTER TABLE `xcrm`.`order` 
ADD COLUMN `deliverytime` DATETIME NULL DEFAULT NULL AFTER `paid`;

