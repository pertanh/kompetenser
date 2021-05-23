CREATE TABLE `kompetenser`.`COMPANY` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `COMPANY_NAME` VARCHAR(45) NOT NULL,
  `ORGANIZATION_NUMBER` VARCHAR(45) NOT NULL,
  `WEB_URL` VARCHAR(45) NULL,
  PRIMARY KEY (`ID`, `COMPANY_NAME`),
  UNIQUE INDEX `ORGANIZATION_NUMBER_UNIQUE` (`ORGANIZATION_NUMBER` ASC) VISIBLE);
  
INSERT INTO `kompetenser`.`COMPANY` (`COMPANY_NAME`, `ORGANIZATION_NUMBER`) VALUES ('Cool company Inc', '555555-5555');
INSERT INTO `kompetenser`.`COMPANY` (`COMPANY_NAME`, `ORGANIZATION_NUMBER`) VALUES ('SAFe Sucks Inc', '333333-3333');