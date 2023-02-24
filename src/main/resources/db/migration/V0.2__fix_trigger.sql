USE `db`;
DELIMITER $$

USE `db`$$
DROP TRIGGER IF EXISTS `db`.`rating_AFTER_INSERT` $$
USE `db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `db`.`rating_AFTER_INSERT` AFTER INSERT ON `rating` FOR EACH ROW
BEGIN
    UPDATE users 
	SET 
		avg_rating = (select avg(rating) from db.rating where db.rating.user_id = new.user_id)
	WHERE
		user_id = NEW.user_id;
END$$


DELIMITER ;