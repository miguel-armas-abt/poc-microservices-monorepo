package validations

import (
	"net/http"
	"net/http/httptest"
	"strings"
	"testing"

	"github.com/gin-gonic/gin"
	"github.com/stretchr/testify/require"
)

type DummyDto struct {
	Field string `json:"field" validate:"required"`
}

func setupBodyValidatorContext(payload string) *gin.Context {
	gin.SetMode(gin.TestMode)
	w := httptest.NewRecorder()
	context, _ := gin.CreateTestContext(w)
	req := httptest.NewRequest(http.MethodPost, "/", strings.NewReader(payload))
	req.Header.Set("Content-Type", "application/json")
	context.Request = req
	return context
}

func TestGivenValidBody_WhenValidateBodyAndGet_ThenReturnsObject(test *testing.T) {
	// Arrange
	context := setupBodyValidatorContext(`{"field":"hello"}`)

	bodyValidator := NewBodyValidator(Validate)

	// Act
	result, isValid := ValidateBodyAndGet[DummyDto](context, bodyValidator)

	// Assert
	require.True(test, isValid)
	require.Equal(test, "hello", result.Field)
}

func TestGivenEmptyField_WhenValidateBodyAndGet_ThenReturnsFalse(test *testing.T) {
	// Arrange
	context := setupBodyValidatorContext(`{"field":""}`)

	bodyValidator := NewBodyValidator(Validate)

	// Act
	_, isValid := ValidateBodyAndGet[DummyDto](context, bodyValidator)

	// Assert
	require.False(test, isValid)
}

func TestGivenMissingField_WhenValidateBodyAndGet_ThenReturnsFalse(test *testing.T) {
	// Arrange
	context := setupBodyValidatorContext(`{}`)

	bodyValidator := NewBodyValidator(Validate)

	// Act
	_, isValid := ValidateBodyAndGet[DummyDto](context, bodyValidator)

	// Assert
	require.False(test, isValid)
}
