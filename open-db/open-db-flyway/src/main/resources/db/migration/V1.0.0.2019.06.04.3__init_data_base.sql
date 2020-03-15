CREATE TABLE `bus_group` (
  `id` bigint(20) NOT NULL,
  `group_name` varchar(50) DEFAULT NULL,
  `group_code` varchar(50) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
