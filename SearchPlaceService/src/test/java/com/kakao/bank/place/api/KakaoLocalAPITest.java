package com.kakao.bank.place.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kakao.bank.place.service.IntegratedSearchPlaceAPI;


@SpringBootTest
class KakaoLocalAPITest {

	@Autowired
	private KakaoLocalAPI kakaoApi;
	
	@Autowired
	private NaverSearchAPI naverApi;
	
	@Autowired
	private IntegratedSearchPlaceAPI api;
	
	@Test
	void test() throws InterruptedException {
		kakaoApi.keyword("은행");
		naverApi.local("은행");
		Thread.sleep(1000);
	}
	
	@Test
	void test2() throws InterruptedException {
		api.get("카카오뱅크").subscribe();
		Thread.sleep(1000);
	}

}
