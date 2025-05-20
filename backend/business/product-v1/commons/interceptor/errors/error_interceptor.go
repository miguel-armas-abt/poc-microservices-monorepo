package errors

import (
	errorDto "com.demo.poc/commons/errors/dto"
	errorSelector "com.demo.poc/commons/errors/selector"

	"net/http"

	"github.com/gin-gonic/gin"
)

type ErrorInterceptor struct {
	selector *errorSelector.ResponseErrorSelector
}

func NewErrorInterceptor(selector *errorSelector.ResponseErrorSelector) *ErrorInterceptor {
	return &ErrorInterceptor{selector: selector}
}

func (errorInterceptor *ErrorInterceptor) InterceptError() gin.HandlerFunc {
	return func(context *gin.Context) {
		context.Next()

		if len(context.Errors) > 0 {
			err := context.Errors[0].Err
			if genericError, isGenericError := err.(errorDto.GenericError); isGenericError {
				response := errorInterceptor.selector.ToErrorDto(err)
				context.JSON(genericError.HttpStatus, response)
				return
			}

			resp := errorInterceptor.selector.ToErrorDto(err)
			context.JSON(http.StatusInternalServerError, resp)
		}
	}
}
