ALTER TABLE `db`.`houses` 
DROP FOREIGN KEY `fk_houses_provinces1`,
DROP FOREIGN KEY `fk_houses_districts1`;
ALTER TABLE `db`.`houses` 
DROP COLUMN `districts_code`,
DROP COLUMN `provinces_code`,
DROP INDEX `fk_houses_districts1_idx` ,
DROP INDEX `fk_houses_provinces1_idx` ;
