package com.demo.bbq.experience.consultmenuoption.util.config;

import com.demo.bbq.experience.consultmenuoption.repository.proxy.MenuOptionProxy;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Clase Config que implementa las configuraciones para conectar con otras APIs RESTful.<br/>
 *
 * @author Miguel Armas Abt <br/>
 */
@Configuration
public class RestClientConfig {

  @Value("${application.http-client.menu-options.base-url}")
  private String menuOptionsBaseUrl;

  @Bean
  MenuOptionProxy menuOptionApi(OkHttpClient.Builder builder) {
    return new Retrofit.Builder()
        .baseUrl(menuOptionsBaseUrl)
        .client(client().build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(MenuOptionProxy.class);
  }

  @Bean
  OkHttpClient.Builder client() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
    return new OkHttpClient.Builder().addInterceptor(interceptor);
  }
}
