package com.kakao.bank.place.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.junit.jupiter.api.Test;

class PlaceTest {

	@Test
	void test() {
		
		JaroWinklerSimilarity simllarity = new JaroWinklerSimilarity();
		
		System.out.println(simllarity.apply("스시쿤 판교", "스시쿤테츠"));
		System.out.println(simllarity.apply("스시쿤 판교", "스시쿤테츠"));
		System.out.println(simllarity.apply("스시쿤 판교", "스시쿤"));
		System.out.println(simllarity.apply("스시쿤 판교", "심야식당쿤"));
		
		
	}

}
