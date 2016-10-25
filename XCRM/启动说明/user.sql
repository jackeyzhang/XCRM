CREATE TABLE `user` (
  `id` int(11) NOT NULL auto_increment,
  `nickname` varchar(200) NOT NULL,
  `department` varchar(200) NOT NULL,
  `title` varchar(200) NOT NULL,
  `username` varchar(200) NOT NULL,
  `password` varchar(200) NOT NULL,
  `isenable` char(1) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
