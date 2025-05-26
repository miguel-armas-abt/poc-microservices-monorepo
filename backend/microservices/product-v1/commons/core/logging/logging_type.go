package logging

type LoggingType string

const (
	ErrorLog         LoggingType = "error"
	RestServerReqLog LoggingType = "rest.server.req"
	RestServerResLog LoggingType = "rest.server.res"
	RestClientReqLog LoggingType = "rest.client.req"
	RestClientResLog LoggingType = "rest.client.res"
)
