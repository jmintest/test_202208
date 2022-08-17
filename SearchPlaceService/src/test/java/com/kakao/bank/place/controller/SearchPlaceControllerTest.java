package com.kakao.bank.place.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.kakao.bank.place.service.IntegratedSearchPlaceAPI;

import reactor.core.publisher.Mono;

@WebFluxTest(controllers = SearchPlaceController.class)
class SearchPlaceControllerTest {

	@MockBean
	IntegratedSearchPlaceAPI api;

	@Autowired
	private WebTestClient webClient;
	
	
	@BeforeEach
	void init() {
		when(api.get(org.mockito.Mockito.anyString())).thenReturn(Mono.empty());
	}

	@Test
	void search() {
		webClient.get()
		 .uri(uriBuilder ->
         	uriBuilder
                 .path("/search/place")
                 .queryParam("query", "은행")
                 .build())
		.exchange()
		.expectStatus().isOk();
		
		verify(api, times(1)).get("은행");
		verifyNoMoreInteractions(api);
	}
	
	@Test
	void badRequest() {
		webClient.get()
		 .uri(uriBuilder ->
         	uriBuilder
                 .path("/search/place")
//                 .queryParam("query", "은행")
                 .build())
		.exchange()
		.expectStatus().isBadRequest();
		
		verifyNoMoreInteractions(api);
	}

}
