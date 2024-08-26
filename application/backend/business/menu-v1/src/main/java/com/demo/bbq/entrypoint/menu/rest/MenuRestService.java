package com.demo.bbq.entrypoint.menu.rest;

import static com.demo.bbq.commons.toolkit.params.filler.HttpHeadersFiller.extractHeadersAsMap;
import static com.demo.bbq.entrypoint.menu.constants.ParameterConstants.CATEGORY_PARAM;
import static com.demo.bbq.entrypoint.menu.constants.ParameterConstants.PRODUCT_CODE_PARAM;

import com.demo.bbq.commons.toolkit.validator.headers.DefaultHeaders;
import com.demo.bbq.commons.toolkit.validator.headers.HeaderValidator;
import com.demo.bbq.commons.toolkit.validator.params.ParamValidator;
import com.demo.bbq.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.response.MenuResponseDTO;
import com.demo.bbq.entrypoint.menu.dto.params.CategoryParam;
import com.demo.bbq.entrypoint.menu.service.MenuService;
import com.demo.bbq.entrypoint.menu.dto.request.MenuUpdateRequestDTO;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/bbq/business/menu/v1/menu-options", produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuRestService {

  private final MenuService service;
  private final ParamValidator paramValidator;
  private final HeaderValidator headerValidator;

  @GetMapping(value = "/{productCode}")
  public ResponseEntity<MenuResponseDTO> findByProductCode(HttpServletRequest servletRequest,
                                                           @PathVariable(name = PRODUCT_CODE_PARAM) String productCode) {
    return Optional.of(extractHeadersAsMap(servletRequest))
        .stream()
        .peek(headers -> headerValidator.validate(headers, DefaultHeaders.class))
        .findFirst()
        .map(headers -> ResponseEntity.ok(service.findByProductCode(headers, productCode)))
        .orElseGet(() -> ResponseEntity.badRequest().build());
  }

  @GetMapping
  public ResponseEntity<List<MenuResponseDTO>> findByCategory(HttpServletRequest servletRequest,
                                                              @RequestParam(value = CATEGORY_PARAM, required = false) String categoryCode) {
    return Optional.of(extractHeadersAsMap(servletRequest)).stream()
        .peek(headers -> headerValidator.validate(headers, DefaultHeaders.class))
        .findFirst()
        .map(headers -> {
          Map<String, String> queryParams = new HashMap<>() {{put(CATEGORY_PARAM, categoryCode); }};
          CategoryParam categoryParam = paramValidator.validateAndRetrieve(queryParams, CategoryParam.class);
          return service.findByCategory(headers, categoryParam.getCategory());
        })
        .filter(menuList -> !menuList.isEmpty())
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.noContent().build());
  }

  @PostMapping
  public ResponseEntity<Void> save(HttpServletRequest servletRequest,
                                   @Valid @RequestBody MenuSaveRequestDTO menuOption) {
    Map<String, String> headers = extractHeadersAsMap(servletRequest);
    headerValidator.validate(headers, DefaultHeaders.class);
    return ResponseEntity.created(buildPostUriLocation.apply(menuOption.getProductCode())).build();
  }

  @PutMapping(value = "/{productCode}")
  public ResponseEntity<Void> update(HttpServletRequest servletRequest,
                                     @Valid @RequestBody MenuUpdateRequestDTO menuOption,
                                     @PathVariable(PRODUCT_CODE_PARAM) String productCode) {
    Map<String, String> headers = extractHeadersAsMap(servletRequest);
    headerValidator.validate(headers, DefaultHeaders.class);

    service.update(headers, productCode, menuOption);
    return ResponseEntity.created(buildUriLocation.apply(productCode)).build();
  }

  @DeleteMapping(value = "/{productCode}")
  public ResponseEntity<Void> delete(HttpServletRequest servletRequest,
                                     @PathVariable(PRODUCT_CODE_PARAM) String productCode) {
    Map<String, String> headers = extractHeadersAsMap(servletRequest);
    headerValidator.validate(headers, DefaultHeaders.class);

    service.deleteByProductCode(headers, productCode);
    return ResponseEntity.noContent().location(buildUriLocation.apply(productCode)).build();
  }

  private final static Function<String, URI> buildPostUriLocation = productCode ->
      ServletUriComponentsBuilder.fromCurrentRequest()
          .path("/{productCode}")
          .buildAndExpand(productCode)
          .toUri();

  private final static Function<String, URI> buildUriLocation = productCode ->
      ServletUriComponentsBuilder.fromCurrentRequest()
          .buildAndExpand()
          .toUri();

}
