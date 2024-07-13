package com.demo.bbq.entrypoint.menu.rest;

import com.demo.bbq.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.response.MenuResponseDTO;
import com.demo.bbq.entrypoint.menu.service.MenuService;
import com.demo.bbq.entrypoint.menu.dto.request.MenuUpdateRequestDTO;
import java.net.URI;
import java.util.List;
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
public class MenuRestServiceImpl implements MenuRestService {

  private final MenuService service;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{productCode}")
  public ResponseEntity<MenuResponseDTO> findByProductCode(HttpServletRequest servletRequest,
                                                           @PathVariable(name = "productCode") String productCode) {
    return ResponseEntity.ok(service.findByProductCode(servletRequest, productCode));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<MenuResponseDTO>> findByCategory(HttpServletRequest servletRequest,
                                                              @RequestParam(value = "category", required = false) String categoryCode) {
    List<MenuResponseDTO> menuOptionList = service.findByCategory(servletRequest, categoryCode);
    return (menuOptionList == null || menuOptionList.isEmpty())
        ? ResponseEntity.noContent().build()
        : ResponseEntity.ok(menuOptionList);
  }

  @PostMapping
  public ResponseEntity<Void> save(HttpServletRequest servletRequest,
                                   @Valid @RequestBody MenuSaveRequestDTO menuOption) {
    service.save(servletRequest, menuOption);
    return ResponseEntity.created(buildPostUriLocation.apply(menuOption.getProductCode())).build();
  }

  @PutMapping(value = "/{productCode}")
  public ResponseEntity<Void> update(HttpServletRequest servletRequest,
                                     @Valid @RequestBody MenuUpdateRequestDTO menuOption, @PathVariable("productCode") String productCode) {
    service.update(servletRequest, productCode, menuOption);
    return ResponseEntity.created(buildUriLocation.apply(productCode)).build();
  }

  @DeleteMapping(value = "/{productCode}")
  public ResponseEntity<Void> delete(HttpServletRequest servletRequest, @PathVariable("productCode") String productCode) {
    service.deleteByProductCode(servletRequest, productCode);
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
