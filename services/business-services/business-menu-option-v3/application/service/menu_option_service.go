package service

import (
	"business-menu-option-v3/domain/model/request"
	"business-menu-option-v3/domain/model/response"
)

type MenuOptionService interface {
	FindAll() ([]response.MenuOptionResponse, error)
	FindById(id uint) (*response.MenuOptionResponse, error)
	Save(request request.MenuOptionRequest) (*response.MenuOptionResponse, error)
	Update(request request.MenuOptionRequest, id uint) (*response.MenuOptionResponse, error)
	Delete(id uint) error
}
