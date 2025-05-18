package config

import (
	repository "com.demo.poc/entrypoint/products/repository"
	"gorm.io/gorm"
)

func NewProductRepository(db *gorm.DB) repository.ProductRepository {
	// create an instance of the implementation of ProductRepository and return a pointer to the interface
	return &productRepositoryImpl{
		db: db,
	}
}
