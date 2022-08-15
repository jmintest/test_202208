package com.kakao.bank.place.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.kakao.bank.place.api.KakaoLocalSearchAPI;

@Configuration
public class SearchPlaceConfig {

	@Bean
	public WebClient webClient() {
		return WebClient.builder().build();
	}
	
	@Bean
	public KakaoLocalSearchAPI kakaoSearchPlaceAPI(WebClient webClient) {
		return new KakaoLocalSearchAPI(webClient);
	}
}
