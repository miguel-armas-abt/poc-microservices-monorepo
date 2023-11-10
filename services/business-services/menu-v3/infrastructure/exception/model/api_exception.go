package model

type ApiException struct {
	Type      string `json:"type"`
	Title     string `json:"title"`
	ErrorCode string `json:"errorCode"`
}
