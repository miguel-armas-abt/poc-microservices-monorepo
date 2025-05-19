package properties

import (
	"fmt"
	"strings"

	"github.com/spf13/viper"
)

var Properties ApplicationProperties

func Init() error {
	reader := viper.New()
	reader.SetConfigName("application")
	reader.SetConfigType("yaml")
	reader.AddConfigPath(".")
	reader.AddConfigPath("./resources")
	reader.SetEnvKeyReplacer(strings.NewReplacer(".", "_"))
	reader.AutomaticEnv()

	reader.BindEnv("database.user", "MYSQL_USERNAME")
	reader.BindEnv("database.password", "MYSQL_PASSWORD")
	reader.BindEnv("database.host", "MYSQL_HOST")
	reader.BindEnv("database.name", "DATABASE")
	reader.BindEnv("server.port", "APPLICATION_PORT")

	if err := reader.ReadInConfig(); err != nil {
		return fmt.Errorf("error reading application.yaml: %w", err)
	}

	if err := reader.Unmarshal(&Properties); err != nil {
		return fmt.Errorf("error deserializing config: %w", err)
	}

	return nil
}
