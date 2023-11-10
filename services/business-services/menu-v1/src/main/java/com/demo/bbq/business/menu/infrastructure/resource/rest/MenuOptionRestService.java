package com.demo.bbq.business.menu.infrastructure.resource.rest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import com.demo.bbq.business.menu.domain.model.request.MenuOptionRequest;
import com.demo.bbq.business.menu.domain.model.response.MenuOption;
import com.demo.bbq.business.menu.infrastructure.documentation.data.MenuOptionDocumentationMetadata;
import com.demo.bbq.business.menu.infrastructure.documentation.data.MenuOptionExample;
import com.demo.bbq.support.exception.documentation.ApiExceptionJsonExample;
import com.demo.bbq.support.exception.model.dto.ApiExceptionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

@ApiResponses(value = {
    @ApiResponse(responseCode = "400",
        content = @Content(schema = @Schema(implementation = ApiExceptionDto.class), examples = @ExampleObject(value = ApiExceptionJsonExample.BAD_REQUEST))),
    @ApiResponse(responseCode = "404",
        content = @Content(schema = @Schema(implementation = ApiExceptionDto.class), examples = @ExampleObject(value = ApiExceptionJsonExample.NOT_FOUND))),
})
public interface MenuOptionRestService {

  @Operation(summary = "Get a menu by ID", tags = MenuOptionDocumentationMetadata.TAG)
  @ApiResponse(responseCode = "200")
  ResponseEntity<MenuOption> findById(HttpServletRequest servletRequest,
                                      @Parameter(example = MenuOptionExample.ID) Long id);

  @Operation(summary = "Get a menu list by category", tags = MenuOptionDocumentationMetadata.TAG)
  @ApiResponse(responseCode = "200")
  ResponseEntity<List<MenuOption>> findByCategory(HttpServletRequest servletRequest,
                                                  @Parameter(description = "Menu category", examples = {
                                                      @ExampleObject(value = MenuOptionExample.CATEGORY_MAIN_DISH),
                                                      @ExampleObject(value = MenuOptionExample.CATEGORY_DRINK),
                                                      @ExampleObject(value = MenuOptionExample.CATEGORY_DESSERT),
                                                  }) String categoryCode);

  @Operation(summary = "Save a menu", tags = MenuOptionDocumentationMetadata.TAG)
  @ApiResponse(responseCode = "201")
  ResponseEntity<Void> save(HttpServletRequest servletRequest,
                            @RequestBody(content = {@Content(schema = @Schema(implementation = MenuOptionRequest.class))}) MenuOptionRequest menuOption);

  @Operation(summary = "Update a menu", tags = MenuOptionDocumentationMetadata.TAG)
  @ApiResponse(responseCode = "201")
  ResponseEntity<Void> update(HttpServletRequest servletRequest,
                              @RequestBody(content = {@Content(schema = @Schema(implementation = MenuOptionRequest.class))}) MenuOptionRequest menuOption,
                              @Parameter(example = MenuOptionExample.ID) Long id);

  @Operation(summary = "Delete a menu", tags = MenuOptionDocumentationMetadata.TAG)
  @ApiResponse(responseCode = "204")
  ResponseEntity<Void> delete(HttpServletRequest servletRequest,
                              @Parameter(example = MenuOptionExample.ID) Long id);
}
