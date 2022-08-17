package com.kakao.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.bank.dto.PlaceScore;
import com.kakao.bank.service.KeywordRankService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/keyword")
public class KeywordRankController {

	@Autowired
	private KeywordRankService service;
	
	@GetMapping(path = "/rank")
	public Flux<PlaceScore> rank() {
		return service.rank();
	}
}
