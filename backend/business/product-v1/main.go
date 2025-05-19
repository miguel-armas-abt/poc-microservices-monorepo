package main

import (
	"log"

	properties "com.demo.poc/commons/core/properties"
	customConfig "com.demo.poc/commons/custom/config"
	"com.demo.poc/commons/custom/injection"
)

func main() {
	if err := properties.Init(); err != nil {
		log.Fatalf("properties load error: %v", err)
	}

	router := injection.NewEngine()
	router.Run(customConfig.ApplicationPort)
}
