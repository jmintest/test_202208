package com.kakao.bank.place.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.kakao.bank.place.api.KakaoLocalAPI;
import com.kakao.bank.place.api.NaverSearchAPI;

@Configuration
public class SearchPlaceConfig {

	@Bean
	public WebClient webClient() {
		return WebClient.builder().build();
	}
	
	@Bean
	public KakaoLocalAPI kakaoSearchPlaceAPI(WebClient webClient) {
		return new KakaoLocalAPI(webClient);
	}
	
	@Bean
	public NaverSearchAPI naverSearchPlaceAPI(WebClient webClient) {
		return new NaverSearchAPI(webClient);
	}
}
