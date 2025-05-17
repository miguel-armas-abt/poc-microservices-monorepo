package dto

import "net/http"

type ApiErrorType struct {
	Code        string
	Description string
	Status      int
}

var (
	BusinessRules = ApiErrorType{Code: "01", Description: "/errors/business-rules", Status: http.StatusBadRequest}
	AuthRules     = ApiErrorType{Code: "02", Description: "/errors/auth-rules", Status: http.StatusUnauthorized}
	NoData        = ApiErrorType{Code: "03", Description: "/errors/no-data", Status: http.StatusBadRequest}
)
