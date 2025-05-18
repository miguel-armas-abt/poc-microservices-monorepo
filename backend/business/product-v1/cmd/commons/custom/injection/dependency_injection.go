// cmd/commons/custom/injection/dependency_injection.go
//go:build wireinject
// +build wireinject

package injection

import (
	"com.demo.poc/cmd/commons/custom/config"
	"com.demo.poc/cmd/entrypoint/products/repository"
	"com.demo.poc/cmd/entrypoint/products/rest"
	"com.demo.poc/cmd/entrypoint/products/service"

	"github.com/gin-gonic/gin"
	wire "github.com/google/wire"
)

func Rest() *gin.Engine {
	wire.Build(
		config.GormDB,
		repository.NewProductRepository,
		service.NewProductService,
		rest.NewProductRestService,
		rest.SetupRouter,
	)
	return nil
}
