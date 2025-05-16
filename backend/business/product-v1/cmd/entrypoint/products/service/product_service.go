package service

import (
	"com.demo.poc/cmd/entrypoint/products/dto/request"
	"com.demo.poc/cmd/entrypoint/products/dto/response"
)

type ProductService interface {
	FindAll() ([]response.ProductResponse, error)
	FindByCode(code string) (*response.ProductResponse, error)
	FindByScope(scope string) ([]response.ProductResponse, error)
	Save(request request.ProductSaveRequest) (*response.ProductResponse, error)
	Update(request request.ProductUpdateRequest, code string) (*response.ProductResponse, error)
	Delete(code string) error
}
