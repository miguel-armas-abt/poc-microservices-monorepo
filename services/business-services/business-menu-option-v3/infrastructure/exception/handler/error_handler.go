package handler

import (
	"business-menu-option-v3/infrastructure/exception/model"
	"github.com/gin-gonic/gin"
	"net/http"
	"strings"
)

const serviceNumber string = "bs.005"

func ErrorHandler(context *gin.Context, err error) {
	if apiError, ok := err.(model.ApiError); ok {
		context.JSON(apiError.ApiErrorType.Status, model.ApiException{
			Type:      apiError.ApiErrorType.Description,
			Title:     apiError.Title,
			ErrorCode: generateErrorCode(&apiError),
		})
		return
	}
	context.JSON(http.StatusInternalServerError, model.ApiException{
		Type:      "/errors/internal-server-error",
		Title:     "Internal server error",
		ErrorCode: "INTERNAL_SERVER_ERROR",
	})
}

func generateErrorCode(apiError *model.ApiError) string {
	return serviceNumber + "." + apiError.ApiErrorType.Code + "." + strings.ToLower(apiError.ErrorCode)
}
