package errors

import (
	"net/http"

	dto "com.demo.poc/pck/core/errors/dto"
)

var (
	ProductNotFound = dto.GenericError{
		HttpStatus: http.StatusNotFound,
		Origin:     dto.OriginOwn,
		Message:    "Product not found",
		Code:       "01.01.01",
	}
)
