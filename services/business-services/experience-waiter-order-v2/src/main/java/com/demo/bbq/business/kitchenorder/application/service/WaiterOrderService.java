package com.demo.bbq.business.kitchenorder.application.service;

import com.demo.bbq.business.kitchenorder.domain.model.response.MenuOrder;
import io.smallrye.mutiny.Multi;

public interface WaiterOrderService {

  Multi<MenuOrder> findByCategory(Integer tableNumber);

}
