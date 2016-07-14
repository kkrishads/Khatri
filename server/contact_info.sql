-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jul 14, 2016 at 12:01 PM
-- Server version: 10.1.13-MariaDB
-- PHP Version: 5.5.37

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `contact_info`
--

-- --------------------------------------------------------

--
-- Table structure for table `contact_list`
--

CREATE TABLE `contact_list` (
  `Id` int(11) NOT NULL,
  `fname` varchar(25) NOT NULL,
  `lname` varchar(25) NOT NULL,
  `email` varchar(50) NOT NULL,
  `mobile` varchar(11) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `city` varchar(50) NOT NULL,
  `area` varchar(50) NOT NULL,
  `pincode` varchar(15) NOT NULL,
  `address` text NOT NULL,
  `latitude` varchar(25) NOT NULL,
  `longitude` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `contact_list`
--

INSERT INTO `contact_list` (`Id`, `fname`, `lname`, `email`, `mobile`, `phone`, `city`, `area`, `pincode`, `address`, `latitude`, `longitude`) VALUES
(1, 'test', 'test', 'test', '5465454', '676767', 'hjjhj', 'jggj', '353453', 'jhjjh rtgfer jbrgjkter jkbgje', '35345', '34535'),
(2, 'hghgh', 'ghghgh', 'ghghgh', 'hggh', 'gh', 'ghgh', 'gh', '534353', 'gh tkjrwet hrtkjret krntgkel', 'ghgh', 'gh'),
(3, 'lklhkg', 'jbjg', 'jghj', 'kj', 'gj', 'jgj', 'jhjh', '345345', 'shfsgdk sdfgksd sdfgksdhg', 'hg', 'jgbg'),
(4, 'sdfsdfsdef', 'sssdfsdfs', 'sdfsdf', '24321423', '2342342', 'sadsa', 'sfsd', '242342', 'sfsdf sfsdfs sdfsdf sdfsdf sfsfd\r\nsfsdf sdfsdf', '', ''),
(5, 'sdfsd', 'sdfsd', 'sdfsd', '7564523', '234365645', 'sdfsf', 'sfsdf', '242343243', 'zdssf', '', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `contact_list`
--
ALTER TABLE `contact_list`
  ADD PRIMARY KEY (`Id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `contact_list`
--
ALTER TABLE `contact_list`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
