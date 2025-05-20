package dto

type ErrorOrigin string

const (
	ERROR_ORIGIN_OWN         ErrorOrigin = "OWN"
	ERROR_ORIGIN_PARTNER     ErrorOrigin = "PARTNER"
	ERROR_ORIGIN_THIRD_PARTY ErrorOrigin = "THIRD_PARTY"
)
