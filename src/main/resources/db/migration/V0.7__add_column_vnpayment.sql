ALTER TABLE `db`.`vnpay_payments` 
ADD COLUMN `vnp_TransactionStatus` VARCHAR(45) NOT NULL AFTER `vnp_date`;
