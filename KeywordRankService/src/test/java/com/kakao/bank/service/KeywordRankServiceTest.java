package com.kakao.bank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import com.kakao.bank.dto.PlaceScore;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@ExtendWith(MockitoExtension.class)
class KeywordRankServiceTest {

	@Mock
	private ReactiveRedisTemplate<String, Long> redis;
	
	@Mock
	ReactiveValueOperations<String, Long> opsForValue;
	
	
	@InjectMocks
	private KeywordRankService service;
	
	
	@BeforeEach
	void init() {
		
		ReflectionTestUtils.setField(service, "rankCount", 10);
		ReflectionTestUtils.setField(service, "prefix", "keyword:");
		when(redis.opsForValue()).thenReturn(opsForValue);
		
		when(redis.keys(String.format("%s%s", "keyword:", "*"))).thenReturn(
				Flux.just(
						"keyword:은행",
						"keyword:곱창",
						"keyword:스시쿤"
						));
		
		when(opsForValue.get("keyword:은행")).thenReturn(Mono.just(10L));
		when(opsForValue.get("keyword:곱창")).thenReturn(Mono.just(20L));
		when(opsForValue.get("keyword:스시쿤")).thenReturn(Mono.just(3L));
	}
	
	
	@Test
	void rank() {
		List<PlaceScore> collect = service.rank().toStream().collect(Collectors.toList());
		
		verify(redis, times(1)).keys("keyword:*");
		verify(redis, times(1)).opsForValue();
		verify(opsForValue, times(1)).get("keyword:은행");
		verify(opsForValue, times(1)).get("keyword:곱창");
		verify(opsForValue, times(1)).get("keyword:스시쿤");
		
		assertEquals(collect.size(), 3);
		assertEquals(collect.get(0).getPlace_name(), "곱창");
		assertEquals(collect.get(1).getPlace_name(), "은행");
		assertEquals(collect.get(2).getPlace_name(), "스시쿤");
		
		assertEquals(collect.get(0).getScore(), 20);
		assertEquals(collect.get(1).getScore(), 10);
		assertEquals(collect.get(2).getScore(), 3);
		
		
		verifyNoMoreInteractions(redis);
		verifyNoMoreInteractions(opsForValue);
		
	}
	
	@Test
	void rankLimit() {
		
		when(redis.keys(String.format("%s%s", "keyword:", "*"))).thenReturn(
				Flux.just(
						"keyword:은행",
						"keyword:곱창",
						"keyword:스시쿤",
						"keyword:테스트1",
						"keyword:테스트2",
						"keyword:테스트3",
						"keyword:테스트4",
						"keyword:테스트5",
						"keyword:테스트6",
						"keyword:테스트7",
						"keyword:테스트8",
						"keyword:테스트9",
						"keyword:테스트10"
						));
		
		when(opsForValue.get("keyword:은행")).thenReturn(Mono.just(10L));
		when(opsForValue.get("keyword:곱창")).thenReturn(Mono.just(20L));
		when(opsForValue.get("keyword:스시쿤")).thenReturn(Mono.just(3L));
		when(opsForValue.get("keyword:테스트1")).thenReturn(Mono.just(1L));
		when(opsForValue.get("keyword:테스트2")).thenReturn(Mono.just(2L));
		when(opsForValue.get("keyword:테스트3")).thenReturn(Mono.just(3L));
		when(opsForValue.get("keyword:테스트4")).thenReturn(Mono.just(4L));
		when(opsForValue.get("keyword:테스트5")).thenReturn(Mono.just(5L));
		when(opsForValue.get("keyword:테스트6")).thenReturn(Mono.just(6L));
		when(opsForValue.get("keyword:테스트7")).thenReturn(Mono.just(7L));
		when(opsForValue.get("keyword:테스트8")).thenReturn(Mono.just(8L));
		when(opsForValue.get("keyword:테스트9")).thenReturn(Mono.just(9L));
		when(opsForValue.get("keyword:테스트10")).thenReturn(Mono.just(10L));
		
		
		
		List<PlaceScore> collect = service.rank().toStream().collect(Collectors.toList());
		
		verify(redis, times(1)).keys("keyword:*");
		verify(redis, times(1)).opsForValue();
		verify(opsForValue, times(1)).get("keyword:은행");
		verify(opsForValue, times(1)).get("keyword:곱창");
		verify(opsForValue, times(1)).get("keyword:스시쿤");
		
		assertEquals(collect.size(), 10);
		assertEquals(collect.get(0).getPlace_name(), "곱창");
		assertEquals(collect.get(1).getPlace_name(), "은행");
		assertEquals(collect.get(2).getPlace_name(), "테스트10");
		assertEquals(collect.get(3).getPlace_name(), "테스트9");
		assertEquals(collect.get(4).getPlace_name(), "테스트8");
		assertEquals(collect.get(5).getPlace_name(), "테스트7");
		assertEquals(collect.get(6).getPlace_name(), "테스트6");
		assertEquals(collect.get(7).getPlace_name(), "테스트5");
		assertEquals(collect.get(8).getPlace_name(), "테스트4");
		assertEquals(collect.get(9).getPlace_name(), "스시쿤");
		
		assertEquals(collect.get(0).getScore(), 20);
		assertEquals(collect.get(1).getScore(), 10);
		assertEquals(collect.get(2).getScore(), 10);
		assertEquals(collect.get(3).getScore(), 9);
		assertEquals(collect.get(4).getScore(), 8);
		assertEquals(collect.get(5).getScore(), 7);
		assertEquals(collect.get(6).getScore(), 6);
		assertEquals(collect.get(7).getScore(), 5);
		assertEquals(collect.get(8).getScore(), 4);
		assertEquals(collect.get(9).getScore(), 3);
		
		verifyNoMoreInteractions(redis);
		verifyNoMoreInteractions(opsForValue);
		
	}


}
