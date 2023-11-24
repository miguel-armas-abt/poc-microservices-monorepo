package request

type ProductSaveRequest struct {
	Code      string  `json:"code" validate:"required,notblank"`
	UnitPrice float64 `json:"unitPrice" validate:"required"`
	Scope     string  `json:"scope" validate:"required,notblank"`
}
