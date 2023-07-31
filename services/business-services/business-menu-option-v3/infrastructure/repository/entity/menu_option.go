package entity

type MenuOption struct {
	Id          uint    `gorm:"primaryKey"`
	IsActive    bool    `gorm:"column:is_active"`
	Category    string  `gorm:"column:category"`
	Description string  `gorm:"column:description"`
	Price       float64 `gorm:"column:price"`
}

func (MenuOption) TableName() string {
	return "menu_options"
}
