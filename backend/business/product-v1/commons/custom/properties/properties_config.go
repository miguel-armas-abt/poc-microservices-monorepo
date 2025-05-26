package properties

import (
	"bytes"
	"fmt"
	"poc/commons/core/constants"
	"strings"

	"github.com/spf13/viper"
)

var Properties ApplicationProperties

func Init(yamlBytes []byte) {
	reader := viper.New()
	reader.SetConfigType("yaml")

	if err := reader.ReadConfig(bytes.NewBuffer(yamlBytes)); err != nil {
		panic(fmt.Sprintf("Error reading embedded application.yaml: %v", err))
	}

	reader.SetEnvKeyReplacer(strings.NewReplacer(constants.DOT, constants.UNDERSCORE))
	reader.AutomaticEnv()

	environmentVariables := []struct{ key, env string }{
		{"server.port", "APPLICATION_PORT"},
		{"database.user", "MYSQL_USERNAME"},
		{"database.password", "MYSQL_PASSWORD"},
		{"database.host", "MYSQL_HOST"},
		{"database.name", "DATABASE"},
	}
	for _, variable := range environmentVariables {
		reader.BindEnv(variable.key, variable.env)
	}

	if err := reader.Unmarshal(&Properties); err != nil {
		panic(fmt.Sprintf("Error deserializing config: %v", err))
	}
}
