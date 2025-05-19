package properties

type ApplicationProperties struct {
	ErrorMessages map[string]string `mapstructure:"errorMessages"`
	ProjectType   ProjectType       `mapstructure:"projectType"`
}
