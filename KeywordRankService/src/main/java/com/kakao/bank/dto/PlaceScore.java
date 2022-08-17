package com.kakao.bank.dto;

public class PlaceScore {

	private String place_name;
	private Long score;

	public String getPlace_name() {
		return place_name;
	}

	public void setPlace_name(String place_name) {
		this.place_name = place_name;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "PlaceScore [place_name=" + place_name + ", score=" + score + "]";
	}

}
