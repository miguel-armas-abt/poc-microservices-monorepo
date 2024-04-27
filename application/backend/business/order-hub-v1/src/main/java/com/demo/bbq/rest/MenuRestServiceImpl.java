package com.demo.bbq.rest;

import com.demo.bbq.repository.menu.MenuRepositoryHelper;
import com.demo.bbq.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import io.reactivex.rxjava3.core.Observable;
import jakarta.servlet.http.HttpServletRequest;
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