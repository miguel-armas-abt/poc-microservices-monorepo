package injection

import (
	"com.demo.poc/cmd/products/repository"
	"com.demo.poc/cmd/products/rest"
	"com.demo.poc/cmd/products/service"
	coreConfig "com.demo.poc/pck/core/config"
	errorHandler "com.demo.poc/pck/core/errors/handler"
	errorSelector "com.demo.poc/pck/core/errors/selector"
	"com.demo.poc/pck/core/validations"
	customConfig "com.demo.poc/pck/custom/config"

	"github.com/gin-gonic/gin"
)

func Rest() *gin.Engine {
	engine := gin.New()

	properties := &coreConfig.Properties
	gormDb := customConfig.GormDB()
	repo := repository.NewProductRepository(gormDb)
	svc := service.NewProductService(repo)

	validate := validations.Validator()

	selector := errorSelector.NewSelector(properties)
	handler := errorHandler.NewErrorHandler(selector)

	restService := rest.NewProductRestService(svc, validate)
	rest.SetupRouter(engine, handler, restService)

	return engine
}
