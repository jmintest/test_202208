package com.kakao.bank.place.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.reactive.function.client.WebClient;

import com.kakao.bank.place.api.KakaoLocalAPI;
import com.kakao.bank.place.api.NaverSearchAPI;

@Configuration
public class SearchPlaceConfig {

	@Bean
	public WebClient webClient() {
		return WebClient.builder().build();
	}

	@Bean
	public KakaoLocalAPI kakaoSearchPlaceAPI(WebClient webClient) {
		return new KakaoLocalAPI(webClient);
	}

	@Bean
	public NaverSearchAPI naverSearchPlaceAPI(WebClient webClient) {
		return new NaverSearchAPI(webClient);
	}
	
	@Bean
	public ReactiveRedisTemplate<String, Long> redisOperations(ReactiveRedisConnectionFactory factory) {

		RedisSerializationContext.RedisSerializationContextBuilder<String, Long> builder = RedisSerializationContext
				.newSerializationContext(new StringRedisSerializer());
		RedisSerializationContext<String, Long> context = builder.value(new GenericToStringSerializer< Long >( Long.class )).build();
		return new ReactiveRedisTemplate<>(factory, context);
	}
}
