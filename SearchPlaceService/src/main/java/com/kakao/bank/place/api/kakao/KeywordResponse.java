package com.kakao.bank.place.api.kakao;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kakao.bank.place.api.kakao.keyword.Document;
import com.kakao.bank.place.api.kakao.keyword.Meta;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KeywordResponse {
	public Meta meta;
	public List<Document> documents;
	@Override
	public String toString() {
		return "KeywordResponse [meta=" + meta + ", documents=" + documents + "]";
	}
	
	
	
}
