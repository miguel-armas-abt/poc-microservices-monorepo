package errors

import (
	"poc/commons/core/constants"
	errorDto "poc/commons/core/errors/dto"
	"poc/commons/core/errors/selector"

	"github.com/go-resty/resty/v2"
)

type RestclientErrorHandler struct {
	errorSelector   *selector.RestClientErrorSelector
	errorExtractors []RestClientErrorExtractor
}

func NewRestCrestclientErrorHandler(
	errorSelector *selector.RestClientErrorSelector,
	errorExtractors []RestClientErrorExtractor,
) RestclientErrorHandler {

	return RestclientErrorHandler{
		errorSelector:   errorSelector,
		errorExtractors: errorExtractors,
	}
}

func (handler *RestclientErrorHandler) HandleError(response *resty.Response, serviceName string, wrapperType string) error {
	responseBody := response.String()

	var errorCode, errorMessage string
	for _, extractor := range handler.errorExtractors {
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

	selectedCode := handler.errorSelector.SelectCode(errorCode, serviceName)
	selectedMessage := handler.errorSelector.SelectMessage(errorCode, errorMessage, serviceName)
	selectedHttpCode := handler.errorSelector.SelectHttpCode(response.StatusCode(), errorCode, serviceName)
	selectedOrigin := handler.errorSelector.SelectOriginType(wrapperType)

	return errorDto.GenericError{
		HttpStatus: selectedHttpCode,
		Origin:     selectedOrigin,
		Code:       selectedCode,
		Message:    selectedMessage,
	}
}
