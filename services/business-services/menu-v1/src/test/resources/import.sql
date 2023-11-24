--CREATE SCHEMA db_menu_options;
--create table menu_options (
--  menu_option_id bigint not null auto_increment,
--  is_active bit,
--  category varchar(255),
--  description varchar(255),
--  price decimal(19,2),
--  primary key (menu_option_id)
--);

insert into menu_options(description, category, product_code) values ('Ají de gallina', 'MAIN', 'MENU0001');
insert into menu_options(description, category, product_code) values ('Chancho al cilindro', 'MAIN', 'MENU0002');
insert into menu_options(description, category, product_code) values ('Jarra de chicha', 'DRINK', 'MENU0003');
insert into menu_options(description, category, product_code) values ('Jarra de maracuyá', 'DRINK', 'MENU0004');
insert into menu_options(description, category, product_code) values ('Torta de chocolate', 'DESSERT', 'MENU0005');
insert into menu_options(description, category, product_code) values ('Pie de manzana', 'DESSERT', 'MENU0006');
