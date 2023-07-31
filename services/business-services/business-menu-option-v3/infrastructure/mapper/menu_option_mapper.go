package mapper

import (
	"business-menu-option-v3/domain/model/request"
	"business-menu-option-v3/domain/model/response"
	"business-menu-option-v3/infrastructure/repository/entity"
	"github.com/mitchellh/mapstructure"
)

func RequestToEntity(request request.MenuOptionRequest) entity.MenuOption {
	var menuOption entity.MenuOption
	mapstructure.Decode(request, &menuOption)
	return menuOption
}

func UpdateRequestToEntity(request request.MenuOptionRequest, menuOption *entity.MenuOption) {
	mapstructure.Decode(request, menuOption)
}

func EntityToResponse(menuOption entity.MenuOption) response.MenuOptionResponse {
	var response response.MenuOptionResponse
	mapstructure.Decode(menuOption, &response)
	return response
}
