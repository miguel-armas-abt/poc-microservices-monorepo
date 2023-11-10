package rest

import (
	"github.com/gin-gonic/gin"
	"menu-v3/application/service"
	"menu-v3/domain/model/request"
	"menu-v3/infrastructure/exception/handler"
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

func (thisRestService *MenuOptionRestService) FindAll(context *gin.Context) {
	menuOptionList, err := thisRestService.menuOptionService.FindAll()
	if err != nil {
		handler.ErrorHandler(context, err)
		return
	}
	context.JSON(http.StatusOK, menuOptionList)
}

func (thisRestService *MenuOptionRestService) FindById(context *gin.Context) {
	id, _ := strconv.ParseUint(context.Param("id"), 10, 64)
	menuOption, err := thisRestService.menuOptionService.FindById(uint(id))
	if err != nil {
		handler.ErrorHandler(context, err)
		return
	}
	context.JSON(http.StatusOK, menuOption)
}

func (thisRestService *MenuOptionRestService) Save(context *gin.Context) {
	var menuOptionRequest request.MenuOptionRequest
	if err := context.ShouldBindJSON(&menuOptionRequest); err != nil {
		handler.ErrorHandler(context, err)
		return
	}

	menuOption, err := thisRestService.menuOptionService.Save(menuOptionRequest)
	if err != nil {
		handler.ErrorHandler(context, err)
		return
	}
	context.JSON(http.StatusCreated, menuOption)
}

func (thisRestService *MenuOptionRestService) Update(context *gin.Context) {
	id, _ := strconv.ParseUint(context.Param("id"), 10, 64)
	var menuOptionRequest request.MenuOptionRequest

	if err := context.ShouldBindJSON(&menuOptionRequest); err != nil {
		handler.ErrorHandler(context, err)
		return
	}

	menuOption, err := thisRestService.menuOptionService.Update(menuOptionRequest, uint(id))
	if err != nil {
		handler.ErrorHandler(context, err)
		return
	}
	context.JSON(http.StatusOK, menuOption)
}

func (thisRestService *MenuOptionRestService) Delete(context *gin.Context) {
	id, _ := strconv.ParseUint(context.Param("id"), 10, 64)
	err := thisRestService.menuOptionService.Delete(uint(id))
	if err != nil {
		handler.ErrorHandler(context, err)
		return
	}
	context.Status(http.StatusNoContent)
}
