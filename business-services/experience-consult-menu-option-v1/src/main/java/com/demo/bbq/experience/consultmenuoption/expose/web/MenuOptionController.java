package com.demo.bbq.experience.consultmenuoption.expose.web;

import com.demo.bbq.experience.consultmenuoption.service.MenuOptionService;
import com.demo.bbq.experience.consultmenuoption.util.model.dto.request.MenuOptionRequest;
import com.demo.bbq.experience.consultmenuoption.util.model.dto.response.MenuOptionResponse;
import java.net.URI;
import java.util.List;
import java.util.function.Supplier;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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

/**
 * <br/>Clase Controller que implementa los métodos necesarios para exponer mediante REST los servicios del contexto
 * Menu Option.<br/>
 *
 * @author Miguel Armas Abt <br/>
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/bbq/experience/v1/menu-options")
public class MenuOptionController {

  private final MenuOptionService service;

  @GetMapping(produces = "application/json", value = "/{id}")
  public ResponseEntity<MenuOptionResponse> findById(@PathVariable(name = "id") Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @GetMapping(produces = "application/json")
  public ResponseEntity<List<MenuOptionResponse>> findByCategory(
      @RequestParam(value = "category", required = false) String category) {
    return ResponseEntity.ok(service.findByCategory(category));
  }

  @PostMapping
  public ResponseEntity<Void> save(@Valid @RequestBody MenuOptionRequest menuOption) {
    service.save(menuOption);
    return ResponseEntity.created(buildUriLocation.get()).build();
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<Void> update(@Valid @RequestBody MenuOptionRequest menuOption, @PathVariable("id") Long id) {
    service.update(id, menuOption);
    return ResponseEntity.created(buildUriLocation.get()).build();
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    service.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  private final static Supplier<URI> buildUriLocation = () ->
      ServletUriComponentsBuilder.fromCurrentRequest()
          .buildAndExpand()
          .toUri();
}













