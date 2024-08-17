package com.demo.bbq.commons.toolkit.validator.headers;

import com.demo.bbq.commons.toolkit.validator.params.DefaultParams;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultHeaders extends DefaultParams implements Serializable {

    @Pattern(regexp = "^(web|app|WEB|APP)$")
    private String channelId;

    private String traceId;
}