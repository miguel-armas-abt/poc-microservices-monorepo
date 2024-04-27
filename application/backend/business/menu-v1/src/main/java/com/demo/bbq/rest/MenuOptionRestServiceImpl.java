package com.demo.bbq.rest;

import com.demo.bbq.application.dto.response.MenuOptionResponseDTO;
import com.demo.bbq.application.service.MenuOptionService;
import com.demo.bbq.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.application.dto.request.MenuOptionUpdateRequestDTO;
import java.net.URI;
import java.util.List;
import java.util.function.Consumer;
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
@RequestMapping("/bbq/business/menu/v1/menu-options")
public class MenuOptionRestServiceImpl implements MenuOptionRestService {

  private final MenuOptionService service;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{productCode}")
  public ResponseEntity<MenuOptionResponseDTO> findByProductCode(HttpServletRequest servletRequest,
                                                                 @PathVariable(name = "productCode") String productCode) {
    logRequest.accept(servletRequest);
    return ResponseEntity.ok(service.findByProductCode(productCode));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<MenuOptionResponseDTO>> findByCategory(HttpServletRequest servletRequest,
                                                                    @RequestParam(value = "category", required = false) String categoryCode) {
    logRequest.accept(servletRequest);
    List<MenuOptionResponseDTO> menuOptionList = service.findByCategory(categoryCode);
    return (menuOptionList == null || menuOptionList.isEmpty())
        ? ResponseEntity.noContent().build()
        : ResponseEntity.ok(service.findByCategory(categoryCode));
  }

  @PostMapping
  public ResponseEntity<Void> save(HttpServletRequest servletRequest,
                                   @Valid @RequestBody MenuOptionSaveRequestDTO menuOption) {
    logRequest.accept(servletRequest);
    service.save(menuOption);
    return ResponseEntity.created(buildPostUriLocation.apply(menuOption.getProductCode())).build();
  }

  @PutMapping(value = "/{productCode}")
  public ResponseEntity<Void> update(HttpServletRequest servletRequest,
                                     @Valid @RequestBody MenuOptionUpdateRequestDTO menuOption, @PathVariable("productCode") String productCode) {
    logRequest.accept(servletRequest);
    service.update(productCode, menuOption);
    return ResponseEntity.created(buildUriLocation.apply(productCode)).build();
  }

  @DeleteMapping(value = "/{productCode}")
  public ResponseEntity<Void> delete(HttpServletRequest servletRequest, @PathVariable("productCode") String productCode) {
    logRequest.accept(servletRequest);
    service.deleteByProductCode(productCode);
    return ResponseEntity.noContent().location(buildUriLocation.apply(productCode)).build();
  }

  private final static Consumer<HttpServletRequest> logRequest = servletRequest->
      log.info("{}", servletRequest.getMethod() + ": " + servletRequest.getRequestURI());

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
