package selector

import (
	"com.demo.poc/pck/core/config"
	"com.demo.poc/pck/core/errors/dto"
)

type ResponseErrorSelector struct {
	properties *config.ApplicationProperties
}

func (responseErrorSelector *ResponseErrorSelector) ToErrorDto(err error) dto.ErrorDto {

	if genericError, isGenericError := err.(dto.GenericError); isGenericError {
		code := genericError.Code
		message, found := responseErrorSelector.properties.ErrorMessages[code]
		if !found {
			message = responseErrorSelector.properties.ErrorMessages[dto.DefaultCode]
		}
		return dto.ErrorDto{
			Origin:  string(genericError.Origin),
			Code:    code,
			Message: message,
		}
	}

	defaultMessage := responseErrorSelector.properties.ErrorMessages[dto.DefaultCode]
	return dto.ErrorDto{
		Origin:  string(dto.OriginOwn),
		Code:    dto.DefaultCode,
		Message: defaultMessage,
	}
}
