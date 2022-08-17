package com.kakao.bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class KeywordRankConfig {
	
	@Bean
	public ReactiveRedisTemplate<String, Long> redisOperations(ReactiveRedisConnectionFactory factory) {

		RedisSerializationContext.RedisSerializationContextBuilder<String, Long> builder = RedisSerializationContext
				.newSerializationContext(new StringRedisSerializer());
		RedisSerializationContext<String, Long> context = builder.value(new GenericToStringSerializer< Long >( Long.class )).build();
		return new ReactiveRedisTemplate<>(factory, context);
	}
}
