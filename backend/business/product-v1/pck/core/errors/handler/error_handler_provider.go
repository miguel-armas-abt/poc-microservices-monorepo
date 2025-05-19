package handler

import "com.demo.poc/pck/core/errors/selector"

func NewErrorHandler(sel *selector.ResponseErrorSelector) *ErrorHandler {
	return &ErrorHandler{selector: sel}
}
