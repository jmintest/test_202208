package com.kakao.bank.place.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.bank.place.api.KakaoLocalAPI;
import com.kakao.bank.place.api.NaverSearchAPI;
import com.kakao.bank.place.api.kakao.KeywordResponse;
import com.kakao.bank.place.api.naver.LocalResponse;
import com.kakao.bank.place.dto.SearchPlaceResponse;

import reactor.core.publisher.Mono;


@ExtendWith(MockitoExtension.class)
class IntegratedSearchPlaceAPITest {

	@Mock
	private KakaoLocalAPI kakaoAPI;
	
	@Mock
	private NaverSearchAPI naverAPI;
	
	@Mock
	private SearchKeywordCounter counter;
	
	@InjectMocks
	private IntegratedSearchPlaceAPI api;
	
	@BeforeEach
	void init() {
		
		ReflectionTestUtils.setField(api, "totalCount", 10);
		when(counter.increment(org.mockito.Mockito.anyString())).thenReturn(Mono.empty());
	}
	
	@Test
	void getEmpty() {
		
		when(kakaoAPI.keyword("query")).thenReturn(Mono.just(KeywordResponse.empty()));
		when(naverAPI.local("query")).thenReturn(Mono.just(LocalResponse.empty()));
		
		SearchPlaceResponse response = api.get("query").block();
		
		verify(kakaoAPI, times(1)).keyword("query");
		verify(naverAPI, times(1)).local("query");
		verify(counter, times(1)).increment("query");
		

		assertTrue(response.place.isEmpty());
		
		verifyNoMoreInteractions(kakaoAPI);
		verifyNoMoreInteractions(naverAPI);
		verifyNoMoreInteractions(counter);
		
	}
	
	@Test
	void getKakaoResult() throws JsonMappingException, JsonProcessingException {
		when(kakaoAPI.keyword("은행")).thenReturn(Mono.just(makeKakaoResult(kakaoResponse)));
		when(naverAPI.local("은행")).thenReturn(Mono.just(LocalResponse.empty()));
		
		SearchPlaceResponse response = api.get("은행").block();
		
		verify(kakaoAPI, times(1)).keyword("은행");
		verify(naverAPI, times(1)).local("은행");
		verify(counter, times(1)).increment("은행");
		
		assertEquals(response.place.size(), 10);
		assertEquals(response.place.stream().filter(e -> e.source.equals("kakao")).count(), 10);
		
		verifyNoMoreInteractions(kakaoAPI);
		verifyNoMoreInteractions(naverAPI);
		verifyNoMoreInteractions(counter);
		
	}
	
	@Test
	void getNaverResult() throws JsonMappingException, JsonProcessingException {
		when(kakaoAPI.keyword("은행")).thenReturn(Mono.just(KeywordResponse.empty()));
		when(naverAPI.local("은행")).thenReturn(Mono.just(makeNaverResult(naverResponse)));
		
		SearchPlaceResponse response = api.get("은행").block();
		
		verify(kakaoAPI, times(1)).keyword("은행");
		verify(naverAPI, times(1)).local("은행");
		verify(counter, times(1)).increment("은행");
		
		assertEquals(response.place.size(), 5);	
		assertEquals(response.place.stream().filter(e -> e.source.equals("naver")).count(), 5);
		
		verifyNoMoreInteractions(kakaoAPI);
		verifyNoMoreInteractions(naverAPI);
		verifyNoMoreInteractions(counter);
		
	}
	
	@Test
	void getMergeResult() throws JsonMappingException, JsonProcessingException {
		when(kakaoAPI.keyword("은행")).thenReturn(Mono.just(makeKakaoResult(kakaoResponse)));
		when(naverAPI.local("은행")).thenReturn(Mono.just(makeNaverResult(naverResponse)));
		
		SearchPlaceResponse response = api.get("은행").block();
		
		verify(kakaoAPI, times(1)).keyword("은행");
		verify(naverAPI, times(1)).local("은행");
		verify(counter, times(1)).increment("은행");
		
		assertEquals(response.place.size(), 10);
		assertEquals(response.place.stream().filter(e -> e.source.equals("kakao")).count(), 5);
		assertEquals(response.place.stream().filter(e -> e.source.equals("naver")).count(), 5);
		
		
		verifyNoMoreInteractions(kakaoAPI);
		verifyNoMoreInteractions(naverAPI);
		verifyNoMoreInteractions(counter);
		
	}
	
	@Test
	void getSameResult() throws JsonMappingException, JsonProcessingException {
		when(kakaoAPI.keyword("곱창")).thenReturn(Mono.just(makeKakaoResult(kakaoResponse2)));
		when(naverAPI.local("곱창")).thenReturn(Mono.just(makeNaverResult(naverResponse2)));
		
		SearchPlaceResponse response = api.get("곱창").block();
		
		verify(kakaoAPI, times(1)).keyword("곱창");
		verify(naverAPI, times(1)).local("곱창");
		verify(counter, times(1)).increment("곱창");
		
		assertEquals(response.place.size(), 10);
		assertEquals(response.place.stream().filter(e -> e.source.equals("kakao")).count(), 9);
		assertEquals(response.place.stream().filter(e -> e.source.equals("naver")).count(), 1);
		
		
		verifyNoMoreInteractions(kakaoAPI);
		verifyNoMoreInteractions(naverAPI);
		verifyNoMoreInteractions(counter);
		
	}
	
