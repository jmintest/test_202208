package com.kakao.bank.place.api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class KakaoLocalSearchAPITest {

	@Autowired
	private KakaoLocalSearchAPI api;
	
	@Test
	void test() {
		api.get("은행");
		//fail("Not yet implemented");
	}

}
