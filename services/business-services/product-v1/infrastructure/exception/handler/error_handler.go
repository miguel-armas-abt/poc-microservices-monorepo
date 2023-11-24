package handler

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"product-v1/infrastructure/exception/model"
)

const serviceName string = "product-v1"

func ErrorHandler(context *gin.Context, err error) {
	if apiError, ok := err.(model.ApiError); ok {
		context.JSON(apiError.ApiErrorType.Status, model.ApiException{
			Type:      apiError.ApiErrorType.Description,
			Message:   apiError.Title,
			ErrorCode: generateErrorCode(&apiError),
		})
		return
	}
	context.JSON(http.StatusInternalServerError, model.ApiException{
		Type:      "/errors/internal-server-error",
		Message:   err.Error(),
		ErrorCode: "INTERNAL_SERVER_ERROR",
	})
}

func generateErrorCode(apiError *model.ApiError) string {
	return serviceName + "." + apiError.ApiErrorType.Code + "." + apiError.ErrorCode
}
