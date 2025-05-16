package service

import (
	exception "com.demo.poc/cmd/commons/custom/errors"
	"com.demo.poc/cmd/entrypoint/products/dto/request"
	"com.demo.poc/cmd/entrypoint/products/dto/response"
	"com.demo.poc/cmd/entrypoint/products/mapper"
	repository "com.demo.poc/cmd/entrypoint/products/repository"

	"gorm.io/gorm"
)

type productServiceImpl struct {
	repository repository.ProductRepository
}

func NewProductService(productRepository repository.ProductRepository) ProductService {
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
