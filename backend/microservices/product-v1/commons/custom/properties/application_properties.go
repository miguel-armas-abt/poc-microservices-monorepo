package properties

import (
	"strings"

	"poc/commons/core/constants"
	db "poc/commons/custom/properties/database"
	logging "poc/commons/custom/properties/logging"
	"poc/commons/custom/properties/restclient"
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
	Port        string   `mapstructure:"port"`
	CorsOrigins []string `mapstructure:"corsOrigins"`
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
