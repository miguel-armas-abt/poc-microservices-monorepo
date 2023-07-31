package response

type MenuOptionResponse struct {
	Id          uint    `json:"id"`
	IsActive    bool    `json:"is_active"`
	Category    string  `json:"category"`
	Description string  `json:"description"`
	Price       float64 `json:"price"`
}
