package config

import (
	"github.com/spf13/viper"
)

type ApplicationProperties struct {
	ErrorMessages map[string]string `mapstructure:"errorMessages"`
}

var Properties ApplicationProperties

func LoadProperties(path string) error {
	v := viper.New()
	v.SetConfigFile(path)
	v.AutomaticEnv()

	if err := v.ReadInConfig(); err != nil {
		return err
	}
	return v.Unmarshal(&Properties)
}
