package properties

import (
	"strings"

	"com.demo.poc/commons/constants"
	db "com.demo.poc/commons/properties/database"
	logging "com.demo.poc/commons/properties/logging"
	"com.demo.poc/commons/properties/restclient"
)

type ApplicationProperties struct {
	Server        ServerProperties                 `mapstructure:"server"`
	ProjectType   ProjectType                      `mapstructure:"projectType"`
	Logging       logging.LoggingTemplate          `mapstructure:"logging"`
	ErrorMessages map[string]string                `mapstructure:"errorMessages"`
	Database      db.DatabaseProperties            `mapstructure:"database"`
	RestClients   map[string]restclient.RestClient `mapstructure:"restClients"`
}

type ServerProperties struct {
	Port string `mapstructure:"port"`
}

func (properties *ApplicationProperties) IsLoggerEnabled(logType string) bool {
	if properties.Logging.LoggingType == nil {
		return true
	}
	key := strings.ReplaceAll(logType, constants.DOT, constants.MIDDLE_DASH)
	enabled, exists := properties.Logging.LoggingType[key]
	if !exists {
		return true
	}
	return enabled
}
