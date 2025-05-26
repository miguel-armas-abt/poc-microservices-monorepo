package repository

import (
	"poc/cmd/products/repository/entity"

	"gorm.io/gorm"
)

type productRepositoryImpl struct {
	db *gorm.DB
}

func NewProductRepositoryImpl(db *gorm.DB) ProductRepository {
	return &productRepositoryImpl{db: db}
}

func (repository *productRepositoryImpl) FindAll() ([]entity.ProductEntity, error) {
	var productList []entity.ProductEntity
	if err := repository.db.Find(&productList).Error; err != nil {
		return nil, err
	}
	return productList, nil
}

func (repository *productRepositoryImpl) FindByCode(code string) (*entity.ProductEntity, error) {
	var product entity.ProductEntity
	if err := repository.db.Where("code=?", code).First(&product).Error; err != nil {
		return nil, err
	}
	return &product, nil
}

func (repository *productRepositoryImpl) FindByScope(scope string) ([]entity.ProductEntity, error) {
	var productList []entity.ProductEntity
	if err := repository.db.Where("scope=?", scope).Find(&productList).Error; err != nil {
		return nil, err
	}
	return productList, nil
}

func (repository *productRepositoryImpl) Save(product *entity.ProductEntity) error {
	return repository.db.Save(product).Error
}

func (repository *productRepositoryImpl) Delete(code string) error {
	var product entity.ProductEntity
	return repository.db.Where("code = ?", code).Delete(&product).Error
}
