-- Adminer 4.6.2 MySQL dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DROP DATABASE IF EXISTS `OpenFleetr`;
CREATE DATABASE `OpenFleetr` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `OpenFleetr`;

DROP TABLE IF EXISTS `APIUserEntity`;
CREATE TABLE `APIUserEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `applicationName` varchar(45) DEFAULT NULL,
  `maintainerEmail` varchar(60) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `APIUserEntity_UserEntity_FK` (`userId`),
  CONSTRAINT `APIUserEntity_UserEntity_FK` FOREIGN KEY (`userId`) REFERENCES `UserEntity` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

TRUNCATE `APIUserEntity`;

DROP TABLE IF EXISTS `CurrentDispatchOrderEntity`;
CREATE TABLE `CurrentDispatchOrderEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `startLatitude` double DEFAULT NULL,
  `startLongitude` double DEFAULT NULL,
  `destinationLatitude` double DEFAULT NULL,
  `destinationLongitude` double DEFAULT NULL,
  `vehicleId` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `creationDate` date DEFAULT NULL,
  `timeStamp` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `dispatcherId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `CurrentDispatchOrderEntity_VehicleEntity_FK` (`vehicleId`),
  KEY `CurrentDispatchOrderEntity_DispatcherEntity_FK` (`dispatcherId`),
  CONSTRAINT `CurrentDispatchOrderEntity_DispatcherEntity_FK` FOREIGN KEY (`dispatcherId`) REFERENCES `DispatcherEntity` (`id`),
  CONSTRAINT `CurrentDispatchOrderEntity_VehicleEntity_FK` FOREIGN KEY (`vehicleId`) REFERENCES `VehicleEntity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

TRUNCATE `CurrentDispatchOrderEntity`;

DROP TABLE IF EXISTS `CurrentLocationEntity`;
CREATE TABLE `CurrentLocationEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vehicleId` int(11) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `timeStamp` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `geographicalAreaId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_CurrentLocationEntity_1_idx` (`vehicleId`),
  KEY `CurrentLocationEntity_GeographicalAreaEntity_FK` (`geographicalAreaId`),
  CONSTRAINT `CurrentLocationEntity_GeographicalAreaEntity_FK` FOREIGN KEY (`geographicalAreaId`) REFERENCES `GeographicalAreaEntity` (`id`),
  CONSTRAINT `fk_CurrentLocationEntity_1` FOREIGN KEY (`vehicleId`) REFERENCES `VehicleEntity` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

TRUNCATE `CurrentLocationEntity`;
INSERT INTO `CurrentLocationEntity` (`id`, `vehicleId`, `longitude`, `latitude`, `timeStamp`, `geographicalAreaId`) VALUES
(1,	1,	31,	31,	'0000-00-00 00:00:00.000',	NULL),
(2,	2,	31.2,	31.2,	'0000-00-00 00:00:00.000',	NULL),
(3,	3,	31.3,	31.3,	'0000-00-00 00:00:00.000',	NULL);

DROP TABLE IF EXISTS `CurrentStatusEntity`;
CREATE TABLE `CurrentStatusEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vehicleId` int(11) DEFAULT NULL,
  `driverId` int(11) DEFAULT NULL,
  `checkOutDate` datetime DEFAULT NULL,
  `checkInDate` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  `notes` varchar(45) DEFAULT NULL,
  `timeStamp` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  KEY `fk_CurrentStatusEntity_1_idx` (`vehicleId`),
  KEY `CurrentStatusEntity_DriverEntity_FK` (`driverId`),
  CONSTRAINT `CurrentStatusEntity_DriverEntity_FK` FOREIGN KEY (`driverId`) REFERENCES `DriverEntity` (`id`),
  CONSTRAINT `fk_CurrentStatusEntity_1` FOREIGN KEY (`vehicleId`) REFERENCES `VehicleEntity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

TRUNCATE `CurrentStatusEntity`;
INSERT INTO `CurrentStatusEntity` (`id`, `vehicleId`, `driverId`, `checkOutDate`, `checkInDate`, `status`, `notes`, `timeStamp`) VALUES
(1,	1,	NULL,	NULL,	NULL,	1,	NULL,	'0000-00-00 00:00:00.000'),
(2,	2,	NULL,	NULL,	NULL,	1,	NULL,	'0000-00-00 00:00:00.000'),
(3,	3,	NULL,	NULL,	NULL,	1,	NULL,	'0000-00-00 00:00:00.000');

DROP TABLE IF EXISTS `DispatcherEntity`;
CREATE TABLE `DispatcherEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `firstName` varchar(45) DEFAULT NULL,
  `lastName` varchar(45) DEFAULT NULL,
  `birthdate` date DEFAULT NULL,
  `phoneNumber` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `DispatcherEntity_UserEntity_FK` (`userId`),
  CONSTRAINT `DispatcherEntity_UserEntity_FK` FOREIGN KEY (`userId`) REFERENCES `UserEntity` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

TRUNCATE `DispatcherEntity`;
INSERT INTO `DispatcherEntity` (`id`, `userId`, `firstName`, `lastName`, `birthdate`, `phoneNumber`) VALUES
(1,	2,	'Tareq',	'Kirresh',	'1997-07-23',	'0595541800');

