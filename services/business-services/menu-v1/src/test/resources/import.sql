--CREATE SCHEMA db_menu_options;
--create table menu_options (
--  menu_option_id bigint not null auto_increment,
--  is_active bit,
--  category varchar(255),
--  description varchar(255),
--  price decimal(19,2),
--  primary key (menu_option_id)
--);

insert into menu_options(description, category, price, is_active) values ('Ají de gallina', 'MAIN', 15, true);
insert into menu_options(description, category, price, is_active) values ('Chancho al cilindro', 'MAIN', 25, false);
insert into menu_options(description, category, price, is_active) values ('Jarra de chicha', 'DRINK', 12, true);
insert into menu_options(description, category, price, is_active) values ('Jarra de maracuyá', 'DRINK', 12, true);
insert into menu_options(description, category, price, is_active) values ('Torta de chocolate', 'DESSERT', 10, false);
insert into menu_options(description, category, price, is_active) values ('Pie de manzana', 'DESSERT', 10, true);
