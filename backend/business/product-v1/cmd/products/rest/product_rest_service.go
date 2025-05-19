package rest

import (
	"net/http"

	"com.demo.poc/cmd/products/dto/request"
	"com.demo.poc/cmd/products/dto/response"
	"com.demo.poc/cmd/products/service"
	"com.demo.poc/commons/validations"
	headers "com.demo.poc/commons/validations/headers"
	"github.com/gin-gonic/gin"
)

type ProductRestService struct {
	productService service.ProductService
	paramValidator *validations.ParamValidator
	bodyValidator  *validations.BodyValidator
}

func NewProductRestService(
	service service.ProductService,
	paramValidator *validations.ParamValidator,
	bodyValidator *validations.BodyValidator,
) *ProductRestService {

	return &ProductRestService{
		productService: service,
		paramValidator: paramValidator,
		bodyValidator:  bodyValidator,
	}
}

func (productRestService *ProductRestService) FindByCode(context *gin.Context) {
	var defaultHeaders headers.DefaultHeaders
	if !productRestService.paramValidator.ValidateParamAndBind(context, &defaultHeaders) {
		return
	}

	code := context.Param("code")
	menuOption, err := productRestService.productService.FindByCode(code)
	if err != nil {
		context.Error(err)
		context.Abort()
		return
	}
	context.JSON(http.StatusOK, menuOption)
}

func (productRestService *ProductRestService) FindByScope(context *gin.Context) {
	var defaultHeaders headers.DefaultHeaders
	if !productRestService.paramValidator.ValidateParamAndBind(context, &defaultHeaders) {
		return
	}

	scope := context.Query("scope")

	var productList []response.ProductResponseDto
	var err error

	if scope == "" {
		productList, err = productRestService.productService.FindAll()
	} else {
		productList, err = productRestService.productService.FindByScope(scope)
	}

	if err != nil {
		context.Error(err)
		context.Abort()
		return
	}

	context.JSON(http.StatusOK, productList)
}

func (productRestService *ProductRestService) Save(context *gin.Context) {
	var defaultHeaders headers.DefaultHeaders
	if !productRestService.paramValidator.ValidateParamAndBind(context, &defaultHeaders) {
		return
	}

	saveRequest, ok := validations.ValidateBodyAndGet[request.ProductSaveRequestDto](context, productRestService.bodyValidator)
	if !ok {
		return
	}

	createdProduct, err := productRestService.productService.Save(saveRequest)

	if err != nil {
		context.Error(err)
		context.Abort()
		return
	}

	context.JSON(http.StatusCreated, createdProduct)
}

func (productRestService *ProductRestService) Update(context *gin.Context) {
	var defaultHeaders headers.DefaultHeaders
	if !productRestService.paramValidator.ValidateParamAndBind(context, &defaultHeaders) {
		return
	}

	code := context.Param("code")
	updateRequest, ok := validations.ValidateBodyAndGet[request.ProductUpdateRequestDto](context, productRestService.bodyValidator)
	if !ok {
		return
	}

	updatedProduct, err := productRestService.productService.Update(updateRequest, code)
	if err != nil {
		context.Error(err)
		context.Abort()
		return
	}

	context.JSON(http.StatusOK, updatedProduct)
}

func (productRestService *ProductRestService) Delete(context *gin.Context) {
	var defaultHeaders headers.DefaultHeaders
	if !productRestService.paramValidator.ValidateParamAndBind(context, &defaultHeaders) {
		return
	}

	code := context.Param("code")
	err := productRestService.productService.Delete(code)
	if err != nil {
		context.Error(err)
		context.Abort()
		return
	}
	context.Status(http.StatusNoContent)
}
