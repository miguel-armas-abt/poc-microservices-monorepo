package handler

import (
	dto "com.demo.poc/pck/core/errors/dto"
	"com.demo.poc/pck/core/errors/selector"

	"net/http"

	"github.com/gin-gonic/gin"
)

type ErrorHandler struct {
	selector *selector.ResponseErrorSelector
}

func (errorHandler *ErrorHandler) Handler() gin.HandlerFunc {
	return func(context *gin.Context) {
		context.Next()

		if len(context.Errors) > 0 {
			err := context.Errors[0].Err
			if genericError, isGenericError := err.(dto.GenericError); isGenericError {
				response := errorHandler.selector.ToErrorDto(err)
				context.JSON(genericError.HttpStatus, response)
				return
			}

			resp := errorHandler.selector.ToErrorDto(err)
			context.JSON(http.StatusInternalServerError, resp)
		}
	}
}
