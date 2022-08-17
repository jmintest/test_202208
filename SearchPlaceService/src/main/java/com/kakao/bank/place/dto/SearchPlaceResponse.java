package com.kakao.bank.place.dto;

import java.util.List;

public class SearchPlaceResponse {

	public SearchPlaceResponse(List<Place> place) {
		this.place = place;
	}
	
	public List<Place> place;

	@Override
	public String toString() {
		return "SearchPlaceResponse [place=" + place + "]";
	}
	
	
}
