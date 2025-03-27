package com.demo.poc.entrypoint.menu.graph;

import com.demo.poc.entrypoint.menu.dto.response.MenuResponseDTO;
import com.demo.poc.entrypoint.menu.service.MenuService;
import io.smallrye.mutiny.Uni;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.graphql.*;

@GraphQLApi
@RequiredArgsConstructor
public class MenuGraphService {

  private final MenuService menuService;

  @Query
  public Uni<MenuResponseDTO> findById(@Name("productCode") String productCode) {
    return menuService.findByProductCode(productCode);
  }

  @Query("findByCategory")
  @Description("Get menu options by category")
  public Uni<List<MenuResponseDTO>> findByCategory(@Name("categoryCode") String categoryCode) {
    return null;
//    return menuOptionService.findByCategory(categoryCode);
  }

//  @Mutation
//  public Uni<Void> save(MenuOptionSaveRequest menuOptionRequest) {
//    return menuOptionService.save(menuOptionRequest);
//  }
//
//  @Mutation
//  public Uni<Void> update(MenuOptionUpdateRequest menuOptionRequest, String productCode) {
//    return menuOptionService.update(menuOptionRequest, productCode);
//  }
//
//  @Mutation
//  public Uni<Void> deleteById(String productCode) {
//    return menuOptionService.deleteByProductCode(productCode);
//  }
}
