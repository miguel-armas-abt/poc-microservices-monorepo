package validations

import (
	"net/http"
	"net/http/httptest"
	"testing"

	errorSelector "poc/commons/core/errors/selector"
	properties "poc/commons/custom/properties"

	"github.com/gin-gonic/gin"
	"github.com/go-playground/validator/v10"
	"github.com/stretchr/testify/require"
)

type DummyParams struct {
	ID string `mapstructure:"id" validate:"required"`
}

func setupParamValidatorContext(headers map[string]string) *gin.Context {
	gin.SetMode(gin.TestMode)
	w := httptest.NewRecorder()
	context, _ := gin.CreateTestContext(w)
	req := httptest.NewRequest(http.MethodGet, "/", nil)
	for key, value := range headers {
		req.Header.Set(key, value)
	}
	context.Request = req
	return context
}

func stubParamValidator() *ParamValidator {
	properties := &properties.ApplicationProperties{
		ProjectType:   properties.PROJECT_TYPE_MS,
		ErrorMessages: map[string]string{"default": "Error"},
	}
	return NewParamValidator(validator.New(), errorSelector.NewResponseErrorSelector(properties))
}

func TestGivenValidHeaders_WhenValidateParamAndBind_ThenReturnsTrueAndBindsFields(test *testing.T) {
	//Arrange
	headers := map[string]string{"id": "12345"}
	ctx := setupParamValidatorContext(headers)
	paramValidator := stubParamValidator()
	var params DummyParams

	//Act
	isValid := paramValidator.ValidateParamAndBind(ctx, &params)

	//Assert
	require.True(test, isValid)
	require.Equal(test, "12345", params.ID)
}

func TestGivenEmptyHeader_WhenValidateParamAndBind_ThenReturnsFalse(test *testing.T) {
	//Arrange
	headers := map[string]string{"id": ""}
	context := setupParamValidatorContext(headers)
	paramValidator := stubParamValidator()
	var params DummyParams

	//Act
	isValid := paramValidator.ValidateParamAndBind(context, &params)

	//Assert
	require.False(test, isValid)
	require.Equal(test, http.StatusBadRequest, context.Writer.Status())
}

func TestGivenMissingHeader_WhenValidateParamAndBind_ThenReturnsFalse(test *testing.T) {
	//Arrange
	context := setupParamValidatorContext(map[string]string{})
	paramValidator := stubParamValidator()
	var params DummyParams

	//Act
	isValid := paramValidator.ValidateParamAndBind(context, &params)

	//Assert
	require.False(test, isValid)
	require.Equal(test, http.StatusBadRequest, context.Writer.Status())
}
