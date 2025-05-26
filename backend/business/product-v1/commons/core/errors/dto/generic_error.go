package dto

import "fmt"

type GenericError struct {
	HttpStatus int
	Origin     ErrorOrigin
	Message    string
	Code       string
}

func (e GenericError) Error() string {
	return fmt.Sprintf("[%s/%s] %s", e.Origin, e.Code, e.Message)
}
