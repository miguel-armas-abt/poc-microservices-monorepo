package dto

type ErrorDto struct {
	Origin  string `json:"origin"`
	Code    string `json:"code"`
	Message string `json:"message"`
}

const DefaultCode = "Default"
