package com.kakao.bank.place.api.geo;

import org.junit.jupiter.api.Test;

import com.kakao.bank.place.util.geo.GeoTrans;
import com.kakao.bank.place.util.geo.GeoTransPoint;

class GeoTransTest {

	// https://www.androidpub.com/android_dev_info/1318647

	@Test
	void geoTest() {
		// 사용방법
		// 카텍 -> 경위도 변경
		GeoTransPoint oKA = new GeoTransPoint(321692, 533236);
		GeoTransPoint oGeo = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, oKA);
		Double lat = oGeo.getY() ;//* 1E6;
		Double lng = oGeo.getX() ;//* 1E6;

		System.out.println(lat.toString() + " " + lng);
		
		// 127.113418511105
		// 127.11332670862662
		// 127.113326
		
		// 37.3973262102103
		// 37.397795
	}
	
	@Test
	void distanceTest() {
		
//		GeoTransPoint oMinGEO = new GeoTransPoint(127.113418, 37.397326);
//		GeoTransPoint oMaxGEO = new GeoTransPoint(127.113326, 37.397795);
		
		GeoTransPoint oMinGEO = new GeoTransPoint(127.04981531616046 , 37.29082152865614);
		GeoTransPoint oMaxGEO = new GeoTransPoint(127.113418511105, 37.3973262102103);
		
		double fDistance = GeoTrans.getDistancebyGeo(oMinGEO, oMaxGEO); //meter
		
		int nDistance = (int) (fDistance * 1000);
		
		System.out.println(nDistance);
		
		
	}

//	// 거리 측정 함수도 이렇게 만들어서 사용하고 있습니다.
//	public int GetDistance_Meter(GeoPoint startP, GeoPoint endP)
//	{
//
//		GeoTransPoint oMinGEO = new GeoTransPoint(startP.getLongitudeE6() / 1E6, startP.getLatitudeE6() / 1E6);
//
//		GeoTransPoint oMaxGEO = new GeoTransPoint(endP.getLongitudeE6() / 1E6, endP.getLatitudeE6() / 1E6);
//
//		double fDistance = GeoTrans.getDistancebyGeo(oMinGEO, oMaxGEO);
//
//		int nDistance = (int) (fDistance * 1000);
//
//		return nDistance;
//
//	}

}
