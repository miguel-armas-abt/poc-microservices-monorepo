package com.demo.bbq.business.menuoption.infrastructure.graphql;

import com.demo.bbq.business.menuoption.application.dto.response.MenuOptionResponseDTO;
import com.demo.bbq.business.menuoption.application.service.MenuOptionService;
import io.smallrye.mutiny.Uni;
import java.util.List;
import javax.inject.Inject;
import org.eclipse.microprofile.graphql.*;

@GraphQLApi
public class MenuOptionGraphQLService {

  @Inject
  MenuOptionService menuOptionService;

  @Query
  public Uni<MenuOptionResponseDTO> findById(@Name("productCode") String productCode) {
    return menuOptionService.findByProductCode(productCode);
  }

  @Query("findByCategory")
  @Description("Get menu options by category")
  public Uni<List<MenuOptionResponseDTO>> findByCategory(@Name("categoryCode") String categoryCode) {
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
