package errors

import (
	"encoding/json"

	"poc/commons/core/constants"
	errorDto "poc/commons/core/errors/dto"
)

type RestClientErrorExtractor interface {
	Extract(jsonBody string) (code, message string, ok bool)

	Supports(wrapperType string) bool
}

type DefaultExtractor struct{}

func (errorExtractor DefaultExtractor) Extract(jsonBody string) (string, string, bool) {
	var dto struct {
		Code    string `json:"code"`
		Message string `json:"message"`
	}
	if err := json.Unmarshal([]byte(jsonBody), &dto); err != nil {
		return constants.EMPTY, constants.EMPTY, false
	}
	if dto.Code == constants.EMPTY && dto.Message == constants.EMPTY {
		return constants.EMPTY, constants.EMPTY, false
	}
	return dto.Code, dto.Message, true
}

func (errorExtractor DefaultExtractor) Supports(wrapperType string) bool {
	return wrapperType == errorDto.CODE_DEFAULT
}
