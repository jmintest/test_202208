package com.kakao.bank.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.kakao.bank.service.KeywordRankService;

import reactor.core.publisher.Flux;

@WebFluxTest(controllers = KeywordRankController.class)
class KeywordRankControllerTest {

	@MockBean
	private KeywordRankService service;

	@Autowired
	private WebTestClient webClient;
	
	
	@BeforeEach
	void init() {
		when(service.rank()).thenReturn(Flux.empty());
	}

	@Test
	void rank() {
		webClient.get()
		 .uri(uriBuilder ->
         	uriBuilder
                 .path("/keyword/rank")
                 .build())
		.exchange()
		.expectStatus().isOk();
		
		verify(service, times(1)).rank();
		verifyNoMoreInteractions(service);
	}
	

}
