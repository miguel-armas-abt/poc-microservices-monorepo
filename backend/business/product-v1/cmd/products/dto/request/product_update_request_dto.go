package request

type ProductUpdateRequestDto struct {
	UnitPrice float64 `json:"unitPrice" validate:"required"`
	Scope     string  `json:"scope" validate:"required"`
}
