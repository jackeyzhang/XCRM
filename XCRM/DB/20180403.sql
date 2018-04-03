ALTER TABLE `xcrm`.`department` 
ADD COLUMN `status` INT NOT NULL COMMENT '0 inactive\n1 active' AFTER `name`;

ALTER TABLE `xcrm`.`department` 
CHANGE COLUMN `status` `status` INT(11) NOT NULL DEFAULT 1 COMMENT '0 inactive\n1 active' ;

