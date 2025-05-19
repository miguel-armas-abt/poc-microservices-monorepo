package validations

import (
	"regexp"

	"github.com/go-playground/validator/v10"
)

var Validate *validator.Validate

func GetValidator() *validator.Validate {
	return Validate
}

func init() {
	Validate = validator.New()

	Validate.RegisterValidation("traceparent", func(fl validator.FieldLevel) bool {
		pattern := `^[0-9A-Fa-f]{2}-[0-9A-Fa-f]{32}-[0-9A-Fa-f]{16}-[0-9A-Fa-f]{2}$`
		matched, _ := regexp.MatchString(pattern, fl.Field().String())
		return matched
	})
}
