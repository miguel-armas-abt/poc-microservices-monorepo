package repository

import (
	"gorm.io/gorm"
)

func NewProductRepository(db *gorm.DB) ProductRepository {
	return &productRepositoryImpl{db: db}
}
