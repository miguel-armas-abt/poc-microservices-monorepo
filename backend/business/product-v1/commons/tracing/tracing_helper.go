package tracing

import (
	"crypto/rand"
	"encoding/hex"
	"fmt"
	"strings"

	"com.demo.poc/commons/constants"
)

func GetTraceId(traceParent string) string {
	parts := strings.Split(traceParent, constants.MIDDLE_DASH)
	if len(parts) >= 3 {
		return parts[1]
	}
	return constants.EMPTY
}

func GetSpanId(traceParent string) string {
	parts := strings.Split(traceParent, constants.MIDDLE_DASH)
	if len(parts) >= 3 {
		return parts[2]
	}
	return constants.EMPTY
}

func generateId64() string {
	b := make([]byte, 8)
	_, _ = rand.Read(b)
	return hex.EncodeToString(b)
}

func GetNewTraceParent(traceParent string) string {
	traceId := GetTraceId(traceParent)
	newSpanId := generateId64()
	return fmt.Sprintf("00-%s-%s-01", traceId, newSpanId)
}

func GetTraceHeadersAsMap(traceParent string) map[string]string {
	if traceParent == constants.EMPTY {
		return map[string]string{}
	}
	return map[string]string{
		"traceParent": traceParent,
		"traceId":     GetTraceId(traceParent),
		"spanId":      GetSpanId(traceParent),
	}
}