	@Test
	void getSameResult2() throws JsonMappingException, JsonProcessingException {
		when(kakaoAPI.keyword("스시쿤")).thenReturn(Mono.just(makeKakaoResult(kakaoResponse3)));
		when(naverAPI.local("스시쿤")).thenReturn(Mono.just(makeNaverResult(naverResponse3)));
		
		SearchPlaceResponse response = api.get("스시쿤").block();
		
		verify(kakaoAPI, times(1)).keyword("스시쿤");
		verify(naverAPI, times(1)).local("스시쿤");
		verify(counter, times(1)).increment("스시쿤");
		
		assertEquals(response.place.size(), 7);
		assertEquals(response.place.stream().filter(e -> e.source.equals("kakao")).count(), 4);
		assertEquals(response.place.stream().filter(e -> e.source.equals("naver")).count(), 3);
		
		
		verifyNoMoreInteractions(kakaoAPI);
		verifyNoMoreInteractions(naverAPI);
		verifyNoMoreInteractions(counter);
		
	}

	private KeywordResponse makeKakaoResult(String response) throws JsonMappingException, JsonProcessingException {
		return mapper.readValue(response, KeywordResponse.class);
	}
	
	private LocalResponse makeNaverResult(String response) throws JsonMappingException, JsonProcessingException {
		return mapper.readValue(response, LocalResponse.class);
	}
	
	static ObjectMapper mapper = new ObjectMapper();
	
	private static String naverResponse3 = "{\r\n"
			+ "    \"lastBuildDate\": \"Wed, 17 Aug 2022 18:59:09 +0900\",\r\n"
			+ "    \"total\": 5,\r\n"
			+ "    \"start\": 1,\r\n"
			+ "    \"display\": 5,\r\n"
			+ "    \"items\": [\r\n"
			+ "        {\r\n"
			+ "            \"title\": \"<b>스시쿤</b>\",\r\n"
			+ "            \"link\": \"https://catchtable.co.kr/sushikun_pangyo\",\r\n"
			+ "            \"category\": \"일식>일식당\",\r\n"
			+ "            \"description\": \"\",\r\n"
			+ "            \"telephone\": \"\",\r\n"
			+ "            \"address\": \"경기도 성남시 분당구 삼평동 670 유스페이스몰1차 지하1층 115호\",\r\n"
			+ "            \"roadAddress\": \"경기도 성남시 분당구 대왕판교로 660 유스페이스몰1차 지하1층 115호\",\r\n"
			+ "            \"mapx\": \"321131\",\r\n"
			+ "            \"mapy\": \"533509\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"title\": \"<b>스시쿤</b>\",\r\n"
			+ "            \"link\": \"https://app.catchtable.co.kr/ct/shop/sushikun_jeongja\",\r\n"
			+ "            \"category\": \"일식>초밥,롤\",\r\n"
			+ "            \"description\": \"\",\r\n"
			+ "            \"telephone\": \"\",\r\n"
			+ "            \"address\": \"경기도 성남시 분당구 정자동 15-1\",\r\n"
			+ "            \"roadAddress\": \"경기도 성남시 분당구 성남대로 389 폴라리스2차 115호 스시쿤\",\r\n"
			+ "            \"mapx\": \"321171\",\r\n"
			+ "            \"mapy\": \"530264\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"title\": \"심야식당쿤\",\r\n"
			+ "            \"link\": \"http://app.catchtable.co.kr/ct/shop/midnightkun\",\r\n"
			+ "            \"category\": \"일식>일식당\",\r\n"
			+ "            \"description\": \"\",\r\n"
			+ "            \"telephone\": \"\",\r\n"
			+ "            \"address\": \"경기도 성남시 분당구 정자동 174-1 더샵스타파크\",\r\n"
			+ "            \"roadAddress\": \"경기도 성남시 분당구 정자일로 121 더샵스타파크\",\r\n"
			+ "            \"mapx\": \"320923\",\r\n"
			+ "            \"mapy\": \"529071\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"title\": \"<b>스시쿤</b>\",\r\n"
			+ "            \"link\": \"\",\r\n"
			+ "            \"category\": \"일식>일식당\",\r\n"
			+ "            \"description\": \"\",\r\n"
			+ "            \"telephone\": \"\",\r\n"
			+ "            \"address\": \"부산광역시 해운대구 우동 518 우일맨션 1층 114호 스시쿤\",\r\n"
			+ "            \"roadAddress\": \"부산광역시 해운대구 우동1로38번길 11\",\r\n"
			+ "            \"mapx\": \"505690\",\r\n"
			+ "            \"mapy\": \"285806\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"title\": \"<b>스시쿤</b>\",\r\n"
			+ "            \"link\": \"\",\r\n"
			+ "            \"category\": \"일식>일식당\",\r\n"
			+ "            \"description\": \"\",\r\n"
			+ "            \"telephone\": \"\",\r\n"
			+ "            \"address\": \"부산광역시 동래구 온천동 1441-66\",\r\n"
			+ "            \"roadAddress\": \"부산광역시 동래구 충렬대로148번길 19-1\",\r\n"
			+ "            \"mapx\": \"498147\",\r\n"
			+ "            \"mapy\": \"290025\"\r\n"
			+ "        }\r\n"
			+ "    ]\r\n"
			+ "}";
	
