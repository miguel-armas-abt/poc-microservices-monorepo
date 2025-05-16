package exception

import (
	dto "com.demo.poc/cmd/commons/core/errors/dto"
)

var (
	ProductNotFound = dto.ApiError{
		ApiErrorType: dto.NoData,
		Title:        "Product not found",
		ErrorCode:    "ERROR0001",
	}
)
