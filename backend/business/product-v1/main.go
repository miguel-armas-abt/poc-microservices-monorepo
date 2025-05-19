package main

import (
	"log"

	"com.demo.poc/commons/injection"
	"com.demo.poc/commons/logging"
	properties "com.demo.poc/commons/properties"
	"github.com/sirupsen/logrus"
)

func main() {
	logging.InitLogger(logrus.InfoLevel)

	if err := properties.Init(); err != nil {
		log.Fatalf("properties load error: %v", err)
	}

	router := injection.NewEngine()
	router.Run(properties.Properties.Server.Port)
}
