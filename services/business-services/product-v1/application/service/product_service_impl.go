package service

import (
	"gorm.io/gorm"
	"product-v1/domain/exception"
	"product-v1/domain/model/request"
	"product-v1/domain/model/response"
	"product-v1/infrastructure/mapper"
	"product-v1/infrastructure/repository/database"
)

type productServiceImpl struct {
	repository database.ProductRepository
}

func NewProductService(productRepository database.ProductRepository) ProductService {
	return &productServiceImpl{
		repository: productRepository,
	}
}

func (thisService *productServiceImpl) FindAll() ([]response.ProductResponse, error) {
	productListFound, err := thisService.repository.FindAll()
	if err != nil {
		return nil, err
	}
	return mapper.EntityListToResponseList(productListFound), nil
}

func (thisService *productServiceImpl) FindByCode(code string) (*response.ProductResponse, error) {
	productFound, err := thisService.repository.FindByCode(code)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, exception.ProductNotFound
		}
		return nil, err
	}
	product := mapper.EntityToResponse(*productFound)
	return &product, nil
}

func (thisService *productServiceImpl) FindByScope(scope string) ([]response.ProductResponse, error) {
	productListFound, err := thisService.repository.FindByScope(scope)
	if err != nil {
		return nil, err
	}

	return mapper.EntityListToResponseList(productListFound), nil
}

func (thisService *productServiceImpl) Save(productRequest request.ProductSaveRequest) (*response.ProductResponse, error) {
	productToSave := mapper.RequestToEntity(productRequest)
	if err := thisService.repository.Save(&productToSave); err != nil {
		return nil, err
	}
	product := mapper.EntityToResponse(productToSave)
	return &product, nil
}

func (thisService *productServiceImpl) Update(productRequest request.ProductUpdateRequest, code string) (*response.ProductResponse, error) {
	productFound, err := thisService.repository.FindByCode(code)
	if err != nil {
		return nil, err
	}
	mapper.UpdateRequestToEntity(productRequest, productFound)

	if err = thisService.repository.Save(productFound); err != nil {
		return nil, err
	}

	product := mapper.EntityToResponse(*productFound)
	return &product, nil
}

func (thisService *productServiceImpl) Delete(code string) error {
	return thisService.repository.Delete(code)
}
