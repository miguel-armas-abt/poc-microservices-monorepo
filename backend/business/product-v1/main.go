package main

import (
	_ "embed"
	"strings"

	"poc/commons/core/constants"
	"poc/commons/custom/injection"
	properties "poc/commons/custom/properties"
)

//go:embed resources/application.yaml
var applicationYAML []byte

func main() {
	router := injection.NewEngine(applicationYAML)

	serverPort := properties.Properties.Server.Port
	if !strings.HasPrefix(serverPort, constants.COLON) {
		serverPort = constants.COLON + serverPort
	}
	router.Run(serverPort)
}
