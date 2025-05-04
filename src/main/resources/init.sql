-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: CourseForDevelopment
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `answers`
--
CREATE DATABASE IF NOT EXISTS CourseForDevelopment;
USE CourseForDevelopment;

DROP TABLE IF EXISTS `answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_correct` bit(1) NOT NULL,
  `question_id` bigint DEFAULT NULL,
  `quiz_id` bigint DEFAULT NULL,
  `option_id` bigint DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `passed_lecture` bit(1) NOT NULL,
  `lecture_id` bigint NOT NULL,
  `progress_timestamp` datetime(6) DEFAULT NULL,
  `score` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3erw1a3t0r78st8ty27x6v3g1` (`question_id`),
  KEY `FK5qlfxfuy6hvtlmirhd8drt3wi` (`quiz_id`),
  KEY `FKn0s3utny5xipstu14dx3mdmfw` (`option_id`),
  KEY `FK5bp3d5loftq2vjn683ephn75a` (`user_id`),
  KEY `FKlk8u4btc9gg8bodkvebp18jg2` (`lecture_id`),
  CONSTRAINT `FK3erw1a3t0r78st8ty27x6v3g1` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`),
  CONSTRAINT `FK5bp3d5loftq2vjn683ephn75a` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK5qlfxfuy6hvtlmirhd8drt3wi` FOREIGN KEY (`quiz_id`) REFERENCES `quizzes` (`id`),
  CONSTRAINT `FKlk8u4btc9gg8bodkvebp18jg2` FOREIGN KEY (`lecture_id`) REFERENCES `lectures` (`id`),
  CONSTRAINT `FKn0s3utny5xipstu14dx3mdmfw` FOREIGN KEY (`option_id`) REFERENCES `options` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answers`
--

