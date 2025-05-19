package validations

import (
	"github.com/go-playground/validator/v10"
)

var Validate *validator.Validate

func Validator() *validator.Validate {
	return Validate
}

func init() {
	Validate = validator.New()
}
