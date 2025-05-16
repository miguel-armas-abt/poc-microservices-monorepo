package rest

import (
	"github.com/gin-gonic/gin"
)

func SetupRouter(productRestService *ProductRestService) *gin.Engine {
	router := gin.Default()

	menuOptionRouter := router.Group("/poc/business/product/v1/products")
	{
		menuOptionRouter.GET("", productRestService.FindByScope)
		menuOptionRouter.GET("/:code", productRestService.FindByCode)
		menuOptionRouter.POST("", productRestService.Save)
		menuOptionRouter.PUT("/:code", productRestService.Update)
		menuOptionRouter.DELETE("/:code", productRestService.Delete)
	}

	return router
}
