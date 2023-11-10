package exception

import (
	"menu-v3/infrastructure/exception/model"
)

var (
	ErrorMenuOptionNotFound = model.ApiError{
		ApiErrorType: model.NoData,
		Title:        "Menu option not found",
		ErrorCode:    "ERROR0001",
	}
)
