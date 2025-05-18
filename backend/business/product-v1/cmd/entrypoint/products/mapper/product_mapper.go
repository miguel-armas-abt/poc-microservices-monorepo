package mapper

import (
	"com.demo.poc/cmd/entrypoint/products/dto/request"
	"com.demo.poc/cmd/entrypoint/products/dto/response"
	"com.demo.poc/cmd/entrypoint/products/repository/entity"
	"github.com/mitchellh/mapstructure"
)

func RequestToEntity(productRequest request.ProductSaveRequestDto) entity.ProductEntity {
	var productEntity entity.ProductEntity
	mapstructure.Decode(productRequest, &productEntity)
	productEntity.IsActive = true
	return productEntity
}

func UpdateRequestToEntity(productRequest request.ProductUpdateRequestDto, productEntity *entity.ProductEntity) {
	mapstructure.Decode(productRequest, productEntity)
}

func EntityToResponse(productEntity entity.ProductEntity) response.ProductResponseDto {
	var productResponse response.ProductResponseDto
	mapstructure.Decode(productEntity, &productResponse)
	return productResponse
}

func EntityListToResponseList(productEntities []entity.ProductEntity) []response.ProductResponseDto {
	productResponses := make([]response.ProductResponseDto, len(productEntities))
	for i, productEntity := range productEntities {
		productResponses[i] = EntityToResponse(productEntity)
	}
	return productResponses
}
