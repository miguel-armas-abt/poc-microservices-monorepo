package properties

type ApplicationProperties struct {
	ErrorMessages map[string]string  `mapstructure:"errorMessages"`
	ProjectType   ProjectType        `mapstructure:"projectType"`
	Database      DatabaseProperties `mapstructure:"database"`
	Server        ServerProperties   `mapstructure:"server"`
}

type ServerProperties struct {
	Port string `mapstructure:"port"`
}
