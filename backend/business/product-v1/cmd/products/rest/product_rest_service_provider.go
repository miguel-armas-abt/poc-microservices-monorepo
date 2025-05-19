package rest

import (
	"com.demo.poc/cmd/products/service"
	"github.com/go-playground/validator/v10"
)

func NewProductRestService(service service.ProductService, validate *validator.Validate) *ProductRestService {
	return &ProductRestService{
		productService: service,
		validate:       validate,
	}
}
