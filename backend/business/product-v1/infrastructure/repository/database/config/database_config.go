package config

import (
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"product-v1/infrastructure/config"
)

func SetupDatabase() *gorm.DB {
	dsn := config.DBUser + ":" + config.DBPassword + "@tcp(" + config.DBHost + ")/" + config.DBName + "?charset=utf8mb4&parseTime=True&loc=Local"
	database, err := gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		panic("[LOG] Failed to connect to MYSQL database")
	}

	//err = database.AutoMigrate(&entity.ProductEntity{}) //to auto create table
	//if err != nil {
	//	panic("[LOG] Failed to auto migrate products table")
	//}

	return database
}
