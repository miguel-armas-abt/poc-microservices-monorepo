package validations

type DefaultHeaders struct {
	ChannelId   string `mapstructure:"channelid" validate:"required,oneof=web app WEB APP"`
	TraceParent string `mapstructure:"traceparent" validate:"required,traceParent"`
}
