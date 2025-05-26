package rest

import (
	"net/http"

	"poc/cmd/products/dto/request"
	"poc/cmd/products/dto/response"
	"poc/cmd/products/service"
	"poc/commons/core/constants"
	utils "poc/commons/core/restserver/utils"
	"poc/commons/core/validations"
	headers "poc/commons/core/validations/headers"

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

func (rest *ProductRestService) FindByCode(ctx *gin.Context) {
	var defaultHeaders headers.DefaultHeaders
	if !rest.paramValidator.ValidateParamAndBind(ctx, &defaultHeaders) {
		return
	}

	code := ctx.Param("code")
	menuOption, err := rest.productService.FindByCode(ctx.Request.Context(), utils.ExtractHeadersAsMap(ctx.Request.Header), code)
	if err != nil {
		ctx.Error(err)
		ctx.Abort()
		return
	}
	ctx.JSON(http.StatusOK, menuOption)
}

func (rest *ProductRestService) FindByScope(ctx *gin.Context) {
	var defaultHeaders headers.DefaultHeaders
	if !rest.paramValidator.ValidateParamAndBind(ctx, &defaultHeaders) {
		return
	}
	headers := utils.ExtractHeadersAsMap(ctx.Request.Header)

	scope := ctx.Query("scope")

	var productList []response.ProductResponseDto
	var err error

	if scope == constants.EMPTY {
		productList, err = rest.productService.FindAll(ctx.Request.Context(), headers)
	} else {
		productList, err = rest.productService.FindByScope(ctx.Request.Context(), headers, scope)
	}

	if err != nil {
		ctx.Error(err)
		ctx.Abort()
		return
	}

	ctx.JSON(http.StatusOK, productList)
}

func (rest *ProductRestService) Save(ctx *gin.Context) {
	var defaultHeaders headers.DefaultHeaders
	if !rest.paramValidator.ValidateParamAndBind(ctx, &defaultHeaders) {
		return
	}

	saveRequest, ok := validations.ValidateBodyAndGet[request.ProductSaveRequestDto](ctx, rest.bodyValidator)
	if !ok {
		return
	}

	createdProduct, err := rest.productService.Save(ctx.Request.Context(), utils.ExtractHeadersAsMap(ctx.Request.Header), saveRequest)

	if err != nil {
		ctx.Error(err)
		ctx.Abort()
		return
	}

	ctx.JSON(http.StatusCreated, createdProduct)
}

func (rest *ProductRestService) Update(ctx *gin.Context) {
	var defaultHeaders headers.DefaultHeaders
	if !rest.paramValidator.ValidateParamAndBind(ctx, &defaultHeaders) {
		return
	}

	code := ctx.Param("code")
	updateRequest, ok := validations.ValidateBodyAndGet[request.ProductUpdateRequestDto](ctx, rest.bodyValidator)
	if !ok {
		return
	}

	updatedProduct, err := rest.productService.Update(ctx.Request.Context(), utils.ExtractHeadersAsMap(ctx.Request.Header), updateRequest, code)
	if err != nil {
		ctx.Error(err)
		ctx.Abort()
		return
	}

	ctx.JSON(http.StatusOK, updatedProduct)
}

func (rest *ProductRestService) Delete(ctx *gin.Context) {
	var defaultHeaders headers.DefaultHeaders
	if !rest.paramValidator.ValidateParamAndBind(ctx, &defaultHeaders) {
		return
	}

	code := ctx.Param("code")
	err := rest.productService.Delete(ctx.Request.Context(), utils.ExtractHeadersAsMap(ctx.Request.Header), code)
	if err != nil {
		ctx.Error(err)
		ctx.Abort()
		return
	}
	ctx.Status(http.StatusNoContent)
}
