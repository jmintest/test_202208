package com.kakao.bank.place.api.naver.local;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    public String title;// 유안타<b>증권</b>,
    public String link;// http;////www.myasset.com/,
    public String category;// 금융,보험>주식,증권,
    public String description;// ,
    public String telephone;// ,
    public String address;// 서울특별시 중구 을지로2가 185 유안타증권 빌딩,
    public String roadAddress;// 서울특별시 중구 을지로 76 유안타증권,
    public String mapx;// 310636,
    public String mapy;// 551982
    
	@Override
	public String toString() {
		return "Item [title=" + title + ", link=" + link + ", category=" + category + ", description=" + description
				+ ", telephone=" + telephone + ", address=" + address + ", roadAddress=" + roadAddress + ", mapx="
				+ mapx + ", mapy=" + mapy + "]";
	}
    
    
    
}
