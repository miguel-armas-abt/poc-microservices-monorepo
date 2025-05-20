package selector

import (
	"com.demo.poc/commons/constants"
	errorDto "com.demo.poc/commons/errors/dto"
	"com.demo.poc/commons/properties"
)

type RestClientErrorSelector struct {
	props *properties.ApplicationProperties
}

func NewRestClientErrorSelector(props *properties.ApplicationProperties) *RestClientErrorSelector {
	return &RestClientErrorSelector{props: props}
}

func (selector *RestClientErrorSelector) SelectCode(errorCode, serviceName string) string {
	restClients := selector.props.RestClients

	if client, exists := restClients[serviceName]; exists {
		if errorTemplate, found := client.Errors[errorCode]; found {
			return errorTemplate.CustomCode
		}
	}
	if errorCode == constants.EMPTY {
		return errorDto.CODE_DEFAULT
	}
	return errorCode
}

func (selector *RestClientErrorSelector) SelectMessage(errorCode, errorMessage, serviceName string) string {
	defaultMsg := selector.props.ErrorMessages[errorDto.CODE_DEFAULT]

	restClients := selector.props.RestClients

	if client, exists := restClients[serviceName]; exists {
		if errorTemplate, found := client.Errors[errorCode]; found && errorTemplate.Message != constants.EMPTY {
			return errorTemplate.Message
		}
	}
	if errorMessage != constants.EMPTY {
		return errorMessage
	}
	return defaultMsg
}

func (selector *RestClientErrorSelector) SelectHttpCode(httpCode int, errorCode, serviceName string) int {
	restClients := selector.props.RestClients

	if client, exists := restClients[serviceName]; exists {
		if errorTemplate, found := client.Errors[errorCode]; found && errorTemplate.HttpCode != 0 {
			return errorTemplate.HttpCode
		}
	}
	return httpCode
}

func (selector *RestClientErrorSelector) SelectOriginType(wrapperType string) errorDto.ErrorOrigin {
	if wrapperType == "ErrorDto" {
		return errorDto.ERROR_ORIGIN_PARTNER
	}
	return errorDto.ERROR_ORIGIN_THIRD_PARTY
}
