package dto

type GenericError struct {
	HttpStatus int
	Origin     ErrorOrigin
	Message    string
	Code       string
}

func (err GenericError) Error() string {
	return err.Message
}
