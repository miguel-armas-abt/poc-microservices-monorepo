package entity

type ProductEntity struct {
	Id        uint    `gorm:"primaryKey"`
	IsActive  bool    `gorm:"column:is_active;not null"`
	Code      string  `gorm:"column:code;unique;not null"`
	UnitPrice float64 `gorm:"column:unit_price;not null"`
	Scope     string  `gorm:"column:scope;not null"`
}

func (ProductEntity) TableName() string {
	return "products"
}
