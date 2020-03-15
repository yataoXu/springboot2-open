CREATE TABLE `bus_group_receiver_map` (
  `id` bigint(20) NOT NULL,
  `receiver_id` bigint(20) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;