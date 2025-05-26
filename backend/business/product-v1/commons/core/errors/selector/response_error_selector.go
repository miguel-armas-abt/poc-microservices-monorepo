package selector

import (
	"poc/commons/core/constants"
	errorDto "poc/commons/core/errors/dto"
	"poc/commons/custom/properties"
)

type ResponseErrorSelector struct{}

func NewResponseErrorSelector() *ResponseErrorSelector {
	return &ResponseErrorSelector{}
}

func (selector *ResponseErrorSelector) ToErrorDto(err error) errorDto.ErrorDto {
	baseError := extractError(err)
	projectType := selector.selectProjectType()
	baseError.Code = selectCustomCode(baseError.Code, projectType)
	baseError.Message = selector.selectMessage(baseError.Message, baseError.Code, projectType)
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

func (selector *ResponseErrorSelector) selectProjectType() properties.ProjectType {
	if projectType := properties.ProjectType(properties.Properties.ProjectType); projectType != constants.EMPTY {
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

func (selector *ResponseErrorSelector) selectMessage(originalMessage, code string, projectType properties.ProjectType) string {
	defaultMessage := properties.Properties.ErrorMessages[errorDto.CODE_DEFAULT]

	if projectType == properties.PROJECT_TYPE_BFF {
		return defaultMessage
	}
	if message, existsMessage := properties.Properties.ErrorMessages[code]; existsMessage {
		return message
	}
	if originalMessage != constants.EMPTY {
		return originalMessage
	}
	return defaultMessage
}
