package properties

import (
	"fmt"

	"github.com/spf13/viper"
)

var Properties ApplicationProperties

func Init() error {
	reader := viper.New()
	reader.SetConfigName("application")
	reader.SetConfigType("yaml")
	reader.AddConfigPath(".")
	reader.AddConfigPath("./resources")
	reader.AutomaticEnv()

	if err := reader.ReadInConfig(); err != nil {
		return fmt.Errorf("error reading application.yaml: %w", err)
	}

	if err := reader.Unmarshal(&Properties); err != nil {
		return fmt.Errorf("error deserializing config: %w", err)
	}

	return nil
}
