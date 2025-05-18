package exception

import (
	"net/http"

	dto "com.demo.poc/cmd/commons/core/errors/dto"
)

var (
	ProductNotFound = dto.GenericError{
		HttpStatus: http.StatusNotFound,
		Origin:     dto.Own,
		Message:    "Product not found",
		Code:       "01.01.01",
	}
)
