package rest

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"product-v1/application/service"
	"product-v1/domain/model/request"
	"product-v1/domain/model/response"
	"product-v1/infrastructure/exception/handler"
)

type ProductRestService struct {
	productService service.ProductService
}

func NewProductRestService(service service.ProductService) *ProductRestService {
	return &ProductRestService{
		productService: service,
	}
}

func (thisRestService *ProductRestService) FindByCode(context *gin.Context) {
	code := context.Param("code")
	menuOption, err := thisRestService.productService.FindByCode(code)
	if err != nil {
		handler.ErrorHandler(context, err)
		return
	}
	context.JSON(http.StatusOK, menuOption)
}

func (thisRestService *ProductRestService) FindByScope(context *gin.Context) {
	scope := context.Query("scope")

	var productList []response.ProductResponse
	var err error

	if scope == "" {
		productList, err = thisRestService.productService.FindAll()
	} else {
		productList, err = thisRestService.productService.FindByScope(scope)
	}

	if err != nil {
		handler.ErrorHandler(context, err)
		return
	}

	context.JSON(http.StatusOK, productList)
}

func (thisRestService *ProductRestService) Save(context *gin.Context) {
	var menuOptionRequest request.ProductSaveRequest
	if err := context.ShouldBindJSON(&menuOptionRequest); err != nil {
		handler.ErrorHandler(context, err)
		return
	}

	menuOption, err := thisRestService.productService.Save(menuOptionRequest)
	if err != nil {
		handler.ErrorHandler(context, err)
		return
	}
	context.JSON(http.StatusCreated, menuOption)
}

func (thisRestService *ProductRestService) Update(context *gin.Context) {
	code := context.Param("code")
	var menuOptionRequest request.ProductUpdateRequest

	if err := context.ShouldBindJSON(&menuOptionRequest); err != nil {
		handler.ErrorHandler(context, err)
		return
	}

	menuOption, err := thisRestService.productService.Update(menuOptionRequest, code)
	if err != nil {
		handler.ErrorHandler(context, err)
		return
	}
	context.JSON(http.StatusOK, menuOption)
}

func (thisRestService *ProductRestService) Delete(context *gin.Context) {
	code := context.Param("code")
	err := thisRestService.productService.Delete(code)
	if err != nil {
		handler.ErrorHandler(context, err)
		return
	}
	context.Status(http.StatusNoContent)
}
