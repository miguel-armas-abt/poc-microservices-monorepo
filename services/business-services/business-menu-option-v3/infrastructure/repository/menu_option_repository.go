package repository

import (
	"business-menu-option-v3/infrastructure/repository/entity"
	"gorm.io/gorm"
)

type MenuOptionRepository interface {
	FindAll() ([]entity.MenuOption, error)
	FindById(id uint) (*entity.MenuOption, error)
	Save(menuOption *entity.MenuOption) error
	Delete(id uint) error
}

type menuOptionRepository struct {
	db *gorm.DB
}

func NewMenuOptionRepository(db *gorm.DB) MenuOptionRepository {
	return &menuOptionRepository{db: db}
}

func (repository *menuOptionRepository) FindAll() ([]entity.MenuOption, error) {
	var menuOptionList []entity.MenuOption
	if err := repository.db.Find(&menuOptionList).Error; err != nil {
		return nil, err
	}
	return menuOptionList, nil
}

func (repository *menuOptionRepository) FindById(id uint) (*entity.MenuOption, error) {
	var menuOption entity.MenuOption
	if err := repository.db.First(&menuOption, id).Error; err != nil {
		return nil, err
	}
	return &menuOption, nil
}

func (repository *menuOptionRepository) Save(menuOption *entity.MenuOption) error {
	return repository.db.Save(menuOption).Error
}

func (repository *menuOptionRepository) Delete(id uint) error {
	return repository.db.Delete(id).Error
}
