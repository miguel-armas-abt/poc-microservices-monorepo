package com.demo.bbq.infrastructure.authadapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AuthAdapterApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthAdapterApplication.class, args);
	}

}
