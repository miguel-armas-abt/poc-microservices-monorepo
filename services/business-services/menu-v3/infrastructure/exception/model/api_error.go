package model

type ApiError struct {
	ApiErrorType ApiErrorType
	Title        string
	ErrorCode    string
}

// ApiError implements error interface
func (err ApiError) Error() string {
	return err.Title
}
