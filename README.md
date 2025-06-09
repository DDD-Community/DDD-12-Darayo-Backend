# DDD 12기 달아요S2 Backend


### 디렉터리 구조

<details>
<summary> 초기 구조 </summary>

```bash
festival/
├── domain/                    # 도메인 로직 전반 (엔티티, 서비스), 영속성 로직
│   ├── constant/              # enum class들
│   ├── entity/                # JPA Entity 등 비즈니스 모델
│   ├── repository/            # JPA Repository 또는 QueryDSL 등 데이터 저장/조회 구현체
│   └── service/               # 도메인 서비스 (Controller가 직접 의존, UseCase 역할을 대신 함)
│
├── infra/                      # 기술 세부 사항
│   └── fcm/                    # fcm
│   └── ext-library/            # 기타 외부 종속성
│
├── presentation/              # 외부 요청을 받는 계층 (REST API ONLY)
│   ├── artist/                
│   │   ├── ArtistController.java
│   │   └── exchanges/         # 요청/응답 DTO
│   ├── performance/
│   │   ├── PerformanceController.java
│   │   └── exchanges/
│   └── advice/

```
domain 아래에 performance, festival 등 분리할 수 있는 도메인들은 분리할 수 있으면 좋겠지만 관리자 페이지 기준 그렇게 많지 않을 것 같아 
통합 관리하여 기능 개발 외적인 고민을 줄여 개발 속도를 높이는 목적

service도 간단한 CRUD 작업정도면 다른 외부 종속성이 필요 없을 것 같아 controller가 바로 사용할 수 있게 함. 역시 불필요한 파일 생성 및 고민을 줄이기 위함.

단순한 구조 controller -> service -> repository를 유지함. 필요한 외부 종속성도 일단은 service에 인터페이스 종속성을 추가하여 주입받아 구현
controller의 요청/응답 dto를 service에서도 재활용하여 사용하여 중복 코드를 최소화, 필요 시 더 추가 가능

</details>


<details>
<summary> MVP 이후 기능 확장 시 </summary>

```bash
festival/
├── domain/
│   ├── artist/
│   │   ├── entity/
│   │   ├── repository/         # 여전히 JPA Repository, QueryDSL 구현체
│   │   └── service/            # 대부분 interface
│   │
│   ├── performance/
│   │   ├── entity/
│   │   ├── repository/
│   │   └── service/
│   │
│   └── shared/                  # 공통 VO, Enum 등
│       ├── vo/
│       └── constant/
│
├── application/                 # 유스케이스 계층
│   ├── artist/
│   │   └── CreateArtistUseCase.java
│   └── performance/
│       └── GetPerformanceListUseCase.java
│
├── infra/                       # 외부 구현, 설정
│   ├── fcm/                    # FCM 관련 로직
│   └── ext-library/            # 기타 외부 종속성
│
├── presentation/
│   ├── rest/                   # REST용 엔트리포인트
│   │   ├── artist/
│   │   │   ├── ArtistController.java
│   │   │   └── exchanges/
│   │   └── performance/
│   │       ├── PerformanceController.java
│   │       └── exchanges/
│   └── websocket/              # WebSocket 핸들러 (예정)
│       └── performance/
│           └── NotificationHandler.java
```

domain 디렉터리 내 세부 디렉터리로 나누고,

기능별로 usecase로 나누고, 여기에 infra에서 구현한 구현체들을 주입받아 사용, 일부 도메인 로직이나 repository도 주입받아 사용가능
의존성은 presentation -> usecase -> domain (로직, interface) 으로 도메인쪽으로 흐르게 됨.

presentation 레이어도 다양한 방법으로 접근할 수 있도록 하위 디렉터리 추가

될 수 있으면 각 레이어별로 dto를 추가하여 하위 레이어가 상위 레이어의 dto를 몰라도 되게 하기. 

이렇게 해서 domain 빌드 -> infra 빌드 -> application 빌드 -> presentation 빌드식의 증분 빌드 가능할 것이라 예상
</details>