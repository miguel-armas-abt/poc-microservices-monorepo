package main

import (
	"com.demo.poc/cmd/commons/custom/config"
	repository "com.demo.poc/cmd/entrypoint/products/repository"
	"com.demo.poc/cmd/entrypoint/products/rest"
	"com.demo.poc/cmd/entrypoint/products/service"
)

func main() {
	db := config.SetupDatabase()
	productRepository := repository.NewProductRepository(db)
	productService := service.NewProductService(productRepository)
	productRestService := rest.NewProductRestService(productService)
	router := rest.SetupRouter(productRestService)
	router.Run(config.ApplicationPort)
}
