create database urnaelectronica;

use urnaelectronica;

CREATE TABLE IF NOT EXISTS candidatos (
  `codigo_candidato` INT NOT NULL AUTO_INCREMENT,
  `cantidad_votos` INT NOT NULL,
  `nombre_candidato` VARCHAR(255) NULL DEFAULT NULL,
  `porcentaje_votos` DOUBLE NOT NULL,
  `tipo_cargo` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`codigo_candidato`));


CREATE TABLE IF NOT EXISTS primer_turno (
  `codigo_turno1` INT NOT NULL AUTO_INCREMENT,
  `cantidad_votos` INT NOT NULL,
  `codigo_candidato` INT NULL DEFAULT NULL,
  PRIMARY KEY (`codigo_turno1`),
  FOREIGN KEY (`codigo_candidato`) REFERENCES `urnaelectronica`.`candidatos` (`codigo_candidato`));


CREATE TABLE IF NOT EXISTS segundo_turno (
  `codigo_turno2` INT NOT NULL AUTO_INCREMENT,
  `cantidad_votos` INT NOT NULL,
  `codigo_candidato` INT NULL DEFAULT NULL,
  PRIMARY KEY (`codigo_turno2`),
  FOREIGN KEY (`codigo_candidato`) REFERENCES `urnaelectronica`.`candidatos` (`codigo_candidato`));
