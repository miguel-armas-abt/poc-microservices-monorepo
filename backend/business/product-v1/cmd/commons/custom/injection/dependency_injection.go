// cmd/commons/custom/injection/dependency_injection.go
//go:build wireinject
// +build wireinject

package di

import (
	"com.demo.poc/cmd/commons/custom/config"
	repo "com.demo.poc/cmd/entrypoint/products/repository"
	rest "com.demo.poc/cmd/entrypoint/products/rest"
	svc "com.demo.poc/cmd/entrypoint/products/service"

	"github.com/gin-gonic/gin"
	wire "github.com/google/wire"
)

func Rest() (*gin.Engine, error) {
	wire.Build(
		config.GormDB,
		repo.NewProductRepository,
		svc.NewProductService,
		rest.NewProductRestService,
		rest.SetupRouter,
	)
	return nil, nil
}
