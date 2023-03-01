ALTER TABLE `db`.`manual_payments` 
ADD COLUMN `verifier_id` VARCHAR(36) NOT NULL AFTER `total`,
ADD INDEX `fk_manual_payments_verifierId_idx` (`verifier_id` ASC) VISIBLE;
ALTER TABLE `db`.`manual_payments` 
ADD CONSTRAINT `fk_manual_payments_verifierId`
  FOREIGN KEY (`verifier_id`)
  REFERENCES `db`.`users` (`user_id`)
  ON DELETE NO ACTION
  ON UPDATE CASCADE;