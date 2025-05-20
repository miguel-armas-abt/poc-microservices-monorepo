package utils

import (
	"net/http"
	"strings"
)

func ExtractHeadersAsMap(headers http.Header) map[string]string {
	result := make(map[string]string, len(headers))
	for key, values := range headers {
		if len(values) == 0 {
			continue
		}
		lowerKey := strings.ToLower(key)
		result[lowerKey] = values[0]
	}
	return result
}
