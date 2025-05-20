package properties

import (
	db "com.demo.poc/commons/properties/database"
	"com.demo.poc/commons/properties/restclient"
)

type ApplicationProperties struct {
	ErrorMessages map[string]string                `mapstructure:"errorMessages"`
	ProjectType   ProjectType                      `mapstructure:"projectType"`
	Database      db.DatabaseProperties            `mapstructure:"database"`
	Server        ServerProperties                 `mapstructure:"server"`
	RestClients   map[string]restclient.RestClient `mapstructure:"restClients"`
}

type ServerProperties struct {
	Port string `mapstructure:"port"`
}
