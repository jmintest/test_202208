package com.kakao.bank.place.api.naver;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kakao.bank.place.api.naver.local.Item;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalResponse {
	// rss-디버그를 쉽게 하고 RSS 리더기만으로 이용할 수 있게 하기 위해 만든 RSS 포맷의 컨테이너이며 그 외의 특별한 의미는 없다.
	// channel-검색 결과를 포함하는 컨테이너이다. 이 안에 있는 title, link, description 등의 항목은 참고용으로 무시해도 무방하다.
	public String lastBuildDate; // 검색 결과를 생성한 시간이다. ("Sun, 14 Aug 2022 18:54:00 +0900")
	public Integer total;// 검색 결과 문서의 총 개수를 의미한다.
	public Integer start;// 검색 결과 문서 중, 문서의 시작점을 의미한다.
	public Integer display;// 검색된 검색 결과의 개수이다.
	public String category;// 검색 결과 업체, 기관의 분류 정보를 제공한다.
	public List<Item> items;// XML 포멧에서는 item 태그로, JSON 포멧에서는 items 속성으로 표현된다. 개별 검색 결과이며 title, link, description, address, mapx, mapy를 포함한다.
	public String title;// 검색 결과 업체, 기관명을 나타낸다.
	public String link;// 검색 결과 업체, 기관의 상세 정보가 제공되는 네이버 페이지의 하이퍼텍스트 link를 나타낸다.
	public String description;// 검색 결과 업체, 기관명에 대한 설명을 제공한다.
	public String telephone;// 빈 문자열 반환. 과거에 제공되던 항목이라 하위 호환성을 위해 존재한다.
	public String address;// 검색 결과 업체, 기관명의 주소를 제공한다.
	public String roadAddress;// 검색 결과 업체, 기관명의 도로명 주소를 제공한다.
	public Integer mapx;// 검색 결과 업체, 기관명 위치 정보의 x좌표를 제공한다. 제공값은 카텍좌표계 값으로 제공된다. 이 좌표값은 지도 API와 연동 가능하다.
	public Integer mapy;// 검색 결과 업체, 기관명 위치 정보의 y좌표를 제공한다. 제공값은 카텍 좌표계 값으로 제공된다. 이 좌표값은 지도 API와 연동 가능하다.
	
	@Override
	public String toString() {
		return "LocalResponse [lastBuildDate=" + lastBuildDate + ", total=" + total + ", start=" + start + ", display="
				+ display + ", category=" + category + ", items=" + items + ", title=" + title + ", link=" + link
				+ ", description=" + description + ", telephone=" + telephone + ", address=" + address
				+ ", roadAddress=" + roadAddress + ", mapx=" + mapx + ", mapy=" + mapy + "]";
	}
	
	public static LocalResponse empty() {
		LocalResponse retval = new LocalResponse();
		retval.items = new ArrayList<>();
		return retval;
	}
	
	
}
