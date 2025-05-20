package main

import (
	"log"
	"net/http"

	"com.demo.poc/commons/injection"
	"com.demo.poc/commons/interceptor/restclient"
	"com.demo.poc/commons/logging"
	properties "com.demo.poc/commons/properties"
	"github.com/sirupsen/logrus"
)

func main() {
	logging.InitLogger(logrus.InfoLevel)

	if err := properties.Init(); err != nil {
		log.Fatalf("properties load error: %v", err)
	}

	http.DefaultClient.Transport = restclient.NewRestClientInterceptor(http.DefaultTransport, &properties.Properties)

	router := injection.NewEngine()
	router.Run(properties.Properties.Server.Port)
}
