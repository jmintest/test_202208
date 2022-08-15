package com.kakao.bank.place.api.kakao.keyword;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Meta {

	public Integer total_count; //검색어에 검색된 문서 수
	public Integer pageable_count; //total_count 중 노출 가능 문서 수 (최대: 45)
	public Boolean is_end; // 현재 페이지가 마지막 페이지인지 여부,값이 false면 다음 요청 시 page 값을 증가시켜 다음 페이지 요청 가능
	public RegionInfo same_name; //질의어의 지역 및 키워드 분석 정보
	
	@Override
	public String toString() {
		return "Meta [total_count=" + total_count + ", pageable_count=" + pageable_count + ", is_end=" + is_end
				+ ", same_name=" + same_name + "]";
	}
	
	
}
