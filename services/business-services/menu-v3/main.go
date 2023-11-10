package main

import (
	"github.com/gin-gonic/gin"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"menu-v3/application/service"
	"menu-v3/infrastructure/expose/webservice/rest"
	"menu-v3/infrastructure/repository"
	"menu-v3/infrastructure/repository/entity"
)

func main() {
	db := setupDatabase()
	menuOptionRepository := repository.NewMenuOptionRepository(db)
	menuOptionService := service.NewMenuOptionService(menuOptionRepository)
	menuOptionRestService := rest.NewMenuOptionRestService(menuOptionService)

	router := gin.Default()

	menuOptionRouter := router.Group("/bbq/business/menu/v3/menu-options")
	{
		menuOptionRouter.GET("", menuOptionRestService.FindAll)
		menuOptionRouter.GET("/:id", menuOptionRestService.FindById)
		menuOptionRouter.POST("", menuOptionRestService.Save)
		menuOptionRouter.PUT("/:id", menuOptionRestService.Update)
		menuOptionRouter.DELETE("/:id", menuOptionRestService.Delete)
	}

	router.Run(":8017")
}

func setupDatabase() *gorm.DB {
	dsn := "bbq_user:qwerty@tcp(127.0.0.1:3306)/db_menu_options?charset=utf8mb4&parseTime=True&loc=Local"
	db, err := gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		panic("[LOG] Failed to connecto to MYSQL database")
	}
	err = db.AutoMigrate(&entity.MenuOption{})
	if err != nil {
		panic("[LOG] Failed to auto migrate menu_option table")
	}
	return db
}
