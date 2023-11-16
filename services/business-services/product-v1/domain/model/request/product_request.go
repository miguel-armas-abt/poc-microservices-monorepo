package request

type ProductRequest struct {
	Code      string  `json:"code"`
	UnitPrice float64 `json:"unitPrice"`
	Scope     string  `json:"scope"`
}
