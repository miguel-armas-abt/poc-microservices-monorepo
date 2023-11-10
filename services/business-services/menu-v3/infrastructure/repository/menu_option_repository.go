package repository

import (
	"menu-v3/infrastructure/repository/entity"
)

type MenuOptionRepository interface {
	FindAll() ([]entity.MenuOption, error)
	FindById(id uint) (*entity.MenuOption, error)
	Save(menuOption *entity.MenuOption) error
	Delete(id uint) error
}
