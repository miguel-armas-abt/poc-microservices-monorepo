package logging

type RestResponseLog struct {
	URI         string            `json:"uri"`
	Status      int               `json:"status"`
	Headers     map[string]string `json:"headers"`
	Body        string            `json:"body"`
	TraceParent string            `json:"trace_parent"`
}
