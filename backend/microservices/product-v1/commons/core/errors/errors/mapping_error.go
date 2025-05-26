package errors

import (
	"net/http"

	errorDto "poc/commons/core/errors/dto"
)

// system: 01.00.xx
func NewMappingError(message string) errorDto.GenericError {
	return errorDto.GenericError{
		HttpStatus: http.StatusInternalServerError,
		Origin:     errorDto.ERROR_ORIGIN_OWN,
		Code:       "01.00.01",
		Message:    message,
	}
}
