-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: localhost    Database: colegiobd
-- ------------------------------------------------------
-- Server version	8.0.26

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
-- Table structure for table `area`
--

DROP TABLE IF EXISTS `area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `area` (
  `idarea` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`idarea`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `area`
--

LOCK TABLES `area` WRITE;
/*!40000 ALTER TABLE `area` DISABLE KEYS */;
INSERT INTO `area` VALUES (1,'Area de laboratorio de computacion'),(2,'Area de laboratorio de Ciencias'),(3,'Area de Musica'),(4,'Area del limpieza');
/*!40000 ALTER TABLE `area` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `articulo`
--

DROP TABLE IF EXISTS `articulo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `articulo` (
  `idarticulo` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `descripcion` varchar(200) NOT NULL,
  `fecha_registro` date DEFAULT NULL,
  `idArea` int NOT NULL,
  `idestado` int NOT NULL,
  `idcategoria` int NOT NULL,
  PRIMARY KEY (`idarticulo`),
  KEY `fk_articulos_Area1_idx` (`idArea`),
  KEY `fk_articulos_estado1_idx` (`idestado`),
  KEY `fk_articulos_categoria1_idx` (`idcategoria`),
  CONSTRAINT `fk_articulos_Area1` FOREIGN KEY (`idArea`) REFERENCES `area` (`idarea`),
  CONSTRAINT `fk_articulos_categoria1` FOREIGN KEY (`idcategoria`) REFERENCES `categoria` (`idcategoria`),
  CONSTRAINT `fk_articulos_estado1` FOREIGN KEY (`idestado`) REFERENCES `estado` (`idestado`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `articulo`
--

LOCK TABLES `articulo` WRITE;
/*!40000 ALTER TABLE `articulo` DISABLE KEYS */;
INSERT INTO `articulo` VALUES (1,'GUITARRA NUEVA','GUITARRA ELECTRONICA DE COLOR PLOMO',NULL,3,3,4),(2,'Escobas','escoba con manga de metal ',NULL,4,3,3),(3,'Mueble','mueble de 1m de color marron',NULL,1,2,5),(4,'Embudo de vidrio','Fragil, 10 cm de tamaño',NULL,2,2,1),(5,'teclado','color negro con entrada USB',NULL,1,1,2),(6,'Mesa Redonda','Mesa Grande','2021-09-01',3,3,4),(7,'Silla','Silla pequeña','2021-09-01',2,2,5),(8,'Articulo','descripcion','2021-09-01',2,2,3),(9,'Nuevo articulo','una descripcion','2021-09-01',3,3,3),(10,'ESCOBAS','ESCOBA CON MANGA DE METAL ','2021-09-01',4,3,3),(12,'Articulo de prueba','Articulo de prueba','2021-09-30',2,2,2),(13,'Articulo de prueba 2','Articulo de prueba 2','2021-09-30',1,1,1),(14,'instrumento','musica muscias','2021-10-16',3,1,4),(15,'GUITARRA NUEVA','GUITARRA ELECTRONICA DE COLOR PLOMO','2021-10-20',3,3,4);
/*!40000 ALTER TABLE `articulo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `articulo_x_inventario`
--

DROP TABLE IF EXISTS `articulo_x_inventario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `articulo_x_inventario` (
  `idarticulo_x_inventario` int NOT NULL AUTO_INCREMENT,
  `idarticulos` int NOT NULL,
  `idinventarios` int NOT NULL,
  `estado` varchar(45) DEFAULT NULL,
  `area` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idarticulo_x_inventario`),
  KEY `fk_articulo_x_inventario_articulos1_idx` (`idarticulos`),
  KEY `fk_articulo_x_inventario_inventarios1_idx` (`idinventarios`),
  CONSTRAINT `fk_articulo_x_inventario_articulos1` FOREIGN KEY (`idarticulos`) REFERENCES `articulo` (`idarticulo`),
  CONSTRAINT `fk_articulo_x_inventario_inventarios1` FOREIGN KEY (`idinventarios`) REFERENCES `inventario` (`idinventario`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `articulo_x_inventario`
--

LOCK TABLES `articulo_x_inventario` WRITE;
/*!40000 ALTER TABLE `articulo_x_inventario` DISABLE KEYS */;
INSERT INTO `articulo_x_inventario` VALUES (1,1,1,'BUENO','Area de laboratorio de Ciencias'),(2,2,1,'BUENO','Area de laboratorio de Ciencias'),(3,3,1,'BUENO','Area de laboratorio de Ciencias'),(4,4,1,'BUENO','Area de laboratorio de Ciencias'),(5,1,2,'BUENO','Area de laboratorio de Ciencias'),(6,2,2,'BUENO','Area de laboratorio de Ciencias'),(7,3,2,'BUENO','Area de laboratorio de Ciencias'),(8,4,2,'BUENO','Area de laboratorio de Ciencias'),(9,5,2,'BUENO','Area de laboratorio de computacion'),(10,6,2,'BUENO','Area de laboratorio de computacion'),(11,7,2,'BUENO','Area de laboratorio de computacion'),(12,8,2,'BUENO','Area de laboratorio de computacion'),(32,1,5,'REGULAR','AREA DE MUSICA'),(33,2,5,'Regular','Area del limpieza'),(34,3,5,'Bueno','Area de laboratorio de computacion'),(35,4,5,'Bueno','Area de laboratorio de Ciencias'),(36,1,8,'INSERVIBLE','AREA DEL LIMPIEZA'),(37,2,8,'Regular','Area del limpieza'),(38,3,8,'Bueno','Area de laboratorio de computacion'),(39,4,8,'Bueno','Area de laboratorio de Ciencias'),(40,1,9,'INSERVIBLE','AREA DE MUSICA'),(41,2,9,'REGULAR','AREA DE LABORATORIO DE CIENCIAS'),(42,3,9,'Bueno','Area de laboratorio de computacion'),(43,1,10,'MUY BUENO','AREA DE MUSICA'),(44,2,10,'REGULAR','AREA DE LABORATORIO DE COMPUTACION'),(45,3,10,'Bueno','Area de laboratorio de computacion'),(46,4,10,'Bueno','Area de laboratorio de Ciencias'),(47,1,11,'MALO','AREA DE MUSICA'),(48,2,11,'MALO','AREA DE LABORATORIO DE CIENCIAS'),(49,3,11,'Bueno','Area de laboratorio de computacion'),(50,4,11,'Bueno','Area de laboratorio de Ciencias'),(51,5,11,'Muy bueno','Area de laboratorio de computacion'),(52,6,11,'Regular','Area de Musica'),(53,7,11,'Bueno','Area de laboratorio de Ciencias'),(54,8,11,'Bueno','Area de laboratorio de Ciencias'),(55,9,11,'Regular','Area de Musica'),(56,10,11,'Regular','Area del limpieza'),(57,12,11,'Bueno','Area de laboratorio de Ciencias'),(58,13,11,'Muy bueno','Area de laboratorio de computacion'),(59,14,11,'BUENO','AREA DE MUSICA'),(60,1,12,'MALO','AREA DE LABORATORIO DE CIENCIAS'),(61,2,12,'Regular','Area del limpieza'),(62,3,12,'Bueno','Area de laboratorio de computacion'),(63,4,12,'Bueno','Area de laboratorio de Ciencias'),(64,5,12,'Muy bueno','Area de laboratorio de computacion'),(65,6,12,'Regular','Area de Musica'),(66,7,12,'Bueno','Area de laboratorio de Ciencias'),(67,8,12,'Bueno','Area de laboratorio de Ciencias'),(68,9,12,'Regular','Area de Musica'),(69,10,12,'Regular','Area del limpieza'),(70,12,12,'Bueno','Area de laboratorio de Ciencias'),(71,13,12,'Muy bueno','Area de laboratorio de computacion'),(72,14,12,'Muy bueno','Area de Musica');
/*!40000 ALTER TABLE `articulo_x_inventario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `idcategoria` int NOT NULL AUTO_INCREMENT,
  `categoria` varchar(45) NOT NULL,
  PRIMARY KEY (`idcategoria`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES (1,'Instrumentos y equipos de ciencia'),(2,'Equipos tecnologicos'),(3,'Instrumento de Limpieza'),(4,'Instrumentos musicales'),(5,'Muebles');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comprobante`
--

DROP TABLE IF EXISTS `comprobante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comprobante` (
  `idcomprobante` int NOT NULL AUTO_INCREMENT,
  `codigo` varchar(45) NOT NULL,
  `fecha` date NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  `idpersona` int NOT NULL,
  PRIMARY KEY (`idcomprobante`),
  KEY `fk_comprobantes_personas1_idx` (`idpersona`),
  CONSTRAINT `fk_comprobantes_personas1` FOREIGN KEY (`idpersona`) REFERENCES `persona` (`idpersona`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comprobante`
--

LOCK TABLES `comprobante` WRITE;
/*!40000 ALTER TABLE `comprobante` DISABLE KEYS */;
INSERT INTO `comprobante` VALUES (1,'OOO','2021-09-09',10.00,2),(2,'OO2','2021-09-10',5.00,3),(3,'OO3','2021-09-11',10.00,2),(4,'GUA0000000004','2021-08-18',15.00,1),(5,'GUA00005','2021-08-18',10.00,4),(6,'GUA_00006','2021-08-18',15.00,5),(7,'GUA_00007','2021-08-18',5.00,6),(8,'GUA_00008','2021-08-18',10.00,7),(9,'GUA_00009','2021-08-18',15.00,8),(10,'GUA_00010','2021-08-18',5.00,9),(11,'GUA_00011','2021-08-18',10.00,10),(12,'GUA_00012','2021-08-18',10.00,11),(13,'GUA_00013','2021-08-18',5.00,12),(14,'GUA_00014','2021-08-18',20.00,13),(15,'GUA_00015','2021-08-18',20.00,14),(16,'GUA_00016','2021-08-19',15.00,15),(17,'GUA_00017','2021-08-20',15.00,16),(18,'GUA00018','2021-08-20',10.00,1),(19,'GUA_00019','2021-08-20',20.00,17),(20,'GUA00020','2021-09-10',10.00,5),(21,'GUA_00021','2021-09-10',5.00,18),(22,'GUA00022','2021-09-13',5.00,18),(23,'GUA00023','2021-09-28',15.00,18),(24,'GUA_00024','2021-09-28',40.00,19),(25,'GUA00025','2021-09-29',25.00,7),(26,'GUA00026','2021-10-01',20.00,5),(27,'GUA00027','2021-10-01',15.00,5),(28,'GUA00028','2021-10-05',25.00,20),(29,'GUA00029','2021-10-05',120.00,19),(30,'GUA00030','2021-10-05',120.00,19),(31,'GUA_00031','2021-10-16',10.00,21),(32,'GUA00032','2021-10-20',20.00,5),(33,'GUA_00033','2021-10-20',50.00,22),(34,'GUA00034','2021-10-20',5.00,20),(35,'GUA_00035','2021-10-24',50.00,20),(36,'GUA_00036','2021-10-24',15.00,5),(37,'GUA_00037','2021-10-24',20.00,22);
/*!40000 ALTER TABLE `comprobante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concepto`
--

DROP TABLE IF EXISTS `concepto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `concepto` (
  `idconcepto` int NOT NULL AUTO_INCREMENT,
  `concepto` varchar(200) NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  PRIMARY KEY (`idconcepto`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concepto`
--

LOCK TABLES `concepto` WRITE;
/*!40000 ALTER TABLE `concepto` DISABLE KEYS */;
INSERT INTO `concepto` VALUES (1,'Certificado',10.00),(2,'FUT',5.00),(4,'arriendos',120.00),(5,'alquiler de cacha',88.00);
/*!40000 ALTER TABLE `concepto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle`
--

DROP TABLE IF EXISTS `detalle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle` (
  `iddetalle` int NOT NULL AUTO_INCREMENT,
  `idcomprobantes` int NOT NULL,
  `idconceptos` int NOT NULL,
  `cantidad` int DEFAULT NULL,
  PRIMARY KEY (`iddetalle`),
  KEY `fk_detalle_comprobantes1_idx` (`idcomprobantes`),
  KEY `fk_detalle_conceptos1_idx` (`idconceptos`),
  CONSTRAINT `fk_detalle_comprobantes1` FOREIGN KEY (`idcomprobantes`) REFERENCES `comprobante` (`idcomprobante`),
  CONSTRAINT `fk_detalle_conceptos1` FOREIGN KEY (`idconceptos`) REFERENCES `concepto` (`idconcepto`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle`
--

LOCK TABLES `detalle` WRITE;
/*!40000 ALTER TABLE `detalle` DISABLE KEYS */;
INSERT INTO `detalle` VALUES (1,2,1,NULL),(2,2,2,NULL),(3,3,2,NULL),(4,3,2,NULL),(5,4,2,NULL),(6,4,1,NULL),(7,5,1,NULL),(8,6,1,NULL),(9,6,2,NULL),(10,7,2,NULL),(11,8,1,NULL),(12,9,1,NULL),(13,9,2,NULL),(14,10,2,NULL),(15,11,1,NULL),(16,12,1,NULL),(17,13,2,NULL),(18,14,2,NULL),(19,14,2,NULL),(20,14,1,NULL),(21,15,1,NULL),(22,15,1,NULL),(23,16,1,NULL),(24,16,2,NULL),(25,17,2,NULL),(26,18,1,NULL),(27,19,1,NULL),(28,19,1,NULL),(29,20,1,NULL),(30,21,2,NULL),(31,22,2,NULL),(32,23,1,NULL),(33,23,2,NULL),(34,24,1,NULL),(35,24,2,NULL),(36,24,1,NULL),(37,24,2,NULL),(38,24,1,NULL),(39,25,1,NULL),(40,25,2,NULL),(41,25,1,NULL),(42,26,1,NULL),(43,26,1,NULL),(44,27,1,NULL),(45,27,2,NULL),(46,28,1,NULL),(47,28,2,NULL),(48,28,1,NULL),(49,29,4,NULL),(50,30,4,NULL),(51,31,1,NULL),(52,32,1,NULL),(53,32,1,NULL),(54,33,1,NULL),(55,33,1,NULL),(56,33,1,NULL),(57,33,1,NULL),(58,33,1,NULL),(59,34,2,NULL),(60,35,1,4),(61,35,2,2),(62,36,1,1),(63,36,2,1),(64,37,1,1),(65,37,2,2);
/*!40000 ALTER TABLE `detalle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estado`
--

DROP TABLE IF EXISTS `estado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estado` (
  `idestado` int NOT NULL AUTO_INCREMENT,
  `estado` varchar(50) NOT NULL,
  PRIMARY KEY (`idestado`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estado`
--

LOCK TABLES `estado` WRITE;
/*!40000 ALTER TABLE `estado` DISABLE KEYS */;
INSERT INTO `estado` VALUES (1,'Muy bueno'),(2,'Bueno'),(3,'Regular'),(4,'Malo'),(5,'Inservible');
/*!40000 ALTER TABLE `estado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventario`
--

DROP TABLE IF EXISTS `inventario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventario` (
  `idinventario` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date DEFAULT NULL,
  `observaciones` longtext NOT NULL,
  `idusuarios` int NOT NULL,
  PRIMARY KEY (`idinventario`),
  KEY `fk_inventarios_usuarios1_idx` (`idusuarios`),
  CONSTRAINT `fk_inventarios_usuarios1` FOREIGN KEY (`idusuarios`) REFERENCES `usuario` (`idusuario`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventario`
--

LOCK TABLES `inventario` WRITE;
/*!40000 ALTER TABLE `inventario` DISABLE KEYS */;
INSERT INTO `inventario` VALUES (1,'Inventario1','2020-03-01','2020-03-02','Se observaron dificultades en la identificacion de los materiales',1),(2,'Inventario2','2020-12-30','2020-12-31','todo en orden',1),(5,'PRUEBA 1 - 2021','2010-05-12','2010-05-12','prueba 1',1),(6,'PRUEBA 2 - 2021','2010-05-12','2010-05-12','rddgc',1),(7,'PRUEBA3 - 2021','2010-05-12','2010-05-12','',1),(8,'INVENTARIO DE PRUEBA 3 - 2021','2010-05-12','2010-05-12','todo en orden',1),(9,'EL INVENTARIO - 2021','2010-05-12','2010-05-12','',1),(10,'INVENTARIO PRUEBA 4 - 2021','2010-05-12','2010-05-12','adsfdgf',1),(11,'INVENTARIO PRUEBA 5 - 2021','2010-05-12','2010-05-12','observaciones',1),(12,'INVENTARIO PRUEBA 6 - 2021','2010-05-12','2010-05-12','ALgunas observaciones',1);
/*!40000 ALTER TABLE `inventario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `persona`
--

DROP TABLE IF EXISTS `persona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `persona` (
  `idpersona` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `apellido` varchar(200) NOT NULL,
  `dni` char(8) NOT NULL,
  `direccion` varchar(45) NOT NULL,
  `telefono` char(11) DEFAULT NULL,
  PRIMARY KEY (`idpersona`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persona`
--

LOCK TABLES `persona` WRITE;
/*!40000 ALTER TABLE `persona` DISABLE KEYS */;
INSERT INTO `persona` VALUES (1,'Julio','Rosas','96857412','av. Las Flores','951753258'),(2,'Maria','Palacios','85265457','Jr. Las Rocolas','965478123'),(3,'Martin','Alva','78541265','jR. LO OJOS','965485123'),(4,'CARLOS','BELTRAN','96888412','av. Las CONDICIONES','966753258'),(5,'Carlos','Orellano Rondan','71650081','Jr. sin jiron','996554112'),(6,'Luis','Mejia Mejia','43567655','Jr. los barny','990088881'),(7,'Jose','Dueñas Trejo','71548523','Av. Los requiem','987445666'),(8,'Martin','Zaragosa','88665544','Jr. los pajaros','963214587'),(9,'Carmen','Luciala','96587412','Jr. bennet','987445123'),(10,'Jhojam','Medina','78986632','Av. las bicis','963854712'),(11,'Jared','Leto','96325511','AV. from yesterday','965874888'),(12,'Martin','Sosa','99998888','Jr. los villones','987412563'),(13,'Julia','Velazquez','99966666','Jr. marca','987789987'),(14,'Jescenia','Melgarejo','7895654','Jr. los shsnacayan','963666333'),(15,'Carlos','Alvarado','45657877','hr. resa','990345432'),(16,'Juan','de la torre','89951663','AV. Los raosa','988765113'),(17,'Maria','Rosas','75395142','Av. Avenida','963852147'),(18,'','','','',''),(19,'Julian','Palacios','78954698','Av Gonzales','963852745'),(20,'Oscar mod','Martinez','31761710','Jr. de ejemolo','963852741'),(21,'CArlos','apejd','96854745','dirf','963852741'),(22,'Miguel','Silva','78952612','Dir','789465132');
/*!40000 ALTER TABLE `persona` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `idusuario` int NOT NULL AUTO_INCREMENT,
  `usuario` varchar(45) NOT NULL,
  `pass` varchar(45) NOT NULL,
  `rol` varchar(45) NOT NULL,
  `idpersonas` int NOT NULL,
  PRIMARY KEY (`idusuario`),
  KEY `fk_usuarios_personas1_idx` (`idpersonas`),
  CONSTRAINT `fk_usuarios_personas1` FOREIGN KEY (`idpersonas`) REFERENCES `persona` (`idpersona`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'admin','81dc9bdb52d04dc20036dbd8313ed055','PERSONAL',1),(2,'carlos','dc599a9972fde3045dab59dbd1ae170b','ADMIN',5),(3,'oscar','f156e7995d521f30e6c59a3d6c75e1e5','ADMINISTRADOR',20);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `v_art_x_inven_d`
--

DROP TABLE IF EXISTS `v_art_x_inven_d`;
/*!50001 DROP VIEW IF EXISTS `v_art_x_inven_d`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_art_x_inven_d` AS SELECT 
 1 AS `idarticulo`,
 1 AS `nombre`,
 1 AS `descripcion`,
 1 AS `area`,
 1 AS `estado`,
 1 AS `categoria`,
 1 AS `idestado`,
 1 AS `idcategoria`,
 1 AS `idinventarios`,
 1 AS `inventario`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_articulos`
--

DROP TABLE IF EXISTS `v_articulos`;
/*!50001 DROP VIEW IF EXISTS `v_articulos`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_articulos` AS SELECT 
 1 AS `idarticulo`,
 1 AS `nombre`,
 1 AS `descripcion`,
 1 AS `area`,
 1 AS `estado`,
 1 AS `categoria`,
 1 AS `idarea`,
 1 AS `idestado`,
 1 AS `idcategoria`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_articulos_x_invent`
--

DROP TABLE IF EXISTS `v_articulos_x_invent`;
/*!50001 DROP VIEW IF EXISTS `v_articulos_x_invent`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_articulos_x_invent` AS SELECT 
 1 AS `idarticulo`,
 1 AS `nombre`,
 1 AS `descripcion`,
 1 AS `area`,
 1 AS `estado`,
 1 AS `categoria`,
 1 AS `idestado`,
 1 AS `idcategoria`,
 1 AS `idinventarios`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_comprobante`
--

DROP TABLE IF EXISTS `v_comprobante`;
/*!50001 DROP VIEW IF EXISTS `v_comprobante`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_comprobante` AS SELECT 
 1 AS `idcomprobante`,
 1 AS `codigo`,
 1 AS `dni`,
 1 AS `nombre`,
 1 AS `fecha`,
 1 AS `total`,
 1 AS `idconceptos`,
 1 AS `concepto`,
 1 AS `monto`,
 1 AS `cantidad`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_usuarios`
--

DROP TABLE IF EXISTS `v_usuarios`;
/*!50001 DROP VIEW IF EXISTS `v_usuarios`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_usuarios` AS SELECT 
 1 AS `idusuario`,
 1 AS `idpersonas`,
 1 AS `dni`,
 1 AS `nombre`,
 1 AS `apellido`,
 1 AS `direccion`,
 1 AS `telefono`,
 1 AS `usuario`,
 1 AS `pass`,
 1 AS `rol`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_ventas`
--

DROP TABLE IF EXISTS `v_ventas`;
/*!50001 DROP VIEW IF EXISTS `v_ventas`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_ventas` AS SELECT 
 1 AS `iddetalle`,
 1 AS `fecha`,
 1 AS `monto`,
 1 AS `nombre`*/;
SET character_set_client = @saved_cs_client;

--
-- Dumping routines for database 'colegiobd'
--
/*!50003 DROP PROCEDURE IF EXISTS `INSERT_COMPRO` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `INSERT_COMPRO`(P_NOMBRE VARCHAR(45), P_APELLIDO VARCHAR(45), P_DNI CHAR(8), P_DIRECCION VARCHAR(45),
P_TELEFONO VARCHAR(45), P_MONTO DECIMAL(10,2))
BEGIN
DECLARE IDPER INT;
DECLARE COD INT;
SET COD = (SELECT MAX(idcomprobante) FROM COMPROBANTE) + 1;
IF((SELECT COUNT(*) FROM PERSONA WHERE DNI = P_DNI) > 0) THEN
	SET IDPER = (SELECT IDPERSONA FROM PERSONA WHERE DNI = P_DNI);
	INSERT INTO COMPROBANTE VALUES (NULL, concat('GUA_',lpad(COD,5,0)),(SELECT CURDATE()), P_MONTO, IDPER);
ELSE
	INSERT INTO PERSONA VALUES(NULL, P_NOMBRE, P_APELLIDO, P_DNI, P_DIRECCION, P_TELEFONO);
	SET IDPER = (SELECT IDPERSONA FROM PERSONA WHERE DNI = P_DNI);
	INSERT INTO COMPROBANTE VALUES (NULL, concat('GUA_',lpad(COD,5,0)),(SELECT CURDATE()), P_MONTO, IDPER);
END IF;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `INSERT_DETALLE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `INSERT_DETALLE`(P_CONCEPTO VARCHAR(45), P_CANTIDAD INT)
BEGIN
DECLARE IDCON INT;
DECLARE IDCOMP INT;
SET IDCOMP = (SELECT MAX(idcomprobante) FROM COMPROBANTE);
SET IDCON = (SELECT IDCONCEPTO FROM CONCEPTO WHERE CONCEPTO = P_CONCEPTO);
INSERT INTO DETALLE(idcomprobantes, idconceptos, cantidad) VALUES (IDCOMP, IDCON,P_CANTIDAD );

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `v_art_x_inven_d`
--

/*!50001 DROP VIEW IF EXISTS `v_art_x_inven_d`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_art_x_inven_d` AS select `a`.`idarticulo` AS `idarticulo`,`a`.`nombre` AS `nombre`,`a`.`descripcion` AS `descripcion`,`ai`.`area` AS `area`,`ai`.`estado` AS `estado`,`c`.`categoria` AS `categoria`,`e`.`idestado` AS `idestado`,`c`.`idcategoria` AS `idcategoria`,`ai`.`idinventarios` AS `idinventarios`,`i`.`nombre` AS `inventario` from (((((`articulo` `a` join `area` `ar` on((`ar`.`idarea` = `a`.`idArea`))) join `estado` `e` on((`e`.`idestado` = `a`.`idestado`))) join `categoria` `c` on((`c`.`idcategoria` = `a`.`idcategoria`))) join `articulo_x_inventario` `ai` on((`ai`.`idarticulos` = `a`.`idarticulo`))) join `inventario` `i` on((`i`.`idinventario` = `ai`.`idinventarios`))) order by `a`.`idarticulo` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_articulos`
--

/*!50001 DROP VIEW IF EXISTS `v_articulos`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_articulos` AS select `a`.`idarticulo` AS `idarticulo`,`a`.`nombre` AS `nombre`,`a`.`descripcion` AS `descripcion`,`ar`.`nombre` AS `area`,`e`.`estado` AS `estado`,`c`.`categoria` AS `categoria`,`ar`.`idarea` AS `idarea`,`e`.`idestado` AS `idestado`,`c`.`idcategoria` AS `idcategoria` from (((`articulo` `a` join `area` `ar` on((`ar`.`idarea` = `a`.`idArea`))) join `estado` `e` on((`e`.`idestado` = `a`.`idestado`))) join `categoria` `c` on((`c`.`idcategoria` = `a`.`idcategoria`))) order by `a`.`idarticulo` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_articulos_x_invent`
--

/*!50001 DROP VIEW IF EXISTS `v_articulos_x_invent`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_articulos_x_invent` AS select `a`.`idarticulo` AS `idarticulo`,`a`.`nombre` AS `nombre`,`a`.`descripcion` AS `descripcion`,`ai`.`area` AS `area`,`ai`.`estado` AS `estado`,`c`.`categoria` AS `categoria`,`e`.`idestado` AS `idestado`,`c`.`idcategoria` AS `idcategoria`,`ai`.`idinventarios` AS `idinventarios` from ((((`articulo` `a` join `area` `ar` on((`ar`.`idarea` = `a`.`idArea`))) join `estado` `e` on((`e`.`idestado` = `a`.`idestado`))) join `categoria` `c` on((`c`.`idcategoria` = `a`.`idcategoria`))) join `articulo_x_inventario` `ai` on((`ai`.`idarticulos` = `a`.`idarticulo`))) order by `a`.`idarticulo` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_comprobante`
--

/*!50001 DROP VIEW IF EXISTS `v_comprobante`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_comprobante` AS select `c`.`idcomprobante` AS `idcomprobante`,`c`.`codigo` AS `codigo`,`p`.`dni` AS `dni`,concat(`p`.`nombre`,' ',`p`.`apellido`) AS `nombre`,`c`.`fecha` AS `fecha`,`c`.`monto` AS `total`,`d`.`idconceptos` AS `idconceptos`,`co`.`concepto` AS `concepto`,`co`.`monto` AS `monto`,`d`.`cantidad` AS `cantidad` from (((`comprobante` `c` join `detalle` `d` on((`d`.`idcomprobantes` = `c`.`idcomprobante`))) join `concepto` `co` on((`co`.`idconcepto` = `d`.`idconceptos`))) join `persona` `p` on((`p`.`idpersona` = `c`.`idpersona`))) order by `c`.`fecha` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_usuarios`
--

/*!50001 DROP VIEW IF EXISTS `v_usuarios`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_usuarios` AS select `u`.`idusuario` AS `idusuario`,`u`.`idpersonas` AS `idpersonas`,`p`.`dni` AS `dni`,`p`.`nombre` AS `nombre`,`p`.`apellido` AS `apellido`,`p`.`direccion` AS `direccion`,`p`.`telefono` AS `telefono`,`u`.`usuario` AS `usuario`,`u`.`pass` AS `pass`,`u`.`rol` AS `rol` from (`usuario` `u` join `persona` `p` on((`p`.`idpersona` = `u`.`idpersonas`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_ventas`
--

/*!50001 DROP VIEW IF EXISTS `v_ventas`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_ventas` AS select `d`.`iddetalle` AS `iddetalle`,`c`.`fecha` AS `fecha`,`c`.`monto` AS `monto`,concat(`p`.`nombre`,' ',`p`.`apellido`) AS `nombre` from ((`detalle` `d` join `comprobante` `c` on((`c`.`idcomprobante` = `d`.`idcomprobantes`))) join `persona` `p` on((`p`.`idpersona` = `c`.`idpersona`))) order by `d`.`iddetalle` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-10-24 17:35:47
