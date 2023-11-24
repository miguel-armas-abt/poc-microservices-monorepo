package repository

import (
	"product-v1/infrastructure/repository/entity"
)

type ProductRepository interface {
	FindAll() ([]entity.ProductEntity, error)
	FindByScope(scope string) ([]entity.ProductEntity, error)
	FindByCode(code string) (*entity.ProductEntity, error)
	Save(product *entity.ProductEntity) error
	Delete(code string) error
}
