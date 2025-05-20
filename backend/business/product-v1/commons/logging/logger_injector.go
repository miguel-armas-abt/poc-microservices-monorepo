package logging

import (
	"fmt"
	"strings"

	"com.demo.poc/commons/constants"
	logDto "com.demo.poc/commons/logging/dto"
	"com.demo.poc/commons/tracing"
	"github.com/sirupsen/logrus"
)

func InjectFields(entry *logrus.Entry, headers map[string]string) *logrus.Entry {
	for key, value := range headers {
		entry = entry.WithField(key, value)
	}
	return entry
}

func LogRequest(req logDto.RestRequestLog) {
	logFields := Logger.WithFields(logrus.Fields{})

	traceMap := tracing.GetTraceHeadersAsMap(req.TraceParent)
	for key, value := range traceMap {
		logFields = logFields.WithField(key, value)
	}

	headersKey := string(RestServerReqLog) + ".headers"
	logFields = logFields.WithField(headersKey, formatHeaders(req.Headers))

	logFields = logFields.WithFields(logrus.Fields{
		string(RestServerReqLog) + ".method": req.Method,
		string(RestServerReqLog) + ".uri":    req.URI,
		string(RestServerReqLog) + ".body":   req.Body,
	})
	logFields.Info(string(RestServerReqLog))
}

func LogResponse(res logDto.RestResponseLog) {
	logFields := Logger.WithFields(logrus.Fields{})

	traceMap := tracing.GetTraceHeadersAsMap(res.TraceParent)
	for key, value := range traceMap {
		logFields = logFields.WithField(key, value)
	}

	headersKey := string(RestServerResLog) + ".headers"
	logFields = logFields.WithField(headersKey, formatHeaders(res.Headers))

	logFields = logFields.WithFields(logrus.Fields{
		string(RestServerResLog) + ".status": res.Status,
		string(RestServerResLog) + ".uri":    res.URI,
		string(RestServerResLog) + ".body":   res.Body,
	})
	logFields.Info(string(RestServerResLog))
}

func formatHeaders(headers map[string]string) string {
	parts := make([]string, 0, len(headers))
	for k, v := range headers {
		parts = append(parts, fmt.Sprintf("%s=%s", k, v))
	}
	return constants.LEFT_BRACKET + strings.Join(parts, constants.COMMA) + constants.RIGHT_BRACKET
}
