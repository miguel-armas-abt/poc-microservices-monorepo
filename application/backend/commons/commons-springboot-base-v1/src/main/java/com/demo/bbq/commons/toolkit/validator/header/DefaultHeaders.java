package com.demo.bbq.commons.toolkit.validator.header;

import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DefaultHeaders implements Serializable {

    @Pattern(regexp = "^(web|app|WEB|APP)$")
    @HeaderName("channel-id")
    private String channelId;
}
