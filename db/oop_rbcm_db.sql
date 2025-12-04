-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Anamakine: 127.0.0.1
-- Üretim Zamanı: 04 Ara 2025, 18:24:46
-- Sunucu sürümü: 10.4.32-MariaDB
-- PHP Sürümü: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Veritabanı: `oop_rbcm_db`
--

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `contacts`
--

CREATE TABLE `contacts` (
  `contact_id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `phone_number` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `linkedin_url` varchar(255) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `contacts`
--

INSERT INTO `contacts` (`contact_id`, `first_name`, `last_name`, `nickname`, `phone_number`, `email`, `linkedin_url`, `birth_date`, `created_at`, `updated_at`) VALUES
(1, 'Mert', 'Günok', 'Mert', '05530000001', 'mert.gunok@example.com', 'https://www.linkedin.com/in/mert-gunok', '1990-03-01', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(2, 'Ersin', 'Destanoğlu', 'Ersin', '05530000002', 'ersin.destanoglu@example.com', 'https://www.linkedin.com/in/ersin-destanoglu', '2001-01-15', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(3, 'Gökhan', 'Sazdağ', 'Gökhan', '05530000003', 'gokhan.sazdag@example.com', 'https://www.linkedin.com/in/gokhan-sazdag', '1996-06-10', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(4, 'Diago', 'Djalo', 'Djalo', '05530000004', 'diago.djalo@example.com', 'https://www.linkedin.com/in/diago-djalo', '1995-11-05', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(5, 'Emirhan', 'Topçu', 'Emirhan', '05530000005', 'emirhan.topcu@example.com', 'https://www.linkedin.com/in/emirhan-topcu', '2000-04-20', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(6, 'Rıdvan', 'Yılmaz', 'Rıdvan', '05530000006', 'ridvan.yilmaz@example.com', 'https://www.linkedin.com/in/ridvan-yilmaz', '2001-05-21', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(7, 'Wilfred', 'Ndidi', 'Ndidi', '05530000007', 'wilfred.ndidi@example.com', 'https://www.linkedin.com/in/wilfred-ndidi', '1996-12-16', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(8, 'Orkun', 'Kökçü', 'Orkun', '05530000008', 'orkun.kokcu@example.com', 'https://www.linkedin.com/in/orkun-kokcu', '2000-12-29', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(9, 'Rafa', 'Silva', 'Rafa Silva', '05530000009', 'rafa.silva@example.com', 'https://www.linkedin.com/in/rafa-silva', '1993-05-09', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(10, 'Vaclav', 'Cerny', 'Cerny', '05530000010', 'vaclav.cerny@example.com', 'https://www.linkedin.com/in/vaclav-cerny', '1997-10-17', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(11, 'Jota', 'Silva', 'Jota', '05530000011', 'jota.silva@example.com', 'https://www.linkedin.com/in/jota-silva', '1999-03-25', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(12, 'El Bilal', 'Toure', 'El-Bilal', '05530000012', 'elbilal.toure@example.com', 'https://www.linkedin.com/in/el-bilal-toure', '2001-10-01', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(13, 'Tammy', 'Abraham', 'Abraham', '05530000013', 'tammy.abraham@example.com', 'https://www.linkedin.com/in/tammy-abraham', '1997-10-02', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(14, 'Cengiz', 'Ünder', 'Cengiz', '05530000014', 'cengiz.under@example.com', 'https://www.linkedin.com/in/cengiz-under', '1997-07-14', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(15, 'Ederson', 'Moraes', 'Ederson', '05530000015', 'ederson.moraes@example.com', 'https://www.linkedin.com/in/ederson-moraes', '1993-08-17', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(16, 'Nelson', 'Semedo', 'Semedo', '05530000016', 'nelson.semedo@example.com', 'https://www.linkedin.com/in/nelson-semedo', '1993-11-16', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(17, 'Milan', 'Skriniar', 'Skriniar', '05530000017', 'milan.skriniar@example.com', 'https://www.linkedin.com/in/milan-skriniar', '1995-02-11', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(18, 'Jayden', 'Oosterwolde', 'Oosterwolde', '05530000018', 'jayden.oosterwolde@example.com', 'https://www.linkedin.com/in/jayden-oosterwolde', '2001-04-10', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(19, 'Archie', 'Brown', 'Archie Brown', '05530000019', 'archie.brown@example.com', 'https://www.linkedin.com/in/archie-brown', '2002-05-12', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(20, 'Edson', 'Alvarez', 'Alvarez', '05530000020', 'edson.alvarez@example.com', 'https://www.linkedin.com/in/edson-alvarez', '1997-10-24', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(21, 'İsmail', 'Yüksek', 'İsmail Yüksek', '05530000021', 'ismail.yuksek@example.com', 'https://www.linkedin.com/in/ismail-yuksek', '1999-01-26', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(22, 'Marco', 'Asencio', 'Asencio', '05530000022', 'marco.asencio@example.com', 'https://www.linkedin.com/in/marco-asencio', '1996-01-21', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(23, 'Dorgeles', 'Nene', 'Nene', '05530000023', 'dorgeles.nene@example.com', 'https://www.linkedin.com/in/dorgeles-nene', '2002-03-18', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(24, 'Kerem', 'Aktürkoğlu', 'Kerem', '05530000024', 'kerem.akturkoglu@example.com', 'https://www.linkedin.com/in/kerem-akturkoglu', '1998-10-21', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(25, 'John', 'Duran', 'John Duran', '05530000025', 'john.duran@example.com', 'https://www.linkedin.com/in/john-duran', '2003-12-13', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(26, 'Youssef', 'El Nesryi', 'El Nesryi', '05530000026', 'youssef.elnesryi@example.com', 'https://www.linkedin.com/in/youssef-el-nesryi', '1997-06-05', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(27, 'Levent', 'Mercan', 'Levent Mercan', '05530000027', 'levent.mercan@example.com', 'https://www.linkedin.com/in/levent-mercan', '2000-12-10', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(28, 'Anderson', 'Talisca', 'Talisca', '05530000028', 'anderson.talisca@example.com', 'https://www.linkedin.com/in/anderson-talisca', '1994-02-01', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(29, 'Uğurcan', 'Çakır', 'Uğurcan Çakır', '05530000029', 'ugurcan.cakir@example.com', 'https://www.linkedin.com/in/ugurcan-cakir', '1996-04-05', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(30, 'Wilried', 'Singo', 'Singo', '05530000030', 'wilried.singo@example.com', 'https://www.linkedin.com/in/wilried-singo', '2000-12-19', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(31, 'Davinson', 'Sanchez', 'Davinson Sanchez', '05530000031', 'davinson.sanchez@example.com', 'https://www.linkedin.com/in/davinson-sanchez', '1996-06-12', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(32, 'Abdülkerim', 'Bardakçı', 'Abdülkerim', '05530000032', 'abdulkerim.bardakci@example.com', 'https://www.linkedin.com/in/abdulkerim-bardakci', '1994-09-07', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(33, 'İsmail', 'Jakobs', 'Jakobs', '05530000033', 'ismail.jakobs@example.com', 'https://www.linkedin.com/in/ismail-jakobs', '1999-08-17', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(34, 'Gabriel', 'Sara', 'Sara', '05530000034', 'gabriel.sara@example.com', 'https://www.linkedin.com/in/gabriel-sara', '1999-06-20', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(35, 'Lucas', 'Torreira', 'Torreira', '05530000035', 'lucas.torreira@example.com', 'https://www.linkedin.com/in/lucas-torreira', '1996-02-11', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(36, 'İlkay', 'Gündogan', 'İlkay', '05530000036', 'ilkay.gundogan@example.com', 'https://www.linkedin.com/in/ilkay-gundogan', '1990-10-24', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(37, 'Leroy', 'Sane', 'Sane', '05530000037', 'leroy.sane@example.com', 'https://www.linkedin.com/in/leroy-sane', '1996-01-11', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(38, 'Barış Alper', 'Yılmaz', 'Barış Alper', '05530000038', 'baris.alper.yilmaz@example.com', 'https://www.linkedin.com/in/baris-alper-yilmaz', '2000-05-23', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(39, 'Victor', 'Osimhen', 'Osimhen', '05530000039', 'victor.osimhen@example.com', 'https://www.linkedin.com/in/victor-osimhen', '1998-12-29', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(40, 'Roland', 'Sallai', 'Sallai', '05530000040', 'roland.sallai@example.com', 'https://www.linkedin.com/in/roland-sallai', '1997-05-22', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(41, 'Mario', 'Lemina', 'Lemina', '05530000041', 'mario.lemina@example.com', 'https://www.linkedin.com/in/mario-lemina', '1993-09-01', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(42, 'Mauro', 'Icardi', 'İcardi', '05530000042', 'mauro.icardi@example.com', 'https://www.linkedin.com/in/mauro-icardi', '1993-02-19', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(43, 'Andre', 'Onana', 'Onana', '05530000043', 'andre.onana@example.com', 'https://www.linkedin.com/in/andre-onana', '1996-04-02', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(44, 'Wagner', 'Pina', 'Wagner', '05530000044', 'wagner.pina@example.com', 'https://www.linkedin.com/in/wagner-pina', '1993-07-10', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(45, 'Batagov', 'Batagov', 'Batagov', '05530000045', 'batagov@example.com', 'https://www.linkedin.com/in/batagov', '2002-04-14', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(46, 'Stefan', 'Savic', 'Stefan Savic', '05530000046', 'stefan.savic@example.com', 'https://www.linkedin.com/in/stefan-savic', '1991-01-08', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(47, 'Mustafa', 'Eskihellaç', 'Mustafa', '05530000047', 'mustafa.eskihellac@example.com', 'https://www.linkedin.com/in/mustafa-eskihellac', '1997-05-05', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(48, 'Benjamin', 'Bouchouari', 'Bouchouari', '05530000048', 'benjamin.bouchouari@example.com', 'https://www.linkedin.com/in/benjamin-bouchouari', '2001-11-13', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(49, 'Tim', 'Folcerelli', 'Folcerelli', '05530000049', 'tim.folcerelli@example.com', 'https://www.linkedin.com/in/tim-jabol-folcerelli', '2002-02-02', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(50, 'Oulai', 'Oulai', 'Oulai', '05530000050', 'oulai@example.com', 'https://www.linkedin.com/in/oulai', '1999-09-09', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(51, 'Zubkov', 'Zubkov', 'Zubkov', '05530000051', 'zubkov@example.com', 'https://www.linkedin.com/in/zubkov', '1996-08-03', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(52, 'Felipe', 'Augusto', 'Augusto', '05530000052', 'felipe.augusto@example.com', 'https://www.linkedin.com/in/felipe-augusto', '1993-03-30', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(53, 'Paul', 'Onuachu', 'Onuachu', '05530000053', 'paul.onuachu@example.com', 'https://www.linkedin.com/in/paul-onuachu', '1994-05-28', '2025-12-04 16:52:59', '2025-12-04 16:52:59'),
(54, 'Ernest', 'Muci', 'Ernest Muci', '05530000054', 'ernest.muci@example.com', 'https://www.linkedin.com/in/ernest-muci', '2001-03-19', '2025-12-04 16:52:59', '2025-12-04 16:52:59');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `name` varchar(50) NOT NULL,
  `surname` varchar(50) NOT NULL,
  `role` varchar(30) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `users`
--

INSERT INTO `users` (`user_id`, `username`, `password_hash`, `name`, `surname`, `role`, `created_at`) VALUES
(1, 'bellingham', 'd96d530c92b0ebb871ca52b6d6f31e48fc06c83cecc0c17a4202b76770544392', 'Ayhan', 'Öner', 'TESTER', '2025-12-04 16:00:04'),
(2, 'tester', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Okan', 'Buruk', 'TESTER', '2025-12-04 16:00:04'),
(3, 'terminatör', '6e7b21d1178f415c69ab23e7330355943a6ef8bb71c047bca0391643997cc760', 'Bilal', 'Keleş', 'JUNIOR_DEV', '2025-12-04 16:00:04'),
(4, 'junior', '8fdd880f097cddfef86895d2c48f649e943bed14639f0ad29671508b536c9fc1', 'Fatih', 'Tekke', 'JUNIOR_DEV', '2025-12-04 16:00:04'),
(5, 'dede', 'bfccfeb7726160d74f8a18407853846aab2ebd57db1dc32409acd6aefc7c4b33', 'Tunahan', 'Tuze', 'SENIOR_DEV', '2025-12-04 16:00:04'),
(6, 'senior', '2161403032b8314a5249774f9418acf04317a84cc1dddba989d108e763d557ac', 'Dominico', 'Tedesco', 'SENIOR_DEV', '2025-12-04 16:00:04'),
(7, 'sezo', '9ad3e9023704376a56cceb23c7486186507546991c151ea63fe274f1f3f4e734', 'Sezai', 'Araplarlı', 'MANAGER', '2025-12-04 16:00:04'),
(8, 'manager', '6ee4a469cd4e91053847f5d3fcb61dbcc91e8f0ef10be7748da4c4a1ba382d17', 'Sergen', 'Yalçın', 'MANAGER', '2025-12-04 16:00:04');

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `contacts`
--
ALTER TABLE `contacts`
  ADD PRIMARY KEY (`contact_id`),
  ADD KEY `idx_contacts_first_name` (`first_name`),
  ADD KEY `idx_contacts_last_name` (`last_name`),
  ADD KEY `idx_contacts_phone_pri` (`phone_number`),
  ADD KEY `idx_contacts_email` (`email`);

--
-- Tablo için indeksler `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Dökümü yapılmış tablolar için AUTO_INCREMENT değeri
--

--
-- Tablo için AUTO_INCREMENT değeri `contacts`
--
ALTER TABLE `contacts`
  MODIFY `contact_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=55;

--
-- Tablo için AUTO_INCREMENT değeri `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
