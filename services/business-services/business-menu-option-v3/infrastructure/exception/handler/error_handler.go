package handler

import (
	"business-menu-option-v3/infrastructure/exception/model"
	"github.com/gin-gonic/gin"
	"net/http"
)

func ErrorHandler(context *gin.Context, err error) {
	var apiException model.APIException

	// Add logic to handle different types of errors and map them to the appropriate APIException
	// For example, you can use if statements or switch cases.
	context.JSON(http.StatusInternalServerError, apiException)
}
