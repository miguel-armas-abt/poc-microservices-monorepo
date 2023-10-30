package com.demo.bbq.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MenuOptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MenuOptionApplication.class, args);
	}

}
