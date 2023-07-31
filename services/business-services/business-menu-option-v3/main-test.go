package main

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

type album struct {
	Id     string `json:"id"`
	Title  string `json:"title"`
	Artist string `json:"artist"`
	Price  string `json:"price"`
}

var albumList = []album{
	{Id: "1", Title: "Rockstar", Artist: "Duki", Price: "99.9"},
	{Id: "2", Title: "She Don't Give FO", Artist: "Duki", Price: "99.9"},
	{Id: "3", Title: "Chico Estrella", Artist: "Duki", Price: "99.9"},
}

func findAll(ctx *gin.Context) {
	ctx.IndentedJSON(http.StatusOK, albumList)
}

func findById(ctx *gin.Context) {
	id := ctx.Param("id")
	for _, album := range albumList {
		if album.Id == id {
			ctx.IndentedJSON(http.StatusOK, album)
			return
		}
	}
	ctx.IndentedJSON(http.StatusNotFound, gin.H{"message": "album not found"})
}

func save(ctx *gin.Context) {
	var album album
	if err := ctx.BindJSON(&album); err != nil {
		return
	}
	albumList = append(albumList, album)
	ctx.IndentedJSON(http.StatusCreated, album)
}

func main() {
	router := gin.Default()
	router.GET("/v3/albums", findAll)
	router.GET("/v3/albums/:id", findById)
	router.POST("/v3/albums", save)
	router.Run("localhost:8080")
}
