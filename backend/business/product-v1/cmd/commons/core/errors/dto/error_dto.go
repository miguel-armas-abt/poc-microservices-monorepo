package dto

type ErrorDto struct {
	Origin  string `json:"origin"`
	Message string `json:"message"`
	Code    string `json:"code"`
}
