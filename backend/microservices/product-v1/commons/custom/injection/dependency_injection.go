package injection

import (
	"net/http"
	"poc/cmd/products/repository"
	"poc/cmd/products/rest"
	"poc/cmd/products/service"
	errorSelector "poc/commons/core/errors/selector"
	errorInterceptor "poc/commons/core/interceptor/errors"
	"poc/commons/core/interceptor/restclient"
	"poc/commons/core/interceptor/restserver"
	"poc/commons/core/logging"
	"poc/commons/core/validations"
	"poc/commons/custom/properties"
	"time"

	customConfig "poc/commons/custom/config"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
	"github.com/sirupsen/logrus"
)

func NewEngine(yamlBytes []byte) *gin.Engine {
	logging.InitLogger(logrus.InfoLevel)
	properties.Init(yamlBytes)
	http.DefaultClient.Transport = restclient.NewRestClientInterceptor(http.DefaultTransport, &properties.Properties)

	props := &properties.Properties

	responseErrorSelector := errorSelector.NewResponseErrorSelector()
	interceptor := errorInterceptor.NewErrorInterceptor(responseErrorSelector)

	corsOrigins := props.Server.CorsOrigins
	engine := gin.New()
	engine.Use(
		gin.Recovery(),
		gin.Logger(),
		interceptor.InterceptError(),
		restserver.InterceptRestServer(props),
		cors.New(cors.Config{
			AllowOrigins:     corsOrigins,
			AllowMethods:     []string{"GET", "POST", "OPTIONS"},
			AllowHeaders:     []string{"Content-Type", "traceParent", "channelId"},
			ExposeHeaders:    []string{"Content-Length"},
			AllowCredentials: true,
			MaxAge:           12 * time.Hour,
		}),
	)

	coreValidator := validations.GetValidator()
	paramValidator := validations.NewParamValidator(coreValidator, responseErrorSelector)
	bodyValidator := validations.NewBodyValidator(coreValidator)

	dbConnection := customConfig.NewDatabaseConnection()
	productRepository := repository.NewProductRepositoryImpl(dbConnection)
	svc := service.NewProductServiceImpl(productRepository)

	restService := rest.NewProductRestService(svc, paramValidator, bodyValidator)
	rest.NewRouter(engine, restService)

	return engine
}
