package validations

import (
	"com.demo.poc/commons/errors/errors"
	"github.com/gin-gonic/gin"
	"github.com/go-playground/validator/v10"
)

type BodyValidator struct {
	coreValidator *validator.Validate
}

func NewBodyValidator(coreValidator *validator.Validate) *BodyValidator {
	return &BodyValidator{coreValidator: coreValidator}
}

func (bodyValidator *BodyValidator) BindJSON(context *gin.Context, obj interface{}) error {
	if err := context.ShouldBindJSON(obj); err != nil {
		return err
	}
	return nil
}

func (bodyValidator *BodyValidator) ValidateStruct(obj interface{}) error {
	return bodyValidator.coreValidator.Struct(obj)
}

func ValidateBodyAndGet[T any](context *gin.Context, bodyValidator *BodyValidator) (T, bool) {
	var defaultObject T
	var responseObject T

	if err := bodyValidator.BindJSON(context, &responseObject); err != nil {
		context.Error(err)
		context.Abort()
		return defaultObject, false
	}

	if err := bodyValidator.ValidateStruct(responseObject); err != nil {
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
