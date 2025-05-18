package service

import repository "com.demo.poc/cmd/entrypoint/products/repository"

type productServiceImpl struct {
	repository repository.ProductRepository
}

func NewProductService(productRepository repository.ProductRepository) ProductService {
	return &productServiceImpl{
		repository: productRepository,
	}
}
