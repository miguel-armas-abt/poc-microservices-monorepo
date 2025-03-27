package com.demo.poc.commons.errors.dto;

import com.demo.poc.commons.properties.ConfigurationBaseProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO implements Serializable  {

    public static final String CODE_DEFAULT = "Default";
    public static final String CODE_EMPTY = "Empty";

    @JsonProperty("type")
    private ErrorType type;

    private String code;

    private String message;

    public static ErrorDTO getDefaultError(ConfigurationBaseProperties properties) {
        return ErrorDTO
            .builder()
            .code(CODE_DEFAULT)
            .message(getMatchMessage(properties, CODE_DEFAULT))
            .type(ErrorType.SYSTEM)
            .build();
    }

    public static String getMatchMessage(ConfigurationBaseProperties properties, String errorCode) {
        return properties
            .errorMessages()
            .get()
            .messages()
            .get(errorCode);
    }
}