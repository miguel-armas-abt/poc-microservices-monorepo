package handler

import (
	dto "com.demo.poc/cmd/commons/core/errors/dto"

	"net/http"

	"github.com/gin-gonic/gin"
)

func ErrorHandler(context *gin.Context, err error) {
	if genericError, isError := err.(dto.GenericError); isError {

		errorResponse := dto.ErrorDto{
			Origin:  genericError.Origin.Code,
			Message: genericError.Message,
			Code:    genericError.Code,
		}

		context.JSON(genericError.HttpStatus, errorResponse)
		return
	}

	defaultResponse := dto.ErrorDto{
		Origin:  dto.Own.Code,
		Message: err.Error(),
		Code:    "Default",
	}

	context.JSON(http.StatusInternalServerError, defaultResponse)
}
