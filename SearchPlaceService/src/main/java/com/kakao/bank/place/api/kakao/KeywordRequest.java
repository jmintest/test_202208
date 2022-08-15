package com.kakao.bank.place.api.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KeywordRequest {

	public String query;
	
	public String x;
	public String y;
	public Integer radius;
	public String rect;
	public Integer page;
	public Integer size;
	public String sort;
	
}
