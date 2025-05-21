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
	rc := props.RestClients[serviceName]

	// Act
	result := FillHeaders(nil, &rc)

	// Assert
	require.Len(test, result, 1)
	require.Equal(test, providedVal, result[providedKey])
}

func TestGivenOnlyForwardedParams_WhenFillHeaders_ThenReturnsOnlyForwarded(test *testing.T) {
	// Arrange
	serviceName := "svc"
	headerName := "channelId"     // inKey
	forwardedKey := "application" // outKey
	headerVal := "APP"

	props := &properties.ApplicationProperties{
		RestClients: map[string]restclient.RestClient{
			serviceName: {
				Request: restclient.RequestTemplate{
					Headers: restclient.HeaderTemplate{
						Forwarded: map[string]string{headerName: forwardedKey},
					},
				},
			},
		},
	}
	rc := props.RestClients[serviceName]
	incoming := map[string]string{headerName: headerVal}

	// Act
	result := FillHeaders(incoming, &rc)

	// Assert
	require.Len(test, result, 1)
	require.Equal(test, headerVal, result[forwardedKey])
}

func TestGivenProvidedAndForwardedParams_WhenFillHeaders_ThenReturnsBothSets(test *testing.T) {
	// Arrange
	serviceName := "svc"
	providedKey, providedValue := "Subscription-Key", "fake-key"
	incomingHeaderKey, incomingHeaderValue := "channelId", "APP"
	forwardedKey := "application" // outKey

	props := &properties.ApplicationProperties{
		RestClients: map[string]restclient.RestClient{
			serviceName: {
				Request: restclient.RequestTemplate{
					Headers: restclient.HeaderTemplate{
						Provided:  map[string]string{providedKey: providedValue},
						Forwarded: map[string]string{incomingHeaderKey: forwardedKey},
					},
				},
			},
		},
	}
	rc := props.RestClients[serviceName]
	incomingHeaders := map[string]string{incomingHeaderKey: incomingHeaderValue}

	// Act
	result := FillHeaders(incomingHeaders, &rc)

	// Assert
	require.Len(test, result, 2)
	require.Equal(test, providedValue, result[providedKey])
	require.Equal(test, incomingHeaderValue, result[forwardedKey])
}
