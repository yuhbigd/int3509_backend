ALTER TABLE `db`.`houses` 
DROP COLUMN `whole_house`,
CHANGE COLUMN `maintainance_fee` `maintenance_fee` DECIMAL(15,2) NULL DEFAULT NULL ,
CHANGE COLUMN `longtitude` `longitude` DECIMAL(9,6) NOT NULL,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`house_id`);
ALTER TABLE `db`.`house_has_images` 
DROP FOREIGN KEY `fk_houses_has_house_images_houses1`;
ALTER TABLE `db`.`house_has_images` 
CHANGE COLUMN `houses_id` `house_id` INT NOT NULL ;
ALTER TABLE `db`.`house_has_images` 
ADD CONSTRAINT `fk_houses_has_house_images_houses1`
  FOREIGN KEY (`house_id`)
  REFERENCES `db`.`houses` (`house_id`)
  ON UPDATE CASCADE;