-- MySQL dump 10.13  Distrib 9.7.1, for macos26.4 (arm64)
--
-- Host: localhost    Database: edEXT_db
-- Datos de prueba: Tarea 1 + Anexo Tarea 2 + Anexo Tarea 3 (ProgApp 2026)
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Categoria`
--

DROP TABLE IF EXISTS `Categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Categoria` (
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Categoria`
--

LOCK TABLES `Categoria` WRITE;
/*!40000 ALTER TABLE `Categoria` DISABLE KEYS */;
INSERT INTO `Categoria` VALUES ('Educativo'),('Industrial'),('Interdisciplinario'),('Social');
/*!40000 ALTER TABLE `Categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Categoria_Curso`
--

DROP TABLE IF EXISTS `Categoria_Curso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Categoria_Curso` (
  `Nombre_Curso` varchar(255) NOT NULL,
  `Categoria_Nombre` varchar(255) NOT NULL,
  KEY `FKq024sk5fhhij66ve2whksc7ld` (`Categoria_Nombre`),
  KEY `FKapspp62e39x992w6w8ow04c20` (`Nombre_Curso`),
  CONSTRAINT `FKapspp62e39x992w6w8ow04c20` FOREIGN KEY (`Nombre_Curso`) REFERENCES `Curso` (`Nombre`),
  CONSTRAINT `FKq024sk5fhhij66ve2whksc7ld` FOREIGN KEY (`Categoria_Nombre`) REFERENCES `Categoria` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Categoria_Curso`
--

LOCK TABLES `Categoria_Curso` WRITE;
/*!40000 ALTER TABLE `Categoria_Curso` DISABLE KEYS */;
INSERT INTO `Categoria_Curso` VALUES ('Dalavuelta','Social'),('Taller de robótica educativa','Social'),('Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación','Social'),('Extensionismo Industrial','Industrial'),('Inclusión Energética','Industrial'),('MicroBit','Industrial'),('Flor del Ceibo','Educativo'),('Taller de robótica educativa','Educativo'),('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela','Educativo'),('Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación','Educativo'),('MicroBit','Educativo'),('Flor del Ceibo','Interdisciplinario');
/*!40000 ALTER TABLE `Categoria_Curso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Curso`
--

DROP TABLE IF EXISTS `Curso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Curso` (
  `Nombre` varchar(255) NOT NULL,
  `URL` varchar(255) DEFAULT NULL,
  `Cant_Creditos` int DEFAULT NULL,
  `Cant_Horas` float DEFAULT NULL,
  `Descripcion` varchar(750) DEFAULT NULL,
  `Duracion` int DEFAULT NULL,
  `F_Alta` datetime(6) DEFAULT NULL,
  `ImagePath` varchar(255) DEFAULT NULL,
  `Nombre_Doc` varchar(255) DEFAULT NULL,
  `Instituto` varchar(255) DEFAULT NULL,
  `Visitas` int DEFAULT '0',
  PRIMARY KEY (`Nombre`),
  KEY `FKmvg8nj45nq9gepl616cj3m1j5` (`Nombre_Doc`),
  KEY `FKutg3fr49sra4br4afg42fn0k` (`Instituto`),
  CONSTRAINT `FKmvg8nj45nq9gepl616cj3m1j5` FOREIGN KEY (`Nombre_Doc`) REFERENCES `Docente` (`nickname`),
  CONSTRAINT `FKutg3fr49sra4br4afg42fn0k` FOREIGN KEY (`Instituto`) REFERENCES `Instituto` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Curso`
--

LOCK TABLES `Curso` WRITE;
/*!40000 ALTER TABLE `Curso` DISABLE KEYS */;
INSERT INTO `Curso` VALUES ('Dalavuelta','https://eva.fing.edu.uy/course/view.php?id=783#section-2',4,60,'Dalavuelta es un proyecto de extensión que nace en el Instituto de Ingeniería Mecánica y Producción Industrial (IIMPI) de Fing, que, si bien inicia su trabajo en el desarrollo de bicicletas accesibles para personas en situación de discapacidad motriz a partir de bicicletas abandonadas, se propuso diseñar otras herramientas para fomentar la accesibilidad.',10,'2024-06-25 00:00:00.000000','c3.jpg',NULL,'IMPII',132),('Extensionismo Industrial','https://eva.fing.edu.uy/course/view.php?id=783#section-2',5,75,'El proyecto tiene como objetivo desarrollar intervenciones curriculares en pequeños emprendimientos productivos de diferentes sectores de la industria nacional. La metodologías de trabajo permite articular diversas intervenciones, combinando actividades de enseñanza, extensión e investigación por parte de docentes del IMPII.',12,'2025-06-16 00:00:00.000000','',NULL,'IMPII',103),('Flor del Ceibo','http://www.flordeceibo.edu.uy/',10,150,'Flor de Ceibo es un proyecto central de la Universidad de la República, que tiene misión por movilizar la participación de estudiantes universitarios en diversas tareas vinculadas con la puesta en funcionamiento del Plan Ceibal en el territorio nacional.',15,'2008-07-27 00:00:00.000000','c6.jpg',NULL,'DISI',430),('Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación','https://eva.fing.edu.uy/mod/folder/view.php?id=89398',4,60,'Se realizarán visitas a escuelas rurales participantes en un proyecto conjunto del grupo PLN y el Programa de Políticas Lingüísticas de ANEP, en el marco del cual se desarrollaron diferentes herramientas para uso de maestros que enseñan inglés con apoyo remoto de profesores especializados desde Montevideo.',12,'2026-05-24 00:00:00.000000','',NULL,'INCO',169),('Inclusión Energética','https://eva.fing.edu.uy/course/view.php?id=783#section-2',3,45,'En el proyecto se conjuga el trabajo de docentes y estudiantes de la carrera Ingeniería Industrial Mecánica a través del Módulo de Extensión, en donde se trabaja en el diseño, construcción y prueba de un prototipo de colector solar adquiriendo conocimientos relevantes para luego poder replicarlos junto a las familias en los talleres. Las premisas fundamentales a la hora de pensar los diseños son: por un lado el bajo costo de los materiales y por otro la fácil construcción de forma de poder construirlos ellos mismos.',6,'2026-02-01 00:00:00.000000','c5.jpg',NULL,'IMPII',98),('MicroBit','https://www.fing.edu.uy/noticias/extension/modulo-de-taller-de-extension-microbit',7,105,'El Centro Ceibal se encuentra distribuyendo placas micro:bit (https://microbit.ceibal.edu.uy/) para que estudiantes de primaria y secundaria aprendan nociones básicas de robótica, electrónica y programación de forma autónoma y lúdica. Estas placas se basan en un microcontrolador y cuentan con leds, botones, acelerómetro, brújula, bluetooth y otros sensores. Además, las placas se programan fácilmente con lenguaje tipo \"scratch\" y python, por lo que son muy útiles para un primer acercamiento a la temática.',15,'2026-03-13 00:00:00.000000','c10.jpg',NULL,'Eléctrica',212),('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela','https://eva.fing.edu.uy/mod/folder/view.php?id=89398',3,45,'Se propone desarrollar una aplicación interactiva para tablet Android basada en el juego de tablero Komikan (versión web del juego https://codepen.io/Borborem/full/OvZBvZ/), que incluya los distintos aspectos concernientes al juego, así como a situaciones específicas particulares.',9,'2026-06-15 00:00:00.000000','c8.jpg',NULL,'INCO',267),('Seminarios de Resolución de Problemas','www.tmu.edu.uy',2,30,'Seminario, todos los jueves en Facultad de Ingeniería a partir del jueves 25 de Julio, en las áreas en que se desarrollan los problemas de las Olimpíadas de Matemática.',5,'2026-07-12 00:00:00.000000','',NULL,'IMERL',120),('Taller de robótica educativa','https://eva.fing.edu.uy/course/view.php?id=1187',6,90,'La asignatura se organiza en dos etapas. La primer etapa se dicta a través de clases teórico-prácticas, donde se espera además que cada estudiante le dedique horas de estudio. La segunda etapa consiste en que los estudiantes trabajen en grupo sobre el diseño e implementación de una experiencia didáctica de inclusión del robot Butiá en el aula, utilizando los conocimientos aprendidos en clase.',8,'2024-02-02 00:00:00.000000','c7.jpg',NULL,'INCO',318),('Talleres plenarios','www.tmu.edu.uy',1,15,'Talleres plenarios: presentados por cuatro reconocidos matemáticos uruguayos, plantearán diversos tópicos de matemática en el marco de los cuales se realizarán actividades fomentando la integración entre estudiantes, docentes e investigadores.',3,'2026-02-01 00:00:00.000000','',NULL,'IMERL',150);
/*!40000 ALTER TABLE `Curso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Docente`
--

DROP TABLE IF EXISTS `Docente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Docente` (
  `nickname` varchar(255) NOT NULL,
  `Instituto` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`nickname`),
  KEY `FKqumtv2okp2yf6q0fvoixbncrt` (`Instituto`),
  CONSTRAINT `FKjyrbt5bvnspsaccyn4j1qunho` FOREIGN KEY (`nickname`) REFERENCES `UsuarioBase` (`Nickname`),
  CONSTRAINT `FKqumtv2okp2yf6q0fvoixbncrt` FOREIGN KEY (`Instituto`) REFERENCES `Instituto` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Docente`
--

LOCK TABLES `Docente` WRITE;
/*!40000 ALTER TABLE `Docente` DISABLE KEYS */;
INSERT INTO `Docente` VALUES ('adri','DISI'),('bruces','DISI'),('house','Eléctrica'),('danny','IMERL'),('timmy','IMERL'),('phils','IMPII'),('benkenobi','INCO'),('heisenberg','INCO'),('waston','INCO');
/*!40000 ALTER TABLE `Docente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `docente_edicioncurso`
--

DROP TABLE IF EXISTS `docente_edicioncurso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `docente_edicioncurso` (
  `ediciones_Nombre` varchar(255) NOT NULL,
  `docentes_Nickname` varchar(255) NOT NULL,
  KEY `FKt45xqi4p1wu4emp55k2ma1w20` (`docentes_Nickname`),
  KEY `FKgq8obxq4v3arm8j78tanoygvs` (`ediciones_Nombre`),
  CONSTRAINT `FKgq8obxq4v3arm8j78tanoygvs` FOREIGN KEY (`ediciones_Nombre`) REFERENCES `EdicionCurso` (`Nombre`),
  CONSTRAINT `FKt45xqi4p1wu4emp55k2ma1w20` FOREIGN KEY (`docentes_Nickname`) REFERENCES `Docente` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `docente_edicioncurso`
--

LOCK TABLES `docente_edicioncurso` WRITE;
/*!40000 ALTER TABLE `docente_edicioncurso` DISABLE KEYS */;
INSERT INTO `docente_edicioncurso` VALUES ('Flor del Ceibo - 2010','bruces'),('Flor del Ceibo - 2012','bruces'),('Flor del Ceibo - 2012','adri'),('Flor del Ceibo - 2025','bruces'),('Flor del Ceibo - 2025','adri'),('Dalavuelta - 2025','phils'),('Extensionismo Industrial - 2025','phils'),('Inclusión Energética - 2026','phils'),('Taller de robótica educativa - 2024','heisenberg'),('Taller de robótica educativa - 2026','heisenberg'),('Taller de robótica educativa - 2026','benkenobi'),('Taller de robótica educativa - 2026-2','benkenobi'),('Taller de robótica educativa - 2026-2','waston'),('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026','waston'),('Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26','heisenberg'),('MicroBit - 2026','house'),('Talleres plenarios - 2026','timmy'),('Talleres plenarios - 2026','danny'),('Seminarios de Resolución de Problemas - 2026','timmy');
/*!40000 ALTER TABLE `docente_edicioncurso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `edi_usu`
--

DROP TABLE IF EXISTS `edi_usu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `edi_usu` (
  `estado` enum('ACEPTADO','INSCRIPTO','RECHAZADO') DEFAULT NULL,
  `Fecha_inscripcion` date DEFAULT NULL,
  `edicion_nombre` varchar(255) NOT NULL,
  `usuario_nickname` varchar(255) NOT NULL,
  `nota` int DEFAULT NULL,
  `url_video` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`edicion_nombre`,`usuario_nickname`),
  KEY `FK7x53s85h4vx08ron0pmdv6skv` (`usuario_nickname`),
  CONSTRAINT `FK2oosku3ny3ylpjey598bp2v42` FOREIGN KEY (`edicion_nombre`) REFERENCES `EdicionCurso` (`Nombre`),
  CONSTRAINT `FK7x53s85h4vx08ron0pmdv6skv` FOREIGN KEY (`usuario_nickname`) REFERENCES `Usuario` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `edi_usu`
--

LOCK TABLES `edi_usu` WRITE;
/*!40000 ALTER TABLE `edi_usu` DISABLE KEYS */;
INSERT INTO `edi_usu` VALUES ('ACEPTADO','2024-07-25','Dalavuelta - 2025','chechi',7,'https://www.youtube.com/watch?v=vfc42Pb5RA8'),('ACEPTADO','2024-08-10','Dalavuelta - 2025','costas',6,NULL),('RECHAZADO','2024-07-28','Dalavuelta - 2025','eleven11',NULL,NULL),('RECHAZADO','2024-08-15','Dalavuelta - 2025','jeffw',NULL,NULL),('ACEPTADO','2024-08-02','Dalavuelta - 2025','roro',7,'https://www.youtube.com/watch?v=Hg469wSrZhI'),('ACEPTADO','2025-07-20','Extensionismo Industrial - 2025','chechi',9,NULL),('ACEPTADO','2025-07-18','Extensionismo Industrial - 2025','costas',9,NULL),('ACEPTADO','2025-07-29','Extensionismo Industrial - 2025','eleven11',1,NULL),('ACEPTADO','2025-08-05','Extensionismo Industrial - 2025','weiss',11,NULL),('ACEPTADO','2010-02-25','Flor del Ceibo - 2010','chechi',8,'https://www.youtube.com/watch?v=c4ZoticPW1s'),('ACEPTADO','2010-02-20','Flor del Ceibo - 2010','eleven11',6,NULL),('ACEPTADO','2012-07-12','Flor del Ceibo - 2012','costas',9,'https://www.youtube.com/watch?v=1qfw6Rd_-ic'),('RECHAZADO','2012-07-15','Flor del Ceibo - 2012','roro',NULL,'https://www.youtube.com/watch?v=Ov4usZlSieM'),('ACEPTADO','2012-07-30','Flor del Ceibo - 2012','weiss',0,NULL),('ACEPTADO','2025-03-15','Flor del Ceibo - 2025','jeffw',6,NULL),('ACEPTADO','2025-03-10','Flor del Ceibo - 2025','roro',9,'https://www.youtube.com/watch?v=eIvqPPcMepU'),('INSCRIPTO','2026-07-18','Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26','eleven11',NULL,NULL),('INSCRIPTO','2026-08-20','Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26','jeffw',NULL,NULL),('INSCRIPTO','2026-06-04','Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26','weiss',NULL,NULL),('ACEPTADO','2026-02-28','Inclusión Energética - 2026','chechi',11,NULL),('RECHAZADO','2026-03-03','Inclusión Energética - 2026','eleven11',NULL,NULL),('ACEPTADO','2026-02-23','Inclusión Energética - 2026','roro',7,'https://www.youtube.com/watch?v=dG3niF2GgHM'),('RECHAZADO','2026-02-25','Inclusión Energética - 2026','weiss',NULL,NULL),('INSCRIPTO','2026-07-12','MicroBit - 2026','chechi',NULL,'https://www.youtube.com/watch?v=lSm4QZqN8cA'),('INSCRIPTO','2026-07-25','MicroBit - 2026','eleven11',NULL,'https://www.youtube.com/watch?v=AfqdNJ75544'),('INSCRIPTO','2026-08-05','MicroBit - 2026','jeffw',NULL,NULL),('INSCRIPTO','2026-07-14','MicroBit - 2026','roro',NULL,'https://www.youtube.com/watch?v=SWOz-kIwDuU'),('ACEPTADO','2026-07-13','Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026','chechi',0,'https://www.youtube.com/watch?v=AHZw09PoIU0'),('ACEPTADO','2026-07-22','Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026','roro',0,NULL),('ACEPTADO','2026-07-20','Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026','weiss',0,NULL),('RECHAZADO','2026-08-30','Seminarios de Resolución de Problemas - 2026','chechi',NULL,NULL),('RECHAZADO','2026-07-20','Seminarios de Resolución de Problemas - 2026','costas',NULL,NULL),('ACEPTADO','2026-08-06','Seminarios de Resolución de Problemas - 2026','roro',2,NULL),('ACEPTADO','2026-07-15','Seminarios de Resolución de Problemas - 2026','weiss',10,NULL),('ACEPTADO','2024-03-03','Taller de robótica educativa - 2024','eleven11',0,NULL),('RECHAZADO','2024-02-20','Taller de robótica educativa - 2024','roro',NULL,NULL),('RECHAZADO','2024-02-18','Taller de robótica educativa - 2024','weiss',NULL,NULL),('ACEPTADO','2026-02-22','Taller de robótica educativa - 2026','costas',10,NULL),('ACEPTADO','2026-02-18','Taller de robótica educativa - 2026','jeffw',8,NULL),('RECHAZADO','2026-02-23','Taller de robótica educativa - 2026','roro',NULL,'https://www.youtube.com/watch?v=qYEWtIBmscA'),('INSCRIPTO','2026-08-22','Taller de robótica educativa - 2026-2','chechi',NULL,'https://www.youtube.com/watch?v=QUYKSWQmkrg'),('INSCRIPTO','2026-09-03','Taller de robótica educativa - 2026-2','roro',NULL,NULL),('INSCRIPTO','2026-08-18','Taller de robótica educativa - 2026-2','weiss',NULL,'https://www.youtube.com/watch?v=sJq7EE0cuVE'),('RECHAZADO','2026-03-05','Talleres plenarios - 2026','costas',NULL,NULL),('ACEPTADO','2026-03-07','Talleres plenarios - 2026','roro',7,NULL),('ACEPTADO','2026-03-04','Talleres plenarios - 2026','weiss',6,NULL);
/*!40000 ALTER TABLE `edi_usu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EdicionCurso`
--

DROP TABLE IF EXISTS `EdicionCurso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EdicionCurso` (
  `Nombre` varchar(255) NOT NULL,
  `Cupo` int DEFAULT NULL,
  `Cupo_actual` int DEFAULT NULL,
  `F_Alta` datetime(6) DEFAULT NULL,
  `F_Fin` date DEFAULT NULL,
  `F_Inicio` date DEFAULT NULL,
  `ImagePath` varchar(255) DEFAULT NULL,
  `Curso` varchar(255) DEFAULT NULL,
  `Instituto` varchar(255) DEFAULT NULL,
  `estado` enum('EN_CURSO','FINALIZADA','NORMAL','SIN_INICIAR') DEFAULT NULL,
  `F_Resultado` date DEFAULT NULL,
  PRIMARY KEY (`Nombre`),
  KEY `FK5o231wjtl4ot06jwuicmi4rt4` (`Curso`),
  KEY `FK5qer9wnx2pe49f5l95jgwsqt1` (`Instituto`),
  CONSTRAINT `FK5o231wjtl4ot06jwuicmi4rt4` FOREIGN KEY (`Curso`) REFERENCES `Curso` (`Nombre`),
  CONSTRAINT `FK5qer9wnx2pe49f5l95jgwsqt1` FOREIGN KEY (`Instituto`) REFERENCES `Instituto` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EdicionCurso`
--

LOCK TABLES `EdicionCurso` WRITE;
/*!40000 ALTER TABLE `EdicionCurso` DISABLE KEYS */;
INSERT INTO `EdicionCurso` VALUES ('Dalavuelta - 2025',15,15,'2024-07-20 00:00:00.000000','2024-11-10','2024-08-20','e4.jpg','Dalavuelta','IMPII','FINALIZADA','2024-11-10'),('Extensionismo Industrial - 2025',15,15,'2025-07-08 00:00:00.000000','2025-11-10','2025-08-10','','Extensionismo Industrial','IMPII','FINALIZADA','2025-11-10'),('Flor del Ceibo - 2010',0,0,'2010-02-16 00:00:00.000000','2010-07-07','2010-03-15','e1.jpg','Flor del Ceibo','DISI','FINALIZADA','2010-07-07'),('Flor del Ceibo - 2012',0,0,'2012-07-10 00:00:00.000000','2012-11-20','2012-08-01','e2.jpg','Flor del Ceibo','DISI','FINALIZADA','2012-11-20'),('Flor del Ceibo - 2025',0,0,'2025-03-06 00:00:00.000000','2025-08-07','2025-04-10','e3.jpg','Flor del Ceibo','DISI','FINALIZADA','2025-08-07'),('Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26',5,5,'2026-06-02 00:00:00.000000','2026-12-15','2026-09-15','','Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación','INCO',NULL,NULL),('Inclusión Energética - 2026',30,30,'2026-02-20 00:00:00.000000','2026-04-30','2026-03-15','','Inclusión Energética','IMPII','FINALIZADA','2026-04-30'),('MicroBit - 2026',30,30,'2026-07-02 00:00:00.000000','2026-12-05','2026-08-12','','MicroBit','Eléctrica',NULL,NULL),('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026',5,5,'2026-07-10 00:00:00.000000','2026-10-07','2026-07-29','','Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela','INCO','FINALIZADA','2026-10-07'),('Seminarios de Resolución de Problemas - 2026',0,0,'2026-07-12 00:00:00.000000','2026-10-20','2026-09-10','','Seminarios de Resolución de Problemas','IMERL','FINALIZADA','2026-10-20'),('Taller de robótica educativa - 2024',10,10,'2024-02-15 00:00:00.000000','2024-05-10','2024-03-10','e7.jpg','Taller de robótica educativa','INCO','FINALIZADA','2024-05-10'),('Taller de robótica educativa - 2026',10,10,'2026-02-15 00:00:00.000000','2026-05-10','2026-03-10','e8.jpg','Taller de robótica educativa','INCO','FINALIZADA','2026-05-10'),('Taller de robótica educativa - 2026-2',20,20,'2026-08-15 00:00:00.000000','2026-11-08','2026-09-10','e9.jpg','Taller de robótica educativa','INCO',NULL,NULL),('Talleres plenarios - 2026',0,0,'2026-03-02 00:00:00.000000','2026-03-30','2026-03-10','','Talleres plenarios','IMERL','FINALIZADA','2026-03-30');
/*!40000 ALTER TABLE `EdicionCurso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Instituto`
--

DROP TABLE IF EXISTS `Instituto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Instituto` (
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Instituto`
--

LOCK TABLES `Instituto` WRITE;
/*!40000 ALTER TABLE `Instituto` DISABLE KEYS */;
INSERT INTO `Instituto` VALUES ('DISI'),('Eléctrica'),('Física'),('IMERL'),('IMPII'),('INCO');
/*!40000 ALTER TABLE `Instituto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `previa`
--

DROP TABLE IF EXISTS `previa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `previa` (
  `Nombre` varchar(255) NOT NULL,
  `Previa_Nombre` varchar(255) NOT NULL,
  KEY `FKg1k3g3bj4094560g63cqwd62o` (`Previa_Nombre`),
  KEY `FKcmaeftvk8jnxw6q559qr4stvd` (`Nombre`),
  CONSTRAINT `FKcmaeftvk8jnxw6q559qr4stvd` FOREIGN KEY (`Nombre`) REFERENCES `Curso` (`Nombre`),
  CONSTRAINT `FKg1k3g3bj4094560g63cqwd62o` FOREIGN KEY (`Previa_Nombre`) REFERENCES `Curso` (`Nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `previa`
--

LOCK TABLES `previa` WRITE;
/*!40000 ALTER TABLE `previa` DISABLE KEYS */;
INSERT INTO `previa` VALUES ('Seminarios de Resolución de Problemas','Talleres plenarios'),('Dalavuelta','Talleres plenarios'),('Extensionismo Industrial','Talleres plenarios');
/*!40000 ALTER TABLE `previa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Prog_Usu`
--

DROP TABLE IF EXISTS `Prog_Usu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Prog_Usu` (
  `fInscripcion` date DEFAULT NULL,
  `miPrograma_nombre` varchar(255) NOT NULL,
  `miUsuario_nickname` varchar(255) NOT NULL,
  PRIMARY KEY (`miPrograma_nombre`,`miUsuario_nickname`),
  KEY `FK8l9n9bxt5pto99bwrrukhaf0v` (`miUsuario_nickname`),
  CONSTRAINT `FK8l9n9bxt5pto99bwrrukhaf0v` FOREIGN KEY (`miUsuario_nickname`) REFERENCES `Usuario` (`nickname`),
  CONSTRAINT `FKrtgx1uhciiud4ip5e9solykgk` FOREIGN KEY (`miPrograma_nombre`) REFERENCES `ProgramaDeFormacion` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Prog_Usu`
--

LOCK TABLES `Prog_Usu` WRITE;
/*!40000 ALTER TABLE `Prog_Usu` DISABLE KEYS */;
INSERT INTO `Prog_Usu` VALUES ('2026-04-20','EFI Ingeniería Mecánica','chechi'),('2026-04-15','EFI Ingeniería Mecánica','eleven11'),('2026-08-15','EFI Robótica','chechi'),('2026-08-11','EFI Robótica','costas'),('2026-08-30','EFI Robótica','jeffw'),('2026-08-22','EFI Robótica','roro'),('2026-07-10','Formación integral','chechi'),('2026-07-10','Formación integral','costas'),('2026-07-12','Formación integral','eleven11'),('2026-07-18','Formación integral','roro'),('2026-07-15','Formación integral','weiss');
/*!40000 ALTER TABLE `Prog_Usu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `programa_curso`
--

DROP TABLE IF EXISTS `programa_curso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `programa_curso` (
  `programa_nombre` varchar(255) NOT NULL,
  `curso_nombre` varchar(255) NOT NULL,
  KEY `FK_progcurso_curso` (`curso_nombre`),
  KEY `FK_progcurso_programa` (`programa_nombre`),
  CONSTRAINT `FK_progcurso_curso` FOREIGN KEY (`curso_nombre`) REFERENCES `Curso` (`Nombre`),
  CONSTRAINT `FK_progcurso_programa` FOREIGN KEY (`programa_nombre`) REFERENCES `ProgramaDeFormacion` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programa_curso`
--

LOCK TABLES `programa_curso` WRITE;
/*!40000 ALTER TABLE `programa_curso` DISABLE KEYS */;
INSERT INTO `programa_curso` VALUES ('EFI Ingeniería Mecánica','Dalavuelta'),('EFI Ingeniería Mecánica','Extensionismo Industrial'),('EFI Ingeniería Mecánica','Inclusión Energética'),('Formación integral','Seminarios de Resolución de Problemas'),('Formación integral','Extensionismo Industrial'),('Formación integral','Flor del Ceibo'),('Formación integral','Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela'),('EFI Robótica','Taller de robótica educativa'),('EFI Robótica','MicroBit');
/*!40000 ALTER TABLE `programa_curso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ProgramaDeFormacion`
--

DROP TABLE IF EXISTS `ProgramaDeFormacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ProgramaDeFormacion` (
  `nombre` varchar(255) NOT NULL,
  `Descripcion` varchar(255) DEFAULT NULL,
  `F_Alta` datetime(6) DEFAULT NULL,
  `ImagePath` varchar(255) DEFAULT NULL,
  `fechaFin` datetime(6) DEFAULT NULL,
  `fechaInicio` datetime(6) DEFAULT NULL,
  `Instituto` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`nombre`),
  KEY `FK_programa_instituto` (`Instituto`),
  CONSTRAINT `FK_programa_instituto` FOREIGN KEY (`Instituto`) REFERENCES `Instituto` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ProgramaDeFormacion`
--

LOCK TABLES `ProgramaDeFormacion` WRITE;
/*!40000 ALTER TABLE `ProgramaDeFormacion` DISABLE KEYS */;
INSERT INTO `ProgramaDeFormacion` VALUES ('EFI Ingeniería Mecánica','Programa mecánica','2026-05-01 00:00:00.000000','p1.jpg','2026-10-31 00:00:00.000000','2026-05-01 00:00:00.000000',NULL),('EFI Robótica','Programa robótica','2026-09-03 00:00:00.000000','p3.jpg','2026-11-18 00:00:00.000000','2026-09-03 00:00:00.000000',NULL),('Formación integral','Programa varios institutos','2026-07-15 00:00:00.000000','p2.jpg','2027-01-01 00:00:00.000000','2026-07-15 00:00:00.000000',NULL);
/*!40000 ALTER TABLE `ProgramaDeFormacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Seguidos`
--

DROP TABLE IF EXISTS `Seguidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Seguidos` (
  `MyNickname` varchar(255) NOT NULL,
  `NicknameOtherUser` varchar(255) NOT NULL,
  KEY `FKdwl40gjfgy7kio6a840u61vkg` (`NicknameOtherUser`),
  KEY `FKi1jmyw6sn3wdaqnd49l4u4ew1` (`MyNickname`),
  CONSTRAINT `FKdwl40gjfgy7kio6a840u61vkg` FOREIGN KEY (`NicknameOtherUser`) REFERENCES `UsuarioBase` (`Nickname`),
  CONSTRAINT `FKi1jmyw6sn3wdaqnd49l4u4ew1` FOREIGN KEY (`MyNickname`) REFERENCES `UsuarioBase` (`Nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Seguidos`
--

LOCK TABLES `Seguidos` WRITE;
/*!40000 ALTER TABLE `Seguidos` DISABLE KEYS */;
INSERT INTO `Seguidos` VALUES ('eleven11','costas'),('eleven11','roro'),('eleven11','chechi'),('eleven11','heisenberg'),('eleven11','waston'),('costas','chechi'),('costas','jeffw'),('costas','benkenobi'),('costas','danny'),('costas','phils'),('roro','costas'),('roro','weiss'),('chechi','eleven11'),('chechi','jeffw'),('chechi','house'),('jeffw','eleven11'),('weiss','costas'),('weiss','heisenberg'),('weiss','timmy'),('heisenberg','roro'),('benkenobi','danny'),('waston','roro'),('waston','weiss'),('waston','heisenberg'),('house','jeffw'),('house','weiss'),('house','benkenobi'),('timmy','eleven11'),('timmy','chechi'),('timmy','jeffw'),('danny','costas'),('danny','jeffw'),('danny','heisenberg'),('phils','bruces'),('bruces','phils'),('adri','heisenberg');
/*!40000 ALTER TABLE `Seguidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Usuario`
--

DROP TABLE IF EXISTS `Usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Usuario` (
  `nickname` varchar(255) NOT NULL,
  PRIMARY KEY (`nickname`),
  CONSTRAINT `FKga2kco8t2ppgny1hx34j2ce9o` FOREIGN KEY (`nickname`) REFERENCES `UsuarioBase` (`Nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Usuario`
--

LOCK TABLES `Usuario` WRITE;
/*!40000 ALTER TABLE `Usuario` DISABLE KEYS */;
INSERT INTO `Usuario` VALUES ('chechi'),('costas'),('eleven11'),('jeffw'),('roro'),('weiss');
/*!40000 ALTER TABLE `Usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UsuarioBase`
--

DROP TABLE IF EXISTS `UsuarioBase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UsuarioBase` (
  `Nickname` varchar(255) NOT NULL,
  `Apellido` varchar(255) DEFAULT NULL,
  `contrasenia` varchar(255) DEFAULT NULL,
  `Correo` varchar(255) NOT NULL,
  `fNac` datetime(6) DEFAULT NULL,
  `ImagePath` varchar(255) DEFAULT NULL,
  `Nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Nickname`),
  UNIQUE KEY `UKql5dmn84ieovnvgygfv13pij5` (`Correo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UsuarioBase`
--

LOCK TABLES `UsuarioBase` WRITE;
/*!40000 ALTER TABLE `UsuarioBase` DISABLE KEYS */;
INSERT INTO `UsuarioBase` VALUES ('adri','García','1234','agarcia@gmail.com','1978-07-28 00:00:00.000000','','Adriana'),('benkenobi','Kenobi','1234','benKenobi@gmail.com','1914-04-02 00:00:00.000000','benkenobi.jpg','Obi-Wan'),('bruces','Sewell','1234','sewell@gmail.com','1959-12-03 00:00:00.000000','','Bruce'),('chechi','Garrido','1234','cgarrido@hotmail.com','1987-09-12 00:00:00.000000','chechi.jpg','Cecilia'),('costas','Costas','1234','gcostas@gmail.com','1983-11-15 00:00:00.000000','','Gerardo'),('danny','Riccio','1234','dan.riccio@gmail.com','1963-07-05 00:00:00.000000','','Daniel'),('eleven11','Twelve','1234','eleven11@gmail.com','1971-12-31 00:00:00.000000','','Eleven'),('heisenberg','White','1234','heisenberg@gmail.com','1956-03-07 00:00:00.000000','walter_white.png','Walter'),('house','House','1234','greghouse@gmail.com','1959-05-15 00:00:00.000000','house.jpg','Gregory'),('jeffw','Williams','1234','jwilliams@gmail.com','1964-11-27 00:00:00.000000','jeffw.jpg','Jeff'),('phils','Schiller','1234','schiller@gmail.com','1961-10-07 00:00:00.000000','','Philip'),('roro','Cotelo','1234','rcotelo@yahoo.com','1975-08-02 00:00:00.000000','roro.jpg','Rodrigo'),('timmy','Cook','1234','tim.cook@apple.com','1960-11-01 00:00:00.000000','timmy.jpg','Tim'),('waston','Watson','1234','e.watson@gmail.com','1990-04-15 00:00:00.000000','waston.jpg','Emma'),('weiss','Weiss','1234','aweiss@hotmail.com','1978-12-23 00:00:00.000000','','Adrian');
/*!40000 ALTER TABLE `UsuarioBase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ValoracionCurso`
--

DROP TABLE IF EXISTS `ValoracionCurso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ValoracionCurso` (
  `puntaje` int NOT NULL,
  `curso_nombre` varchar(255) NOT NULL,
  `usuario_nickname` varchar(255) NOT NULL,
  PRIMARY KEY (`curso_nombre`,`usuario_nickname`),
  KEY `FKhs37u8nwx8stdnsqgsurng6th` (`usuario_nickname`),
  CONSTRAINT `FK6puxt5h1sljvi3mmgk2aj7b50` FOREIGN KEY (`curso_nombre`) REFERENCES `Curso` (`Nombre`),
  CONSTRAINT `FKhs37u8nwx8stdnsqgsurng6th` FOREIGN KEY (`usuario_nickname`) REFERENCES `Usuario` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ValoracionCurso`
--

LOCK TABLES `ValoracionCurso` WRITE;
/*!40000 ALTER TABLE `ValoracionCurso` DISABLE KEYS */;
INSERT INTO `ValoracionCurso` VALUES (2,'Dalavuelta','chechi'),(4,'Dalavuelta','costas'),(5,'Dalavuelta','roro'),(2,'Extensionismo Industrial','chechi'),(1,'Extensionismo Industrial','costas'),(3,'Extensionismo Industrial','eleven11'),(1,'Extensionismo Industrial','weiss'),(4,'Flor del Ceibo','chechi'),(5,'Flor del Ceibo','costas'),(3,'Flor del Ceibo','eleven11'),(3,'Flor del Ceibo','jeffw'),(3,'Flor del Ceibo','roro'),(4,'Flor del Ceibo','weiss'),(5,'Inclusión Energética','chechi'),(5,'Inclusión Energética','roro'),(4,'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela','chechi'),(4,'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela','roro'),(4,'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela','weiss'),(2,'Seminarios de Resolución de Problemas','roro'),(4,'Taller de robótica educativa','eleven11'),(5,'Taller de robótica educativa','jeffw'),(1,'Talleres plenarios','weiss');
/*!40000 ALTER TABLE `ValoracionCurso` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed
