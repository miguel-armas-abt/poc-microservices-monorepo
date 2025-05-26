package logging

type RestRequestLog struct {
	Method      string            `json:"method"`
	URI         string            `json:"uri"`
	Headers     map[string]string `json:"headers"`
	Body        string            `json:"body"`
	TraceParent string            `json:"trace_parent"`
}
