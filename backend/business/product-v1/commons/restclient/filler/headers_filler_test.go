package filler

import (
	"testing"

	"com.demo.poc/commons/properties"
	"com.demo.poc/commons/properties/restclient"
	"github.com/stretchr/testify/require"
)

func TestGivenOnlyProvidedParams_WhenFillHeaders_ThenReturnsOnlyProvided(test *testing.T) {
	// Arrange
	serviceName := "svc"
	providedKey := "Subscription-Key"
	providedVal := "fake-key"

	props := &properties.ApplicationProperties{
		RestClients: map[string]restclient.RestClient{
			serviceName: {
				Request: restclient.RequestTemplate{
					Headers: restclient.HeaderTemplate{
						Provided: map[string]string{providedKey: providedVal},
					},
				},
			},
		},
	}
	hf := NewHeaderFiller(props, serviceName)

	// Act
	result := hf.FillHeaders(nil)

	// Assert
	require.Len(test, result, 1)
	require.Equal(test, providedVal, result[providedKey])
}

func TestGivenOnlyForwardedParams_WhenFillHeaders_ThenReturnsOnlyForwarded(test *testing.T) {
	// Arrange
	serviceName := "svc"
	forwardedKey := "channelId"
	headerName := "channelId"
	headerVal := "APP"

	props := &properties.ApplicationProperties{
		RestClients: map[string]restclient.RestClient{
			serviceName: {
				Request: restclient.RequestTemplate{
					Headers: restclient.HeaderTemplate{
						Forwarded: map[string]string{forwardedKey: headerName},
					},
				},
			},
		},
	}
	hf := NewHeaderFiller(props, serviceName)
	incoming := map[string]string{headerName: headerVal}

	// Act
	result := hf.FillHeaders(incoming)

	// Assert
	require.Len(test, result, 1)
	require.Equal(test, headerVal, result[forwardedKey])
}

func TestGivenProvidedAndForwardedParams_WhenFillHeaders_ThenReturnsBothSets(test *testing.T) {
	// Arrange
	serviceName := "svc"
	providedKey, providedValue := "prov-key", "prov-value"
	forwardedKey := "fwd-key"
	incomingHeaderKey, incomingHeaderValue := "header-key", "header-value"

	props := &properties.ApplicationProperties{
		RestClients: map[string]restclient.RestClient{
			serviceName: {
				Request: restclient.RequestTemplate{
					Headers: restclient.HeaderTemplate{
						Provided:  map[string]string{providedKey: providedValue},
						Forwarded: map[string]string{forwardedKey: incomingHeaderKey},
					},
				},
			},
		},
	}
	hf := NewHeaderFiller(props, serviceName)
	incomingHeaders := map[string]string{incomingHeaderKey: incomingHeaderValue}

	// Act
	result := hf.FillHeaders(incomingHeaders)

	// Assert
	require.Len(test, result, 2)
	require.Equal(test, providedValue, result[providedKey])
	require.Equal(test, incomingHeaderValue, result[forwardedKey])
}
