package com.demo.bbq.repository.menu.menuv2;

import static com.demo.bbq.repository.menu.menuv2.MenuV2Config.MENU_V2_SERVICE_NAME;

import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.repository.menu.MenuRepositoryHelper;
import com.demo.bbq.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.repository.menu.wrapper.request.MenuOptionSaveRequestWrapper;
import com.demo.bbq.repository.menu.wrapper.request.MenuOptionUpdateRequestWrapper;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MenuV2RepositoryHelper implements MenuRepositoryHelper {

  private final MenuV2Repository menuV2Repository;
  private final ServiceConfigurationProperties properties;

  @Override
  public Observable<MenuOptionResponseWrapper> findByCategory(HttpServletRequest httpRequest, String category) {
    return menuV2Repository.searchByCategory(getHeaders(httpRequest), category);
  }

  @Override
  public Maybe<MenuOptionResponseWrapper> findByProductCode(HttpServletRequest httpRequest, String productCode) {
    return menuV2Repository.findByProductCode(getHeaders(httpRequest), productCode)
        .toMaybe();
  }

  @Override
  public Completable save(HttpServletRequest httpRequest, MenuOptionSaveRequestWrapper menuOption) {
    return menuV2Repository.save(getHeaders(httpRequest), menuOption)
        .ignoreElement();
  }

  @Override
  public Completable update(HttpServletRequest httpRequest, String productCode, MenuOptionUpdateRequestWrapper menuOption) {
    return menuV2Repository.update(getHeaders(httpRequest), productCode, menuOption)
        .ignoreElement();
  }

  @Override
  public Completable delete(HttpServletRequest httpRequest, String productCode) {
    return menuV2Repository.delete(getHeaders(httpRequest), productCode)
        .ignoreElement();
  }

  @Override
  public boolean supports(Class<?> selectedClass) {
    return this.getClass().isAssignableFrom(selectedClass);
  }

  private Map<String, String> getHeaders(HttpServletRequest httpRequest) {
    return properties.searchHeaders(httpRequest, MENU_V2_SERVICE_NAME);
  }
}
