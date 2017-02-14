-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        5.5.32 - MySQL Community Server (GPL)
-- 서버 OS:                        Win32
-- HeidiSQL 버전:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- jjdev2 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `jjdev2` /*!40100 DEFAULT CHARACTER SET euckr */;
USE `jjdev2`;

-- 테이블 jjdev2.ss_religion 구조 내보내기
CREATE TABLE IF NOT EXISTS `ss_religion` (
  `no` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=euckr;

-- 테이블 데이터 jjdev2.ss_religion:~4 rows (대략적) 내보내기
/*!40000 ALTER TABLE `ss_religion` DISABLE KEYS */;
INSERT INTO `ss_religion` (`no`, `name`) VALUES
	(1, '무교'),
	(2, '불교'),
	(3, '기독교'),
	(4, '천주교');
/*!40000 ALTER TABLE `ss_religion` ENABLE KEYS */;

-- 테이블 jjdev2.ss_school 구조 내보내기
CREATE TABLE IF NOT EXISTS `ss_school` (
  `no` int(11) NOT NULL AUTO_INCREMENT,
  `graduate` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=euckr;

-- 테이블 데이터 jjdev2.ss_school:~3 rows (대략적) 내보내기
/*!40000 ALTER TABLE `ss_school` DISABLE KEYS */;
INSERT INTO `ss_school` (`no`, `graduate`) VALUES
	(1, '고졸'),
	(2, '전문대졸'),
	(3, '대졸');
/*!40000 ALTER TABLE `ss_school` ENABLE KEYS */;

-- 테이블 jjdev2.ss_skill 구조 내보내기
CREATE TABLE IF NOT EXISTS `ss_skill` (
  `no` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=euckr;

-- 테이블 데이터 jjdev2.ss_skill:~5 rows (대략적) 내보내기
/*!40000 ALTER TABLE `ss_skill` DISABLE KEYS */;
INSERT INTO `ss_skill` (`no`, `name`) VALUES
	(1, 'java'),
	(2, 'html5'),
	(3, 'JSP'),
	(4, 'SQL'),
	(5, 'javascript');
/*!40000 ALTER TABLE `ss_skill` ENABLE KEYS */;

-- 테이블 jjdev2.ss_staff 구조 내보내기
CREATE TABLE IF NOT EXISTS `ss_staff` (
  `no` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `sn` char(14) DEFAULT NULL,
  `graduateday` date DEFAULT NULL,
  `schoolno` int(11) DEFAULT NULL,
  `religionno` int(11) DEFAULT NULL,
  PRIMARY KEY (`no`),
  KEY `FK__school` (`schoolno`),
  KEY `FK__religion` (`religionno`),
  CONSTRAINT `FK__religion` FOREIGN KEY (`religionno`) REFERENCES `ss_religion` (`no`),
  CONSTRAINT `FK__school` FOREIGN KEY (`schoolno`) REFERENCES `ss_school` (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=euckr;

-- 테이블 데이터 jjdev2.ss_staff:~16 rows (대략적) 내보내기
/*!40000 ALTER TABLE `ss_staff` DISABLE KEYS */;
INSERT INTO `ss_staff` (`no`, `name`, `sn`, `graduateday`, `schoolno`, `religionno`) VALUES
	(2, '방상엽', '850172-234234', '2016-12-07', 2, 3),
	(4, '김진남', '901232-3457854', '2016-11-29', 2, 1),
	(5, '김태희', '820405-2456789', '2016-11-03', 3, 4),
	(6, '송혜교', '801214-4572515', '2016-11-08', 3, 3),
	(7, '한지민', '830504-2569874', '2016-12-22', 1, 4),
	(8, '김미림', '890101-2354567', '2016-12-08', 3, 1),
	(9, '홍길동', '880102-12345', '2016-12-12', 1, 1),
	(10, '공헌재', '902121-1324578', '2016-12-11', 3, 3),
	(24, '홍인용', '887451-123', '2016-12-12', 3, 4),
	(25, '이형렬', '123123-1231231', '2016-12-07', 1, 1),
	(28, '바보', '123123-1231231', '2016-12-07', 1, 2),
	(29, 'a3444444', '123123-1231231', '2016-12-01', 1, 1),
	(31, '222222222222222', '123123-2312312', '2016-12-07', 1, 1),
	(32, '44444444444444444', '441231-1231231', '2016-12-07', 1, 1),
	(33, '55555555', '123123-1231231', '2016-12-07', 1, 1),
	(34, 'test', '131231-1231231', '2017-02-01', 3, 1);
/*!40000 ALTER TABLE `ss_staff` ENABLE KEYS */;

-- 테이블 jjdev2.ss_staffskill 구조 내보내기
CREATE TABLE IF NOT EXISTS `ss_staffskill` (
  `no` int(11) NOT NULL AUTO_INCREMENT,
  `staffno` int(11) DEFAULT NULL,
  `skillno` int(11) DEFAULT NULL,
  PRIMARY KEY (`no`),
  KEY `FK__staff` (`staffno`),
  KEY `FK__skill` (`skillno`),
  CONSTRAINT `FK__skill` FOREIGN KEY (`skillno`) REFERENCES `ss_skill` (`no`),
  CONSTRAINT `FK__staff` FOREIGN KEY (`staffno`) REFERENCES `ss_staff` (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=euckr;

-- 테이블 데이터 jjdev2.ss_staffskill:~30 rows (대략적) 내보내기
/*!40000 ALTER TABLE `ss_staffskill` DISABLE KEYS */;
INSERT INTO `ss_staffskill` (`no`, `staffno`, `skillno`) VALUES
	(5, 2, 4),
	(6, 2, 5),
	(8, 4, 1),
	(9, 4, 2),
	(10, 4, 3),
	(11, 5, 3),
	(12, 6, 2),
	(13, 7, 2),
	(14, 8, 1),
	(15, 8, 3),
	(16, 8, 5),
	(31, 9, 1),
	(32, 9, 2),
	(33, 9, 3),
	(34, 9, 4),
	(35, 9, 5),
	(45, 10, 5),
	(60, 24, 1),
	(61, 24, 2),
	(62, 24, 3),
	(63, 24, 4),
	(64, 24, 5),
	(70, 31, 2),
	(71, 31, 3),
	(72, 32, 4),
	(73, 32, 5),
	(74, 33, 1),
	(78, 34, 4),
	(79, 34, 5);
/*!40000 ALTER TABLE `ss_staffskill` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
