package model

import "net/http"

type ApiErrorType struct {
	Code        string
	Description string
	Status      int
}

var (
	NoData           = ApiErrorType{Code: "01", Description: "/errors/no-data", Status: http.StatusBadRequest}
	BusinessRules    = ApiErrorType{Code: "02", Description: "/errors/business-rules", Status: http.StatusBadRequest}
	AuthRules        = ApiErrorType{Code: "03", Description: "/errors/auth-rules", Status: http.StatusUnauthorized}
	MalformedRequest = ApiErrorType{Code: "05", Description: "/errors/malformed-request", Status: http.StatusBadRequest}
)
