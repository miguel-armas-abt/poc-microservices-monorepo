package mapper

import (
	"github.com/mitchellh/mapstructure"
	"product-v1/domain/model/request"
	"product-v1/domain/model/response"
	"product-v1/infrastructure/repository/entity"
)

func RequestToEntity(productRequest request.ProductRequest) entity.ProductEntity {
	var productEntity entity.ProductEntity
	mapstructure.Decode(productRequest, &productEntity)
	productEntity.IsActive = true
	return productEntity
}

func UpdateRequestToEntity(productRequest request.ProductRequest, productEntity *entity.ProductEntity) {
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
