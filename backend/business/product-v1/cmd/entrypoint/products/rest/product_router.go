package rest

import (
	"github.com/gin-gonic/gin"
)

func SetupRouter(productRestService *ProductRestService) *gin.Engine {
	router := gin.Default()

	productRouter := router.Group("/poc/business/product/v1/products")
	{
		productRouter.GET("", productRestService.FindByScope)
		productRouter.GET("/:code", productRestService.FindByCode)
		productRouter.POST("", productRestService.Save)
		productRouter.PUT("/:code", productRestService.Update)
		productRouter.DELETE("/:code", productRestService.Delete)
	}

	return router
}
