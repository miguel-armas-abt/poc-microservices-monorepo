package errors

import (
	"net/http"

	errorDto "com.demo.poc/commons/core/errors/dto"
)

var (
	ProductNotFound = errorDto.GenericError{
		HttpStatus: http.StatusNotFound,
		Origin:     errorDto.ERROR_ORIGIN_OWN,
		Message:    "Product not found",
		Code:       "01.01.01",
	}
)
