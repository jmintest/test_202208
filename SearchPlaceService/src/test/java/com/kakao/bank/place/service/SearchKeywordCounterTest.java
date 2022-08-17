package com.kakao.bank.place.service;

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
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class SearchKeywordCounterTest {

	@Mock
	private ReactiveRedisTemplate<String, Long> redis;
	
	@Mock
	private ReactiveValueOperations<String, Long> opsForValue;
	
	@InjectMocks
	private SearchKeywordCounter counter;
	
	@BeforeEach
	void init() {
		ReflectionTestUtils.setField(counter, "prefix", "keyword:");
		when(redis.opsForValue()).thenReturn(opsForValue);
		when(opsForValue.increment(org.mockito.Mockito.anyString())).thenReturn(Mono.just(1L));
		
		
	}
	
	@Test
	void increment() {
		counter.increment("query");
		verify(redis, times(1)).opsForValue();
		verify(opsForValue, times(1)).increment("keyword:query");
		verifyNoMoreInteractions(redis);
		verifyNoMoreInteractions(opsForValue);
	}

}