	private static String naverResponse2 = "{\r\n"
			+ "    \"lastBuildDate\": \"Wed, 17 Aug 2022 18:55:23 +0900\",\r\n"
			+ "    \"total\": 5,\r\n"
			+ "    \"start\": 1,\r\n"
			+ "    \"display\": 5,\r\n"
			+ "    \"items\": [\r\n"
			+ "        {\r\n"
			+ "            \"title\": \"백화양<b>곱창</b>\",\r\n"
			+ "            \"link\": \"\",\r\n"
			+ "            \"category\": \"한식>곱창,막창,양\",\r\n"
			+ "            \"description\": \"\",\r\n"
			+ "            \"telephone\": \"\",\r\n"
			+ "            \"address\": \"부산광역시 중구 남포동6가 32\",\r\n"
			+ "            \"roadAddress\": \"부산광역시 중구 자갈치로23번길 6\",\r\n"
			+ "            \"mapx\": \"493835\",\r\n"
			+ "            \"mapy\": \"278063\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"title\": \"청어람 망원점\",\r\n"
			+ "            \"link\": \"\",\r\n"
			+ "            \"category\": \"한식>곱창,막창,양\",\r\n"
			+ "            \"description\": \"\",\r\n"
			+ "            \"telephone\": \"\",\r\n"
			+ "            \"address\": \"서울특별시 마포구 망원동 482-3\",\r\n"
			+ "            \"roadAddress\": \"서울특별시 마포구 망원로 97\",\r\n"
			+ "            \"mapx\": \"303671\",\r\n"
			+ "            \"mapy\": \"551215\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"title\": \"곱 마포점\",\r\n"
			+ "            \"link\": \"https://blog.naver.com/restaurant_gop\",\r\n"
			+ "            \"category\": \"한식>곱창,막창,양\",\r\n"
			+ "            \"description\": \"\",\r\n"
			+ "            \"telephone\": \"\",\r\n"
			+ "            \"address\": \"서울특별시 마포구 도화동 179-11\",\r\n"
			+ "            \"roadAddress\": \"서울특별시 마포구 도화길 31-1\",\r\n"
			+ "            \"mapx\": \"307353\",\r\n"
			+ "            \"mapy\": \"549278\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"title\": \"해성막창집 본점\",\r\n"
			+ "            \"link\": \"\",\r\n"
			+ "            \"category\": \"한식>곱창,막창,양\",\r\n"
			+ "            \"description\": \"\",\r\n"
			+ "            \"telephone\": \"\",\r\n"
			+ "            \"address\": \"부산광역시 해운대구 중동 1732\",\r\n"
			+ "            \"roadAddress\": \"부산광역시 해운대구 중동1로19번길 29\",\r\n"
			+ "            \"mapx\": \"506170\",\r\n"
			+ "            \"mapy\": \"285621\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"title\": \"평양집\",\r\n"
			+ "            \"link\": \"\",\r\n"
			+ "            \"category\": \"한식>곱창,막창,양\",\r\n"
			+ "            \"description\": \"\",\r\n"
			+ "            \"telephone\": \"\",\r\n"
			+ "            \"address\": \"서울특별시 용산구 한강로1가 137-1\",\r\n"
			+ "            \"roadAddress\": \"서울특별시 용산구 한강대로 186\",\r\n"
			+ "            \"mapx\": \"309513\",\r\n"
			+ "            \"mapy\": \"548574\"\r\n"
			+ "        }\r\n"
			+ "    ]\r\n"
			+ "}";
	
	private static String naverResponse = "{\r\n"
			+ "    \"lastBuildDate\": \"Wed, 17 Aug 2022 18:49:39 +0900\",\r\n"
			+ "    \"total\": 5,\r\n"
			+ "    \"start\": 1,\r\n"
			+ "    \"display\": 5,\r\n"
			+ "    \"items\": [\r\n"
			+ "        {\r\n"
			+ "            \"title\": \"신한<b>은행</b> 서울시청금융센터\",\r\n"
			+ "            \"link\": \"\",\r\n"
			+ "            \"category\": \"금융,보험>은행\",\r\n"
			+ "            \"description\": \"\",\r\n"
			+ "            \"telephone\": \"\",\r\n"
			+ "            \"address\": \"서울특별시 중구 태평로1가 31\",\r\n"
			+ "            \"roadAddress\": \"서울특별시 중구 세종대로 110 (태평로1가)\",\r\n"
			+ "            \"mapx\": \"309947\",\r\n"
			+ "            \"mapy\": \"552092\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"title\": \"신용카드한도\",\r\n"
			+ "            \"link\": \"https://sscchhgg4545.imweb.me/\",\r\n"
			+ "            \"category\": \"금융,보험>은행\",\r\n"
			+ "            \"description\": \"\",\r\n"
			+ "            \"telephone\": \"\",\r\n"
			+ "            \"address\": \"서울특별시 중구 태평로1가 31 문의 010 9741 8315\",\r\n"
			+ "            \"roadAddress\": \"서울특별시 중구 세종대로 110 문의 010 9741 8315\",\r\n"
			+ "            \"mapx\": \"309947\",\r\n"
			+ "            \"mapy\": \"552092\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"title\": \"NH농협<b>은행</b> 광화문금융센터\",\r\n"
			+ "            \"link\": \"http://banking.nonghyup.com/\",\r\n"
			+ "            \"category\": \"금융,보험>은행\",\r\n"
			+ "            \"description\": \"\",\r\n"
			+ "            \"telephone\": \"\",\r\n"
			+ "            \"address\": \"서울특별시 중구 태평로1가 25\",\r\n"
			+ "            \"roadAddress\": \"서울특별시 중구 세종대로 124 프레스센터 2층\",\r\n"
			+ "            \"mapx\": \"309897\",\r\n"
			+ "            \"mapy\": \"552155\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"title\": \"우리<b>은행</b> 365 시청역 구매표실옆\",\r\n"
			+ "            \"link\": \"\",\r\n"
			+ "            \"category\": \"금융,보험>은행\",\r\n"
			+ "            \"description\": \"\",\r\n"
			+ "            \"telephone\": \"\",\r\n"
			+ "            \"address\": \"서울특별시 중구 정동 5-5\",\r\n"
			+ "            \"roadAddress\": \"서울특별시 중구 세종대로 101 (정동)\",\r\n"
			+ "            \"mapx\": \"309822\",\r\n"
			+ "            \"mapy\": \"551963\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"title\": \"부산<b>은행</b> 서울금융센터\",\r\n"
			+ "            \"link\": \"http://www.busanbank.co.kr/\",\r\n"
			+ "            \"category\": \"\",\r\n"
			+ "            \"description\": \"\",\r\n"
			+ "            \"telephone\": \"\",\r\n"
			+ "            \"address\": \"서울특별시 중구 을지로1가 16\",\r\n"
			+ "            \"roadAddress\": \"서울특별시 중구 무교로 6 (을지로1가)\",\r\n"
			+ "            \"mapx\": \"310041\",\r\n"
			+ "            \"mapy\": \"552056\"\r\n"
			+ "        }\r\n"
			+ "    ]\r\n"
			+ "}";
	
