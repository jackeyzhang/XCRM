ALTER TABLE `xcrm`.`order` 
ADD COLUMN `taxrate` DECIMAL(16,6) NOT NULL DEFAULT 0 AFTER `workstatus`;


CREATE TABLE `xcrm`.`salary` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `product` INT NOT NULL,
  `workflowtemplateid` INT NOT NULL,
  `status` INT NULL DEFAULT 0,
  PRIMARY KEY (`id`));
 
  
CREATE TABLE `xcrm`.`salaryitem` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `salaryid` INT NOT NULL,
  `dep` INT NOT NULL,
  `amount` double DEFAULT NULL,
  `status` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `salaryitem-fk_idx` (`salaryid` ASC),
  CONSTRAINT `salaryitem-fk`
    FOREIGN KEY (`salaryid`)
    REFERENCES `xcrm`.`salary` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);


