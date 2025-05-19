package main

import (
	"log"

	"com.demo.poc/commons/injection"
	properties "com.demo.poc/commons/properties"
)

func main() {
	if err := properties.Init(); err != nil {
		log.Fatalf("properties load error: %v", err)
	}

	router := injection.NewEngine()
	router.Run(properties.Properties.Server.Port)
}
