package com.kakao.bank.place.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kakao.bank.place.api.KakaoLocalAPI;
import com.kakao.bank.place.api.NaverSearchAPI;
import com.kakao.bank.place.api.kakao.KeywordResponse;
import com.kakao.bank.place.api.naver.LocalResponse;
import com.kakao.bank.place.dto.Place;
import com.kakao.bank.place.dto.SearchPlaceResponse;

import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
public class IntegratedSearchPlaceAPI {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private KakaoLocalAPI kakaoAPI;
	
	@Autowired
	private NaverSearchAPI naverAPI;
	
	@Autowired
	private SearchKeywordCounter counter;
	
	@Value("${api.itg.count:10}")
	private int totalCount;

	
	public Mono<SearchPlaceResponse> get(String query){
		
		return Mono.zip(
				kakaoAPI.keyword(query),
				naverAPI.local(query)
				)
		.map(this::merge)
		.map(placeList -> {
			return new SearchPlaceResponse(placeList);
		})
		.map(response->{
			List<Place> place = response.place;
			for (Place place2 : place) {
				logger.info("place [query={}/name={}/address={}/source={}]", query, place2.place_name, place2.address_name, place2.source);
			}
			return response;
		})
//		.doOnSubscribe(e -> { System.out.println("doOnSubscribe");})
		.doOnSuccess(e -> { counter.increment(query).subscribe();})
		.log();
		
	}
	
	public List<Place> merge(Tuple2<KeywordResponse, LocalResponse> result){
		
		Tuple2<List<Place>, List<Place>> combine = result
				.mapT1(t1 -> t1.documents.stream().map(Place::convert).collect(Collectors.toList()))
				.mapT2(t2 -> t2.items.stream().map(Place::convert).collect(Collectors.toList()));
		
		
		List<Place> retval = new ArrayList<>();
		
		List<Place> kakaoResult = combine.getT1();
		List<Place> naverResult = combine.getT2();
		
		for(int i = 0 ; i < kakaoResult.size() ; i ++) {
			Place kakaoPlace = kakaoResult.get(i);
			for(int j = 0 ; j < naverResult.size() ; j ++) {
				Place naverPlace = naverResult.get(j);
				if(kakaoPlace.similar(naverPlace)) {
					retval.add(kakaoPlace);
					kakaoPlace.mark = true;
					naverPlace.mark = true;
				}
			}
		}
		
		int sameCount = retval.size(); // 카카오 결과 10개, 네이버 결과 5개 중 3개 겹친다고 했을때
		int lack  = totalCount - sameCount; // 7개를 채워야함 
		int kakaoLack = lack - (naverResult.size() - sameCount); //  네이버에서 2개, 카카오에서 나머지(5개) 채움
		int naverLack = naverResult.size() - sameCount;
		
		int addCount = 0;
		for(int i = 0 ; i < kakaoResult.size() ; i ++) {
			Place kakaoPlace = kakaoResult.get(i);
			if(!kakaoPlace.mark) {
				retval.add(kakaoPlace);
				addCount++;
			}
			if(addCount>=kakaoLack) {
				break;
			}
		}
		
		addCount = 0;
		for(int i = 0 ; i < naverResult.size() ; i ++) {
			Place naverPlace = naverResult.get(i);
			if(!naverPlace.mark) {
				retval.add(naverPlace);
				addCount++;
			}
			if(addCount>=naverLack) {
				break;
			}
		}
		
		logger.info("merge result. [total={}/sameCount={}/kakaoLack={}/naverLack={}/retval={}]", totalCount, sameCount, kakaoLack, naverLack, retval.size());
		
		return retval;
		
	}

}
