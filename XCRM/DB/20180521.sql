ALTER TABLE `xcrm`.`order` 
ADD COLUMN `taxrate` DECIMAL(16,6) NOT NULL DEFAULT 0 AFTER `workstatus`;
