package mapper

import (
	"com.demo.poc/cmd/entrypoint/products/dto/request"
	"com.demo.poc/cmd/entrypoint/products/dto/response"
	"com.demo.poc/cmd/entrypoint/products/repository/entity"
	"github.com/mitchellh/mapstructure"
)

func RequestToEntity(productRequest request.ProductSaveRequest) entity.ProductEntity {
	var productEntity entity.ProductEntity
	mapstructure.Decode(productRequest, &productEntity)
	productEntity.IsActive = true
	return productEntity
}

func UpdateRequestToEntity(productRequest request.ProductUpdateRequest, productEntity *entity.ProductEntity) {
	mapstructure.Decode(productRequest, productEntity)
}

func EntityToResponse(productEntity entity.ProductEntity) response.ProductResponse {
	var productResponse response.ProductResponse
	mapstructure.Decode(productEntity, &productResponse)
	return productResponse
}

func EntityListToResponseList(productEntities []entity.ProductEntity) []response.ProductResponse {
	productResponses := make([]response.ProductResponse, len(productEntities))
	for i, productEntity := range productEntities {
		productResponses[i] = EntityToResponse(productEntity)
	}
	return productResponses
}
