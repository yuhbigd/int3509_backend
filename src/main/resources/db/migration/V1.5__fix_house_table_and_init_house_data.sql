INSERT INTO `db`.`house_types` (`id`, `type`) VALUES ('1', 'chung cư');
INSERT INTO `db`.`house_types` (`id`, `type`) VALUES ('2', 'nhà mặt đất');
INSERT INTO `db`.`house_types` (`id`, `type`) VALUES ('3', 'đất');
INSERT INTO `db`.`house_types` (`id`, `type`) VALUES ('4', 'văn phòng/mặt bằng kinh doanh');
INSERT INTO `db`.`house_types` (`id`, `type`) VALUES ('5', 'phòng trọ');


INSERT INTO `db`.`trade_categories` (`id`, `category`) VALUES ('1', 'sell');
INSERT INTO `db`.`trade_categories` (`id`, `category`) VALUES ('2', 'lease');

ALTER TABLE `db`.`houses` 
ADD COLUMN `district_code` VARCHAR(20) NOT NULL AFTER `wards_code`,
ADD COLUMN `province_code` VARCHAR(20) NOT NULL AFTER `district_code`,
ADD INDEX `fk_houses_district_idx` (`district_code` ASC) VISIBLE,
ADD INDEX `fk_houses_province_idx` (`province_code` ASC) VISIBLE;
;
ALTER TABLE `db`.`houses` 
ADD CONSTRAINT `fk_houses_district`
  FOREIGN KEY (`district_code`)
  REFERENCES `db`.`districts` (`code`)
  ON DELETE NO ACTION
  ON UPDATE CASCADE,
ADD CONSTRAINT `fk_houses_province`
  FOREIGN KEY (`province_code`)
  REFERENCES `db`.`provinces` (`code`)
  ON DELETE NO ACTION
  ON UPDATE CASCADE;