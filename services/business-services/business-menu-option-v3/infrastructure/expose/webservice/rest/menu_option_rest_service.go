package rest

import (
	"business-menu-option-v3/application/service"
	"business-menu-option-v3/domain/model/request"
	"business-menu-option-v3/infrastructure/exception/handler"
	"github.com/gin-gonic/gin"
	"net/http"
	"strconv"
)

type MenuOptionRestService struct {
	menuOptionService service.MenuOptionService
}

func NewMenuOptionRestService(service service.MenuOptionService) *MenuOptionRestService {
	return &MenuOptionRestService{
		menuOptionService: service,
	}
}

func (restService *MenuOptionRestService) FindAll(context *gin.Context) {
	menuOptionList, err := restService.menuOptionService.FindAll()
	if err != nil {
		handler.ErrorHandler(context, err)
		return
	}
	context.JSON(http.StatusOK, menuOptionList)
}

func (restService *MenuOptionRestService) FindById(context *gin.Context) {
	id, _ := strconv.ParseUint(context.Param("id"), 10, 64)
	menuOption, err := restService.menuOptionService.FindById(uint(id))
	if err != nil {
		handler.ErrorHandler(context, err)
		return
	}
	context.JSON(http.StatusOK, menuOption)
}

func (restService *MenuOptionRestService) Save(context *gin.Context) {
	var menuOptionRequest request.MenuOptionRequest
	if err := context.ShouldBindJSON(&menuOptionRequest); err != nil {
		handler.ErrorHandler(context, err)
		return
	}

	menuOption, err := restService.menuOptionService.Save(menuOptionRequest)
	if err != nil {
		handler.ErrorHandler(context, err)
		return
	}
	context.JSON(http.StatusCreated, menuOption)
}

func (restService *MenuOptionRestService) Update(context *gin.Context) {
	id, _ := strconv.ParseUint(context.Param("id"), 10, 64)
	var menuOptionRequest request.MenuOptionRequest

	if err := context.ShouldBindJSON(&menuOptionRequest); err != nil {
		handler.ErrorHandler(context, err)
		return
	}

	menuOption, err := restService.menuOptionService.Update(menuOptionRequest, uint(id))
	if err != nil {
		handler.ErrorHandler(context, err)
		return
	}
	context.JSON(http.StatusOK, menuOption)
}

func (restService *MenuOptionRestService) Delete(context *gin.Context) {
	id, _ := strconv.ParseUint(context.Param("id"), 10, 64)
	err := restService.menuOptionService.Delete(uint(id))
	if err != nil {
		handler.ErrorHandler(context, err)
		return
	}
	context.Status(http.StatusNoContent)
}
