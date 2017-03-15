ALTER TABLE `xcrm`.`order` 
ADD COLUMN `status` INT(11) NULL DEFAULT 1 COMMENT '1 已下订单\n2 已付定金\n3 已付全款\n4 订单取消';
