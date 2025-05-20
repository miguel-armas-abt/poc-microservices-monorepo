package validations

import (
	"regexp"

	"com.demo.poc/commons/tracing"
	"github.com/go-playground/validator/v10"
)

var Validate *validator.Validate

func GetValidator() *validator.Validate {
	return Validate
}

func init() {
	Validate = validator.New()

	Validate.RegisterValidation(tracing.TRACE_PARENT, func(fl validator.FieldLevel) bool {
		pattern := tracing.TRACE_PARENT_REGEX
		matched, _ := regexp.MatchString(pattern, fl.Field().String())
		return matched
	})
}
