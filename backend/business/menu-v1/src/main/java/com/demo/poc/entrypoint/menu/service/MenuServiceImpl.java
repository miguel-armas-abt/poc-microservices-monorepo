package com.demo.poc.entrypoint.menu.service;

import com.demo.poc.entrypoint.menu.dto.request.MenuSaveRequestDto;
import com.demo.poc.entrypoint.menu.dto.request.MenuUpdateRequestDto;
import com.demo.poc.entrypoint.menu.dto.response.MenuResponseDto;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

  private final MenuProductMatcher menuProductMatcher;

  @Override
  public Multi<MenuResponseDto> findByCategory(String categoryCode) {
    return menuProductMatcher.findAll()
        .onItem()
        .transformToMulti(list -> Multi.createFrom().iterable(list))
        .filter(menu -> Optional.ofNullable(categoryCode).isEmpty() || categoryCode.equals(menu.getCategory()));
  }

  @Override
  public Uni<MenuResponseDto> findByProductCode(String productCode) {
    return menuProductMatcher.findAll()
        .onItem()
        .transformToMulti(list -> Multi.createFrom().iterable(list))
        .filter(menu -> productCode.equals(menu.getProductCode()))
        .collect()
        .first();
  }

  @Override
  public Uni<Void> save(MenuSaveRequestDto menuOptionRequest) {
    return menuProductMatcher.save(menuOptionRequest);
  }

  @Override
  public Uni<Void> update(MenuUpdateRequestDto menuOptionRequest, String productCode) {
    return menuProductMatcher.update(productCode, menuOptionRequest);
  }

  @Override
  public Uni<Void> deleteByProductCode(String productCode) {
    return menuProductMatcher.deleteByProductCode(productCode);
  }
}
