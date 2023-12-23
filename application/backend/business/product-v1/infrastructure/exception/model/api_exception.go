package model

type ApiException struct {
	Type      string `json:"type"`
	Message   string `json:"message"`
	ErrorCode string `json:"errorCode"`
}
