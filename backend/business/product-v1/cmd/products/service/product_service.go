package service

import (
	"com.demo.poc/cmd/products/dto/request"
	"com.demo.poc/cmd/products/dto/response"
)

type ProductService interface {
	FindAll(headers map[string]string) ([]response.ProductResponseDto, error)

	FindByCode(headers map[string]string, code string) (*response.ProductResponseDto, error)

	FindByScope(headers map[string]string, scope string) ([]response.ProductResponseDto, error)

	Save(headers map[string]string, request request.ProductSaveRequestDto) (*response.ProductResponseDto, error)

	Update(headers map[string]string, request request.ProductUpdateRequestDto, code string) (*response.ProductResponseDto, error)

	Delete(headers map[string]string, code string) error
}
