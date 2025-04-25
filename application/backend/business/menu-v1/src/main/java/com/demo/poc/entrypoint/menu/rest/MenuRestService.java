package com.demo.poc.entrypoint.menu.rest;

import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import com.demo.poc.commons.core.validations.ParamValidator;
import com.demo.poc.entrypoint.menu.params.CategoryParam;
import com.demo.poc.entrypoint.menu.dto.request.MenuSaveRequestDto;
import com.demo.poc.entrypoint.menu.dto.request.MenuUpdateRequestDto;
import com.demo.poc.entrypoint.menu.dto.response.MenuResponseDto;
import com.demo.poc.entrypoint.menu.service.MenuService;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.demo.poc.commons.core.restserver.utils.RestServerExtractor.extractHeadersAsMap;
import static com.demo.poc.commons.core.restserver.utils.RestServerExtractor.extractQueryParamsAsMap;
import static com.demo.poc.entrypoint.menu.constants.ParameterConstants.PRODUCT_CODE_PARAM;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/poc/business/menu/v1/menu-options", produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuRestService {

  private final MenuService service;
  private final ParamValidator paramValidator;

  @GetMapping(value = "/{productCode}")
  public ResponseEntity<MenuResponseDto> findByProductCode(HttpServletRequest servletRequest,
                                                           @PathVariable(name = PRODUCT_CODE_PARAM) String productCode) {
    return Optional.of(extractHeadersAsMap(servletRequest))
        .stream()
        .peek(headers -> paramValidator.validate(headers, DefaultHeaders.class))
        .findFirst()
        .map(headers -> ResponseEntity.ok(service.findByProductCode(headers, productCode)))
        .orElseGet(() -> ResponseEntity.badRequest().build());
  }

  @GetMapping
  public ResponseEntity<List<MenuResponseDto>> findByCategory(HttpServletRequest servletRequest) {
    return Optional.of(extractHeadersAsMap(servletRequest)).stream()
        .peek(headers -> paramValidator.validate(headers, DefaultHeaders.class))
        .findFirst()
        .map(headers -> {
          CategoryParam categoryParam = paramValidator.validateAndRetrieve(extractQueryParamsAsMap(servletRequest), CategoryParam.class);
          return service.findByCategory(headers, categoryParam.getCategory());
        })
        .filter(menuList -> !menuList.isEmpty())
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.noContent().build());
  }

  @PostMapping
  public ResponseEntity<Void> save(HttpServletRequest servletRequest,
                                   @Valid @RequestBody MenuSaveRequestDto menuOption) {
    Map<String, String> headers = extractHeadersAsMap(servletRequest);
    paramValidator.validate(headers, DefaultHeaders.class);
    return ResponseEntity.created(buildPostUriLocation.apply(menuOption.getProductCode())).build();
  }

  @PutMapping(value = "/{productCode}")
  public ResponseEntity<Void> update(HttpServletRequest servletRequest,
                                     @Valid @RequestBody MenuUpdateRequestDto menuOption,
                                     @PathVariable(PRODUCT_CODE_PARAM) String productCode) {
    Map<String, String> headers = extractHeadersAsMap(servletRequest);
    paramValidator.validate(headers, DefaultHeaders.class);

    service.update(headers, productCode, menuOption);
    return ResponseEntity.created(buildUriLocation.apply(productCode)).build();
  }

  @DeleteMapping(value = "/{productCode}")
  public ResponseEntity<Void> delete(HttpServletRequest servletRequest,
                                     @PathVariable(PRODUCT_CODE_PARAM) String productCode) {
    Map<String, String> headers = extractHeadersAsMap(servletRequest);
    paramValidator.validate(headers, DefaultHeaders.class);

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
