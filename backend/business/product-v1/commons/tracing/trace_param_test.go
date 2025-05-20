package tracing

import (
	"regexp"
	"strings"
	"testing"

	"com.demo.poc/commons/constants"
	"github.com/stretchr/testify/require"
)

func TestGivenValidTraceParent_WhenGetTraceIdAndSpanId_ThenReturnCorrectParts(test *testing.T) {
	// Arrange
	traceParent := "00-aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa-bbbbbbbbbbbbbbbb-01"
	expectedTraceId := "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
	expectedSpanId := "bbbbbbbbbbbbbbbb"

	// Act
	actualTraceId := GetTraceId(traceParent)
	actualSpanId := GetSpanId(traceParent)

	// Assert
	require.Equal(test, expectedTraceId, actualTraceId)
	require.Equal(test, expectedSpanId, actualSpanId)
}

func TestGivenValidTraceParent_WhenGetTraceHeadersAsMap_ThenReturnCorrectMap(test *testing.T) {
	// Arrange
	traceParent := "00-aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa-bbbbbbbbbbbbbbbb-01"
	expectedTraceId := "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
	expectedSpanId := "bbbbbbbbbbbbbbbb"

	// Act
	headersMap := GetTraceHeadersAsMap(traceParent)

	// Assert
	require.Len(test, headersMap, 3)
	require.Equal(test, traceParent, headersMap[TRACE_PARENT])
	require.Equal(test, expectedTraceId, headersMap[TRACE_ID])
	require.Equal(test, expectedSpanId, headersMap[SPAN_ID])
}

func TestGivenEmptyTraceParent_WhenGetTraceHeadersAsMap_ThenReturnEmptyMap(test *testing.T) {
	// Arrange
	traceParent := constants.EMPTY

	// Act
	headersMap := GetTraceHeadersAsMap(traceParent)

	// Assert
	require.Empty(test, headersMap)
}

func TestGivenOriginalTraceParent_WhenGetNewTraceParent_ThenMatchesPatternAndKeepsTraceId(test *testing.T) {
	// Arrange
	original := "00-680c67d2bf68b0c6e171c1925a857543-ffffffffffffffff-01"
	pattern := regexp.MustCompile(TRACE_PARENT_REGEX)
	expectedTraceId := GetTraceId(original)

	// Act
	newTrace := GetNewTraceParent(original)

	// Assert: match expression and keep the traceId
	require.True(test, pattern.MatchString(newTrace))
	require.Equal(test, expectedTraceId, GetTraceId(newTrace))
}

func TestGivenOriginalTraceParent_WhenGetNewTraceParent_SpanIdLength16(test *testing.T) {
	// Arrange
	original := "00-aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa-0000000000000000-01"

	// Act
	newTrace := GetNewTraceParent(original)
	parts := strings.Split(newTrace, "-")
	newSpan := parts[2]

	// Assert: generated spanId has 16 hex characteres
	require.Len(test, newSpan, 16)
	require.Regexp(test, "^[0-9a-fA-F]{16}$", newSpan)
}
