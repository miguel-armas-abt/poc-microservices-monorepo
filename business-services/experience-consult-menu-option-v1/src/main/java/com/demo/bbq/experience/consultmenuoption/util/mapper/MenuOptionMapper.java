package com.demo.bbq.experience.consultmenuoption.util.mapper;

import com.demo.bbq.experience.consultmenuoption.util.model.dto.response.MenuOptionResponse;
import com.demo.bbq.experience.consultmenuoption.util.model.dto.thirdparty.MenuOptionThird;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuOptionMapper {

  MenuOptionResponse fromThirdToResponse(MenuOptionThird menuOption);
}
