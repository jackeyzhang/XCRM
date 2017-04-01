ALTER TABLE `xcrm`.`bookitem` 
CHANGE COLUMN `status` `status` INT(2) NOT NULL COMMENT '0 初始化 1 激活 2 取消';