package com.kakao.bank.place.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.bank.place.dto.SearchPlaceResponse;
import com.kakao.bank.place.service.IntegratedSearchPlaceAPI;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/search")
public class SearchPlaceController {

	
	@Autowired
	private IntegratedSearchPlaceAPI api;
	
	@GetMapping(path = "/place")
	public Mono<SearchPlaceResponse> place(@RequestParam(name = "query", required = true) String query) {
		return api.get(query);
	}
}
