package com.kakao.bank.service;

import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Service;

import com.kakao.bank.dto.PlaceScore;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class KeywordRankService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ReactiveRedisTemplate<String, Long> redis;

	@Value("${api.itg.redis.prefix:keyword:}")
	private String prefix;
	
	@Value("${api.rank.count:10}")
	private int rankCount;
	
	public Flux<PlaceScore> rank() {
		ReactiveValueOperations<String, Long> opsForValue = redis.opsForValue();
		
		return redis.keys(String.format("%s%s", prefix, "*"))
			.flatMap(key ->{
				PlaceScore placeScore = new PlaceScore();
				placeScore.setPlace_name(key.substring(prefix.length()));
				return Mono.zip(Mono.just(placeScore), opsForValue.get(key));
			}).map(data ->{
				PlaceScore place = data.getT1();
				place.setScore(data.getT2());
				return place;
			})
			.sort(Comparator.comparing(PlaceScore::getScore).reversed())
			.take(rankCount)
			;
	}
	
}
