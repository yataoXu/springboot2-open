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

CREATE TABLE `bus_group` (
  `id` bigint(20) NOT NULL,
  `group_name` varchar(50) DEFAULT NULL,
  `group_code` varchar(50) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `bus_group_receiver_map` (
  `id` bigint(20) NOT NULL,
  `receiver_id` bigint(20) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `bus_region` (
  `id` bigint(20) DEFAULT NULL,
  `region_code` varchar(50) DEFAULT NULL,
  `region_name` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
