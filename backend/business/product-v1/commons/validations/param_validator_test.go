package validations

import (
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/gin-gonic/gin"
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

func TestGivenValidHeaders_WhenValidateParamAndBind_ThenReturnsTrueAndBindsFields(test *testing.T) {
	// Arrange
	headers := map[string]string{"id": "12345"}
	context := setupParamValidatorContext(headers)
	paramValidator := NewParamValidator(Validate)
	var params DummyParams

	// Act
	isValid := paramValidator.ValidateParamAndBind(context, &params)

	// Assert
	require.True(test, isValid)
	require.Equal(test, "12345", params.ID)
}

func TestGivenEmptyHeader_WhenValidateParamAndBind_ThenReturnsFalse(test *testing.T) {
	// Arrange
	headers := map[string]string{"id": ""}
	context := setupParamValidatorContext(headers)
	paramValidator := NewParamValidator(Validate)
	var params DummyParams

	// Act
	isValid := paramValidator.ValidateParamAndBind(context, &params)

	// Assert
	require.False(test, isValid)
}

func TestGivenMissingHeader_WhenValidateParamAndBind_ThenReturnsFalse(test *testing.T) {
	// Arrange
	context := setupParamValidatorContext(map[string]string{})
	paramValidator := NewParamValidator(Validate)
	var params DummyParams

	// Act
	isValid := paramValidator.ValidateParamAndBind(context, &params)

	// Assert
	require.False(test, isValid)
}
