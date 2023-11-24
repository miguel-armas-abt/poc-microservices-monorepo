package main

import (
	"github.com/gin-gonic/gin"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"product-v1/application/service"
	"product-v1/infrastructure/repository"
	"product-v1/infrastructure/repository/entity"
	"product-v1/infrastructure/resource/rest"
)

func main() {
	db := setupDatabase()
	productRepository := repository.NewProductRepository(db)
	productService := service.NewProductService(productRepository)
	productRestService := rest.NewProductRestService(productService)

	router := gin.Default()

	menuOptionRouter := router.Group("/bbq/business/product/v1/products")
	{
		menuOptionRouter.GET("", productRestService.FindByScope)
		menuOptionRouter.GET("/:code", productRestService.FindByCode)
		menuOptionRouter.POST("", productRestService.Save)
		menuOptionRouter.PUT("/:code", productRestService.Update)
		menuOptionRouter.DELETE("/:code", productRestService.Delete)
	}

	router.Run(":8017")
}

func setupDatabase() *gorm.DB {
	dsn := "bbq_user:qwerty@tcp(127.0.0.1:3306)/db_products?charset=utf8mb4&parseTime=True&loc=Local"
	db, err := gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		panic("[LOG] Failed to connect to MYSQL database")
	}
	err = db.AutoMigrate(&entity.ProductEntity{})
	if err != nil {
		panic("[LOG] Failed to auto migrate products table")
	}
	return db
}
