package main

import (
	"com.demo.poc/cmd/commons/custom/config"
	"com.demo.poc/cmd/commons/custom/injection"
)

func main() {
	router := injection.Rest()
	router.Run(config.ApplicationPort)
}
