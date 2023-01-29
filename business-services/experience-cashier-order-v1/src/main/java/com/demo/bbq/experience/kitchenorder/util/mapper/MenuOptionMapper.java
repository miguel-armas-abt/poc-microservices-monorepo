package com.demo.bbq.experience.kitchenorder.util.mapper;

import com.demo.bbq.experience.kitchenorder.util.model.dto.response.MenuOptionResponse;
import com.demo.bbq.experience.kitchenorder.util.model.dto.thirdparty.MenuOptionThird;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuOptionMapper {

  MenuOptionResponse fromThirdToResponse(MenuOptionThird menuOption);
}
