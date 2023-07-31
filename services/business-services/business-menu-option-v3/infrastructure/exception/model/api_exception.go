package model

type APIException struct {
	Type      string `json:"type"`
	Title     string `json:"title"`
	ErrorCode string `json:"errorCode"`
}
