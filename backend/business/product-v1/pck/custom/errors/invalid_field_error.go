package errors

import (
	"net/http"

	"com.demo.poc/pck/core/errors/dto"
)

func NewInvalidFieldError(message string) dto.GenericError {
	return dto.GenericError{
		HttpStatus: http.StatusBadRequest,
		Origin:     dto.OriginOwn,
		Code:       "01.02.01",
		Message:    message,
	}
}
