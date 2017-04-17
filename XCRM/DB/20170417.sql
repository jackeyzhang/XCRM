ALTER TABLE `bookitem` 
DROP COLUMN `addfee`,
ADD COLUMN `additionfee` DECIMAL(16,6) NULL DEFAULT 0 AFTER `discount`;
