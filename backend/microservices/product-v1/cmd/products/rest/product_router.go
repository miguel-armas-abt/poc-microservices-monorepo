package rest

import (
	"github.com/gin-gonic/gin"
)

func NewRouter(
	engine *gin.Engine,
	productRestService *ProductRestService) *gin.Engine {

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
