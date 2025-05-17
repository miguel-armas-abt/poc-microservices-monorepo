package config

import (
	"com.demo.poc/cmd/commons/custom/config"
	repo "com.demo.poc/cmd/entrypoint/products/repository"
	rest "com.demo.poc/cmd/entrypoint/products/rest"
	svc "com.demo.poc/cmd/entrypoint/products/service"

	"github.com/gin-gonic/gin"
	wire "github.com/google/wire"
)

func InitializeRouter() (*gin.Engine, string) {
	wire.Build(
		config.SetupDatabase,
		repo.NewProductRepository,
		svc.NewProductService,
		rest.NewProductRestService,
		rest.SetupRouter,
		wire.Value(config.ApplicationPort),
	)
	return nil, ""
}
