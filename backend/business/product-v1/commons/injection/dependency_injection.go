package injection

import (
	"com.demo.poc/cmd/products/repository"
	"com.demo.poc/cmd/products/rest"
	"com.demo.poc/cmd/products/service"
	coreConfig "com.demo.poc/commons/config"
	errorSelector "com.demo.poc/commons/errors/selector"
	errorInterceptor "com.demo.poc/commons/interceptor/errors"
	properties "com.demo.poc/commons/properties"
	"com.demo.poc/commons/validations"

	"github.com/gin-gonic/gin"
)

func NewEngine() *gin.Engine {
	engine := gin.New()

	props := &properties.Properties
	dbConnection := coreConfig.NewDatabaseConnection()
	repo := repository.NewProductRepositoryImpl(dbConnection)
	svc := service.NewProductServiceImpl(repo)

	validate := validations.NewValidator()

	selector := errorSelector.NewResponseErrorSelector(props)
	interceptor := errorInterceptor.NewErrorInterceptor(selector)

	restService := rest.NewProductRestService(svc, validate)
	rest.NewRouter(engine, interceptor, restService)

	return engine
}
