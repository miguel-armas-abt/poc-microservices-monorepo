package request

type MenuOptionRequest struct {
	IsActive    bool    `json:"is_active"`
	Category    string  `json:"category"`
	Description string  `json:"description"`
	Price       float64 `json:"price"`
}
