package selector

import (
	"com.demo.poc/commons/constants"
	errorDto "com.demo.poc/commons/errors/dto"
	properties "com.demo.poc/commons/properties"
)

type ResponseErrorSelector struct {
	properties *properties.ApplicationProperties
}

func NewResponseErrorSelector(properties *properties.ApplicationProperties) *ResponseErrorSelector {
	return &ResponseErrorSelector{properties: properties}
}

func (responseErrorSelector *ResponseErrorSelector) ToErrorDto(err error) errorDto.ErrorDto {
	baseError := extractError(err)
	projectType := responseErrorSelector.selectProjectType()
	baseError.Code = selectCustomCode(baseError.Code, projectType)
	baseError.Message = responseErrorSelector.selectMessage(baseError.Message, baseError.Code, projectType)
	return baseError
}

func extractError(err error) errorDto.ErrorDto {
	if genericError, isGenericError := err.(errorDto.GenericError); isGenericError {
		recoveredError := errorDto.ErrorDto{
			Origin: string(genericError.Origin),
			Code:   genericError.Code,
		}
		if message := genericError.Message; message != constants.EMPTY {
			recoveredError.Message = message
		}
		return recoveredError
	}
	return errorDto.ErrorDto{Origin: string(errorDto.ERROR_ORIGIN_OWN)}
}

func (responseErrorSelector *ResponseErrorSelector) selectProjectType() properties.ProjectType {
	if projectType := properties.ProjectType(responseErrorSelector.properties.ProjectType); projectType != constants.EMPTY {
		return projectType
	}
	return properties.PROJECT_TYPE_MS
}

func selectCustomCode(code string, projectType properties.ProjectType) string {
	if projectType == properties.PROJECT_TYPE_BFF {
		return errorDto.CODE_DEFAULT
	}
	if code == constants.EMPTY {
		return errorDto.CODE_DEFAULT
	}
	return code
}

func (responseErrorSelector *ResponseErrorSelector) selectMessage(originalMessage, code string, projectType properties.ProjectType) string {
	defaultMessage := responseErrorSelector.properties.ErrorMessages[errorDto.CODE_DEFAULT]

	if projectType == properties.PROJECT_TYPE_BFF {
		return defaultMessage
	}
	if message, existsMessage := responseErrorSelector.properties.ErrorMessages[code]; existsMessage {
		return message
	}
	if originalMessage != constants.EMPTY {
		return originalMessage
	}
	return defaultMessage
}