	private static String kakaoResponse3 = "{\r\n"
			+ "    \"documents\": [\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"경기 성남시 분당구 삼평동 670\",\r\n"
			+ "            \"category_group_code\": \"FD6\",\r\n"
			+ "            \"category_group_name\": \"음식점\",\r\n"
			+ "            \"category_name\": \"음식점 > 일식 > 초밥,롤\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"26785774\",\r\n"
			+ "            \"phone\": \"031-628-6972\",\r\n"
			+ "            \"place_name\": \"스시쿤 판교\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/26785774\",\r\n"
			+ "            \"road_address_name\": \"경기 성남시 분당구 대왕판교로 660\",\r\n"
			+ "            \"x\": \"127.10725295332934\",\r\n"
			+ "            \"y\": \"37.40019450762178\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"경기 성남시 분당구 정자동 15-1\",\r\n"
			+ "            \"category_group_code\": \"FD6\",\r\n"
			+ "            \"category_group_name\": \"음식점\",\r\n"
			+ "            \"category_name\": \"음식점 > 일식 > 초밥,롤\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"18776048\",\r\n"
			+ "            \"phone\": \"031-719-6972\",\r\n"
			+ "            \"place_name\": \"스시쿤테츠\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/18776048\",\r\n"
			+ "            \"road_address_name\": \"경기 성남시 분당구 성남대로 389\",\r\n"
			+ "            \"x\": \"127.10747645864168\",\r\n"
			+ "            \"y\": \"37.37084347391123\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"경남 양산시 물금읍 가촌리 1284\",\r\n"
			+ "            \"category_group_code\": \"FD6\",\r\n"
			+ "            \"category_group_name\": \"음식점\",\r\n"
			+ "            \"category_name\": \"음식점 > 일식 > 초밥,롤\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"2039302431\",\r\n"
			+ "            \"phone\": \"\",\r\n"
			+ "            \"place_name\": \"스시쿤\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/2039302431\",\r\n"
			+ "            \"road_address_name\": \"경남 양산시 물금읍 백호로 156\",\r\n"
			+ "            \"x\": \"129.009424676053\",\r\n"
			+ "            \"y\": \"35.3122365972389\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"부산 부산진구 당감동 571-7\",\r\n"
			+ "            \"category_group_code\": \"FD6\",\r\n"
			+ "            \"category_group_name\": \"음식점\",\r\n"
			+ "            \"category_name\": \"음식점 > 일식 > 초밥,롤\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"1153805815\",\r\n"
			+ "            \"phone\": \"\",\r\n"
			+ "            \"place_name\": \"스시쿤\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/1153805815\",\r\n"
			+ "            \"road_address_name\": \"부산 부산진구 가야대로507번길 239\",\r\n"
			+ "            \"x\": \"129.038631955181\",\r\n"
			+ "            \"y\": \"35.1561782263929\"\r\n"
			+ "        }\r\n"
			+ "    ],\r\n"
			+ "    \"meta\": {\r\n"
			+ "        \"is_end\": true,\r\n"
			+ "        \"pageable_count\": 4,\r\n"
			+ "        \"same_name\": {\r\n"
			+ "            \"keyword\": \"스시쿤\",\r\n"
			+ "            \"region\": [],\r\n"
			+ "            \"selected_region\": \"\"\r\n"
			+ "        },\r\n"
			+ "        \"total_count\": 4\r\n"
			+ "    }\r\n"
			+ "}";
	
