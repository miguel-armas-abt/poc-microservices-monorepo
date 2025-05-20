package repository

import (
	"com.demo.poc/cmd/products/repository/entity"
	"gorm.io/gorm"
)

type productRepositoryImpl struct {
	db *gorm.DB
}

func NewProductRepositoryImpl(db *gorm.DB) ProductRepository {
	return &productRepositoryImpl{db: db}
}

func (productRepository *productRepositoryImpl) FindAll() ([]entity.ProductEntity, error) {
	var productList []entity.ProductEntity
	if err := productRepository.db.Find(&productList).Error; err != nil {
		return nil, err
	}
	return productList, nil
}

func (productRepository *productRepositoryImpl) FindByCode(code string) (*entity.ProductEntity, error) {
	var product entity.ProductEntity
	if err := productRepository.db.Where("code=?", code).First(&product).Error; err != nil {
		return nil, err
	}
	return &product, nil
}

func (productRepository *productRepositoryImpl) FindByScope(scope string) ([]entity.ProductEntity, error) {
	var productList []entity.ProductEntity
	if err := productRepository.db.Where("scope=?", scope).Find(&productList).Error; err != nil {
		return nil, err
	}
	return productList, nil
}

func (thisRepository *productRepositoryImpl) Save(product *entity.ProductEntity) error {
	return thisRepository.db.Save(product).Error
}

func (thisRepository *productRepositoryImpl) Delete(code string) error {
	var product entity.ProductEntity
	return thisRepository.db.Where("code = ?", code).Delete(&product).Error
}
