CREATE DATABASE emms_db;

USE emms_db;

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

CREATE TABLE emails (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        email VARCHAR(255) NOT NULL UNIQUE,
                        clientName VARCHAR(100) NOT NULL,
                        gender ENUM('Male', 'Female') NOT NULL,
                        age INT NOT NULL CHECK (age > 0),
                        job VARCHAR(100)
);
