package com.kakao.bank.place.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import com.kakao.bank.place.api.kakao.KeywordResponse;
import com.kakao.bank.place.api.naver.LocalResponse;

import reactor.core.publisher.Mono;

public class KakaoLocalSearchAPI {

	@Value("${api.kakao.url:https://dapi.kakao.com}")
	private String apiUrl;
	
	@Value("${api.kakao.url.local.search:/v2/local/search/keyword.json}")
	private String localSeacrh;
	
	@Value("${api.kakao.place.accessToken:5107c1f7fc6b2b98959417aa5a07ad1d}")
	private String accessToken;
	
	private WebClient webClient;
	
	private static String API_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";

	public KakaoLocalSearchAPI(WebClient webClient) {
		this.webClient = webClient;
	}
	
	public Mono<LocalResponse> get(String query){
		
		System.out.println(apiUrl);
		
//		new UriBuilder(apiUrl).path(localSeacrh).queryParam("query", query)
		
		this.webClient
		.get()
		.uri(apiUrl, uri ->
			uri.path(localSeacrh)
			.queryParam("query", query)
			.build()
		)
		.header("Authorization", String.format("KakaoAK %s", accessToken))
		.retrieve()
		.bodyToMono(KeywordResponse.class)
		.map(body -> {
			System.out.println(body + " testest");
			return body;
		}).subscribe();
		
		return null;
	}
	
	
	
}
