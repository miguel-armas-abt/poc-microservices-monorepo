package exception

import (
	"product-v1/infrastructure/exception/model"
)

var (
	ProductNotFound = model.ApiError{
		ApiErrorType: model.NoData,
		Title:        "Product not found",
		ErrorCode:    "ERROR0001",
	}
)
