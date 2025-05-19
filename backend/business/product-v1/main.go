package main

import (
	"log"

	coreConfing "com.demo.poc/pck/core/config"
	customConfig "com.demo.poc/pck/custom/config"
	"com.demo.poc/pck/custom/injection"
)

func main() {
	if err := coreConfing.LoadProperties("application.yaml"); err != nil {
		log.Fatalf("properties load error: %v", err)
	}

	router := injection.Rest()
	router.Run(customConfig.ApplicationPort)
}
