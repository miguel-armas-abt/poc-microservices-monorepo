package validations

import (
	coreErrors "poc/commons/core/errors/errors"
	selector "poc/commons/core/errors/selector"
	restServerUtils "poc/commons/core/restserver/utils"

	"github.com/gin-gonic/gin"
	"github.com/go-playground/validator/v10"
	"github.com/mitchellh/mapstructure"
)

type ParamValidator struct {
	coreValidator *validator.Validate
	errorSelector *selector.ResponseErrorSelector
}

func NewParamValidator(
	coreValidator *validator.Validate,
	errorSelector *selector.ResponseErrorSelector) *ParamValidator {

	return &ParamValidator{
		coreValidator: coreValidator,
		errorSelector: errorSelector,
	}
}

func (validator *ParamValidator) ValidateStruct(params interface{}) error {
	return validator.coreValidator.Struct(params)
}

func (validator *ParamValidator) ValidateParamAndBind(ctx *gin.Context, params interface{}) bool {
	paramMapper, err := mapstructure.NewDecoder(&mapstructure.DecoderConfig{
		TagName: "mapstructure",
		Result:  params,
	})

	if err != nil {
		genericError := coreErrors.NewInvalidFieldError(err.Error())
		ctx.AbortWithStatusJSON(genericError.HttpStatus, validator.errorSelector.ToErrorDto(genericError))
		return false
	}

	paramMap := restServerUtils.ExtractHeadersAsMap(ctx.Request.Header)
	if err := paramMapper.Decode(paramMap); err != nil {
		genericError := coreErrors.NewInvalidFieldError(err.Error())
		ctx.AbortWithStatusJSON(genericError.HttpStatus, validator.errorSelector.ToErrorDto(genericError))
		return false
	}

	if err := validator.ValidateStruct(params); err != nil {
		genericError := coreErrors.NewInvalidFieldError(err.Error())
		ctx.AbortWithStatusJSON(genericError.HttpStatus, validator.errorSelector.ToErrorDto(genericError))
		return false
	}

	return true
}
