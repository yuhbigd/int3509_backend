ALTER TABLE `db`.`vnpay_payments` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`vnp_trans_no`);
