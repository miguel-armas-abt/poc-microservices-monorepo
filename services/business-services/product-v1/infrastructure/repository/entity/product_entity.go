package entity

type ProductEntity struct {
	Id        uint    `gorm:"primaryKey"`
	IsActive  bool    `gorm:"column:is_active"`
	Code      string  `gorm:"column:code;unique"`
	UnitPrice float64 `gorm:"column:unit_price"`
	Scope     string  `gorm:"column:scope"`
}

func (ProductEntity) TableName() string {
	return "products"
}
