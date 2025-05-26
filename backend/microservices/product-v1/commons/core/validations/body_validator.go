package validations

import (
	"poc/commons/core/errors/errors"

	"github.com/gin-gonic/gin"
	"github.com/go-playground/validator/v10"
)

type BodyValidator struct {
	playgroundValidator *validator.Validate
}

func NewBodyValidator(coreValidator *validator.Validate) *BodyValidator {
	return &BodyValidator{playgroundValidator: coreValidator}
}

func ValidateBodyAndGet[T any](context *gin.Context, bodyValidator *BodyValidator) (T, bool) {
	var defaultObject T
	var responseObject T

	if err := bodyValidator.bindJSON(context, &responseObject); err != nil {
		context.Error(err)
		context.Abort()
		return defaultObject, false
	}

	if err := bodyValidator.validateStruct(responseObject); err != nil {
		if constraints, hasContraints := err.(validator.ValidationErrors); hasContraints {
			context.Error(errors.NewInvalidFieldError(constraints.Error()))
		} else {
			context.Error(err)
		}
		context.Abort()
		return defaultObject, false
	}
	return responseObject, true
}

func (bodyValidator *BodyValidator) bindJSON(context *gin.Context, obj interface{}) error {
	if err := context.ShouldBindJSON(obj); err != nil {
		return err
	}
	return nil
}

func (bodyValidator *BodyValidator) validateStruct(obj interface{}) error {
	return bodyValidator.playgroundValidator.Struct(obj)
}
