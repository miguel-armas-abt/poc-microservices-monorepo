package service

import repository "com.demo.poc/cmd/products/repository"

func NewProductService(productRepository repository.ProductRepository) ProductService {
	return &productServiceImpl{
		repository: productRepository,
	}
}
