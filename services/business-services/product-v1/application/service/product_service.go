package service

import (
	"product-v1/domain/model/request"
	"product-v1/domain/model/response"
)

type ProductService interface {
	FindAll() ([]response.ProductResponse, error)
	FindByCode(code string) (*response.ProductResponse, error)
	FindByScope(scope string) ([]response.ProductResponse, error)
	Save(request request.ProductSaveRequest) (*response.ProductResponse, error)
	Update(request request.ProductUpdateRequest, code string) (*response.ProductResponse, error)
	Delete(code string) error
}
