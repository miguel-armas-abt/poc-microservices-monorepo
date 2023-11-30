package main

import (
	"product-v1/application/service"
	environment "product-v1/infrastructure/config"
	"product-v1/infrastructure/repository/database"
	databaseConfig "product-v1/infrastructure/repository/database/config"
	"product-v1/infrastructure/resource/rest"
)

func main() {
	db := databaseConfig.SetupDatabase()
	productRepository := database.NewProductRepository(db)
	productService := service.NewProductService(productRepository)
	productRestService := rest.NewProductRestService(productService)
	router := rest.SetupRouter(productRestService)
	router.Run(environment.ApplicationPort)
}
