package service

import (
	"business-menu-option-v3/domain/model/request"
	"business-menu-option-v3/domain/model/response"
	"business-menu-option-v3/infrastructure/mapper"
	"business-menu-option-v3/infrastructure/repository"
)

type MenuOptionService interface {
	FindAll() ([]response.MenuOptionResponse, error)
	FindById(id uint) (*response.MenuOptionResponse, error)
	Save(request request.MenuOptionRequest) (*response.MenuOptionResponse, error)
	Update(request request.MenuOptionRequest, id uint) (*response.MenuOptionResponse, error)
	Delete(id uint) error
}

type menuOptionService struct {
	repository repository.MenuOptionRepository
}

func NewMenuOptionService(menuOptionRepository repository.MenuOptionRepository) MenuOptionService {
	return &menuOptionService{repository: menuOptionRepository}
}

func (service *menuOptionService) FindAll() ([]response.MenuOptionResponse, error) {
	menuOptionList, err := service.repository.FindAll()
	if err != nil {
		return nil, err
	}
	menuOptionResponseList := make([]response.MenuOptionResponse, len(menuOptionList))
	for i, menuOption := range menuOptionList {
		menuOptionResponseList[i] = mapper.EntityToResponse(menuOption)
	}
	return menuOptionResponseList, nil
}

func (service *menuOptionService) FindById(id uint) (*response.MenuOptionResponse, error) {
	menuOption, err := service.repository.FindById(id)
	if err != nil {
		return nil, err
	}
	menuOptionResponse := mapper.EntityToResponse(*menuOption)
	return &menuOptionResponse, nil
}

func (service *menuOptionService) Save(request request.MenuOptionRequest) (*response.MenuOptionResponse, error) {
	menuOption := mapper.RequestToEntity(request)
	err := service.repository.Save(&menuOption)
	if err != nil {
		return nil, err
	}
	menuOptionResponse := mapper.EntityToResponse(menuOption)
	return &menuOptionResponse, nil
}

func (service *menuOptionService) Update(request request.MenuOptionRequest, id uint) (*response.MenuOptionResponse, error) {
	menuOption, err := service.repository.FindById(id)
	if err != nil {
		return nil, err
	}

	mapper.UpdateRequestToEntity(request, menuOption)

	err = service.repository.Save(menuOption)
	if err != nil {
		return nil, err
	}

	menuOptionResponse := mapper.EntityToResponse(*menuOption)
	return &menuOptionResponse, nil
}

func (service *menuOptionService) Delete(id uint) error {
	return service.repository.Delete(id)
}