LOCK TABLES `answers` WRITE;
/*!40000 ALTER TABLE `answers` DISABLE KEYS */;
INSERT INTO `answers` VALUES (1,_binary '\0',NULL,NULL,NULL,NULL,'2025-03-20 07:15:07.130946',_binary '',9,NULL,100),(2,_binary '\0',NULL,NULL,NULL,'e44d4b45-6223-41c7-947d-49639bbd9289','2025-03-21 00:13:47.046317',_binary '',9,NULL,100),(3,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-03-21 00:17:03.492859',_binary '',9,NULL,100),(4,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-03-21 00:17:13.481681',_binary '',9,NULL,100),(5,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-03-21 00:18:22.254589',_binary '',9,NULL,100),(6,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-03-21 01:48:14.147676',_binary '\0',9,NULL,50),(7,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-03-21 18:54:17.376690',_binary '\0',9,NULL,50),(8,_binary '\0',NULL,NULL,NULL,'28532f6f-5007-473d-8453-9eb28d25bd57','2025-03-21 23:13:02.881449',_binary '\0',9,NULL,66),(9,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-03-21 23:37:44.356438',_binary '\0',9,NULL,50),(10,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-03-22 16:59:10.564182',_binary '\0',9,NULL,66),(11,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-06 18:45:40.261670',_binary '\0',6,NULL,50),(12,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-06 18:49:41.053969',_binary '\0',6,NULL,50),(13,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-06 18:51:29.522898',_binary '\0',6,NULL,50),(14,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-07 15:58:15.369141',_binary '\0',6,NULL,25),(15,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-07 15:58:46.165610',_binary '\0',9,NULL,75),(16,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-07 16:01:58.022232',_binary '\0',9,NULL,75),(17,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-07 16:13:45.253616',_binary '\0',9,NULL,50),(18,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-07 16:26:12.797111',_binary '\0',9,NULL,0),(19,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-07 16:27:20.980493',_binary '\0',9,NULL,75),(20,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-07 16:30:56.191137',_binary '\0',2,NULL,33),(22,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-07 16:33:03.895803',_binary '\0',2,NULL,0),(23,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-07 16:34:31.622431',_binary '\0',2,NULL,0),(24,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-07 16:34:50.805514',_binary '',2,NULL,100),(25,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-07 16:35:32.809576',_binary '\0',2,NULL,0),(26,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-07 16:43:21.739307',_binary '',2,NULL,100),(27,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-08 04:49:03.912041',_binary '\0',6,NULL,25),(28,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-08 04:50:01.655000',_binary '\0',6,NULL,0),(29,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-08 04:50:36.067582',_binary '',6,NULL,100),(30,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-08 04:51:39.721140',_binary '\0',6,NULL,33),(31,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-08 04:52:10.098766',_binary '\0',6,NULL,33),(32,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-08 04:54:04.980056',_binary '\0',9,NULL,66),(33,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-08 04:55:11.562824',_binary '\0',9,NULL,75),(34,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-08 04:55:37.636683',_binary '\0',9,NULL,60),(35,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-08 04:57:03.800968',_binary '',9,NULL,100),(36,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-08 05:01:41.439363',_binary '',9,NULL,100),(37,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-08 05:02:43.479719',_binary '',9,NULL,100),(38,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-08 05:03:33.463553',_binary '',9,NULL,100),(39,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-08 05:07:35.355475',_binary '',9,NULL,100),(40,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-08 05:08:34.672567',_binary '',9,NULL,100),(41,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-08 05:15:59.154057',_binary '',9,NULL,100),(42,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-08 05:26:51.877379',_binary '',9,NULL,100),(43,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-08 19:59:57.758555',_binary '\0',9,NULL,66),(44,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-08 20:06:33.348072',_binary '',9,NULL,100),(45,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-08 20:31:37.006047',_binary '\0',9,NULL,50),(46,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-09 04:29:15.047723',_binary '\0',9,NULL,50),(47,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-09 04:38:10.202900',_binary '\0',9,NULL,0),(48,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-09 06:45:14.126370',_binary '\0',9,NULL,0),(49,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-09 06:45:59.819670',_binary '\0',9,NULL,50),(50,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-09 07:23:11.004304',_binary '\0',2,NULL,75),(51,_binary '\0',NULL,NULL,NULL,'260fdc79-066d-46ab-ab44-2b671080e8f7','2025-04-09 07:23:20.145597',_binary '',2,NULL,100);
/*!40000 ALTER TABLE `answers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assignments`
--

DROP TABLE IF EXISTS `assignments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assignments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `assignment_file_url` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `due_date` date DEFAULT NULL,
  `submitted_at` datetime(6) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `lecture_id` bigint NOT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbk2idc38tyuiq1bwofb2mt96c` (`lecture_id`),
  KEY `FK8iy60sfbpptmtm10f4al96qvq` (`user_id`),
  CONSTRAINT `FK8iy60sfbpptmtm10f4al96qvq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKbk2idc38tyuiq1bwofb2mt96c` FOREIGN KEY (`lecture_id`) REFERENCES `lectures` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assignments`
--

LOCK TABLES `assignments` WRITE;
/*!40000 ALTER TABLE `assignments` DISABLE KEYS */;
/*!40000 ALTER TABLE `assignments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `add_at` datetime(6) DEFAULT NULL,
  `course_id` bigint DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1vllsie342rrqn6niy90pufd5` (`course_id`),
  KEY `FKg5uhi8vpsuy0lgloxk2h4w5o6` (`user_id`),
  CONSTRAINT `FK1vllsie342rrqn6niy90pufd5` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `FKg5uhi8vpsuy0lgloxk2h4w5o6` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (1,'2025-03-17 19:35:44.110910',2,'28532f6f-5007-473d-8453-9eb28d25bd57'),(2,'2025-03-17 23:02:37.264807',2,'260fdc79-066d-46ab-ab44-2b671080e8f7'),(3,'2025-03-17 23:27:14.283394',1,'260fdc79-066d-46ab-ab44-2b671080e8f7'),(4,'2025-03-19 15:22:57.050499',4,'260fdc79-066d-46ab-ab44-2b671080e8f7'),(5,'2025-03-20 04:13:00.647964',5,'260fdc79-066d-46ab-ab44-2b671080e8f7'),(6,'2025-03-21 23:04:54.817164',5,'28532f6f-5007-473d-8453-9eb28d25bd57'),(7,'2025-04-09 07:07:05.580747',3,'260fdc79-066d-46ab-ab44-2b671080e8f7');
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `cover_image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'1','1'),(2,'2','2'),(3,'3','3'),(4,'4','4');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `certificate`
--

DROP TABLE IF EXISTS `certificate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `certificate` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_title` varchar(255) DEFAULT NULL,
  `issued_at` datetime(6) DEFAULT NULL,
  `user_id` varchar(255) NOT NULL,
  `course_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtnnj9ktwn18vtvap4yuptwxhg` (`user_id`),
  KEY `FK13t46rtyipt6ayvme7crsdvs4` (`course_id`),
  CONSTRAINT `FK13t46rtyipt6ayvme7crsdvs4` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `FKtnnj9ktwn18vtvap4yuptwxhg` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `certificate`
--

LOCK TABLES `certificate` WRITE;
/*!40000 ALTER TABLE `certificate` DISABLE KEYS */;
INSERT INTO `certificate` VALUES (1,NULL,'2025-04-07 18:54:46.712314','260fdc79-066d-46ab-ab44-2b671080e8f7',1),(2,NULL,'2025-04-09 06:08:25.427692','260fdc79-066d-46ab-ab44-2b671080e8f7',5),(3,NULL,'2025-04-09 07:07:24.749379','260fdc79-066d-46ab-ab44-2b671080e8f7',2),(4,NULL,'2025-04-09 07:54:10.079884','260fdc79-066d-46ab-ab44-2b671080e8f7',4);
/*!40000 ALTER TABLE `certificate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comment_text` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `comment_id` bigint DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `video_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmk3c8pbvysjndxywunibl2voc` (`comment_id`),
  KEY `FKqm52p1v3o13hy268he0wcngr5` (`user_id`),
  KEY `FK5ycss2ogsxqh86q64s2oxtufe` (`video_id`),
  CONSTRAINT `FK5ycss2ogsxqh86q64s2oxtufe` FOREIGN KEY (`video_id`) REFERENCES `videos` (`id`),
  CONSTRAINT `FKmk3c8pbvysjndxywunibl2voc` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`),
  CONSTRAINT `FKqm52p1v3o13hy268he0wcngr5` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comment_text` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `star` int NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `comment_id` bigint DEFAULT NULL,
  `courses_id` bigint DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `video_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe2dbs56lhmp8fucafi3xvhjyd` (`comment_id`),
  KEY `FKgu2h7mc80xtaqeckoscpgacn6` (`courses_id`),
  KEY `FK8omq0tc18jd43bu5tjh6jvraq` (`user_id`),
  KEY `FKesqgvcfwlscgco0dqkdnvw8l3` (`video_id`),
  CONSTRAINT `FK8omq0tc18jd43bu5tjh6jvraq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKe2dbs56lhmp8fucafi3xvhjyd` FOREIGN KEY (`comment_id`) REFERENCES `comments` (`id`),
  CONSTRAINT `FKesqgvcfwlscgco0dqkdnvw8l3` FOREIGN KEY (`video_id`) REFERENCES `videos` (`id`),
  CONSTRAINT `FKgu2h7mc80xtaqeckoscpgacn6` FOREIGN KEY (`courses_id`) REFERENCES `courses` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `cover_image` text,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_published` bit(1) NOT NULL,
  `level` varchar(255) DEFAULT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `instructor_id` varchar(255) NOT NULL,
  `pending_delete` bit(1) NOT NULL,
  `category_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcyfum8goa6q5u13uog0563gyp` (`instructor_id`),
  KEY `FK72l5dj585nq7i6xxv1vj51lyn` (`category_id`),
  CONSTRAINT `FK72l5dj585nq7i6xxv1vj51lyn` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  CONSTRAINT `FKcyfum8goa6q5u13uog0563gyp` FOREIGN KEY (`instructor_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (1,NULL,'https://bucket-of-mockproject.s3.amazonaws.com/Screenshot (1).png','2025-03-17 12:27:01.055000','1',_binary '','Level 01',1.00,'1','2025-03-17 19:30:27.969480','bd1089ed-3354-41ae-b520-66db7243dc8f',_binary '\0',1),(2,NULL,'https://bucket-of-mockproject.s3.amazonaws.com/Screenshot (1).png6ed7b2cc-d496-4eae-9c4a-270344931c34','2025-03-17 12:31:06.096000','2',_binary '','Level 02',2.00,'2','2025-03-17 19:32:56.619867','bd1089ed-3354-41ae-b520-66db7243dc8f',_binary '\0',1),(3,NULL,'https://bucket-of-mockproject.s3.amazonaws.com/Screenshot (1).png88483448-2860-41e0-af65-35370f089bf7','2025-03-19 08:10:32.320000','3',_binary '\0','Level 03',3.00,'3','2025-03-19 15:15:19.552531','bd1089ed-3354-41ae-b520-66db7243dc8f',_binary '\0',1),(4,NULL,'https://bucket-of-mockproject.s3.amazonaws.com/Screenshot (1).pngc54eea0b-e584-4e05-922e-834e37982894','2025-03-19 08:17:52.766000','4',_binary '','Level 04',4.00,'4','2025-03-19 15:21:31.758299','bd1089ed-3354-41ae-b520-66db7243dc8f',_binary '\0',1),(5,NULL,'https://bucket-of-mockproject.s3.amazonaws.com/Screenshot (13).png4b788e63-aa8c-4693-9f7a-a9b6442c90f1','2025-03-19 21:08:21.620000','5',_binary '','Level 01',5.00,'5','2025-03-20 04:11:17.486670','bd1089ed-3354-41ae-b520-66db7243dc8f',_binary '\0',1);
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend`
--

DROP TABLE IF EXISTS `friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friend` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `friend_id` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5j28qgyvon52ycu9sfieraerm` (`friend_id`),
  KEY `FKeab81424e9dtc4a8hjlq4xiew` (`user_id`),
  CONSTRAINT `FK5j28qgyvon52ycu9sfieraerm` FOREIGN KEY (`friend_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKeab81424e9dtc4a8hjlq4xiew` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `friend_chk_1` CHECK ((`status` between 0 and 2))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend`
--

LOCK TABLES `friend` WRITE;
/*!40000 ALTER TABLE `friend` DISABLE KEYS */;
/*!40000 ALTER TABLE `friend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learning_progress`
--

DROP TABLE IF EXISTS `learning_progress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `learning_progress` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `completed` bit(1) NOT NULL,
  `completed_at` datetime(6) DEFAULT NULL,
  `current_second` int DEFAULT NULL,
  `lecture_id` bigint NOT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKr1avf5acxxy5rkpukxod18mk9` (`lecture_id`),
  KEY `FKkex4pny6sl5affwroon8lfbjt` (`user_id`),
  CONSTRAINT `FKkex4pny6sl5affwroon8lfbjt` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKr1avf5acxxy5rkpukxod18mk9` FOREIGN KEY (`lecture_id`) REFERENCES `lectures` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learning_progress`
--

LOCK TABLES `learning_progress` WRITE;
/*!40000 ALTER TABLE `learning_progress` DISABLE KEYS */;
INSERT INTO `learning_progress` VALUES (6,_binary '','2025-04-07 16:58:34.670784',59,1,'e44d4b45-6223-41c7-947d-49639bbd9289'),(7,_binary '','2025-04-07 18:50:56.117149',202,8,'260fdc79-066d-46ab-ab44-2b671080e8f7'),(9,_binary '','2025-04-07 18:54:43.183661',66,1,'260fdc79-066d-46ab-ab44-2b671080e8f7'),(10,_binary '','2025-04-08 04:07:30.660435',202,4,'260fdc79-066d-46ab-ab44-2b671080e8f7'),(11,_binary '','2025-04-08 04:50:36.061018',202,6,'260fdc79-066d-46ab-ab44-2b671080e8f7'),(12,_binary '','2025-04-08 05:08:34.665860',0,9,'260fdc79-066d-46ab-ab44-2b671080e8f7'),(13,_binary '','2025-04-08 06:14:31.612937',202,5,'260fdc79-066d-46ab-ab44-2b671080e8f7'),(15,_binary '','2025-04-09 06:07:34.145567',202,11,'260fdc79-066d-46ab-ab44-2b671080e8f7'),(16,_binary '','2025-04-09 07:23:20.138093',0,2,'260fdc79-066d-46ab-ab44-2b671080e8f7'),(17,_binary '','2025-04-09 07:53:45.085202',202,7,'260fdc79-066d-46ab-ab44-2b671080e8f7'),(18,_binary '\0',NULL,196,10,'260fdc79-066d-46ab-ab44-2b671080e8f7');
/*!40000 ALTER TABLE `learning_progress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lectures`
--

DROP TABLE IF EXISTS `lectures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lectures` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `section_id` bigint DEFAULT NULL,
  `completed` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK69sw3j0r9oaugk25btekv616j` (`section_id`),
  CONSTRAINT `FK69sw3j0r9oaugk25btekv616j` FOREIGN KEY (`section_id`) REFERENCES `sections` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lectures`
--

LOCK TABLES `lectures` WRITE;
/*!40000 ALTER TABLE `lectures` DISABLE KEYS */;
INSERT INTO `lectures` VALUES (1,'2025-03-17 19:30:28.038591','1','video','2025-03-17 19:30:28.038591',1,_binary '\0'),(2,'2025-03-17 19:32:56.632871','2','quiz','2025-03-17 19:32:56.632871',2,_binary '\0'),(3,'2025-03-19 15:15:19.576486','3.1.1','video','2025-03-19 15:15:19.576486',3,_binary '\0'),(4,'2025-03-19 15:15:19.576486','3.1.2','quiz','2025-03-19 15:15:19.576486',3,_binary '\0'),(5,'2025-03-19 15:15:19.576486','3.1.3','quiz','2025-03-19 15:15:19.576486',3,_binary '\0'),(6,'2025-03-19 15:21:31.770146','4','quiz','2025-03-19 15:21:31.770146',4,_binary '\0'),(7,'2025-03-19 15:21:31.770146','3','video','2025-03-19 15:21:31.770146',4,_binary '\0'),(8,'2025-03-20 04:11:17.512826','b1','video','2025-03-20 04:11:17.512826',5,_binary '\0'),(9,'2025-03-20 04:11:17.512826','b2','quiz','2025-03-20 04:11:17.512826',5,_binary '\0'),(10,'2025-03-20 04:11:17.512826','b1','video','2025-03-20 04:11:17.512826',6,_binary '\0'),(11,'2025-03-20 04:11:17.512826','b2','video','2025-03-20 04:11:17.512826',6,_binary '\0');
/*!40000 ALTER TABLE `lectures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `recall` bit(1) NOT NULL,
  `url_file` varchar(255) DEFAULT NULL,
  `url_image` varchar(255) DEFAULT NULL,
  `friend_id` bigint DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhf5bj8qr11u9j71my2kuhqimb` (`friend_id`),
  KEY `FKpdrb79dg3bgym7pydlf9k3p1n` (`user_id`),
  CONSTRAINT `FKhf5bj8qr11u9j71my2kuhqimb` FOREIGN KEY (`friend_id`) REFERENCES `friend` (`id`),
  CONSTRAINT `FKpdrb79dg3bgym7pydlf9k3p1n` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `options`
--

DROP TABLE IF EXISTS `options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `options` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_correct` bit(1) NOT NULL,
  `text` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `question_id` bigint DEFAULT NULL,
  `option_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5bmv46so2y5igt9o9n9w4fh6y` (`question_id`),
  CONSTRAINT `FK5bmv46so2y5igt9o9n9w4fh6y` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `options`
--

LOCK TABLES `options` WRITE;
/*!40000 ALTER TABLE `options` DISABLE KEYS */;
INSERT INTO `options` VALUES (1,_binary '\0','1',1,0),(2,_binary '\0','2',1,0),(3,_binary '','3',1,0),(4,_binary '\0','4',1,0),(5,_binary '\0','1',2,0),(6,_binary '','2',2,0),(7,_binary '','3',2,0),(8,_binary '','4',2,0),(9,_binary '\0','5',2,0),(10,_binary '','1',3,0),(11,_binary '\0','2',3,0),(12,_binary '\0','3',3,0),(13,_binary '\0','4',3,0),(14,_binary '\0','1',4,0),(15,_binary '','2',4,0),(16,_binary '','3',4,0),(17,_binary '','4',4,0),(18,_binary '\0','5',4,0),(19,_binary '\0','1',5,0),(20,_binary '\0','2',5,0),(21,_binary '\0','3',5,0),(22,_binary '','4',5,0),(23,_binary '\0','1',6,0),(24,_binary '\0','2',6,0),(25,_binary '\0','3',6,0),(26,_binary '','4',6,0),(27,_binary '','5',6,0),(28,_binary '\0','1',7,0),(29,_binary '\0','2',7,0),(30,_binary '\0','3',7,0),(31,_binary '','4',7,0),(32,_binary '','1',8,0),(33,_binary '','2',8,0),(34,_binary '\0','3',8,0);
/*!40000 ALTER TABLE `options` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_statuses`
--

DROP TABLE IF EXISTS `payment_statuses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_statuses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `status_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_o6las05xmph4if0d1cdd5nvsv` (`status_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_statuses`
--

LOCK TABLES `payment_statuses` WRITE;
/*!40000 ALTER TABLE `payment_statuses` DISABLE KEYS */;
INSERT INTO `payment_statuses` VALUES (1,'COMPLETED\n'),(2,'FAILED'),(3,'PENDING');
/*!40000 ALTER TABLE `payment_statuses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `enrollment` bit(1) DEFAULT NULL,
  `payment_date` datetime(6) DEFAULT NULL,
  `payment_id` varchar(255) DEFAULT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `course_id` bigint DEFAULT NULL,
  `payment_status_id` bigint DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8nlm4urshp5drsk0nlkprig36` (`course_id`),
  KEY `FK3ahqbxi8ygjekxaa1atyycpr0` (`payment_status_id`),
  KEY `FKj94hgy9v5fw1munb90tar2eje` (`user_id`),
  CONSTRAINT `FK3ahqbxi8ygjekxaa1atyycpr0` FOREIGN KEY (`payment_status_id`) REFERENCES `payment_statuses` (`id`),
  CONSTRAINT `FK8nlm4urshp5drsk0nlkprig36` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `FKj94hgy9v5fw1munb90tar2eje` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (4,_binary '','2025-03-17 23:16:11.418572','95441236',2.00,2,1,'260fdc79-066d-46ab-ab44-2b671080e8f7'),(5,_binary '','2025-03-17 23:27:39.200988','22174630',1.00,1,1,'260fdc79-066d-46ab-ab44-2b671080e8f7'),(6,_binary '','2025-03-19 15:23:02.672019','84862007',4.00,4,1,'260fdc79-066d-46ab-ab44-2b671080e8f7'),(7,_binary '','2025-03-20 04:13:07.651108','65567264',5.00,5,1,'260fdc79-066d-46ab-ab44-2b671080e8f7'),(8,_binary '','2025-03-21 23:05:06.452015','28376369',2.00,2,1,'28532f6f-5007-473d-8453-9eb28d25bd57'),(9,_binary '','2025-03-21 23:05:06.472908','28376369',5.00,5,1,'28532f6f-5007-473d-8453-9eb28d25bd57');
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `question_type` varchar(255) DEFAULT NULL,
  `title` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `quiz_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn3gvco4b0kewxc0bywf1igfms` (`quiz_id`),
  CONSTRAINT `FKn3gvco4b0kewxc0bywf1igfms` FOREIGN KEY (`quiz_id`) REFERENCES `quizzes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
INSERT INTO `questions` VALUES (1,'2025-03-17 19:32:56.656319',NULL,'2','2025-03-17 19:32:56.656319',1),(2,'2025-03-17 19:32:56.671120',NULL,'2.1','2025-03-17 19:32:56.671120',1),(3,'2025-03-19 15:15:19.647355',NULL,'3.1.2.question','2025-03-19 15:15:19.647355',2),(4,'2025-03-19 15:15:19.665415',NULL,'3.1.3.question','2025-03-19 15:15:19.665415',3),(5,'2025-03-19 15:21:31.794018',NULL,'4','2025-03-19 15:21:31.794018',4),(6,'2025-03-19 15:21:31.805982',NULL,'4.1','2025-03-19 15:21:31.805982',4),(7,'2025-03-20 04:11:17.719635',NULL,'q1','2025-03-20 04:11:17.719635',5),(8,'2025-03-20 04:11:17.750593',NULL,'q2','2025-03-20 04:11:17.750593',5);
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quizzes`
--

DROP TABLE IF EXISTS `quizzes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quizzes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `title` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `lecture_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_24fr01ycyysh7qn6ktju7wlqa` (`lecture_id`),
  CONSTRAINT `FKfv5kl929xnpn1hjm8tg9a4rl5` FOREIGN KEY (`lecture_id`) REFERENCES `lectures` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quizzes`
--

LOCK TABLES `quizzes` WRITE;
/*!40000 ALTER TABLE `quizzes` DISABLE KEYS */;
INSERT INTO `quizzes` VALUES (1,'2025-03-17 19:32:56.652558','','2025-03-17 19:32:56.652558',2),(2,'2025-03-19 15:15:19.643500','3.1.2.quiz','2025-03-19 15:15:19.643500',4),(3,'2025-03-19 15:15:19.661864','','2025-03-19 15:15:19.661864',5),(4,'2025-03-19 15:21:31.791424','4','2025-03-19 15:21:31.791424',6),(5,'2025-03-20 04:11:17.706005','','2025-03-20 04:11:17.706564',9);
/*!40000 ALTER TABLE `quizzes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rating`
--

DROP TABLE IF EXISTS `rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rating` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comment` varchar(255) NOT NULL,
  `create_at` datetime(6) NOT NULL,
  `rating` int NOT NULL,
  `course_id` bigint NOT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfwmdlipeg1hhaagr7fubq2hi4` (`course_id`),
  KEY `FKf68lgbsbxl310n0jifwpfqgfh` (`user_id`),
  CONSTRAINT `FKf68lgbsbxl310n0jifwpfqgfh` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKfwmdlipeg1hhaagr7fubq2hi4` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rating`
--

LOCK TABLES `rating` WRITE;
/*!40000 ALTER TABLE `rating` DISABLE KEYS */;
/*!40000 ALTER TABLE `rating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime(6) DEFAULT NULL,
  `role_name` varchar(255) NOT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `version` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'2025-03-15 08:55:28.954362','INSTRUCTOR','2025-03-15 08:55:28.953792',0),(2,'2025-03-15 08:55:28.954362','STUDENT','2025-03-15 08:55:28.954362',0),(3,'2025-03-15 08:55:29.089835','ADMIN','2025-03-15 08:55:29.089835',0);
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sections`
--

DROP TABLE IF EXISTS `sections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sections` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `course_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7ty9cevpq04d90ohtso1q8312` (`course_id`),
  CONSTRAINT `FK7ty9cevpq04d90ohtso1q8312` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sections`
--

LOCK TABLES `sections` WRITE;
/*!40000 ALTER TABLE `sections` DISABLE KEYS */;
INSERT INTO `sections` VALUES (1,'1',1),(2,'2',2),(3,'3.1',3),(4,'4',4),(5,'s1',5),(6,'s2',5);
/*!40000 ALTER TABLE `sections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_payment`
--

DROP TABLE IF EXISTS `transaction_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction_payment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(38,2) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhk9snweqa29vnjf96ajm6t0df` (`user_id`),
  CONSTRAINT `FKhk9snweqa29vnjf96ajm6t0df` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `transaction_payment_chk_1` CHECK ((`status` between 0 and 2))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction_payment`
--

LOCK TABLES `transaction_payment` WRITE;
/*!40000 ALTER TABLE `transaction_payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(38,2) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqwv7rmvc8va8rep7piikrojds` (`user_id`),
  CONSTRAINT `FKqwv7rmvc8va8rep7piikrojds` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `transactions_chk_1` CHECK ((`status` between 0 and 2))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_course_progress`
--

DROP TABLE IF EXISTS `user_course_progress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_course_progress` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `completed` bit(1) NOT NULL,
  `course_id` bigint NOT NULL,
  `lecture_id` bigint NOT NULL,
  `progress_timestamp` datetime(6) NOT NULL,
  `score` int DEFAULT NULL,
  `section_id` bigint NOT NULL,
  `time_spent` int DEFAULT NULL,
  `user_id` varchar(255) NOT NULL,
  `status` enum('NOT_STARTED','IN_PROGRESS','COMPLETED') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_course` (`user_id`,`course_id`),
  KEY `FKjj3sw5b9g86gydd6tdvnuailh` (`course_id`),
  CONSTRAINT `FKj5thv4d609cmoa8mxffdfluy1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKjj3sw5b9g86gydd6tdvnuailh` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_course_progress`
--

LOCK TABLES `user_course_progress` WRITE;
/*!40000 ALTER TABLE `user_course_progress` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_course_progress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` varchar(255) NOT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `email` text NOT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `gender` enum('FEMALE','MALE','PRIVATE') DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `version` int DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
  CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('260fdc79-066d-46ab-ab44-2b671080e8f7','https://lh3.googleusercontent.com/a/ACg8ocIIthnFrJII2akKXZ2Oeemajre9UBx4qTJwDYAl4VY9bdmSVQ=s96-c',NULL,'2025-03-17 18:54:14.231227','trinhdongkhanh.work@gmail.com','Đồng Khánh Trịnh','PRIVATE',NULL,'',NULL,NULL,'107608426357057044403',0,2,_binary '\0',NULL,NULL,NULL,NULL),('28532f6f-5007-473d-8453-9eb28d25bd57','https://lh3.googleusercontent.com/a/ACg8ocKXvvX_gTjC5iEIyLCqWokzg-VJcyj6j0Xv3UqzwFMfkn59xOs=s96-c',NULL,'2025-03-17 19:35:27.209000','thanhnth181@gmail.com','Thanh Hoài','PRIVATE',NULL,'',NULL,NULL,'107259986098838661956',0,2,_binary '\0',NULL,NULL,NULL,NULL),('908549c9-ae47-4c18-8c1e-e48550d59b27','http://example.com/avatar.jpg','2024-10-23','2025-05-03 10:30:19.313856','user232@example.com','John Doe','MALE',NULL,'$2a$10$B.r4Y4A4DC2bgVo5p4Bq9u6ttOg9mQjlFvztqM3U5YWbNpmxSA62u','123456789','2024-10-23 19:35:32.000130','admin1',0,2,_binary '\0',NULL,NULL,NULL,NULL),('bd1089ed-3354-41ae-b520-66db7243dc8f','https://bucket-of-mockproject.s3.amazonaws.com/679b15f0-147b-43b5-a18d-4c3cdfc66cad','2025-03-27','2025-03-17 19:14:46.940581','tingkhanh2004@gmail.com','TRỊNH ĐỒNG KHÁNH','FEMALE',NULL,'$2a$10$ykkRMqQt2.spCiDYu8rWcu1ep/6PxiiBnMqZeldtiWoNOTKbUjRpK','0378187802',NULL,'khanhtd',0,1,_binary '\0',NULL,NULL,NULL,NULL),('e44d4b45-6223-41c7-947d-49639bbd9289','http://example.com/avatar.jpg','2025-03-15','2025-03-15 08:55:29.206190','khanhtdps36301@fpt.edu.vn','khanhtd36301','MALE',_binary '','$2a$10$A2UBFnOePuS3B.DKeIS6MuzPnGljHY15QEwiwqP8So7UxqULCRNN.','123456789','2025-03-15 08:55:29.206190','admin',0,3,_binary '\0',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `videos`
--

DROP TABLE IF EXISTS `videos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `videos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `duration` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `video_url` varchar(255) DEFAULT NULL,
  `lecture_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7ijhr0gca9bbsylvcu6b3xiq` (`lecture_id`),
  CONSTRAINT `FK7ijhr0gca9bbsylvcu6b3xiq` FOREIGN KEY (`lecture_id`) REFERENCES `lectures` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `videos`
--

LOCK TABLES `videos` WRITE;
/*!40000 ALTER TABLE `videos` DISABLE KEYS */;
INSERT INTO `videos` VALUES (1,NULL,'2025-02-20 17-59-22.mkv','https://bucket-of-mockproject.s3.amazonaws.com/Pam Flirts with Michael - The Office.mp4',1),(2,NULL,'Screenshot (1).png','https://bucket-of-mockproject.s3.amazonaws.com/Pam Flirts with Michael - The Office.mp4',3),(3,NULL,'Screenshot (1).png','https://bucket-of-mockproject.s3.amazonaws.com/Pam Flirts with Michael - The Office.mp4',7),(4,NULL,'Screenshot (1).png','https://bucket-of-mockproject.s3.amazonaws.com/Pam Flirts with Michael - The Office.mp4',8),(5,NULL,'Screenshot (1).png','https://bucket-of-mockproject.s3.amazonaws.com/Pam Flirts with Michael - The Office.mp4',10),(6,NULL,'Screenshot (1).png','https://bucket-of-mockproject.s3.amazonaws.com/Pam Flirts with Michael - The Office.mp4',11);
/*!40000 ALTER TABLE `videos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wallet`
--

DROP TABLE IF EXISTS `wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wallet` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `balance` decimal(38,2) NOT NULL,
  `update_at` datetime(6) DEFAULT NULL,
  `role_id` bigint NOT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2539eablmfpyro5934fp8l3k8` (`role_id`),
  KEY `FKgbusavqq0bdaodex4ee6v0811` (`user_id`),
  CONSTRAINT `FK2539eablmfpyro5934fp8l3k8` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKgbusavqq0bdaodex4ee6v0811` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wallet`
--

LOCK TABLES `wallet` WRITE;
/*!40000 ALTER TABLE `wallet` DISABLE KEYS */;
/*!40000 ALTER TABLE `wallet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wishlist`
--

DROP TABLE IF EXISTS `wishlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wishlist` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `add_at` datetime(6) DEFAULT NULL,
  `course_id` bigint DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3oou0jaquu0ulln4n7xs3ltao` (`course_id`),
  KEY `FKtrd6335blsefl2gxpb8lr0gr7` (`user_id`),
  CONSTRAINT `FK3oou0jaquu0ulln4n7xs3ltao` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `FKtrd6335blsefl2gxpb8lr0gr7` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wishlist`
--

LOCK TABLES `wishlist` WRITE;
/*!40000 ALTER TABLE `wishlist` DISABLE KEYS */;
/*!40000 ALTER TABLE `wishlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `withdraws`
--

DROP TABLE IF EXISTS `withdraws`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `withdraws` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `otp` varchar(255) DEFAULT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `request_date` datetime(6) DEFAULT NULL,
  `status` enum('PENDING','COMPLETED','CANCELLED') DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `otp_creation_time` datetime(6) DEFAULT NULL,
  `otp_expiration_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKb9fxn8o7hsdme1wlhoaq7sr8b` (`user_id`),
  CONSTRAINT `FKb9fxn8o7hsdme1wlhoaq7sr8b` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `withdraws`
--

LOCK TABLES `withdraws` WRITE;
/*!40000 ALTER TABLE `withdraws` DISABLE KEYS */;
/*!40000 ALTER TABLE `withdraws` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-03 21:22:27
