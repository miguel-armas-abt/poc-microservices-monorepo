package rest

import (
	errorInterceptor "com.demo.poc/commons/interceptor/errors"
	"github.com/gin-gonic/gin"
)

func NewRouter(
	engine *gin.Engine,
	errorInterceptor *errorInterceptor.ErrorInterceptor,
	productRestService *ProductRestService) *gin.Engine {

	engine.Use(gin.Recovery(), gin.Logger(), errorInterceptor.Handler())

	productRouter := engine.Group("/poc/business/product/v1")
	{
		productRouter.GET("/products", productRestService.FindByScope)
		productRouter.GET("/products/:code", productRestService.FindByCode)
		productRouter.POST("/products", productRestService.Save)
		productRouter.PUT("/products/:code", productRestService.Update)
		productRouter.DELETE("/products/:code", productRestService.Delete)
	}

	return engine
}
