package com.demo.bbq.business.orderhub.infrastructure.resource.rest;

import com.demo.bbq.support.exception.documentation.ApiExceptionJsonExample;
import com.demo.bbq.support.exception.model.dto.ApiExceptionDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@ApiResponses(value = {
    @ApiResponse(responseCode = "400",
        content = @Content(schema = @Schema(implementation = ApiExceptionDto.class), examples = @ExampleObject(value = ApiExceptionJsonExample.BAD_REQUEST))),
    @ApiResponse(responseCode = "404",
        content = @Content(schema = @Schema(implementation = ApiExceptionDto.class), examples = @ExampleObject(value = ApiExceptionJsonExample.NOT_FOUND))),
})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/bbq/experience/order-hub/v1")
public class OrderHubRestService {

}