	private static String kakaoResponse2 = "{\r\n"
			+ "    \"documents\": [\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"부산 해운대구 중동 1732\",\r\n"
			+ "            \"category_group_code\": \"FD6\",\r\n"
			+ "            \"category_group_name\": \"음식점\",\r\n"
			+ "            \"category_name\": \"음식점 > 한식 > 육류,고기 > 곱창,막창\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"27503208\",\r\n"
			+ "            \"phone\": \"051-731-3113\",\r\n"
			+ "            \"place_name\": \"해성막창집 본점\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/27503208\",\r\n"
			+ "            \"road_address_name\": \"부산 해운대구 중동1로19번길 29\",\r\n"
			+ "            \"x\": \"129.163275061964\",\r\n"
			+ "            \"y\": \"35.1638726172772\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"부산 중구 남포동6가 32\",\r\n"
			+ "            \"category_group_code\": \"FD6\",\r\n"
			+ "            \"category_group_name\": \"음식점\",\r\n"
			+ "            \"category_name\": \"음식점 > 한식 > 육류,고기 > 곱창,막창\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"7886000\",\r\n"
			+ "            \"phone\": \"051-245-2086\",\r\n"
			+ "            \"place_name\": \"백화양곱창 6호\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/7886000\",\r\n"
			+ "            \"road_address_name\": \"부산 중구 자갈치로23번길 6\",\r\n"
			+ "            \"x\": \"129.02706875483264\",\r\n"
			+ "            \"y\": \"35.097002085808725\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"서울 송파구 방이동 64-1\",\r\n"
			+ "            \"category_group_code\": \"FD6\",\r\n"
			+ "            \"category_group_name\": \"음식점\",\r\n"
			+ "            \"category_name\": \"음식점 > 한식 > 육류,고기 > 곱창,막창\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"983993205\",\r\n"
			+ "            \"phone\": \"02-413-3610\",\r\n"
			+ "            \"place_name\": \"별미곱창 본점\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/983993205\",\r\n"
			+ "            \"road_address_name\": \"서울 송파구 오금로11길 14\",\r\n"
			+ "            \"x\": \"127.10890911822922\",\r\n"
			+ "            \"y\": \"37.5143143020754\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"서울 마포구 도화동 179-11\",\r\n"
			+ "            \"category_group_code\": \"FD6\",\r\n"
			+ "            \"category_group_name\": \"음식점\",\r\n"
			+ "            \"category_name\": \"음식점 > 한식 > 육류,고기 > 곱창,막창\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"21732408\",\r\n"
			+ "            \"phone\": \"02-713-5201\",\r\n"
			+ "            \"place_name\": \"곱 마포점\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/21732408\",\r\n"
			+ "            \"road_address_name\": \"서울 마포구 도화길 31-1\",\r\n"
			+ "            \"x\": \"126.94937599308592\",\r\n"
			+ "            \"y\": \"37.541028722318174\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"서울 서초구 서초동 1571-19\",\r\n"
			+ "            \"category_group_code\": \"FD6\",\r\n"
			+ "            \"category_group_name\": \"음식점\",\r\n"
			+ "            \"category_name\": \"음식점 > 한식 > 육류,고기 > 곱창,막창\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"1235425275\",\r\n"
			+ "            \"phone\": \"02-583-6692\",\r\n"
			+ "            \"place_name\": \"세광양대창 교대본점\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/1235425275\",\r\n"
			+ "            \"road_address_name\": \"서울 서초구 반포대로28길 79\",\r\n"
			+ "            \"x\": \"127.01257422554994\",\r\n"
			+ "            \"y\": \"37.4920062884773\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"서울 동대문구 전농동 295-48\",\r\n"
			+ "            \"category_group_code\": \"FD6\",\r\n"
			+ "            \"category_group_name\": \"음식점\",\r\n"
			+ "            \"category_name\": \"음식점 > 한식 > 육류,고기 > 곱창,막창\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"15958681\",\r\n"
			+ "            \"phone\": \"02-2247-0254\",\r\n"
			+ "            \"place_name\": \"은하곱창\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/15958681\",\r\n"
			+ "            \"road_address_name\": \"서울 동대문구 전농로15길 22\",\r\n"
			+ "            \"x\": \"127.0564453467807\",\r\n"
			+ "            \"y\": \"37.578129135581044\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"대구 동구 신천동 334-14\",\r\n"
			+ "            \"category_group_code\": \"FD6\",\r\n"
			+ "            \"category_group_name\": \"음식점\",\r\n"
			+ "            \"category_name\": \"음식점 > 한식 > 육류,고기 > 곱창,막창\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"1942730766\",\r\n"
			+ "            \"phone\": \"053-744-5995\",\r\n"
			+ "            \"place_name\": \"우야지막창 동대구역점\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/1942730766\",\r\n"
			+ "            \"road_address_name\": \"대구 동구 동부로30길 34\",\r\n"
			+ "            \"x\": \"128.628596597816\",\r\n"
			+ "            \"y\": \"35.8740874371291\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"대구 서구 평리동 1097-3\",\r\n"
			+ "            \"category_group_code\": \"FD6\",\r\n"
			+ "            \"category_group_name\": \"음식점\",\r\n"
			+ "            \"category_name\": \"음식점 > 한식 > 육류,고기 > 곱창,막창\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"13133002\",\r\n"
			+ "            \"phone\": \"053-573-9282\",\r\n"
			+ "            \"place_name\": \"구공탄막창 형님본점\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/13133002\",\r\n"
			+ "            \"road_address_name\": \"대구 서구 국채보상로 308\",\r\n"
			+ "            \"x\": \"128.56460676807444\",\r\n"
			+ "            \"y\": \"35.871708413946926\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"서울 마포구 망원동 482-3\",\r\n"
			+ "            \"category_group_code\": \"FD6\",\r\n"
			+ "            \"category_group_name\": \"음식점\",\r\n"
			+ "            \"category_name\": \"음식점 > 한식 > 육류,고기 > 곱창,막창\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"13292214\",\r\n"
			+ "            \"phone\": \"\",\r\n"
			+ "            \"place_name\": \"청어람 망원점\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/13292214\",\r\n"
			+ "            \"road_address_name\": \"서울 마포구 망원로 97\",\r\n"
			+ "            \"x\": \"126.907469581002\",\r\n"
			+ "            \"y\": \"37.558105169041376\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"서울 강남구 역삼동 700-33\",\r\n"
			+ "            \"category_group_code\": \"FD6\",\r\n"
			+ "            \"category_group_name\": \"음식점\",\r\n"
			+ "            \"category_name\": \"음식점 > 한식 > 육류,고기 > 곱창,막창\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"7891890\",\r\n"
			+ "            \"phone\": \"02-501-2937\",\r\n"
			+ "            \"place_name\": \"별양집\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/7891890\",\r\n"
			+ "            \"road_address_name\": \"서울 강남구 테헤란로43길 17\",\r\n"
			+ "            \"x\": \"127.043672099153\",\r\n"
			+ "            \"y\": \"37.50436609869932\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"서울 영등포구 당산동6가 313-1\",\r\n"
			+ "            \"category_group_code\": \"FD6\",\r\n"
			+ "            \"category_group_name\": \"음식점\",\r\n"
			+ "            \"category_name\": \"음식점 > 한식 > 육류,고기 > 곱창,막창\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"26971510\",\r\n"
			+ "            \"phone\": \"02-3667-2315\",\r\n"
			+ "            \"place_name\": \"당산옛날곱창\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/26971510\",\r\n"
			+ "            \"road_address_name\": \"서울 영등포구 당산로47길 14\",\r\n"
			+ "            \"x\": \"126.90228518018989\",\r\n"
			+ "            \"y\": \"37.535371499608544\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"대구 남구 대명동 64-2\",\r\n"
			+ "            \"category_group_code\": \"FD6\",\r\n"
			+ "            \"category_group_name\": \"음식점\",\r\n"
			+ "            \"category_name\": \"음식점 > 한식 > 육류,고기 > 곱창,막창\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"12483743\",\r\n"
			+ "            \"phone\": \"053-622-7071\",\r\n"
			+ "            \"place_name\": \"구공탄막창 2호점\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/12483743\",\r\n"
			+ "            \"road_address_name\": \"대구 남구 대명로 303\",\r\n"
			+ "            \"x\": \"128.58886426570672\",\r\n"
			+ "            \"y\": \"35.84494584474169\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"서울 강남구 삼성동 76-10\",\r\n"
			+ "            \"category_group_code\": \"FD6\",\r\n"
			+ "            \"category_group_name\": \"음식점\",\r\n"
			+ "            \"category_name\": \"음식점 > 한식 > 육류,고기 > 곱창,막창\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"8664636\",\r\n"
			+ "            \"phone\": \"02-511-0068\",\r\n"
			+ "            \"place_name\": \"곰바위\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/8664636\",\r\n"
			+ "            \"road_address_name\": \"서울 강남구 영동대로115길 10\",\r\n"
			+ "            \"x\": \"127.05880695418199\",\r\n"
			+ "            \"y\": \"37.51486885062181\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"부산 부산진구 부전동 519-18\",\r\n"
			+ "            \"category_group_code\": \"FD6\",\r\n"
			+ "            \"category_group_name\": \"음식점\",\r\n"
			+ "            \"category_name\": \"음식점 > 한식 > 육류,고기 > 곱창,막창\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"8988183\",\r\n"
			+ "            \"phone\": \"051-808-2072\",\r\n"
			+ "            \"place_name\": \"문화양곱창\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/8988183\",\r\n"
			+ "            \"road_address_name\": \"부산 부산진구 가야대로784번길 56-8\",\r\n"
			+ "            \"x\": \"129.056712633763\",\r\n"
			+ "            \"y\": \"35.1550308363705\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"부산 중구 부평동2가 26-1\",\r\n"
			+ "            \"category_group_code\": \"FD6\",\r\n"
			+ "            \"category_group_name\": \"음식점\",\r\n"
			+ "            \"category_name\": \"음식점 > 한식 > 육류,고기 > 곱창,막창\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"16723710\",\r\n"
			+ "            \"phone\": \"051-248-0228\",\r\n"
			+ "            \"place_name\": \"대정양곱창 본관\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/16723710\",\r\n"
			+ "            \"road_address_name\": \"부산 중구 중구로23번길 31\",\r\n"
			+ "            \"x\": \"129.026098752709\",\r\n"
			+ "            \"y\": \"35.0999553291537\"\r\n"
			+ "        }\r\n"
			+ "    ],\r\n"
			+ "    \"meta\": {\r\n"
			+ "        \"is_end\": false,\r\n"
			+ "        \"pageable_count\": 45,\r\n"
			+ "        \"same_name\": {\r\n"
			+ "            \"keyword\": \"곱창\",\r\n"
			+ "            \"region\": [],\r\n"
			+ "            \"selected_region\": \"\"\r\n"
			+ "        },\r\n"
			+ "        \"total_count\": 14492\r\n"
			+ "    }\r\n"
			+ "}";
	
