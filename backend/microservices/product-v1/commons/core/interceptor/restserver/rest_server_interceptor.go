package restserver

import (
	"bytes"
	"io"

	"poc/commons/core/constants"
	"poc/commons/core/logging"
	logDto "poc/commons/core/logging/dto"
	"poc/commons/core/tracing"
	"poc/commons/custom/properties"

	"github.com/gin-gonic/gin"
)

type BodyWriter struct {
	gin.ResponseWriter
	body *bytes.Buffer
}

func (w BodyWriter) Write(b []byte) (int, error) {
	w.body.Write(b)
	return w.ResponseWriter.Write(b)
}

func InterceptRestServer(props *properties.ApplicationProperties) gin.HandlerFunc {
	return func(context *gin.Context) {

		var requestBody []byte
		if context.Request.Body != nil {
			requestBody, _ = io.ReadAll(context.Request.Body)
			context.Request.Body = io.NopCloser(bytes.NewBuffer(requestBody))
		}

		requestHeaders := make(map[string]string)
		for key, value := range context.Request.Header {
			if len(value) > 0 {
				requestHeaders[key] = value[0]
			}
		}

		traceParent := context.Request.Header.Get(tracing.TRACE_PARENT)

		if props.IsLoggerEnabled(string(logging.RestServerReqLog)) {
			logging.LogRequest(logDto.RestRequestLog{
				Method:      context.Request.Method,
				URI:         context.Request.RequestURI,
				Headers:     requestHeaders,
				Body:        string(requestBody),
				TraceParent: traceParent,
			}, string(logging.RestServerReqLog))
		}

		bodyWriter := &BodyWriter{body: bytes.NewBufferString(constants.EMPTY), ResponseWriter: context.Writer}
		context.Writer = bodyWriter

		context.Next()

		responseHeaders := make(map[string]string)
		for key, value := range context.Writer.Header() {
			if len(value) > 0 {
				responseHeaders[key] = value[0]
			}
		}

		if props.IsLoggerEnabled(string(logging.RestServerResLog)) {
			logging.LogResponse(logDto.RestResponseLog{
				URI:         context.Request.RequestURI,
				Status:      context.Writer.Status(),
				Headers:     responseHeaders,
				Body:        bodyWriter.body.String(),
				TraceParent: traceParent,
			}, string(logging.RestServerResLog))
		}
	}
}
