package com.kakao.bank.place.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class SearchKeywordCounter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ReactiveRedisTemplate<String, Long> redis;
	
	
	@Value("${api.itg.redis.prefix:keyword:}")
	private String prefix;
	
	
	public Mono<Long> increment(String keyword) {
		String key = String.format("%s%s", prefix, keyword);
		logger.info("increment keyword. [keyword={}]", key);
		return redis.opsForValue().increment(key);
	}
	
	
}
