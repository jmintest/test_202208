package com.kakao.bank.place.dto;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kakao.bank.place.api.kakao.keyword.Document;
import com.kakao.bank.place.api.naver.local.Item;
import com.kakao.bank.place.util.RemoveTag;
import com.kakao.bank.place.util.geo.GeoTransPoint;
import com.kakao.bank.place.util.geo.GeoUtil;

public class Place {
	public String place_name;
	public String place_url;
	public String phone;
	public String category_name;
	public String address_name;
	public String road_address_name;
	public String x;
	public String y;
	
	public String source;
	
	@JsonIgnore
	public boolean mark = false;
	
	
	public static Place convert(Document doc) {
		Place place = new Place();
		place.place_name = doc.place_name;
		place.place_url = doc.place_url;
		place.phone = doc.phone;
		place.category_name = doc.category_name;
		place.address_name = doc.address_name;
		place.road_address_name = doc.road_address_name;
		place.x = doc.x;
		place.y = doc.y;
		place.source = "kakao";
		return place;
	}
	
	public static Place convert(Item item) {
		Place place = new Place();
		place.place_name = item.title;
		place.place_url = item.link;
		place.phone = item.telephone;
		place.category_name = item.category;
		place.address_name = item.address;
		place.road_address_name = item.roadAddress;
		
		int x = Integer.parseInt(item.mapx);
		int y = Integer.parseInt(item.mapy);
		GeoTransPoint convert = GeoUtil.convertKATEC2WGS84(x, y);
		place.x = convert.getX()+"";
		place.y = convert.getY()+"";
		
		place.source = "naver";
		
		return place;
	}

	@Override
	public String toString() {
		return "Place [place_name=" + place_name + ", place_url=" + place_url + ", phone=" + phone + ", category_name="
				+ category_name + ", address_name=" + address_name + ", road_address_name=" + road_address_name + ", x="
				+ x + ", y=" + y + "]";
	}

	/***
	 * 태그를 제거한 문자열 유사도가 0.8 이상, 좌표가 100m 이내면 동일 업체
	 * @param obj
	 * @return
	 */
	public boolean similar(Place dst) {
		return (isSimilarName(dst) && isNear(dst));
	}
	
	private boolean isNear(Place dst) {
		return distance(dst) <= 100; // 100m 이내
		
	}

	private double distance(Place dst) {
		return GeoUtil.distance(
				Double.parseDouble(this.x),
				Double.parseDouble(this.y),
				Double.parseDouble(dst.x),
				Double.parseDouble(dst.y));
	}

	private boolean isSimilarName(Place dst) {
		String t1 = RemoveTag.remove(this.place_name);
		String t2 = RemoveTag.remove(dst.place_name);
		JaroWinklerSimilarity simllarity = new JaroWinklerSimilarity();
		Double apply = simllarity.apply(t1, t2);
		return apply >= 0.8;
	}
}
