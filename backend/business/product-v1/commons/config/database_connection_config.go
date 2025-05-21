package config

import (
	"fmt"

	properties "com.demo.poc/commons/properties"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

func NewDatabaseConnection() *gorm.DB {
	databaseProperties := properties.Properties.Database

	dsn := fmt.Sprintf(
		"%s:%s@tcp(%s)/%s?charset=utf8mb4&parseTime=True&loc=Local",
		databaseProperties.User,
		databaseProperties.Password,
		databaseProperties.Host,
		databaseProperties.Name,
	)

	database, err := gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		panic(fmt.Sprintf("Could not connect to database: %v", err))
	}

	return database
}
