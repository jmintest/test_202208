package com.kakao.bank;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@Disabled
public class KakaoSearchAPITest2 {

	
	private static String API_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";
	
	@Test
	@Disabled
	void test5() {
		
		HttpClient httpClient = HttpClient.create()
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
				
				.option(ChannelOption.SO_KEEPALIVE, true)
				  .option(EpollChannelOption.TCP_KEEPIDLE, 300)
				  .option(EpollChannelOption.TCP_KEEPINTVL, 60)
				  .option(EpollChannelOption.TCP_KEEPCNT, 8)
		
				  .responseTimeout(Duration.ofSeconds(1))
				  
				  .doOnConnected(conn -> conn
						    .addHandler(new ReadTimeoutHandler(10, TimeUnit.SECONDS))
						    .addHandler(new WriteTimeoutHandler(10)));
		
				  ;
		
		
		WebClient webClient = WebClient.builder()
				  .clientConnector(new ReactorClientHttpConnector(httpClient))
				  .build();
		
		webClient.get()
		.uri(uriBuilder ->
		uriBuilder.path(API_URL)
			.queryParam("name", "value")
                        .build()
				).retrieve()
		.bodyToMono(String.class)
		.map(body -> {
			System.out.println(body + " testest");
			return body;
		}).subscribe();
	}
}
