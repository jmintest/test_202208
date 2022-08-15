package com.kakao.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SearchPlaceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchPlaceServiceApplication.class, args);
	}

}
