package logging

import (
	"fmt"
	"strings"

	"poc/commons/core/constants"
	logDto "poc/commons/core/logging/dto"
	"poc/commons/core/tracing"

	"github.com/sirupsen/logrus"
)

func InjectFields(entry *logrus.Entry, headers map[string]string) *logrus.Entry {
	for key, value := range headers {
		entry = entry.WithField(key, value)
	}
	return entry
}

func LogRequest(req logDto.RestRequestLog, logType string) {
	logFields := Logger.WithFields(logrus.Fields{})

	traceMap := tracing.GetTraceHeadersAsMap(req.TraceParent)
	for key, value := range traceMap {
		logFields = logFields.WithField(key, value)
	}

	headersKey := logType + ".headers"
	logFields = logFields.WithField(headersKey, formatHeaders(req.Headers))

	logFields = logFields.WithFields(logrus.Fields{
		logType + ".method": req.Method,
		logType + ".uri":    req.URI,
		logType + ".body":   req.Body,
	})
	logFields.Info(logType)
}

func LogResponse(res logDto.RestResponseLog, logType string) {
	logFields := Logger.WithFields(logrus.Fields{})

	traceMap := tracing.GetTraceHeadersAsMap(res.TraceParent)
	for key, value := range traceMap {
		logFields = logFields.WithField(key, value)
	}

	headersKey := logType + ".headers"
	logFields = logFields.WithField(headersKey, formatHeaders(res.Headers))

	logFields = logFields.WithFields(logrus.Fields{
		logType + ".status": res.Status,
		logType + ".uri":    res.URI,
		logType + ".body":   res.Body,
	})
	logFields.Info(logType)
}

func formatHeaders(headers map[string]string) string {
	parts := make([]string, 0, len(headers))
	for k, v := range headers {
		parts = append(parts, fmt.Sprintf("%s=%s", k, v))
	}
	return constants.LEFT_BRACKET + strings.Join(parts, constants.COMMA) + constants.RIGHT_BRACKET
}
