CREATE TABLE IF NOT EXISTS `app_user` (
    `id`        int(10) unsigned NOT NULL AUTO_INCREMENT,
    `username`  VARCHAR(50),
    `password`  VARCHAR(100),
    `email`     VARCHAR(100),
    `deleted`   int NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_idx_username` (`username`),
    UNIQUE KEY `uniq_idx_email` (`email`)
) engine = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `auth_role` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) NOT NULL COMMENT 'name of role',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `description` varchar(50) DEFAULT '' COMMENT 'description of role',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `auth_user_role` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL DEFAULT '0',
  `role_id` int NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `confirm_token` (
    `id`           int(10) unsigned NOT NULL AUTO_INCREMENT,
    `token`        VARCHAR(100),
    `created_at`   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `expires_at`   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `confirmed_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) engine = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;