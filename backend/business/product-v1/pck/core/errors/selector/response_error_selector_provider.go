package selector

import "com.demo.poc/pck/core/config"

func NewSelector(properties *config.ApplicationProperties) *ResponseErrorSelector {
	return &ResponseErrorSelector{properties: properties}
}
