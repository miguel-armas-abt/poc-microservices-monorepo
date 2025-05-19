package request

type ProductSaveRequestDto struct {
	Code      string  `json:"code" validate:"required"`
	UnitPrice float64 `json:"unitPrice" validate:"required"`
	Scope     string  `json:"scope" validate:"required"`
}
