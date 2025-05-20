package service

import (
	"com.demo.poc/cmd/products/dto/request"
	"com.demo.poc/cmd/products/dto/response"
	productErrors "com.demo.poc/cmd/products/errors"
	"com.demo.poc/cmd/products/mapper"
	"com.demo.poc/cmd/products/repository"

	"gorm.io/gorm"
)

type productServiceImpl struct {
	productRepository repository.ProductRepository
}

func NewProductServiceImpl(productRepository repository.ProductRepository) ProductService {
	return &productServiceImpl{
		productRepository: productRepository,
	}
}

func (thisService *productServiceImpl) FindAll(headers map[string]string) ([]response.ProductResponseDto, error) {
	productListFound, err := thisService.productRepository.FindAll()
	if err != nil {
		return nil, err
	}
	return mapper.EntityListToResponseList(productListFound), nil
}

func (thisService *productServiceImpl) FindByCode(headers map[string]string, code string) (*response.ProductResponseDto, error) {
	productFound, err := thisService.productRepository.FindByCode(code)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, productErrors.ProductNotFound
		}
		return nil, err
	}
	product := mapper.EntityToResponse(*productFound)
	return &product, nil
}

func (thisService *productServiceImpl) FindByScope(headers map[string]string, scope string) ([]response.ProductResponseDto, error) {
	productListFound, err := thisService.productRepository.FindByScope(scope)
	if err != nil {
		return nil, err
	}

	return mapper.EntityListToResponseList(productListFound), nil
}

func (thisService *productServiceImpl) Save(headers map[string]string, productRequest request.ProductSaveRequestDto) (*response.ProductResponseDto, error) {
	productToSave := mapper.RequestToEntity(productRequest)
	if err := thisService.productRepository.Save(&productToSave); err != nil {
		return nil, err
	}
	product := mapper.EntityToResponse(productToSave)
	return &product, nil
}

func (thisService *productServiceImpl) Update(headers map[string]string, productRequest request.ProductUpdateRequestDto, code string) (*response.ProductResponseDto, error) {
	productFound, err := thisService.productRepository.FindByCode(code)
	if err != nil {
		return nil, err
	}
	mapper.UpdateRequestToEntity(productRequest, productFound)

	if err = thisService.productRepository.Save(productFound); err != nil {
		return nil, err
	}

	product := mapper.EntityToResponse(*productFound)
	return &product, nil
}

func (thisService *productServiceImpl) Delete(headers map[string]string, code string) error {
	return thisService.productRepository.Delete(code)
}
