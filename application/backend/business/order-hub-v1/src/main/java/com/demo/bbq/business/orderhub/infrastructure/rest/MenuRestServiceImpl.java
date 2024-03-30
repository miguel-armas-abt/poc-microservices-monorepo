package com.demo.bbq.business.orderhub.infrastructure.rest;

import com.demo.bbq.business.orderhub.domain.repository.menu.MenuRepositoryHelper;
import com.demo.bbq.business.orderhub.domain.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import io.reactivex.Observable;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@RequiredArgsConstructor
public class MenuRestServiceImpl extends OrderHubRestService {

  private final MenuRepositoryHelper menuRepositoryHelper;

  @GetMapping(value = "/menu-options", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Observable<MenuOptionResponseWrapper> findMenuByCategory(HttpServletRequest servletRequest,
                                                                  @RequestParam(value = "category") String categoryCode) {
    return menuRepositoryHelper.getService().findByCategory(categoryCode);
  }
}