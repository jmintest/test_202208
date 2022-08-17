package com.kakao.bank.place.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import com.kakao.bank.place.api.kakao.KeywordResponse;

import reactor.core.publisher.Mono;

public class KakaoLocalAPI {

	@Value("${api.kakao.url:https://dapi.kakao.com}")
	private String apiUrl;
	
	@Value("${api.kakao.url.local.search:/v2/local/search/keyword.json}")
	private String localSeacrh;
	
	@Value("${api.kakao.accessToken:5107c1f7fc6b2b98959417aa5a07ad1d}")
	private String accessToken;
	
	private WebClient webClient;
	
	public KakaoLocalAPI(WebClient webClient) {
		this.webClient = webClient;
	}
	
	public Mono<KeywordResponse> keyword(String query){
		
		return this.webClient
		.get()
		.uri(apiUrl, uri ->
			uri.path(localSeacrh)
			.queryParam("query", query)
			.queryParam("size", 10)
			.build()
		)
		.header("Authorization", String.format("KakaoAK %s", accessToken))
		.retrieve()
		.bodyToMono(KeywordResponse.class)
		.onErrorReturn(KeywordResponse.empty())
		.log();
		
//		.map(body -> {
//			System.out.println(body + " testest");
//			return body;
//		}).subscribe();
	}
	
	
	
}
