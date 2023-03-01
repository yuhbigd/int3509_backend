ALTER TABLE `db`.`vnpay_payments` 
ADD UNIQUE INDEX `payment_id_UNIQUE` (`payment_id` ASC) VISIBLE;
INSERT INTO `db`.`payment_type` (`id`, `type`) VALUES ('1', 'manual');
INSERT INTO `db`.`payment_type` (`id`, `type`) VALUES ('2', 'vnp');
ALTER TABLE `db`.`vnpay_payments` 
CHANGE COLUMN `vnp_back_code` `vnp_bank_code` VARCHAR(45) NOT NULL ;
