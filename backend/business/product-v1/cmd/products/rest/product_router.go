package rest

import (
	errorInterceptor "com.demo.poc/commons/core/interceptor/errors"
	"github.com/gin-gonic/gin"
)

func NewRouter(
	engine *gin.Engine,
	errorInterceptor *errorInterceptor.ErrorInterceptor,
	productRestService *ProductRestService) *gin.Engine {

	engine.Use(gin.Recovery(), gin.Logger(), errorInterceptor.Handler())

	productRouter := engine.Group("/poc/business/product/v1/products")
	{
		productRouter.GET("", productRestService.FindByScope)
		productRouter.GET("/:code", productRestService.FindByCode)
		productRouter.POST("", productRestService.Save)
		productRouter.PUT("/:code", productRestService.Update)
		productRouter.DELETE("/:code", productRestService.Delete)
	}

	return engine
}