	private static String kakaoResponse = "{\r\n"
			+ "    \"documents\": [\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"서울 중구 을지로1가 101-1\",\r\n"
			+ "            \"category_group_code\": \"BK9\",\r\n"
			+ "            \"category_group_name\": \"은행\",\r\n"
			+ "            \"category_name\": \"금융,보험 > 금융서비스 > 은행 > 하나은행\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"8124674\",\r\n"
			+ "            \"phone\": \"1599-1111\",\r\n"
			+ "            \"place_name\": \"하나은행 본점\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/8124674\",\r\n"
			+ "            \"road_address_name\": \"서울 중구 을지로 35\",\r\n"
			+ "            \"x\": \"126.981866951611\",\r\n"
			+ "            \"y\": \"37.566491371702\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"서울 영등포구 여의도동 36-3\",\r\n"
			+ "            \"category_group_code\": \"BK9\",\r\n"
			+ "            \"category_group_name\": \"은행\",\r\n"
			+ "            \"category_name\": \"금융,보험 > 금융서비스 > 은행 > KB국민은행\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"8202771\",\r\n"
			+ "            \"phone\": \"02-2073-7114\",\r\n"
			+ "            \"place_name\": \"KB국민은행 여의도본점\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/8202771\",\r\n"
			+ "            \"road_address_name\": \"서울 영등포구 국제금융로8길 26\",\r\n"
			+ "            \"x\": \"126.927905661537\",\r\n"
			+ "            \"y\": \"37.5208657732053\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"서울 영등포구 영등포동4가 68-2\",\r\n"
			+ "            \"category_group_code\": \"BK9\",\r\n"
			+ "            \"category_group_name\": \"은행\",\r\n"
			+ "            \"category_name\": \"금융,보험 > 금융서비스 > 은행 > KB국민은행\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"8198779\",\r\n"
			+ "            \"phone\": \"\",\r\n"
			+ "            \"place_name\": \"KB국민은행 영등포지점\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/8198779\",\r\n"
			+ "            \"road_address_name\": \"서울 영등포구 영등포로 208\",\r\n"
			+ "            \"x\": \"126.904255816231\",\r\n"
			+ "            \"y\": \"37.51968487408\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"광주 광산구 수완동 1080\",\r\n"
			+ "            \"category_group_code\": \"BK9\",\r\n"
			+ "            \"category_group_name\": \"은행\",\r\n"
			+ "            \"category_name\": \"금융,보험 > 금융서비스 > 은행 > KB국민은행\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"11658267\",\r\n"
			+ "            \"phone\": \"062-962-3071\",\r\n"
			+ "            \"place_name\": \"KB국민은행 수완지점\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/11658267\",\r\n"
			+ "            \"road_address_name\": \"광주 광산구 임방울대로 348\",\r\n"
			+ "            \"x\": \"126.824308571022\",\r\n"
			+ "            \"y\": \"35.1909981519218\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"서울 중구 회현동1가 203\",\r\n"
			+ "            \"category_group_code\": \"BK9\",\r\n"
			+ "            \"category_group_name\": \"은행\",\r\n"
			+ "            \"category_name\": \"금융,보험 > 금융서비스 > 은행 > 우리은행\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"1310395223\",\r\n"
			+ "            \"phone\": \"02-2002-3000\",\r\n"
			+ "            \"place_name\": \"우리은행 본점\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/1310395223\",\r\n"
			+ "            \"road_address_name\": \"서울 중구 소공로 51\",\r\n"
			+ "            \"x\": \"126.98175547982937\",\r\n"
			+ "            \"y\": \"37.55944556203258\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"부산 동구 범일동 830-58\",\r\n"
			+ "            \"category_group_code\": \"BK9\",\r\n"
			+ "            \"category_group_name\": \"은행\",\r\n"
			+ "            \"category_name\": \"금융,보험 > 금융서비스 > 은행 > KB국민은행\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"146711523\",\r\n"
			+ "            \"phone\": \"1599-9999\",\r\n"
			+ "            \"place_name\": \"KB국민은행 범일동종합금융센터\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/146711523\",\r\n"
			+ "            \"road_address_name\": \"부산 동구 범일로102번길 8\",\r\n"
			+ "            \"x\": \"129.060378063338\",\r\n"
			+ "            \"y\": \"35.1388665447651\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"서울 중구 충정로1가 75-1\",\r\n"
			+ "            \"category_group_code\": \"BK9\",\r\n"
			+ "            \"category_group_name\": \"은행\",\r\n"
			+ "            \"category_name\": \"금융,보험 > 금융서비스 > 은행 > NH농협은행\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"8234184\",\r\n"
			+ "            \"phone\": \"1661-3000\",\r\n"
			+ "            \"place_name\": \"NH농협은행 본사\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/8234184\",\r\n"
			+ "            \"road_address_name\": \"서울 중구 새문안로 16\",\r\n"
			+ "            \"x\": \"126.96854456956765\",\r\n"
			+ "            \"y\": \"37.56628134476508\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"서울 마포구 동교동 160-4\",\r\n"
			+ "            \"category_group_code\": \"BK9\",\r\n"
			+ "            \"category_group_name\": \"은행\",\r\n"
			+ "            \"category_name\": \"금융,보험 > 금융서비스 > 은행 > KB국민은행\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"8201410\",\r\n"
			+ "            \"phone\": \"02-335-2100\",\r\n"
			+ "            \"place_name\": \"KB국민은행 서교동종합금융센터\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/8201410\",\r\n"
			+ "            \"road_address_name\": \"서울 마포구 양화로 147\",\r\n"
			+ "            \"x\": \"126.9220546249462\",\r\n"
			+ "            \"y\": \"37.556047909888946\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"서울 중구 을지로2가 50\",\r\n"
			+ "            \"category_group_code\": \"BK9\",\r\n"
			+ "            \"category_group_name\": \"은행\",\r\n"
			+ "            \"category_name\": \"금융,보험 > 금융서비스 > 은행 > 기업은행\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"793156041\",\r\n"
			+ "            \"phone\": \"02-729-6114\",\r\n"
			+ "            \"place_name\": \"IBK기업은행 영업부본점\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/793156041\",\r\n"
			+ "            \"road_address_name\": \"서울 중구 을지로 79\",\r\n"
			+ "            \"x\": \"126.9865643391636\",\r\n"
			+ "            \"y\": \"37.56649199972894\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"전남 순천시 연향동 1324-2\",\r\n"
			+ "            \"category_group_code\": \"BK9\",\r\n"
			+ "            \"category_group_name\": \"은행\",\r\n"
			+ "            \"category_name\": \"금융,보험 > 금융서비스 > 은행 > KB국민은행\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"8698423\",\r\n"
			+ "            \"phone\": \"\",\r\n"
			+ "            \"place_name\": \"KB국민은행 연향종합금융센터\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/8698423\",\r\n"
			+ "            \"road_address_name\": \"전남 순천시 연향번영길 149\",\r\n"
			+ "            \"x\": \"127.518745142281\",\r\n"
			+ "            \"y\": \"34.9533138264752\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"서울 마포구 상암동 1605\",\r\n"
			+ "            \"category_group_code\": \"BK9\",\r\n"
			+ "            \"category_group_name\": \"은행\",\r\n"
			+ "            \"category_name\": \"금융,보험 > 금융서비스 > 은행 > 우리은행\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"14920153\",\r\n"
			+ "            \"phone\": \"02-374-9911\",\r\n"
			+ "            \"place_name\": \"우리은행 상암DMC금융센터\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/14920153\",\r\n"
			+ "            \"road_address_name\": \"서울 마포구 월드컵북로 396\",\r\n"
			+ "            \"x\": \"126.88942135174955\",\r\n"
			+ "            \"y\": \"37.57943602295691\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"인천 부평구 부평동 199-14\",\r\n"
			+ "            \"category_group_code\": \"BK9\",\r\n"
			+ "            \"category_group_name\": \"은행\",\r\n"
			+ "            \"category_name\": \"금융,보험 > 금융서비스 > 은행 > KB국민은행\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"328946284\",\r\n"
			+ "            \"phone\": \"032-527-8001\",\r\n"
			+ "            \"place_name\": \"KB국민은행 부평종합금융센터\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/328946284\",\r\n"
			+ "            \"road_address_name\": \"인천 부평구 부평대로 20\",\r\n"
			+ "            \"x\": \"126.72336258899549\",\r\n"
			+ "            \"y\": \"37.493288663121554\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"서울 서초구 서초동 1318-2\",\r\n"
			+ "            \"category_group_code\": \"BK9\",\r\n"
			+ "            \"category_group_name\": \"은행\",\r\n"
			+ "            \"category_name\": \"금융,보험 > 금융서비스 > 은행 > 하나은행\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"8458527\",\r\n"
			+ "            \"phone\": \"02-534-0312\",\r\n"
			+ "            \"place_name\": \"하나은행 강남역지점\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/8458527\",\r\n"
			+ "            \"road_address_name\": \"서울 서초구 강남대로 405\",\r\n"
			+ "            \"x\": \"127.02676902248052\",\r\n"
			+ "            \"y\": \"37.4988191403645\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"서울 광진구 자양동 636-10\",\r\n"
			+ "            \"category_group_code\": \"BK9\",\r\n"
			+ "            \"category_group_name\": \"은행\",\r\n"
			+ "            \"category_name\": \"금융,보험 > 금융서비스 > 은행 > KB국민은행\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"8670456\",\r\n"
			+ "            \"phone\": \"02-455-2311\",\r\n"
			+ "            \"place_name\": \"KB국민은행 자양동지점\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/8670456\",\r\n"
			+ "            \"road_address_name\": \"서울 광진구 뚝섬로 632\",\r\n"
			+ "            \"x\": \"127.079748413502\",\r\n"
			+ "            \"y\": \"37.5317773918479\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"address_name\": \"제주특별자치도 제주시 노형동 726-2\",\r\n"
			+ "            \"category_group_code\": \"BK9\",\r\n"
			+ "            \"category_group_name\": \"은행\",\r\n"
			+ "            \"category_name\": \"금융,보험 > 금융서비스 > 은행 > KB국민은행\",\r\n"
			+ "            \"distance\": \"\",\r\n"
			+ "            \"id\": \"8201706\",\r\n"
			+ "            \"phone\": \"\",\r\n"
			+ "            \"place_name\": \"KB국민은행 연북로지점\",\r\n"
			+ "            \"place_url\": \"http://place.map.kakao.com/8201706\",\r\n"
			+ "            \"road_address_name\": \"제주특별자치도 제주시 연북로 50\",\r\n"
			+ "            \"x\": \"126.486732606393\",\r\n"
			+ "            \"y\": \"33.4816941457389\"\r\n"
			+ "        }\r\n"
			+ "    ],\r\n"
			+ "    \"meta\": {\r\n"
			+ "        \"is_end\": false,\r\n"
			+ "        \"pageable_count\": 45,\r\n"
			+ "        \"same_name\": {\r\n"
			+ "            \"keyword\": \"은행\",\r\n"
			+ "            \"region\": [],\r\n"
			+ "            \"selected_region\": \"\"\r\n"
			+ "        },\r\n"
			+ "        \"total_count\": 46203\r\n"
			+ "    }\r\n"
			+ "}"; 

}
