package service

import (
	"menu-v3/domain/model/request"
	"menu-v3/domain/model/response"
)

type MenuOptionService interface {
	FindAll() ([]response.MenuOptionResponse, error)
	FindById(id uint) (*response.MenuOptionResponse, error)
	Save(request request.MenuOptionRequest) (*response.MenuOptionResponse, error)
	Update(request request.MenuOptionRequest, id uint) (*response.MenuOptionResponse, error)
	Delete(id uint) error
}
