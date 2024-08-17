package com.demo.bbq.entrypoint.menu.rest;

import static com.demo.bbq.commons.restclient.headers.HeadersBuilderUtil.parseHeaders;
import static com.demo.bbq.entrypoint.menu.params.constant.ParameterConstants.CATEGORY_QUERY_PARAM;

import com.demo.bbq.commons.toolkit.validator.params.ParamValidator;
import com.demo.bbq.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.response.MenuResponseDTO;
import com.demo.bbq.entrypoint.menu.params.pojo.MenuByCategoryParams;
import com.demo.bbq.entrypoint.menu.service.MenuService;
import com.demo.bbq.entrypoint.menu.dto.request.MenuUpdateRequestDTO;
import java.net.URI;
import java.util.List;
import java.util.Map;
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
public class MenuRestServiceImpl {

  private final MenuService service;
  private final ParamValidator paramValidator;

  @GetMapping(value = "/{productCode}")
  public ResponseEntity<MenuResponseDTO> findByProductCode(HttpServletRequest servletRequest,
                                                           @PathVariable(name = "productCode") String productCode) {

    return ResponseEntity.ok(service.findByProductCode(parseHeaders(servletRequest), productCode));
  }

  @GetMapping
  public ResponseEntity<List<MenuResponseDTO>> findByCategory(HttpServletRequest servletRequest,
                                                              @RequestParam(value = CATEGORY_QUERY_PARAM, required = false) String categoryCode) {

    paramValidator.validate(Map.of(CATEGORY_QUERY_PARAM, categoryCode), MenuByCategoryParams.class);
    List<MenuResponseDTO> menuOptionList = service.findByCategory(parseHeaders(servletRequest), categoryCode);
    return (menuOptionList == null || menuOptionList.isEmpty())
        ? ResponseEntity.noContent().build()
        : ResponseEntity.ok(menuOptionList);
  }

  @PostMapping
  public ResponseEntity<Void> save(HttpServletRequest servletRequest,
                                   @Valid @RequestBody MenuSaveRequestDTO menuOption) {
    service.save(parseHeaders(servletRequest), menuOption);
    return ResponseEntity.created(buildPostUriLocation.apply(menuOption.getProductCode())).build();
  }

  @PutMapping(value = "/{productCode}")
  public ResponseEntity<Void> update(HttpServletRequest servletRequest,
                                     @Valid @RequestBody MenuUpdateRequestDTO menuOption,
                                     @PathVariable("productCode") String productCode) {

    service.update(parseHeaders(servletRequest), productCode, menuOption);
    return ResponseEntity.created(buildUriLocation.apply(productCode)).build();
  }

  @DeleteMapping(value = "/{productCode}")
  public ResponseEntity<Void> delete(HttpServletRequest servletRequest,
                                     @PathVariable("productCode") String productCode) {

    service.deleteByProductCode(parseHeaders(servletRequest), productCode);
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
