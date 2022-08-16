package com.kakao.bank.place.util.geo;

public class GeoUtil {

	public static GeoTransPoint convertKATEC2WGS84(int x, int y) {
		GeoTransPoint oKA = new GeoTransPoint(x, y);
		return GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, oKA);
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		GeoTransPoint oMinGEO = new GeoTransPoint(x1, y1);
		GeoTransPoint oMaxGEO = new GeoTransPoint(x2, y2);
		return GeoTrans.getDistancebyGeo(oMinGEO, oMaxGEO) * 1000; //meter
	}
}


