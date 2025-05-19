package logging

import (
	logDto "com.demo.poc/commons/logging/dto"
	"com.demo.poc/commons/tracing"
	"github.com/sirupsen/logrus"
)

func InjectFields(entry *logrus.Entry, headers map[string]string) *logrus.Entry {
	for k, v := range headers {
		entry = entry.WithField(k, v)
	}
	return entry
}

func LogRequest(req logDto.RestRequestLog) {
	traceMap := tracing.GetTraceHeadersAsMap(req.TraceParent)
	traceFields := logrus.Fields{}
	for key, value := range traceMap {
		traceFields[key] = value
	}
	entry := Logger.WithFields(traceFields)

	entry = InjectFields(entry, req.Headers)
	entry = entry.WithFields(logrus.Fields{
		string(RestServerReqLog) + ".method": req.Method,
		string(RestServerReqLog) + ".uri":    req.URI,
		string(RestServerReqLog) + ".body":   req.Body,
	})
	entry.Info(string(RestServerReqLog) + " request logged")
}

func LogResponse(res logDto.RestResponseLog) {
	traceMap := tracing.GetTraceHeadersAsMap(res.TraceParent)
	traceFields := logrus.Fields{}
	for key, value := range traceMap {
		traceFields[key] = value
	}
	entry := Logger.WithFields(traceFields)

	entry = InjectFields(entry, res.Headers)
	entry = entry.WithFields(logrus.Fields{
		string(RestServerResLog) + ".status": res.Status,
		string(RestServerResLog) + ".uri":    res.URI,
		string(RestServerResLog) + ".body":   res.Body,
	})
	entry.Info(string(RestServerResLog) + " response logged")
}
