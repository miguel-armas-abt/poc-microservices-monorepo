package config

import (
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

func SetupDatabase() *gorm.DB {
	dsn := DBUser + ":" + DBPassword + "@tcp(" + DBHost + ")/" + DBName + "?charset=utf8mb4&parseTime=True&loc=Local"
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
