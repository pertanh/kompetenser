CREATE TABLE `kompetenser`.`CITY` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `CITY_NAME` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`, `CITY_NAME`));
  
INSERT INTO `kompetenser`.`CITY` (`CITY_NAME`) VALUES (’Ale’);
INSERT INTO `kompetenser`.`CITY` (`CITY_NAME`) VALUES (’Alingsås);
INSERT INTO `kompetenser`.`CITY` (`CITY_NAME`) VALUES (’Alvesta’);