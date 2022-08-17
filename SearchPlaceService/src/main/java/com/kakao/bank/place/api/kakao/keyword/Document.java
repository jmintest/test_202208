package com.kakao.bank.place.api.kakao.keyword;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Document {

	public String id; //장소 ID
	public String place_name; //장소명, 업체명
	public String category_name; //카테고리 이름
	public String category_group_code; //중요 카테고리만 그룹핑한 카테고리 그룹 코드
	public String category_group_name; //중요 카테고리만 그룹핑한 카테고리 그룹명
	public String phone; //전화번호
	public String address_name; //전체 지번 주소
	public String road_address_name; //전체 도로명 주소
	public String x; //X 좌표값, 경위도인 경우 longitude (경도)
	public String y; //Y 좌표값, 경위도인 경우 latitude(위도)
	public String place_url; //장소 상세페이지 URL
	public String distance; //중심좌표까지의 거리 (단, x,y 파라미터를 준 경우에만 존재) 단위 meter
	
	
	@Override
	public String toString() {
		return "Document [id=" + id + ", place_name=" + place_name + ", category_name=" + category_name
				+ ", category_group_code=" + category_group_code + ", category_group_name=" + category_group_name
				+ ", phone=" + phone + ", address_name=" + address_name + ", road_address_name=" + road_address_name
				+ ", x=" + x + ", y=" + y + ", place_url=" + place_url + ", distance=" + distance + "]";
	}
	
	
}
