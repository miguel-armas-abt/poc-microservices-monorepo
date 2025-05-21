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

func NewProductServiceImpl(
	productRepository repository.ProductRepository) ProductService {

	return &productServiceImpl{
		productRepository: productRepository,
	}
}

func (service *productServiceImpl) FindAll(headers map[string]string) ([]response.ProductResponseDto, error) {
	productListFound, err := service.productRepository.FindAll()
	if err != nil {
		return nil, err
	}
	return mapper.EntityListToResponseList(productListFound), nil
}

func (service *productServiceImpl) FindByCode(headers map[string]string, code string) (*response.ProductResponseDto, error) {
	productFound, err := service.productRepository.FindByCode(code)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, productErrors.ProductNotFound
		}
		return nil, err
	}
	product := mapper.EntityToResponse(*productFound)
	return &product, nil
}

func (service *productServiceImpl) FindByScope(headers map[string]string, scope string) ([]response.ProductResponseDto, error) {
	productListFound, err := service.productRepository.FindByScope(scope)
	if err != nil {
		return nil, err
	}

	return mapper.EntityListToResponseList(productListFound), nil
}

func (service *productServiceImpl) Save(headers map[string]string, productRequest request.ProductSaveRequestDto) (*response.ProductResponseDto, error) {
	productToSave := mapper.RequestToEntity(productRequest)
	if err := service.productRepository.Save(&productToSave); err != nil {
		return nil, err
	}
	product := mapper.EntityToResponse(productToSave)
	return &product, nil
}

func (service *productServiceImpl) Update(headers map[string]string, productRequest request.ProductUpdateRequestDto, code string) (*response.ProductResponseDto, error) {
	productFound, err := service.productRepository.FindByCode(code)
	if err != nil {
		return nil, err
	}
	mapper.UpdateRequestToEntity(productRequest, productFound)

	if err = service.productRepository.Save(productFound); err != nil {
		return nil, err
	}

	product := mapper.EntityToResponse(*productFound)
	return &product, nil
}

func (service *productServiceImpl) Delete(headers map[string]string, code string) error {
	return service.productRepository.Delete(code)
}
