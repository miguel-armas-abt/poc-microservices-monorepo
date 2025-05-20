package errors

import (
	"com.demo.poc/commons/constants"
	errorDto "com.demo.poc/commons/errors/dto"
	errorSelector "com.demo.poc/commons/errors/selector"
	"github.com/go-resty/resty/v2"
)

func HandleError(
	response *resty.Response,
	serviceName string,
	wrapperType string,
	selector *errorSelector.RestClientErrorSelector,
	extractors []RestClientErrorExtractor,
) error {
	responseBody := response.String()

	var errorCode, errorMessage string
	for _, extractor := range extractors {
		if extractor.Supports(wrapperType) {
			if code, msg, exists := extractor.Extract(responseBody); exists {
				errorCode, errorMessage = code, msg
				break
			}
		}
	}

	if errorCode == constants.EMPTY {
		errorCode = errorDto.CODE_DEFAULT
		errorMessage = "unexpected error"
	}

	selectedCode := selector.SelectCode(errorCode, serviceName)
	selectedMessage := selector.SelectMessage(errorCode, errorMessage, serviceName)
	selectedHttpCode := selector.SelectHttpCode(response.StatusCode(), errorCode, serviceName)
	selectedOrigin := selector.SelectOriginType(wrapperType)

	return errorDto.GenericError{
		HttpStatus: selectedHttpCode,
		Origin:     selectedOrigin,
		Code:       selectedCode,
		Message:    selectedMessage,
	}
}
