CREATE TABLE `xcrm`.`salesseason` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `desc` VARCHAR(500) NULL,
  PRIMARY KEY (`idsalesseason`));

  
INSERT INTO `xcrm`.`salesseason` (`name`, `desc`) VALUES ('2017年夏季款', '2017年上市的夏季款');

ALTER TABLE `xcrm`.`product` 
ADD COLUMN `salesseason` INT(11) NULL DEFAULT 1 AFTER `edituser`;

