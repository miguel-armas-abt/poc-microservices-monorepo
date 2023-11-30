package com.demo.bbq.business.menu.infrastructure.resource.rest;

import com.demo.bbq.business.menu.application.service.MenuOptionService;
import com.demo.bbq.business.menu.domain.model.request.MenuOptionSaveRequest;
import com.demo.bbq.business.menu.domain.model.request.MenuOptionUpdateRequest;
import com.demo.bbq.business.menu.domain.model.response.MenuOption;
import com.demo.bbq.support.logstash.Markers;
import java.net.URI;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  @GetMapping(produces = MediaType.APPLICATION_JSON, value = "/{productCode}")
  public ResponseEntity<MenuOption> findByProductCode(HttpServletRequest servletRequest,
                                             @PathVariable(name = "productCode") String productCode) {
    logRequest.accept(servletRequest);
    return ResponseEntity.ok(service.findByProductCode(productCode));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON)
  public ResponseEntity<List<MenuOption>> findByCategory(HttpServletRequest servletRequest,
                                                         @RequestParam(value = "category", required = false) String categoryCode) {
    logRequest.accept(servletRequest);
    List<MenuOption> menuOptionList = service.findByCategory(categoryCode);
    return (menuOptionList == null || menuOptionList.isEmpty())
        ? ResponseEntity.noContent().build()
        : ResponseEntity.ok(service.findByCategory(categoryCode));
  }

  @PostMapping
  public ResponseEntity<Void> save(HttpServletRequest servletRequest,
                                   @Valid @RequestBody MenuOptionSaveRequest menuOption) {
    logRequest.accept(servletRequest);
    service.save(menuOption);
    return ResponseEntity.created(buildPostUriLocation.apply(menuOption.getProductCode())).build();
  }

  @PutMapping(value = "/{productCode}")
  public ResponseEntity<Void> update(HttpServletRequest servletRequest,
                                     @Valid @RequestBody MenuOptionUpdateRequest menuOption, @PathVariable("productCode") String productCode) {
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
      log.info(Markers.SENSITIVE_JSON, "{}", servletRequest.getMethod() + ": " + servletRequest.getRequestURI());

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
