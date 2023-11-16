package service

import (
	"product-v1/domain/model/request"
	"product-v1/domain/model/response"
)

type ProductService interface {
	FindAll() ([]response.ProductResponse, error)
	FindById(id uint) (*response.ProductResponse, error)
	FindByScope(scope string) ([]response.ProductResponse, error)
	Save(request request.ProductRequest) (*response.ProductResponse, error)
	Update(request request.ProductRequest, id uint) (*response.ProductResponse, error)
	Delete(id uint) error
}
