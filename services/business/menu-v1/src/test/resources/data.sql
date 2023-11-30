CREATE SCHEMA db_menu_options;
create table menu_options (
 id bigint not null auto_increment,
 description varchar(255),
 category varchar(255),
 product_code varchar(255),
 primary key (id)
);

insert into menu_options(id, description, category, product_code) values (1, 'Ají de gallina', 'MAIN', 'MENU0001');
insert into menu_options(id, description, category, product_code) values (2, 'Torta de chocolate', 'DESSERT', 'MENU0002');
insert into menu_options(id, description, category, product_code) values (3, 'Jarra de chicha', 'DRINK', 'MENU0003');
