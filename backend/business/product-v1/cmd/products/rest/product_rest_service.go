package rest

import (
	"net/http"

	"com.demo.poc/cmd/products/dto/request"
	"com.demo.poc/cmd/products/dto/response"
	"com.demo.poc/cmd/products/service"
	"com.demo.poc/commons/constants"
	utils "com.demo.poc/commons/restserver/utils"
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

func (rest *ProductRestService) FindByCode(context *gin.Context) {
	var defaultHeaders headers.DefaultHeaders
	if !rest.paramValidator.ValidateParamAndBind(context, &defaultHeaders) {
		return
	}
	headers := utils.ExtractHeadersAsMap(context.Request.Header)

	code := context.Param("code")
	menuOption, err := rest.productService.FindByCode(headers, code)
	if err != nil {
		context.Error(err)
		context.Abort()
		return
	}
	context.JSON(http.StatusOK, menuOption)
}

func (rest *ProductRestService) FindByScope(context *gin.Context) {
	var defaultHeaders headers.DefaultHeaders
	if !rest.paramValidator.ValidateParamAndBind(context, &defaultHeaders) {
		return
	}
	headers := utils.ExtractHeadersAsMap(context.Request.Header)

	scope := context.Query("scope")

	var productList []response.ProductResponseDto
	var err error

	if scope == constants.EMPTY {
		productList, err = rest.productService.FindAll(headers)
	} else {
		productList, err = rest.productService.FindByScope(headers, scope)
	}

	if err != nil {
		context.Error(err)
		context.Abort()
		return
	}

	context.JSON(http.StatusOK, productList)
}

func (rest *ProductRestService) Save(context *gin.Context) {
	var defaultHeaders headers.DefaultHeaders
	if !rest.paramValidator.ValidateParamAndBind(context, &defaultHeaders) {
		return
	}
	headers := utils.ExtractHeadersAsMap(context.Request.Header)

	saveRequest, ok := validations.ValidateBodyAndGet[request.ProductSaveRequestDto](context, rest.bodyValidator)
	if !ok {
		return
	}

	createdProduct, err := rest.productService.Save(headers, saveRequest)

	if err != nil {
		context.Error(err)
		context.Abort()
		return
	}

	context.JSON(http.StatusCreated, createdProduct)
}

func (rest *ProductRestService) Update(context *gin.Context) {
	var defaultHeaders headers.DefaultHeaders
	if !rest.paramValidator.ValidateParamAndBind(context, &defaultHeaders) {
		return
	}
	headers := utils.ExtractHeadersAsMap(context.Request.Header)

	code := context.Param("code")
	updateRequest, ok := validations.ValidateBodyAndGet[request.ProductUpdateRequestDto](context, rest.bodyValidator)
	if !ok {
		return
	}

	updatedProduct, err := rest.productService.Update(headers, updateRequest, code)
	if err != nil {
		context.Error(err)
		context.Abort()
		return
	}

	context.JSON(http.StatusOK, updatedProduct)
}

func (rest *ProductRestService) Delete(context *gin.Context) {
	var defaultHeaders headers.DefaultHeaders
	if !rest.paramValidator.ValidateParamAndBind(context, &defaultHeaders) {
		return
	}
	headers := utils.ExtractHeadersAsMap(context.Request.Header)

	code := context.Param("code")
	err := rest.productService.Delete(headers, code)
	if err != nil {
		context.Error(err)
		context.Abort()
		return
	}
	context.Status(http.StatusNoContent)
}
