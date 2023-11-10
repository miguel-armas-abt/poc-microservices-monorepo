package mapper

import (
	"github.com/mitchellh/mapstructure"
	"menu-v3/domain/model/request"
	"menu-v3/domain/model/response"
	"menu-v3/infrastructure/repository/entity"
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
