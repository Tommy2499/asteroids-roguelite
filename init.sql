CREATE TABLE IF NOT EXISTS `Users` (
    `user_id` INT NOT NULL AUTO_INCREMENT,
    `user_name` VARCHAR(50) NOT NULL UNIQUE,
    `user_password` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`user_id`)
);
ALTER TABLE `Users` AUTO_INCREMENT = 10001;

CREATE TABLE IF NOT EXISTS `Profiles` (
    `profile_id` INT NOT NULL AUTO_INCREMENT,
    `user_id` INT NOT NULL,
    `profile_name` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`profile_id`),
    FOREIGN KEY (`user_id`) REFERENCES `Users`(`user_id`) ON DELETE CASCADE,
    CONSTRAINT `unique_profile_per_user` UNIQUE (`user_id`, `profile_name`)
);
ALTER TABLE `Profiles` AUTO_INCREMENT = 15001;

CREATE TABLE IF NOT EXISTS `Scores` (
    `score_id` INT NOT NULL AUTO_INCREMENT,
    `profile_id` INT NOT NULL,
    `difficulty` ENUM('EASY', 'MEDIUM', 'HARD') NOT NULL,
    `score` INT NOT NULL,
    `level` INT NOT NULL,
    `duration_seconds` INT NOT NULL,
    `time_played` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`score_id`),
    FOREIGN KEY (`profile_id`) REFERENCES `Profiles`(`profile_id`) ON DELETE CASCADE
);
ALTER TABLE `Scores` AUTO_INCREMENT = 20001;

CREATE TABLE IF NOT EXISTS `Settings` (
    `setting_id` INT NOT NULL AUTO_INCREMENT,
    `profile_id` INT NOT NULL,
    `sound` BOOLEAN DEFAULT TRUE,
    `graphics` ENUM('low', 'medium', 'high') DEFAULT 'medium',
    PRIMARY KEY (`setting_id`),
    FOREIGN KEY (`profile_id`) REFERENCES `Profiles`(`profile_id`) ON DELETE CASCADE
);
ALTER TABLE `Settings` AUTO_INCREMENT = 30001;

CREATE VIEW `Leaderboard` AS 
SELECT 
    `Users`.`user_name`, 
    `Profiles`.`profile_name`,
    `Scores`.`score`, 
    `Scores`.`level`, 
    `Scores`.`duration_seconds`, 
    `Scores`.`time_played`
FROM `Scores`
JOIN `Profiles` ON `Scores`.`profile_id` = `Profiles`.`profile_id`
JOIN `Users` ON `Profiles`.`user_id` = `Users`.`user_id`
ORDER BY `Scores`.`score` DESC 
LIMIT 100;

