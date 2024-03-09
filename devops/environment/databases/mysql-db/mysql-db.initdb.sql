CREATE USER 'bbq_user'@'%' IDENTIFIED BY 'qwerty';
GRANT ALL PRIVILEGES ON *.* TO 'bbq_user'@'%' WITH GRANT OPTION;
CREATE DATABASE db_products;
CREATE DATABASE db_menu_options;