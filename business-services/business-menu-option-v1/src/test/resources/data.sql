-- CREATE SCHEMA db_menu_purchase;
create table menu_options (
  menu_option_id bigint not null auto_increment,
  is_active bit,
  category varchar(255),
  description varchar(255),
  price decimal(19,2),
  primary key (menu_option_id)
);

insert into menu_options(description, category, price, is_active) values ('Ají de gallina', 'main-dish', 15, true);
insert into menu_options(description, category, price, is_active) values ('Chancho al cilindro', 'main-dish', 25, false);
insert into menu_options(description, category, price, is_active) values ('Jarra de chicha', 'drink', 12, true);
insert into menu_options(description, category, price, is_active) values ('Jarra de maracuyá', 'drink', 12, true);
insert into menu_options(description, category, price, is_active) values ('Torta de chocolate', 'dessert', 10, false);
insert into menu_options(description, category, price, is_active) values ('Pie de manzana', 'dessert', 10, true);
