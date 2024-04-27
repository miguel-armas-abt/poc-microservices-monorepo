package com.demo.bbq.utils.errors.dto;

import com.demo.bbq.utils.errors.external.ExternalClientErrorWrapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
public class ErrorDTO extends ExternalClientErrorWrapper implements Serializable  {

    @Serial
    private static final long serialVersionUID = 281390055501829628L;

    public static final String CODE_DEFAULT = "Default";

    @JsonProperty("type")
    private ErrorType type;

    private String code;

    private String message;
}
