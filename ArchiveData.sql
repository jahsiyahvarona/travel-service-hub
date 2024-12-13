-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 13, 2024 at 02:56 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `Final2`
--

-- --------------------------------------------------------

--
-- Table structure for table `bookings`
--

CREATE TABLE `bookings` (
  `id` bigint(20) NOT NULL,
  `end_date` date NOT NULL,
  `price` double NOT NULL,
  `start_date` date NOT NULL,
  `status` enum('CONFIRMED','DENIED','REPORTED','UNCONFIRMED') NOT NULL,
  `timestamp` datetime(6) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `package_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bookings`
--

INSERT INTO `bookings` (`id`, `end_date`, `price`, `start_date`, `status`, `timestamp`, `customer_id`, `package_id`) VALUES
(1, '2024-12-19', 1000, '2024-12-12', 'CONFIRMED', '2024-12-12 12:51:51.000000', 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `cities`
--

CREATE TABLE `cities` (
  `id` bigint(20) NOT NULL,
  `country` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `state` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cities`
--

INSERT INTO `cities` (`id`, `country`, `name`, `state`) VALUES
(1, 'United States', 'New York', 'New York'),
(2, 'United States', 'Los Angeles', 'California'),
(3, 'United States', 'Chicago', 'Illinois'),
(4, 'United States', 'Houston', 'Texas'),
(5, 'United States', 'Phoenix', 'Arizona'),
(6, 'United States', 'San Francisco', 'California'),
(7, 'United States', 'Miami', 'Florida'),
(8, 'United States', 'Dallas', 'Texas'),
(9, 'United States', 'Atlanta', 'Georgia'),
(10, 'United States', 'Denver', 'Colorado'),
(11, 'United Kingdom', 'London', NULL),
(12, 'Japan', 'Tokyo', NULL),
(13, 'France', 'Paris', NULL),
(14, 'Germany', 'Berlin', NULL),
(15, 'Australia', 'Sydney', NULL),
(16, 'Australia', 'Melbourne', NULL),
(17, 'Canada', 'Toronto', 'Ontario'),
(18, 'Canada', 'Vancouver', 'British Columbia'),
(19, 'Canada', 'Montreal', 'Quebec'),
(20, 'United Arab Emirates', 'Dubai', NULL),
(21, 'Singapore', 'Singapore', NULL),
(22, 'India', 'Mumbai', 'Maharashtra'),
(23, 'India', 'Delhi', NULL),
(24, 'India', 'Bangalore', 'Karnataka'),
(25, 'South Africa', 'Cape Town', NULL),
(26, 'South Africa', 'Johannesburg', NULL),
(27, 'Brazil', 'Rio de Janeiro', NULL),
(28, 'Brazil', 'São Paulo', NULL),
(29, 'Argentina', 'Buenos Aires', NULL),
(30, 'Mexico', 'Mexico City', NULL),
(31, 'Mexico', 'Guadalajara', NULL),
(32, 'Chile', 'Santiago', NULL),
(33, 'Colombia', 'Bogotá', NULL),
(34, 'Peru', 'Lima', NULL),
(35, 'Venezuela', 'Caracas', NULL),
(36, 'Spain', 'Madrid', NULL),
(37, 'Spain', 'Barcelona', NULL),
(38, 'Italy', 'Rome', NULL),
(39, 'Italy', 'Milan', NULL),
(40, 'Italy', 'Naples', NULL),
(41, 'Turkey', 'Istanbul', NULL),
(42, 'Turkey', 'Ankara', NULL),
(43, 'Greece', 'Athens', NULL),
(44, 'Egypt', 'Cairo', NULL),
(45, 'Nigeria', 'Lagos', NULL),
(46, 'Nigeria', 'Abuja', NULL),
(47, 'Ghana', 'Accra', NULL),
(48, 'Morocco', 'Casablanca', NULL),
(49, 'Morocco', 'Marrakesh', NULL),
(50, 'Kenya', 'Nairobi', NULL),
(51, 'Malaysia', 'Johor Bahru', NULL),
(52, 'Thailand', 'Bangkok', NULL),
(53, 'Thailand', 'Phuket', NULL),
(54, 'Vietnam', 'Ho Chi Minh City', NULL),
(55, 'Vietnam', 'Hanoi', NULL),
(56, 'Indonesia', 'Jakarta', NULL),
(57, 'Indonesia', 'Surabaya', NULL),
(58, 'Malaysia', 'Kuala Lumpur', NULL),
(59, 'Philippines', 'Manila', NULL),
(60, 'Philippines', 'Cebu', NULL),
(61, 'United States', 'Birmingham', 'Alabama'),
(62, 'United States', 'Anchorage', 'Alaska'),
(63, 'United States', 'Phoenix', 'Arizona'),
(64, 'United States', 'Little Rock', 'Arkansas'),
(65, 'United States', 'Los Angeles', 'California'),
(66, 'United States', 'Denver', 'Colorado'),
(67, 'United States', 'Bridgeport', 'Connecticut'),
(68, 'United States', 'Wilmington', 'Delaware'),
(69, 'United States', 'Jacksonville', 'Florida'),
(70, 'United States', 'Atlanta', 'Georgia'),
(71, 'United States', 'Honolulu', 'Hawaii'),
(72, 'United States', 'Boise', 'Idaho'),
(73, 'United States', 'Chicago', 'Illinois'),
(74, 'United States', 'Indianapolis', 'Indiana'),
(75, 'United States', 'Des Moines', 'Iowa'),
(76, 'United States', 'Wichita', 'Kansas'),
(77, 'United States', 'Louisville', 'Kentucky'),
(78, 'United States', 'New Orleans', 'Louisiana'),
(79, 'United States', 'Portland', 'Maine'),
(80, 'United States', 'Baltimore', 'Maryland'),
(81, 'United States', 'Boston', 'Massachusetts'),
(82, 'United States', 'Detroit', 'Michigan'),
(83, 'United States', 'Minneapolis', 'Minnesota'),
(84, 'United States', 'Jackson', 'Mississippi'),
(85, 'United States', 'Kansas City', 'Missouri'),
(86, 'United States', 'Billings', 'Montana'),
(87, 'United States', 'Omaha', 'Nebraska'),
(88, 'United States', 'Las Vegas', 'Nevada'),
(89, 'United States', 'Manchester', 'New Hampshire'),
(90, 'United States', 'Newark', 'New Jersey'),
(91, 'United States', 'Albuquerque', 'New Mexico'),
(92, 'United States', 'New York City', 'New York'),
(93, 'United States', 'Charlotte', 'North Carolina'),
(94, 'United States', 'Fargo', 'North Dakota'),
(95, 'United States', 'Columbus', 'Ohio'),
(96, 'United States', 'Oklahoma City', 'Oklahoma'),
(97, 'United States', 'Portland', 'Oregon'),
(98, 'United States', 'Philadelphia', 'Pennsylvania'),
(99, 'United States', 'Providence', 'Rhode Island'),
(100, 'United States', 'Columbia', 'South Carolina'),
(101, 'United States', 'Sioux Falls', 'South Dakota'),
(102, 'United States', 'Memphis', 'Tennessee'),
(103, 'United States', 'Houston', 'Texas'),
(104, 'United States', 'Salt Lake City', 'Utah'),
(105, 'United States', 'Burlington', 'Vermont'),
(106, 'United States', 'Virginia Beach', 'Virginia'),
(107, 'United States', 'Seattle', 'Washington'),
(108, 'United States', 'Charleston', 'West Virginia'),
(109, 'United States', 'Milwaukee', 'Wisconsin'),
(110, 'United States', 'Cheyenne', 'Wyoming'),
(111, 'Maldives', 'Malé', NULL),
(112, 'Greece', 'Santorini', NULL),
(113, 'French Polynesia', 'Bora Bora', NULL),
(114, 'Peru', 'Machu Picchu', NULL),
(115, 'New Zealand', 'Queenstown', NULL),
(116, 'Canada', 'Banff', 'Alberta'),
(117, 'Iceland', 'Reykjavik', NULL),
(118, 'Italy', 'Florence', NULL),
(119, 'Italy', 'Venice', NULL),
(120, 'Scotland', 'Edinburgh', NULL),
(121, 'Cuba', 'Havana', NULL),
(122, 'Guatemala', 'Antigua', NULL),
(123, 'Croatia', 'Dubrovnik', NULL),
(124, 'Thailand', 'Koh Samui', NULL),
(125, 'Japan', 'Kyoto', NULL),
(126, 'Fiji', 'Fiji', NULL),
(127, 'Monaco', 'Monte Carlo', NULL),
(128, 'Tanzania', 'Zanzibar', NULL),
(129, 'Jordan', 'Petra', NULL),
(130, 'Israel', 'Jerusalem', NULL),
(131, 'China', 'Hong Kong', NULL),
(132, 'South Korea', 'Seoul', NULL),
(133, 'Morocco', 'Marrakech', NULL),
(134, 'Cambodia', 'Siem Reap', NULL),
(135, 'French Polynesia', 'Tahiti', NULL),
(136, 'South Africa', 'Cape Town', NULL),
(137, 'Peru', 'Cusco', NULL),
(138, 'United States', 'Yellowstone National Park', NULL),
(139, 'Canada', 'Whistler', 'British Columbia'),
(140, 'United States', 'Aspen', 'Colorado'),
(141, 'United States', 'Orlando', 'Florida'),
(142, 'Mexico', 'Cancún', NULL),
(143, 'Mexico', 'Tulum', NULL),
(144, 'Croatia', 'Hvar', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `like_dislikes`
--

CREATE TABLE `like_dislikes` (
  `id` bigint(20) NOT NULL,
  `is_like` bit(1) NOT NULL,
  `package_id` bigint(20) NOT NULL,
  `provider_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `like_dislikes`
--

INSERT INTO `like_dislikes` (`id`, `is_like`, `package_id`, `provider_id`, `user_id`) VALUES
(1, b'1', 1, 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `notification`
--

CREATE TABLE `notification` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `is_read` bit(1) NOT NULL,
  `message` varchar(255) DEFAULT NULL,
  `reason` enum('CONFIRMED_BOOKING','DENIED_BOOKING','LIKED','NEW_BOOKING','NEW_REVIEW','REPLIED','UNCONFIRMED_BOOKING','WARNING') DEFAULT NULL,
  `target_url` varchar(255) DEFAULT NULL,
  `notifee_id` bigint(20) DEFAULT NULL,
  `notifier_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `notification`
--

INSERT INTO `notification` (`id`, `created_at`, `is_read`, `message`, `reason`, `target_url`, `notifee_id`, `notifier_id`) VALUES
(1, '2024-12-12 12:51:42.000000', b'0', 'your package: test1, was liked by: demoshaka', 'LIKED', '/provider/managePackages', 1, 2),
(2, '2024-12-12 12:51:51.000000', b'1', 'You have a new booking request from demoshaka', 'NEW_BOOKING', '/provider/manageBookings', 1, 2),
(3, '2024-12-12 12:54:00.000000', b'1', 'Your booking has been Confirmed', 'CONFIRMED_BOOKING', '/customer/bookings', 2, 1),
(4, '2024-12-12 12:55:30.000000', b'1', 'demoshaka has left a review on your package: test1', 'NEW_REVIEW', '/provider/replyToReviews', 1, 2),
(5, '2024-12-12 12:56:36.000000', b'0', 'You have a new reply from provider1', 'REPLIED', '/customer/reviews', 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `packages`
--

CREATE TABLE `packages` (
  `id` bigint(20) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `price` double NOT NULL,
  `city_id` bigint(20) NOT NULL,
  `provider_details_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `packages`
--

INSERT INTO `packages` (`id`, `description`, `image_url`, `name`, `price`, `city_id`, `provider_details_id`) VALUES
(1, 'testtest', '/uploads/2b4baaba-98b4-4a90-b438-00dfe303c8f7.png', 'test1', 1000, 46, 1);

-- --------------------------------------------------------

--
-- Table structure for table `reports`
--

CREATE TABLE `reports` (
  `id` bigint(20) NOT NULL,
  `punishment_type` enum('BAN','NONE','SUSPENSION','WARNING') DEFAULT NULL,
  `reason` varchar(255) NOT NULL,
  `status` enum('ACCEPTED','PENDING','REJECTED') NOT NULL,
  `timestamp` datetime(6) NOT NULL,
  `booking_id` bigint(20) DEFAULT NULL,
  `package_id` bigint(20) DEFAULT NULL,
  `reporter_id` bigint(20) NOT NULL,
  `review_id` bigint(20) DEFAULT NULL,
  `reviewed_by_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `reviews`
--

CREATE TABLE `reviews` (
  `id` bigint(20) NOT NULL,
  `content` varchar(255) NOT NULL,
  `reply_content` text DEFAULT NULL,
  `reply_timestamp` datetime(6) DEFAULT NULL,
  `status` enum('ACTIVE','DELETED','FLAGGED') NOT NULL,
  `timestamp` datetime(6) NOT NULL,
  `author_id` bigint(20) NOT NULL,
  `package_id` bigint(20) NOT NULL,
  `provider_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reviews`
--

INSERT INTO `reviews` (`id`, `content`, `reply_content`, `reply_timestamp`, `status`, `timestamp`, `author_id`, `package_id`, `provider_id`) VALUES
(1, 'it was cooll\r\n', 'thank', '2024-12-12 12:56:36.000000', 'ACTIVE', '2024-12-12 12:55:41.000000', 2, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `usage_statistics`
--

CREATE TABLE `usage_statistics` (
  `stat_id` bigint(20) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `date_range` date DEFAULT NULL,
  `end_date` date NOT NULL,
  `metric_type` enum('ACTIVE_USERS','AVERAGE_RATING','BOOKINGS','VIEWS') NOT NULL,
  `start_date` date NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `value` decimal(38,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `active` bit(1) NOT NULL,
  `banned` bit(1) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `profile_pic` varchar(255) DEFAULT NULL,
  `role` enum('CUSTOMER','PROVIDER','SYSADMIN') NOT NULL,
  `username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `active`, `banned`, `email`, `password`, `profile_pic`, `role`, `username`) VALUES
(1, b'1', b'0', 'provider1@gmail.com', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', '/uploads/a9b9fc6b-a16c-4269-9029-451e512f6b44.png', 'PROVIDER', 'provider1'),
(2, b'1', b'0', 'customer1@gmail.com', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', '/uploads/0f9c00c5-3824-4cf5-a20b-ed1d19803f6b.png', 'CUSTOMER', 'demoshaka');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bookings`
--
ALTER TABLE `bookings`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKib6gjgj2e9binkktxmm175bmm` (`customer_id`),
  ADD KEY `FKpsi0jsj8kjkqse44mspx80bk3` (`package_id`);

--
-- Indexes for table `cities`
--
ALTER TABLE `cities`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `like_dislikes`
--
ALTER TABLE `like_dislikes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK432j9a90kl3kololl1d2n0gpi` (`package_id`),
  ADD KEY `FK411j62qrmyek4vt191nriu2u8` (`provider_id`),
  ADD KEY `FKbtk4yjroqliwbmxtij64ehrcl` (`user_id`);

--
-- Indexes for table `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKswlp5a9inexy3f2cjt8mcqdfb` (`notifee_id`),
  ADD KEY `FKfheryvu9f3wbusdirk9ir0pyp` (`notifier_id`);

--
-- Indexes for table `packages`
--
ALTER TABLE `packages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKblsakbjnv6d7xxm13kso62o9s` (`city_id`),
  ADD KEY `FK7qshph01qkw532seeflt9sq2s` (`provider_details_id`);

--
-- Indexes for table `reports`
--
ALTER TABLE `reports`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK423qtj995s4jhe7db7dc8g5bw` (`booking_id`),
  ADD KEY `FKn1j4yroekokksj7bpmsx7jgvw` (`package_id`),
  ADD KEY `FKd3qiw2om5d2oh5xb7fbdcq225` (`reporter_id`),
  ADD KEY `FK2wlcdglmlgsr07wtwcnlg7kbk` (`review_id`),
  ADD KEY `FKn1iu2ses25aoxypgjeym3owpl` (`reviewed_by_id`);

--
-- Indexes for table `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKse5kx11600wtv0jh9jobvrdpi` (`author_id`),
  ADD KEY `FKai8kee8qoke1i4u3vuo18ek40` (`package_id`),
  ADD KEY `FK6v6isw4stf5vu1fktr1whlx06` (`provider_id`);

--
-- Indexes for table `usage_statistics`
--
ALTER TABLE `usage_statistics`
  ADD PRIMARY KEY (`stat_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  ADD UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bookings`
--
ALTER TABLE `bookings`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `like_dislikes`
--
ALTER TABLE `like_dislikes`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `notification`
--
ALTER TABLE `notification`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `packages`
--
ALTER TABLE `packages`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `reports`
--
ALTER TABLE `reports`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `usage_statistics`
--
ALTER TABLE `usage_statistics`
  MODIFY `stat_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bookings`
--
ALTER TABLE `bookings`
  ADD CONSTRAINT `FKib6gjgj2e9binkktxmm175bmm` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKpsi0jsj8kjkqse44mspx80bk3` FOREIGN KEY (`package_id`) REFERENCES `packages` (`id`);

--
-- Constraints for table `like_dislikes`
--
ALTER TABLE `like_dislikes`
  ADD CONSTRAINT `FK411j62qrmyek4vt191nriu2u8` FOREIGN KEY (`provider_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FK432j9a90kl3kololl1d2n0gpi` FOREIGN KEY (`package_id`) REFERENCES `packages` (`id`),
  ADD CONSTRAINT `FKbtk4yjroqliwbmxtij64ehrcl` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `notification`
--
ALTER TABLE `notification`
  ADD CONSTRAINT `FKfheryvu9f3wbusdirk9ir0pyp` FOREIGN KEY (`notifier_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKswlp5a9inexy3f2cjt8mcqdfb` FOREIGN KEY (`notifee_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `packages`
--
ALTER TABLE `packages`
  ADD CONSTRAINT `FK7qshph01qkw532seeflt9sq2s` FOREIGN KEY (`provider_details_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKblsakbjnv6d7xxm13kso62o9s` FOREIGN KEY (`city_id`) REFERENCES `cities` (`id`);

--
-- Constraints for table `reports`
--
ALTER TABLE `reports`
  ADD CONSTRAINT `FK2wlcdglmlgsr07wtwcnlg7kbk` FOREIGN KEY (`review_id`) REFERENCES `reviews` (`id`),
  ADD CONSTRAINT `FK423qtj995s4jhe7db7dc8g5bw` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`),
  ADD CONSTRAINT `FKd3qiw2om5d2oh5xb7fbdcq225` FOREIGN KEY (`reporter_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKn1iu2ses25aoxypgjeym3owpl` FOREIGN KEY (`reviewed_by_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKn1j4yroekokksj7bpmsx7jgvw` FOREIGN KEY (`package_id`) REFERENCES `packages` (`id`);

--
-- Constraints for table `reviews`
--
ALTER TABLE `reviews`
  ADD CONSTRAINT `FK6v6isw4stf5vu1fktr1whlx06` FOREIGN KEY (`provider_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKai8kee8qoke1i4u3vuo18ek40` FOREIGN KEY (`package_id`) REFERENCES `packages` (`id`),
  ADD CONSTRAINT `FKse5kx11600wtv0jh9jobvrdpi` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
