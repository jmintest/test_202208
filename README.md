# 서비스 개요

```
과제 코드에서 직접 활용한 라이브러리에 대해 사용 목적과 선택 사유 등을 README 파일에 명시
```

## 사용 기술
 * Java 8
 * Spring boot 2.7.2
 * Micro service architecture
 * Spring cloud(gateway, eureka)
 * Webflux(+WebClient)
 * Redis

## 주요 기술 설명

 * Redis : 키워드 별 검색 횟수에 대한 동시성 처리를 위해 Redis INCR 명령 사용(Redis atomic increment operations)
```
동시성 이슈가 발생할 수 있는 부분을 염두에 둔 설계 및 구현 (예시. 키워드 별로 검색된 횟수)
```
 * Micro service architecture : 반응성(Low Latency), 확장성(Scalability), 가용성(Availability)을 높이기 위한 전략으로 MSA 아키텍쳐를 구성하였습니다.
 docker/kubernetes 등의 기술과 연계하면 상시 동작하는 서비스로 구성할 수 있습니다. 
* Spring cloud(gateway, eureka) : Micro service architecture 구성을 위하여 Spring cloud의 gateway와 eureka server를 활용하였습니다.

```
서비스 오류 및 장애 처리 방법에 대한 고려
대용량 트래픽 처리를 위한 반응성(Low Latency), 확장성(Scalability), 가용성(Availability)을 높이기 위한 고려
지속적 유지 보수 및 확장에 용이한 아키텍처에 대한 설계
```

 * Webflux(+WebClient) : 대용량 트래픽 처리를 위한 반응성(Low Latency)을 위하여 Webflux를 사용하였습니다. 모든 구간을 비동기적 처리를 위해 사용하였습니다.
 
 ```
 대용량 트래픽 처리를 위한 반응성(Low Latency)
 ```
 
## API

### 장소 검색

* URL
```
 GET /search/place
```

* Parameter

| Name | Type | Description | Required |
| ---- | ---- | ---- | ---- |
| query | String | 검색을 원하는 질의어 | O |

* Response

| Name | Type | Description |
| ---- | ---- | ---- |
| place | List | 결과 목록 |

* place

| Name | Type | Description |
| ---- | ---- | ---- |
| place_name | String | 장소명, 업체명 |
| place_url | String | 카테고리 이름 |
| phone | String | 전화번호 |
| category_name | String | 카테고리 이름 |
| address_name | String | 전체 지번 주소 |
| road_address_name | String | - |
| x | String | X 좌표값, 경위도인 경우 longitude (경도) |
| y | String | Y 좌표값, 경위도인 경우 latitude(위도) |
| source | String | 사용 API(kakao/naver) |



### 검색 키워드 목록

* URL
```
 GET /keyword/rank
```

* Response

| Name | Type | Description |
| ---- | ---- | ---- |
| - | List | 결과 목록 |

| Name | Type | Description |
| ---- | ---- | ---- |
| place_name | String | 장소명, 업체명 |
| score | Integer | 검색 횟수 |


## cURL

* 장소 검색
```
curl --location --request GET 'http://localhost:8080/search/place?query=은행'
```
![image](https://user-images.githubusercontent.com/111035377/185112319-72da8882-bbb2-44f8-a2e9-8d01977270d4.png)


* 검색어 목록
```
curl --location --request GET 'http://localhost:8080/keyword/rank'
```
![image](https://user-images.githubusercontent.com/111035377/185112402-8b891334-5343-449b-9968-2509f7939965.png)



## 기타
 * Junit code coverage
   * https://github.com/jmintest/test_202208/issues/11#issuecomment-1217814712
   * https://github.com/jmintest/test_202208/issues/11#issuecomment-1217872793
 * 부하 테스트(Jmeter)
   *  https://github.com/jmintest/test_202208/issues/11#issuecomment-1217885798