DROP TABLE IF EXISTS `DriverEntity`;
CREATE TABLE `DriverEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `firstName` varchar(45) DEFAULT NULL,
  `lastName` varchar(45) DEFAULT NULL,
  `birthDate` varchar(45) DEFAULT NULL,
  `phoneNumber` varchar(45) DEFAULT NULL,
  `timeStamp` varchar(45) DEFAULT 'CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP',
  PRIMARY KEY (`id`),
  KEY `fk_DriverEntity_1_idx` (`userId`),
  CONSTRAINT `fk_DriverEntity_1` FOREIGN KEY (`userId`) REFERENCES `UserEntity` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

TRUNCATE `DriverEntity`;

DROP TABLE IF EXISTS `GeographicalAreaEntity`;
CREATE TABLE `GeographicalAreaEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) DEFAULT NULL,
  `polygon` geometry DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

TRUNCATE `GeographicalAreaEntity`;

DROP TABLE IF EXISTS `HistoricalDispatchOrderEntity`;
CREATE TABLE `HistoricalDispatchOrderEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `startLatitude` double DEFAULT NULL,
  `startLongitude` double DEFAULT NULL,
  `destinationLatitude` double DEFAULT NULL,
  `destinationLongitude` double DEFAULT NULL,
  `vehicleId` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `completionDate` datetime DEFAULT NULL,
  `timeStamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

TRUNCATE `HistoricalDispatchOrderEntity`;

DROP TABLE IF EXISTS `HistoricalLocationEntity`;
CREATE TABLE `HistoricalLocationEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vehicleId` int(11) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `timeStamp` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_HistoricalLocationEntity_1_idx` (`vehicleId`),
  CONSTRAINT `fk_HistoricalLocationEntity_1` FOREIGN KEY (`vehicleId`) REFERENCES `VehicleEntity` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

TRUNCATE `HistoricalLocationEntity`;

DROP TABLE IF EXISTS `HistoricalStatusEntity`;
CREATE TABLE `HistoricalStatusEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vehicleId` int(11) DEFAULT NULL,
  `driverId` int(11) DEFAULT NULL,
  `checkOutDate` varchar(45) DEFAULT NULL,
  `checkInDate` varchar(45) DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  `notes` varchar(45) DEFAULT NULL,
  `timeStamp` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_HistoricalStatusEntity_1_idx` (`vehicleId`),
  CONSTRAINT `fk_HistoricalStatusEntity_1` FOREIGN KEY (`vehicleId`) REFERENCES `VehicleEntity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

TRUNCATE `HistoricalStatusEntity`;

DROP TABLE IF EXISTS `JurisdictionEntity`;
CREATE TABLE `JurisdictionEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dispatcherId` int(11) DEFAULT NULL,
  `geographicalAreaId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `JurisdictionEntity_DispatcherEntity_FK` (`dispatcherId`),
  KEY `JurisdictionEntity_GeographicalAreaEntity_FK` (`geographicalAreaId`),
  CONSTRAINT `JurisdictionEntity_DispatcherEntity_FK` FOREIGN KEY (`dispatcherId`) REFERENCES `DispatcherEntity` (`id`),
  CONSTRAINT `JurisdictionEntity_GeographicalAreaEntity_FK` FOREIGN KEY (`geographicalAreaId`) REFERENCES `GeographicalAreaEntity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

TRUNCATE `JurisdictionEntity`;

DROP TABLE IF EXISTS `NotificationEntity`;
CREATE TABLE `NotificationEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apiUser` int(11) DEFAULT NULL,
  `dispatcherId` int(11) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `wasHandled` bit(1) DEFAULT NULL,
  `handledTimestamp` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `dispatchOrderId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `NotificationEntity_APIUserEntity_FK` (`apiUser`),
  KEY `NotificationEntity_DispatcherEntity_FK` (`dispatcherId`),
  KEY `NotificationEntity_CurrentDispatchOrderEntity_FK` (`dispatchOrderId`),
  CONSTRAINT `NotificationEntity_APIUserEntity_FK` FOREIGN KEY (`apiUser`) REFERENCES `APIUserEntity` (`id`),
  CONSTRAINT `NotificationEntity_CurrentDispatchOrderEntity_FK` FOREIGN KEY (`dispatchOrderId`) REFERENCES `CurrentDispatchOrderEntity` (`id`),
  CONSTRAINT `NotificationEntity_DispatcherEntity_FK` FOREIGN KEY (`dispatcherId`) REFERENCES `DispatcherEntity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

TRUNCATE `NotificationEntity`;

DROP TABLE IF EXISTS `UserEntity`;
CREATE TABLE `UserEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `token` varchar(45) DEFAULT NULL,
  `level` int(11) DEFAULT '1',
  `timeStamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

TRUNCATE `UserEntity`;
INSERT INTO `UserEntity` (`id`, `userName`, `password`, `token`, `level`, `timeStamp`) VALUES
(1,	'admin',	'admin',	'e7fa3d52-9a94-469c-ad3e-1b4be54fa66d',	4,	'2018-05-06 08:47:43'),
(2,	'dispatcher',	'dispatcher',	NULL,	3,	'2018-05-28 07:43:01');

DROP TABLE IF EXISTS `VehicleEntity`;
CREATE TABLE `VehicleEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vehicleType` varchar(45) DEFAULT NULL,
  `timeStamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

TRUNCATE `VehicleEntity`;
INSERT INTO `VehicleEntity` (`id`, `vehicleType`, `timeStamp`) VALUES
(1,	'\"ambulance 2\"',	NULL),
(2,	'\"ambulance 3\"',	NULL),
(3,	'\"ambulance 3\"',	NULL);

-- 2018-05-28 07:59:10
