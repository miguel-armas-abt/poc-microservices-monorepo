package com.demo.bbq.business;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class DiningRoomOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiningRoomOrderApplication.class, args);
	}

	@Bean
	public GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder()
				.group("springshop-public")
				.packagesToScan("com.demo.bbq.business.menuoption")
				.build();
	}
}
