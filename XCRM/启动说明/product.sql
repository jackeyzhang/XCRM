CREATE TABLE `prdpic` (
  `idprdpic` int(11) NOT NULL AUTO_INCREMENT,
  `fielname` varchar(45) NOT NULL,
  `prdid` int(11) DEFAULT NULL,
  PRIMARY KEY (`idprdpic`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `code` varchar(45) NOT NULL,
  `barCode` varchar(45) DEFAULT NULL,
  `cid` int(11) NOT NULL,
  `unit` varchar(45) DEFAULT NULL,
  `period` varchar(45) DEFAULT NULL,
  `department` varchar(45) DEFAULT NULL,
  `point` varchar(45) DEFAULT NULL,
  `remark` longtext,
  `ttt` datetime DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `editdate` datetime DEFAULT NULL,
  `createuser` int(11) DEFAULT NULL,
  `edituser` int(11) DEFAULT NULL,
  `picture` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `picture` FOREIGN KEY (`id`) REFERENCES `prdpic` (`idprdpic`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

