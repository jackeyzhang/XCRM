ALTER TABLE `xcrm`.`order` 
ADD COLUMN `totaldiscount` DECIMAL(16,6) NOT NULL DEFAULT 100 AFTER `status`;
