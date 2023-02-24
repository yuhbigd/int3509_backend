ALTER TABLE `db`.`rating` 
ADD COLUMN `from_id` VARCHAR(36) NOT NULL AFTER `user_id`;
