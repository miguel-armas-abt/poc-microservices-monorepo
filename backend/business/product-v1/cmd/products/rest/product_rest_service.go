package rest

import (
	"net/http"

	"com.demo.poc/cmd/products/dto/request"
	"com.demo.poc/cmd/products/dto/response"
	"com.demo.poc/cmd/products/service"
	coreErrors "com.demo.poc/commons/core/errors/errors"
	"com.demo.poc/commons/core/validations"
	"github.com/gin-gonic/gin"
	"github.com/go-playground/validator/v10"
)

type ProductRestService struct {
	productService service.ProductService
	validate       *validator.Validate
}

func NewProductRestService(service service.ProductService, validate *validator.Validate) *ProductRestService {
	return &ProductRestService{
		productService: service,
		validate:       validate,
	}
}

func (thisRestService *ProductRestService) FindByCode(context *gin.Context) {
	code := context.Param("code")
	menuOption, err := thisRestService.productService.FindByCode(code)
	if err != nil {
		context.Error(err)
		context.Abort()
		return
	}
	context.JSON(http.StatusOK, menuOption)
}

func (thisRestService *ProductRestService) FindByScope(context *gin.Context) {
	scope := context.Query("scope")

	var productList []response.ProductResponseDto
	var err error

	if scope == "" {
		productList, err = thisRestService.productService.FindAll()
	} else {
		productList, err = thisRestService.productService.FindByScope(scope)
	}

	if err != nil {
		context.Error(err)
		context.Abort()
		return
	}

	context.JSON(http.StatusOK, productList)
}

func (thisRestService *ProductRestService) Save(context *gin.Context) {
	var saveRequest request.ProductSaveRequestDto

	if err := context.ShouldBindJSON(&saveRequest); err != nil {
		context.Error(err)
		context.Abort()
		return
	}

	if err := validations.Validate.Struct(saveRequest); err != nil {
		if constraints, hasConstraints := err.(validator.ValidationErrors); hasConstraints {
			context.Error(coreErrors.NewInvalidFieldError(constraints.Error()))
			context.Abort()
			return
		}
		context.Error(err)
		context.Abort()
		return
	}

	createdProduct, err := thisRestService.productService.Save(saveRequest)

	if err != nil {
		context.Error(err)
		context.Abort()
		return
	}

	context.JSON(http.StatusCreated, createdProduct)
}

func (thisRestService *ProductRestService) Update(context *gin.Context) {
	code := context.Param("code")
	var updateRequest request.ProductUpdateRequestDto

	if err := context.ShouldBindJSON(&updateRequest); err != nil {
		context.Error(err)
		context.Abort()
		return
	}

	if err := validations.Validate.Struct(updateRequest); err != nil {
		if constraints, hasConstraints := err.(validator.ValidationErrors); hasConstraints {
			context.Error(coreErrors.NewInvalidFieldError(constraints.Error()))
			context.Abort()
			return
		}
		context.Error(err)
		context.Abort()
		return
	}

	updatedProduct, err := thisRestService.productService.Update(updateRequest, code)
	if err != nil {
		context.Error(err)
		context.Abort()
		return
	}

	context.JSON(http.StatusOK, updatedProduct)
}

func (thisRestService *ProductRestService) Delete(context *gin.Context) {
	code := context.Param("code")
	err := thisRestService.productService.Delete(code)
	if err != nil {
		context.Error(err)
		context.Abort()
		return
	}
	context.Status(http.StatusNoContent)
}
