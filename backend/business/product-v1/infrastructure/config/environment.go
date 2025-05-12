package config

import "os"

var (
	DBUser          = getEnv("MYSQL_USERNAME", "poc_user")
	DBPassword      = getEnv("MYSQL_PASSWORD", "qwerty")
	DBHost          = getEnv("MYSQL_HOST", "127.0.0.1:3306")
	DBName          = getEnv("DATABASE", "db_products")
	ApplicationPort = getEnv("APPLICATION_PORT", ":8017")
)

func getEnv(key, defaultValue string) string {
	value := os.Getenv(key)
	if value == "" {
		return defaultValue
	}
	return value
}
