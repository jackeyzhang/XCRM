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
  
  
-- add work item table
 
CREATE TABLE `xcrm`.`workitem` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `index` INT NOT NULL DEFAULT 0,
  `dep` INT NOT NULL,
  `userid` INT NULL,
  `weight` INT NULL,
  `status` INT NOT NULL DEFAULT 0,
  `spendtime` INT NULL COMMENT 'minutes',
  PRIMARY KEY (`id`));

  
-- add work item tempalte table
CREATE TABLE `xcrm`.`workitemtemplate` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `index` INT NOT NULL DEFAULT 0,
  `userid` INT NULL,
  `weight` INT NULL,
  `status` INT NOT NULL DEFAULT 0,
  `dep` INT NULL,
  PRIMARY KEY (`id`));

-- create table department
CREATE TABLE `xcrm`.`department` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(450) NOT NULL,
  PRIMARY KEY (`id`));

  
-- init department records
INSERT INTO `department` VALUES (1,'管理层'),(2,'设计部'),(3,'制版部'),(4,'裁剪部'),(5,'车工部'),(6,'制花部'),(7,'贴花部'),(8,'手工部'),(9,'库管部');
  

CREATE TABLE `workflowtemplate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `enable` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `xcrm`.`workflowanditemtemplate` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `workflowtemplate` INT NOT NULL,
  `workitemtemplate` INT NOT NULL,
  PRIMARY KEY (`id`));
  
  
ALTER TABLE `xcrm`.`workflowanditemtemplate` 
ADD INDEX `workflowtempalte_idx` (`workflowtemplate` ASC),
ADD INDEX `workitemtemplate_idx` (`workitemtemplate` ASC);
ALTER TABLE `xcrm`.`workflowanditemtemplate` 
ADD CONSTRAINT `workflowtemplate`
  FOREIGN KEY (`workflowtemplate`)
  REFERENCES `xcrm`.`workflowtemplate` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `workitemtemplate`
  FOREIGN KEY (`workitemtemplate`)
  REFERENCES `xcrm`.`workitemtemplate` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  

ALTER TABLE `xcrm`.`product` 
ADD COLUMN `workflow` INT(11) NULL AFTER `salesseason`;


