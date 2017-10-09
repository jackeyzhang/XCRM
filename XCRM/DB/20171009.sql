-- change order table add workstatus column
ALTER TABLE `xcrm`.`order` 
ADD COLUMN `workstatus` INT(11) NOT NULL DEFAULT 0 COMMENT '0 means init status which is not start yet;' AFTER `totaldiscount`;


-- add a new table
CREATE TABLE `xcrm`.`workflow` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `bookitem` INT NOT NULL,
  `index` INT NOT NULL,
  `status` INT NOT NULL DEFAULT 0,
  `progress` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`));

ALTER TABLE `xcrm`.`workflow` 
ADD INDEX `bookitemfk_idx` (`bookitem` ASC);
ALTER TABLE `xcrm`.`workflow` 
ADD CONSTRAINT `bookitemfk`
  FOREIGN KEY (`bookitem`)
  REFERENCES `xcrm`.`bookitem` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


  
  
  