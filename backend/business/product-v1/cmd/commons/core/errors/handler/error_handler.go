package handler

import (
	dto "com.demo.poc/cmd/commons/core/errors/dto"

	"net/http"

	"github.com/gin-gonic/gin"
)

const serviceName string = "product-v1"

func ErrorHandler(context *gin.Context, err error) {
	if apiError, ok := err.(dto.ApiError); ok {
		context.JSON(apiError.ApiErrorType.Status, dto.ApiException{
			Type:      apiError.ApiErrorType.Description,
			Message:   apiError.Title,
			ErrorCode: generateErrorCode(&apiError),
		})
		return
	}
	context.JSON(http.StatusInternalServerError, dto.ApiException{
		Type:      "/errors/internal-server-error",
		Message:   err.Error(),
		ErrorCode: "INTERNAL_SERVER_ERROR",
	})
}

func generateErrorCode(apiError *dto.ApiError) string {
	return serviceName + "." + apiError.ApiErrorType.Code + "." + apiError.ErrorCode
}
