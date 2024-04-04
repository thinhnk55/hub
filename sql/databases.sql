CREATE DATABASE hub CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE USER 'hub'@'localhost' IDENTIFIED BY 'AMLU%$09378fsrk';
GRANT ALL ON hub.* TO 'hub'@'localhost';
CREATE USER 'hub'@'%' IDENTIFIED BY 'AMLU%$09378fsrk';
GRANT ALL ON hub.* TO 'hub'@'%';