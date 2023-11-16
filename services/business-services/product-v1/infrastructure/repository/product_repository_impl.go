package repository

import (
	"gorm.io/gorm"
	"product-v1/infrastructure/repository/entity"
)

type productRepositoryImpl struct {
	db *gorm.DB
}

func NewProductRepository(db *gorm.DB) ProductRepository {
	// create an instance of the implementation of ProductRepository and return a pointer to the interface
	return &productRepositoryImpl{
		db: db,
	}
}

func (thisRepository *productRepositoryImpl) FindAll() ([]entity.ProductEntity, error) {
	var productList []entity.ProductEntity
	if err := thisRepository.db.Find(&productList).Error; err != nil {
		return nil, err
	}
	return productList, nil
}

func (thisRepository *productRepositoryImpl) FindById(id uint) (*entity.ProductEntity, error) {
	var product entity.ProductEntity
	if err := thisRepository.db.First(&product, id).Error; err != nil {
		return nil, err
	}
	return &product, nil
}

func (thisRepository *productRepositoryImpl) FindByScope(scope string) ([]entity.ProductEntity, error) {
	var productList []entity.ProductEntity
	if err := thisRepository.db.Where("scope=?", scope).Find(&productList).Error; err != nil {
		return nil, err
	}
	return productList, nil
}

func (thisRepository *productRepositoryImpl) Save(product *entity.ProductEntity) error {
	return thisRepository.db.Save(product).Error
}

func (thisRepository *productRepositoryImpl) Delete(id uint) error {
	var product entity.ProductEntity
	return thisRepository.db.Where("id = ?", id).Delete(&product).Error
}
