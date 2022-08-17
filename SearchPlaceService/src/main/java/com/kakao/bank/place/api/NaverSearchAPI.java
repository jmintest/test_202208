package com.kakao.bank.place.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import com.kakao.bank.place.api.naver.LocalResponse;

import reactor.core.publisher.Mono;

public class NaverSearchAPI {

	//https://openapi.naver.com/v1/search/local.json?query=%EC%A3%BC%EC%8B%9D&display=10&start=1&sort=random

	
	@Value("${api.naver.url:https://openapi.naver.com}")
	private String apiUrl;
	
	@Value("${api.naver.url.local.search:/v1/search/local.json}")
	private String localSeacrh;
	
	@Value("${api.naver.client-id:nRQjKOm0VztkH8T24xvx}")
	private String clientId;
	
	@Value("${api.naver.client-secret:cK6kFInGeU}")
	private String clientSecret;
	
	private WebClient webClient;
	
	public NaverSearchAPI(WebClient webClient) {
		this.webClient = webClient;
	}
	
	
	public Mono<LocalResponse> local(String query){
		
		return this.webClient
		.get()
		.uri(apiUrl, uri ->
			uri.path(localSeacrh)
			.queryParam("query", query)
			.queryParam("display", 10)
			.queryParam("start", 1)
			.build()
		)
		.header("X-Naver-Client-Id", clientId)
		.header("X-Naver-Client-Secret", clientSecret)
		.retrieve()
		.bodyToMono(LocalResponse.class)
		.onErrorReturn(LocalResponse.empty())
		.log();
//		.map(body -> {
//			System.out.println(body + " testest");
//			return body;
//		}).subscribe();
		
	}
	
	
	
}
