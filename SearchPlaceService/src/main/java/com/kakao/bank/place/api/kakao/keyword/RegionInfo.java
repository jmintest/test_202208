package com.kakao.bank.place.api.kakao.keyword;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionInfo {

	public List<String> region; // 질의어에서 인식된 지역의 리스트	예: '중앙로 맛집' 에서 중앙로에 해당하는 지역 리스트
	public String keyword; // 질의어에서 지역 정보를 제외한 키워드 예: '중앙로 맛집' 에서 '맛집'
	public String selected_region; // 인식된 지역 리스트 중, 현재 검색에 사용된 지역 정보
	
	@Override
	public String toString() {
		return "RegionInfo [region=" + region + ", keyword=" + keyword + ", selected_region=" + selected_region + "]";
	}
	
	
}
