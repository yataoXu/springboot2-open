CREATE TABLE `bus_receiver` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `region_code` varchar(50) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `enname` varchar(50) DEFAULT NULL,
  `memberfamily` int(3) DEFAULT NULL,
  `create_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

