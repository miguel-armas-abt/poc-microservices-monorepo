package handler

import (
	"github.com/gin-gonic/gin"
	"menu-v3/infrastructure/exception/model"
	"net/http"
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
	return apiError.ApiErrorType.Code + "." + apiError.ErrorCode
}
