-- Lost & Found project: MySQL schema (matches DAO.java)
-- Run in MySQL Workbench or: mysql -u root -p < schema.sql

CREATE DATABASE IF NOT EXISTS lost_and_found;
USE lost_and_found;

-- 1) Users (DAO.addUser). "users" is used instead of "user" to avoid clashing with MySQL's own user table/keyword.
CREATE TABLE IF NOT EXISTS users (
    userId    INT AUTO_INCREMENT PRIMARY KEY,
    userName  VARCHAR(100) NOT NULL,
    phNo      VARCHAR(15)  NOT NULL,
    email     VARCHAR(100) NOT NULL UNIQUE,
    role      VARCHAR(20)  NOT NULL DEFAULT 'Student'
);

-- 2) Items, both lost and found (DAO.addItem). itemType = 'LOST_ITEM' or 'FOUND_ITEM'.
CREATE TABLE IF NOT EXISTS item (
    itemId       INT AUTO_INCREMENT PRIMARY KEY,
    itemName     VARCHAR(100) NOT NULL,
    description  VARCHAR(500),
    color        VARCHAR(50),
    category     VARCHAR(50),
    itemType     ENUM('LOST_ITEM', 'FOUND_ITEM') NOT NULL,
    userId       INT NOT NULL,
    location     VARCHAR(150),
    reportedOn   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES users(userId)
);

-- 3) Claims (DAO.addClaim / DAO.updateClaim)
CREATE TABLE IF NOT EXISTS claim (
    claimId   INT AUTO_INCREMENT PRIMARY KEY,
    proof     VARCHAR(500) NOT NULL,
    userId    INT NOT NULL,
    itemName  VARCHAR(100) NOT NULL,
    location  VARCHAR(150),
    status    ENUM('Pending', 'Approved', 'Rejected') NOT NULL DEFAULT 'Pending',
    claimedOn TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES users(userId)
);

-- Handy queries for testing / future features
-- All found items matching a lost item's name:
--   SELECT f.* FROM item f JOIN item l ON f.itemName = l.itemName
--   WHERE f.itemType = 'FOUND_ITEM' AND l.itemType = 'LOST_ITEM' AND l.itemId = 1;
-- All pending claims with the claimant's contact details:
--   SELECT c.claimId, c.itemName, u.userName, u.phNo FROM claim c
--   JOIN users u ON c.userId = u.userId WHERE c.status = 'Pending';
