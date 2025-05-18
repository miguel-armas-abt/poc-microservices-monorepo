package service

import (
	"com.demo.poc/cmd/entrypoint/products/dto/request"
	"com.demo.poc/cmd/entrypoint/products/dto/response"
)

type ProductService interface {
	FindAll() ([]response.ProductResponseDto, error)
	FindByCode(code string) (*response.ProductResponseDto, error)
	FindByScope(scope string) ([]response.ProductResponseDto, error)
	Save(request request.ProductSaveRequestDto) (*response.ProductResponseDto, error)
	Update(request request.ProductUpdateRequestDto, code string) (*response.ProductResponseDto, error)
	Delete(code string) error
}
