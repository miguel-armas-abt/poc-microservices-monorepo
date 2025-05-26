package errors

import (
	"net/http"

	errorDto "poc/commons/core/errors/dto"
)

// no such properties and components: 01.01.xx
func NoSuchRestClientError(message string) errorDto.GenericError {
	return errorDto.GenericError{
		HttpStatus: http.StatusInternalServerError,
		Origin:     errorDto.ERROR_ORIGIN_OWN,
		Code:       "01.01.01",
		Message:    message,
	}
}
