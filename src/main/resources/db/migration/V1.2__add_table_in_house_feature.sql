CREATE TABLE `db`.`trade_categories` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `category` ENUM("sell", "lease") NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);
ALTER TABLE `db`.`houses` 
ADD COLUMN `trade_category` INT NOT NULL AFTER `house_type`,
CHANGE COLUMN `description` `description` TEXT CHARACTER SET 'armscii8' NOT NULL ,
ADD INDEX `fk_houses_trade_category_idx` (`trade_category` ASC) VISIBLE;
;
ALTER TABLE `db`.`houses` 
ADD CONSTRAINT `fk_houses_trade_category`
  FOREIGN KEY (`trade_category`)
  REFERENCES `db`.`trade_categories` (`id`)
  ON DELETE NO ACTION
  ON UPDATE CASCADE;
