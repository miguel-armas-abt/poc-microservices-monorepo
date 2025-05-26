package service

import (
	"context"
	"poc/cmd/products/dto/request"
	"poc/cmd/products/dto/response"
)

type ProductService interface {
	FindAll(
		ctx context.Context,
		headers map[string]string) ([]response.ProductResponseDto, error)

	FindByCode(
		ctx context.Context,
		headers map[string]string, code string) (*response.ProductResponseDto, error)

	FindByScope(
		ctx context.Context,
		headers map[string]string, scope string) ([]response.ProductResponseDto, error)

	Save(
		ctx context.Context,
		headers map[string]string, request request.ProductSaveRequestDto) (*response.ProductResponseDto, error)

	Update(
		ctx context.Context,
		headers map[string]string, request request.ProductUpdateRequestDto, code string) (*response.ProductResponseDto, error)

	Delete(
		ctx context.Context,
		headers map[string]string, code string) error
}
