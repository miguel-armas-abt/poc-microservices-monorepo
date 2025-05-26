package restclient

import (
	"bytes"
	"io"
	"net/http"

	"poc/commons/core/logging"
	logDto "poc/commons/core/logging/dto"
	"poc/commons/core/tracing"
	"poc/commons/custom/properties"
)

type LoggingRoundTripper struct {
	rt    http.RoundTripper
	props *properties.ApplicationProperties
}

func NewRestClientInterceptor(rt http.RoundTripper, props *properties.ApplicationProperties) *LoggingRoundTripper {
	return &LoggingRoundTripper{rt: rt, props: props}
}

func (loggingRoundTripper *LoggingRoundTripper) RoundTrip(httpRequest *http.Request) (*http.Response, error) {
	var requestBody []byte

	if httpRequest.Body != nil {
		buffer, _ := io.ReadAll(httpRequest.Body)
		requestBody = buffer
		httpRequest.Body = io.NopCloser(bytes.NewBuffer(buffer))
	}

	if loggingRoundTripper.props.IsLoggerEnabled(string(logging.RestClientReqLog)) {
		logging.LogRequest(logDto.RestRequestLog{
			Method:      httpRequest.Method,
			URI:         httpRequest.URL.String(),
			Headers:     headerMap(httpRequest.Header),
			Body:        string(requestBody),
			TraceParent: httpRequest.Header.Get(tracing.TRACE_PARENT),
		}, string(logging.RestClientReqLog))
	}

	response, err := loggingRoundTripper.rt.RoundTrip(httpRequest)
	if err != nil {
		return response, err
	}

	var responseBody []byte
	if response.Body != nil {
		buffer, _ := io.ReadAll(response.Body)
		responseBody = buffer
		response.Body = io.NopCloser(bytes.NewBuffer(buffer))
	}

	if loggingRoundTripper.props.IsLoggerEnabled(string(logging.RestClientResLog)) {
		logging.LogResponse(logDto.RestResponseLog{
			URI:         httpRequest.URL.String(),
			Status:      response.StatusCode,
			Headers:     headerMap(response.Header),
			Body:        string(responseBody),
			TraceParent: httpRequest.Header.Get(tracing.TRACE_PARENT),
		}, string(logging.RestClientResLog))
	}

	return response, nil
}

func headerMap(httpHeaders http.Header) map[string]string {
	headers := make(map[string]string)
	for key, value := range httpHeaders {
		if len(value) > 0 {
			headers[key] = value[0]
		}
	}
	return headers
}
