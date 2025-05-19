package rest

import (
	"com.demo.poc/pck/core/errors/handler"
	"github.com/gin-gonic/gin"
)

func SetupRouter(
	engine *gin.Engine,
	errorHandler *handler.ErrorHandler,
	productRestService *ProductRestService) *gin.Engine {

	engine.Use(gin.Recovery(), gin.Logger(), errorHandler.Handler())

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
