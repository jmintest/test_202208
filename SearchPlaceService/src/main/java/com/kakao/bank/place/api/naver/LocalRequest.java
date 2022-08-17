package com.kakao.bank.place.api.naver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalRequest {

	public String query;
	public Integer display;
	public Integer start;
	public String sort;

}
