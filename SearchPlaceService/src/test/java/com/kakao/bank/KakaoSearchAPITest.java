package com.kakao.bank;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class KakaoSearchAPITest {

	
	private static String API_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";
	
//	@BeforeAll
//	void setup2() {
//		System.out.println("setup2");
//	}
	
//	@BeforeEach
//	void setup() {
//		System.out.println("setup");
//	}
	
	@Test
	void test() {
		System.out.println("test1");
	}
	
	@Test
	void test2() {
		System.out.println("test2");
	}
	
	
	@Test
	void test3() throws InterruptedException, ExecutionException {
		System.out.println(get(API_URL).join());
	}
	
	public CompletableFuture<String> get(String uri) {
	    HttpClient client = HttpClient.newHttpClient();
	    HttpRequest request = HttpRequest.newBuilder()
	          .uri(URI.create(uri))
	          .header("Authorization", "KakaoAK 5107c1f7fc6b2b98959417aa5a07ad1d")
	          .build();

	    CompletableFuture<HttpResponse<String>> sendAsync = client.sendAsync(request, BodyHandlers.ofString());
	    return client.sendAsync(request, BodyHandlers.ofString())
	          .thenApply(HttpResponse::body);
	}
	
	@Test
	void test4() {
		 HttpClient client = HttpClient.newHttpClient();
		    HttpRequest request = HttpRequest.newBuilder()
		          .uri(URI.create(API_URL))
		          .header("Authorization", "KakaoAK 5107c1f7fc6b2b98959417aa5a07ad1d")
		          .build();
		    
		    client.sendAsync(request, BodyHandlers.ofString())
		    .thenApply(response -> {
		    	System.out.println(response + " test");
		    	if(response.statusCode() != 200) {
		    		throw new RuntimeException();
		    	}
		    	return response;
		    })
		    .thenApply(HttpResponse::body)
		    .exceptionallyCompose(null)
		    .exceptionally(e -> "Error: " + e.getMessage())
		    .thenAccept(str -> { System.out.println(str + " test2"); })
		    .join();
	}
	
	@Test
	void test5() {
		 HttpClient client = HttpClient.newHttpClient();
		    HttpRequest request = HttpRequest.newBuilder()
		          .uri(URI.create(API_URL))
		          .header("Authorization", "KakaoAK 5107c1f7fc6b2b98959417aa5a07ad1d")
		          .build();
		    
		    client.sendAsync(request, BodyHandlers.ofString())
		    .whenComplete((response, err) ->{
		    	if(err!= null ) {
		    		err.printStackTrace();
		    	}else {
		    		System.out.println(response);
		    		System.out.println(response.body());
		    	}
		    })
		    .join();
	}
	
	@Test
	void test6() {
		
		
		 HttpClient client = HttpClient.newBuilder()
					.connectTimeout(Duration.ofMillis(3000))
					.build();
		    HttpRequest request = HttpRequest.newBuilder()
		          .uri(URI.create("http://localhost:1234"))
		          .header("Authorization", "KakaoAK 5107c1f7fc6b2b98959417aa5a07ad1d")
		          .timeout(Duration.ofMillis(3000))
		          .build();
		    
		    client.sendAsync(request, BodyHandlers.ofString())
		    .whenComplete((response, err) ->{
		    	if(err!= null ) {
		    		System.out.println(err.getClass());
		    	}else {
		    		System.out.println(response);
		    		System.out.println(response.body());
		    	}
		    })
		    .join();
	}
	
	@Test
	void test7() {
		
		
		 HttpClient client = HttpClient.newBuilder()
					.connectTimeout(Duration.ofMillis(3000))
					.build();
		    HttpRequest request = HttpRequest.newBuilder()
		          .uri(URI.create("http://localhost:8080/test"))
		          .header("Authorization", "KakaoAK 5107c1f7fc6b2b98959417aa5a07ad1d")
		          .timeout(Duration.ofMillis(2000))
		          .build();
		    
		    client.sendAsync(request, BodyHandlers.ofString())
		    .whenComplete((response, err) ->{
		    	if(err!= null ) {
		    		System.out.println(err.getClass());
		    		err.printStackTrace();
		    	}else {
		    		System.out.println(response);
		    		System.out.println(response.body());
		    	}
		    }).thenAccept(System.out::println);
		    
	}
}
