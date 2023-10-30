package service

import (
	"business-menu-option-v3/domain/exception"
	"business-menu-option-v3/domain/model/request"
	"business-menu-option-v3/domain/model/response"
	"business-menu-option-v3/infrastructure/mapper"
	"business-menu-option-v3/infrastructure/repository"
	"gorm.io/gorm"
)

type menuOptionServiceImpl struct {
	repository repository.MenuOptionRepository
}

func NewMenuOptionService(menuOptionRepository repository.MenuOptionRepository) MenuOptionService {
	return &menuOptionServiceImpl{
		repository: menuOptionRepository,
	}
}

func (thisService *menuOptionServiceImpl) FindAll() ([]response.MenuOptionResponse, error) {
	menuOptionList, err := thisService.repository.FindAll()
	if err != nil {
		return nil, err
	}
	menuOptionResponseList := make([]response.MenuOptionResponse, len(menuOptionList))
	for i, menuOption := range menuOptionList {
		menuOptionResponseList[i] = mapper.EntityToResponse(menuOption)
	}
	return menuOptionResponseList, nil
}

func (thisService *menuOptionServiceImpl) FindById(id uint) (*response.MenuOptionResponse, error) {
	menuOption, err := thisService.repository.FindById(id)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, exception.ErrorMenuOptionNotFound
		}
		return nil, err
	}
	menuOptionResponse := mapper.EntityToResponse(*menuOption)
	return &menuOptionResponse, nil
}

func (thisService *menuOptionServiceImpl) Save(request request.MenuOptionRequest) (*response.MenuOptionResponse, error) {
	menuOption := mapper.RequestToEntity(request)
	if err := thisService.repository.Save(&menuOption); err != nil {
		return nil, err
	}
	menuOptionResponse := mapper.EntityToResponse(menuOption)
	return &menuOptionResponse, nil
}

func (thisService *menuOptionServiceImpl) Update(request request.MenuOptionRequest, id uint) (*response.MenuOptionResponse, error) {
	menuOption, err := thisService.repository.FindById(id)
	if err != nil {
		return nil, err
	}
	mapper.UpdateRequestToEntity(request, menuOption)

	if err = thisService.repository.Save(menuOption); err != nil {
		return nil, err
	}

	menuOptionResponse := mapper.EntityToResponse(*menuOption)
	return &menuOptionResponse, nil
}

func (thisService *menuOptionServiceImpl) Delete(id uint) error {
	return thisService.repository.Delete(id)
}
