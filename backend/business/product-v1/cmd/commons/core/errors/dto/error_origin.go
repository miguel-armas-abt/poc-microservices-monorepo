package dto

type ErrorOrigin struct {
	Code string
}

var (
	Own        = ErrorOrigin{Code: "OWN"}
	Partner    = ErrorOrigin{Code: "PARTNER"}
	ThirdParty = ErrorOrigin{Code: "THIRD_PARTY"}
)
