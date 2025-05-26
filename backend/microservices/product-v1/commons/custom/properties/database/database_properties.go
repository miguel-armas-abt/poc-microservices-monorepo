package properties

type DatabaseProperties struct {
	User     string `mapstructure:"user"`
	Password string `mapstructure:"password"`
	Host     string `mapstructure:"host"`
	Name     string `mapstructure:"name"`
}
