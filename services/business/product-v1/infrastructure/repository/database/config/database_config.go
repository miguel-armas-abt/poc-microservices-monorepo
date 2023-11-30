package config

import (
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"product-v1/infrastructure/config"
	"product-v1/infrastructure/repository/database/entity"
)

func SetupDatabase() *gorm.DB {
	dsn := config.DBUser + ":" + config.DBPassword + "@tcp(" + config.DBHost + ")/" + config.DBName + "?charset=utf8mb4&parseTime=True&loc=Local"
	database, err := gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		panic("[LOG] Failed to connect to MYSQL database")
	}

	err = database.AutoMigrate(&entity.ProductEntity{})
	if err != nil {
		panic("[LOG] Failed to auto migrate products table")
	}

	return database
}
