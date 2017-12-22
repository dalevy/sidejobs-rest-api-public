CREATE TABLE `areas` (
  `id` varchar(5) NOT NULL,
  `name` varchar(35) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `categories` (
  `id` varchar(5) NOT NULL,
  `name` varchar(35) NOT NULL,
  `status` varchar(15) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `categories_areas` (
  `category_id` varchar(5) NOT NULL,
  `area_id` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `specialties` (
  `id` varchar(5) NOT NULL,
  `name` varchar(35) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE ALGORITHM=UNDEFINED DEFINER=`isxdb`@`%` SQL SECURITY DEFINER VIEW `join_categories_areas` AS select `ca`.`category_id` AS `category_id`,`c`.`name` AS `category_name`,`c`.`status` AS `status`,`ca`.`area_id` AS `area_id`,`a`.`name` AS `area_name` from ((`categories` `c` join `areas` `a`) join `categories_areas` `ca`) where ((`ca`.`category_id` = `c`.`id`) and (`ca`.`area_id` = `a`.`id`));
