CREATE TABLE IF NOT EXISTS `db`.`followings` (
  `following_id` VARCHAR(255) NOT NULL,
  `user_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`following_id`, `user_id`),
  INDEX `FK43t41y3ifyaj2u398y82twk8a` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK2esl6fie7kkco91akxengn1pg`
    FOREIGN KEY (`following_id`)
    REFERENCES `db`.`users` (`user_id`),
  CONSTRAINT `FK43t41y3ifyaj2u398y82twk8a`
    FOREIGN KEY (`user_id`)
    REFERENCES `db`.`users` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;