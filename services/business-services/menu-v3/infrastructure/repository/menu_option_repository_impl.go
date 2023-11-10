package repository

import (
	"gorm.io/gorm"
	"menu-v3/infrastructure/repository/entity"
)

type menuOptionRepositoryImpl struct {
	db *gorm.DB
}

func NewMenuOptionRepository(db *gorm.DB) MenuOptionRepository {
	// create an instance of the implementation of MenuOptionRepository and return a pointer to the interface
	return &menuOptionRepositoryImpl{
		db: db,
	}
}

func (thisRepository *menuOptionRepositoryImpl) FindAll() ([]entity.MenuOption, error) {
	var menuOptionList []entity.MenuOption
	if err := thisRepository.db.Find(&menuOptionList).Error; err != nil {
		return nil, err
	}
	return menuOptionList, nil
}

func (thisRepository *menuOptionRepositoryImpl) FindById(id uint) (*entity.MenuOption, error) {
	var menuOption entity.MenuOption
	if err := thisRepository.db.First(&menuOption, id).Error; err != nil {
		return nil, err
	}
	return &menuOption, nil
}

func (thisRepository *menuOptionRepositoryImpl) Save(menuOption *entity.MenuOption) error {
	return thisRepository.db.Save(menuOption).Error
}

func (thisRepository *menuOptionRepositoryImpl) Delete(id uint) error {
	return thisRepository.db.Delete(id).Error
}
