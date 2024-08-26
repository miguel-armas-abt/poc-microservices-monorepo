package com.demo.bbq.commons.toolkit.validator.headers;

import com.demo.bbq.commons.toolkit.validator.params.DefaultParams;
import com.demo.bbq.commons.toolkit.validator.utils.ParamName;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty
    @ParamName("channel-id")
    private String channelId;

    @NotEmpty
    @ParamName("trace-id")
    private String traceId;
}