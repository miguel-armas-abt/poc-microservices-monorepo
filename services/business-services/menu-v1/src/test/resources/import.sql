--CREATE SCHEMA db_menu_options;
--create table menu_options (
--  menu_option_id bigint not null auto_increment,
--  is_active bit,
--  category varchar(255),
--  description varchar(255),
--  price decimal(19,2),
--  primary key (menu_option_id)
--);

insert into menu_options(description, category, product_code) values ('Ají de gallina', 'MAIN', 15);
insert into menu_options(description, category, product_code) values ('Chancho al cilindro', 'MAIN', 25);
insert into menu_options(description, category, product_code) values ('Jarra de chicha', 'DRINK', 12);
insert into menu_options(description, category, product_code) values ('Jarra de maracuyá', 'DRINK', 12);
insert into menu_options(description, category, product_code) values ('Torta de chocolate', 'DESSERT', 10);
insert into menu_options(description, category, product_code) values ('Pie de manzana', 'DESSERT', 10);
