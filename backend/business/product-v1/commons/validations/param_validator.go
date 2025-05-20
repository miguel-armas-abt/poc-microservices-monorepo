package validations

import (
	coreErrors "com.demo.poc/commons/errors/errors"
	restServerUtils "com.demo.poc/commons/restserver/utils"

	"github.com/gin-gonic/gin"
	"github.com/go-playground/validator/v10"
	"github.com/mitchellh/mapstructure"
)

type ParamValidator struct {
	coreValidator *validator.Validate
}

func NewParamValidator(coreValidator *validator.Validate) *ParamValidator {
	return &ParamValidator{coreValidator: coreValidator}
}

func (paramValidator *ParamValidator) ValidateStruct(params interface{}) error {
	return paramValidator.coreValidator.Struct(params)
}

func (paramValidator *ParamValidator) ValidateParamAndBind(context *gin.Context, params interface{}) bool {
	paramMapper, err := mapstructure.NewDecoder(&mapstructure.DecoderConfig{
		TagName: "mapstructure",
		Result:  params,
	})

	if err != nil {
		context.Error(coreErrors.NewInvalidFieldError(err.Error()))
		context.Abort()
		return false
	}

	paramMap := restServerUtils.ExtractHeadersAsMap(context.Request.Header)
	if err := paramMapper.Decode(paramMap); err != nil {
		context.Error(coreErrors.NewInvalidFieldError(err.Error()))
		context.Abort()
		return false
	}

	if err := paramValidator.ValidateStruct(params); err != nil {
		context.Error(coreErrors.NewInvalidFieldError(err.Error()))
		context.Abort()
		return false
	}

	return true
}
