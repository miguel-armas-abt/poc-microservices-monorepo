package dto

type ErrorOrigin string

const (
	OriginOwn        ErrorOrigin = "OWN"
	OriginPartner    ErrorOrigin = "PARTNER"
	OriginThirdParty ErrorOrigin = "THIRD_PARTY"
)
