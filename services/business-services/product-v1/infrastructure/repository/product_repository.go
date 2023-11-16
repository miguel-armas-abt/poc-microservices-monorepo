package repository

import (
	"product-v1/infrastructure/repository/entity"
)

type ProductRepository interface {
	FindAll() ([]entity.ProductEntity, error)
	FindByScope(scope string) ([]entity.ProductEntity, error)
	FindById(id uint) (*entity.ProductEntity, error)
	Save(product *entity.ProductEntity) error
	Delete(id uint) error
}
