package injection

import (
	"com.demo.poc/cmd/products/repository"
	"com.demo.poc/cmd/products/rest"
	"com.demo.poc/cmd/products/service"
	errorSelector "com.demo.poc/commons/core/errors/selector"
	errorInterceptor "com.demo.poc/commons/core/interceptor/errors"
	properties "com.demo.poc/commons/core/properties"
	"com.demo.poc/commons/core/validations"
	customConfig "com.demo.poc/commons/custom/config"

	"github.com/gin-gonic/gin"
)

func NewEngine() *gin.Engine {
	engine := gin.New()

	props := &properties.Properties
	dbConnection := customConfig.NewDatabaseConnection()
	repo := repository.NewProductRepositoryImpl(dbConnection)
	svc := service.NewProductServiceImpl(repo)

	validate := validations.NewValidator()

	selector := errorSelector.NewResponseErrorSelector(props)
	interceptor := errorInterceptor.NewErrorInterceptor(selector)

	restService := rest.NewProductRestService(svc, validate)
	rest.NewRouter(engine, interceptor, restService)

	return engine
}
