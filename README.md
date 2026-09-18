# back 

회원(member)과 게시글(post) 두 바운디드 컨텍스트로 구성된 Spring Boot 미션 프로젝트입니다.
두 컨텍스트는 서로의 테이블에 직접 접근하지 않고, 이벤트 기반 복제(replication)와 HTTP API 호출로만 통신합니다.

## 실행 방법

### 요구 사항
- JDK 25 (`build.gradle`의 `java.toolchain.languageVersion = JavaLanguageVersion.of(25)` 로 지정되어 있어, Gradle toolchain이 JDK 25를 자동으로 찾아 사용합니다)
- 별도의 DB 서버 설치 불필요 — 파일 기반 H2 DB를 사용합니다.

### DB 설정
`application-dev.yml` 기준 (`spring.profiles.active: dev` 가 기본값):

```yaml
spring:
  datasource:
    url: jdbc:h2:./db_dev;MODE=MySQL
    username: sa
    password:
    driver-class-name: org.h2.Driver
```

- 프로젝트 루트에 `db_dev.mv.db` 파일로 저장되며, 최초 실행 시 자동 생성됩니다.
- `spring.jpa.hibernate.ddl-auto: update` 이므로 재실행해도 기존 데이터/스키마가 유지됩니다(초기화되지 않음).
- `spring-boot-h2console` 의존성이 포함되어 있어 `/h2-console` 로 직접 조회도 가능합니다.

### 실행 명령

```bash
./gradlew bootRun
```

- 서버 포트: `8080`
- 최초 실행 시 `MemberDataInit` → `PostDataInit`(`@Order(1)`, `@Order(2)` `ApplicationRunner`) 이 순서대로 동작하며 샘플 회원 6명, 게시글 6개, 댓글 8개를 자동으로 시딩합니다.
- 이미 데이터가 있으면(각각 `count() > 0`) 시딩을 건너뛰므로, 두 번째 실행부터는 아무 것도 추가되지 않습니다.

## 구조 설명

### 모듈 구성

```
src/main/java/com/back
├── BackApplication.java
├── bounded_context
│   ├── member                     # 회원 컨텍스트 — 원본(Source) 데이터를 소유
│   │   ├── app                    # MemberFacade, MemberJoinUseCase (유스케이스)
│   │   ├── domain                 # Member(SourceMember), MemberPolicy
│   │   ├── in                     # MemberController(HTTP), MemberEventListener(post 이벤트 구독), MemberDataInit(시딩)
│   │   └── out                    # MemberRepository
│   └── post                       # 게시글 컨텍스트 — member의 복제본(Replica)을 로컬로 보유
│       ├── app                    # PostFacade, PostWriteUseCase
│       ├── domain                 # Post, PostComment, PostMember(ReplicaMember)
│       ├── in                     # PostEventListener(member 이벤트 구독), PostDataInit(시딩)
│       └── out                    # PostRepository, PostMemberRepository
├── global                         # 공통 인프라 — EventPublisher, RsData, BaseEntity/BaseIdAndTime, DomainException, GlobalConfig
└── shared                         # 컨텍스트 간 공유 계약
    ├── member
    │   ├── domain                 # BaseMember, SourceMember, ReplicaMember (원본/복제 상위 클래스)
    │   ├── dto                    # MemberDto
    │   ├── event                  # MemberJoinedEvent, MemberModifiedEvent
    │   └── out                    # MemberApiClient (member 컨텍스트로의 실제 HTTP 클라이언트)
    └── post
        ├── dto                    # PostDto, PostCommentDto
        └── event                  # PostCreatedEvent, PostCommentCreatedEvent
```

각 바운디드 컨텍스트는 내부적으로 `app`(유스케이스) / `domain` / `in`(인바운드 어댑터: 컨트롤러·이벤트 리스너·데이터 시딩) / `out`(아웃바운드 어댑터: 레포지토리)으로 계층화되어 있습니다.

### 이벤트와 HTTP API를 구분한 이유

컨텍스트 간 통신은 두 가지 방식으로 나뉩니다.

1. **Spring 애플리케이션 이벤트** — 호출자가 리턴값을 필요로 하지 않는 "부수 효과"에 사용
   - `MemberJoinedEvent` / `MemberModifiedEvent` (member → post): 회원 가입·정보 변경을 post 컨텍스트의 복제본에 반영
   - `PostCreatedEvent` / `PostCommentCreatedEvent` (post → member): 글/댓글 작성 시 작성자의 활동 점수 반영
   - 모든 리스너는 `@TransactionalEventListener(phase = AFTER_COMMIT)` + `@Transactional(propagation = REQUIRES_NEW)` 로 동작합니다. 원본 트랜잭션이 커밋된 뒤 별도 트랜잭션에서 처리되므로, 복제/집계 로직이 실패하더라도 원본 저장 로직에는 영향을 주지 않는 최종적 일관성(eventual consistency) 구조입니다.
2. **동기 HTTP 호출** — 호출자가 그 자리에서 리턴값을 받아 응답에 포함해야 하는 경우에 사용
   - `PostWriteUseCase.write()` 가 글 저장 직후 `MemberApiClient`(`RestClient`)로 `GET /api/v1/members/randomSecureTip` 을 실제 HTTP로 호출해 보안팁 문자열을 받아옵니다.
   - 이벤트 리스너는 호출자가 반환값을 받을 수 없는 구조이므로, 즉시 응답에 포함해야 하는 값은 이벤트가 아니라 HTTP API 호출로 가져옵니다.

### 회원 복제 흐름

1. 회원 가입 시 `MemberJoinUseCase.join()`이 `Member`(SourceMember, PK auto-increment)를 저장하고 `MemberJoinedEvent(MemberDto)`를 발행합니다.
2. 트랜잭션 커밋 후 `PostEventListener.handle(MemberJoinedEvent)`가 `PostFacade.syncMember()`를 호출해 `PostMember`(ReplicaMember)를 저장합니다. `ReplicaMember`는 `@GeneratedValue` 없이 원본과 **동일한 id**를 그대로 대입받습니다.
3. `ReplicaMember.id`는 auto-increment가 아니므로, `save()` 호출 시 JPA가 내부적으로 `merge()`를 사용합니다 — 행이 이미 존재하면 SELECT 후 UPDATE, 없으면 INSERT를 수행합니다. 그 덕분에 같은 id로 몇 번을 다시 저장해도 행이 중복 생성되지 않습니다.
4. 글 작성(`PostCreatedEvent`, +3점) / 댓글 작성(`PostCommentCreatedEvent`, +1점) 시 `MemberEventListener`가 원본 `Member.increaseActivityScore()`를 호출합니다.
5. `increaseActivityScore()`는 내부에서 다시 `MemberModifiedEvent`를 발행하고, `PostEventListener`가 `syncMember()`를 재호출해 post 컨텍스트의 복제본 활동 점수도 함께 갱신합니다.

결과적으로 post 컨텍스트는 member 컨텍스트의 테이블에 전혀 접근하지 않고, 자신의 로컬 복제 테이블(`POST_MEMBER`)만 조회하면서도 항상 최신 회원 정보를 유지합니다.

## 확인 결과

아래는 `db_dev.mv.db`를 초기화한 뒤 `./gradlew bootRun`으로 최초 1회 실행, 이어서 동일 DB로 재실행한 2회분의 실제 로그/SQL/응답입니다. (JDK 25.0.4 Temurin, H2 2.4.240으로 DB 파일을 직접 조회)

### 1) 초기 데이터 개수

```sql
SELECT
  (SELECT COUNT(*) FROM MEMBER_MEMBER) AS MEMBER_COUNT,
  (SELECT COUNT(*) FROM POST_POST) AS POST_COUNT,
  (SELECT COUNT(*) FROM POST_POST_COMMENT) AS COMMENT_COUNT,
  (SELECT COUNT(*) FROM POST_MEMBER) AS POST_MEMBER_COUNT;
```
```
MEMBER_COUNT | POST_COUNT | COMMENT_COUNT | POST_MEMBER_COUNT
6            | 6          | 8             | 6
```

회원 6명(system, holding, admin, user1, user2, user3), 글 6개, 댓글 8개, post 컨텍스트에 복제된 회원 6명이 시딩됩니다.

### 2) 회원별 글 수·댓글 수에 따른 활동 점수

```sql
SELECT
  m.ID, m.USERNAME, m.NICKNAME,
  (SELECT COUNT(*) FROM POST_POST p WHERE p.AUTHOR_ID = m.ID) AS POST_COUNT,
  (SELECT COUNT(*) FROM POST_POST_COMMENT c WHERE c.AUTHOR_ID = m.ID) AS COMMENT_COUNT,
  (SELECT COUNT(*) FROM POST_POST p WHERE p.AUTHOR_ID = m.ID) * 3
    + (SELECT COUNT(*) FROM POST_POST_COMMENT c WHERE c.AUTHOR_ID = m.ID) * 1 AS EXPECTED_SCORE,
  m.ACTIVITY_SCORE AS ACTUAL_SCORE
FROM MEMBER_MEMBER m
ORDER BY m.ID;
```
```
ID | USERNAME | NICKNAME | POST_COUNT | COMMENT_COUNT | EXPECTED_SCORE | ACTUAL_SCORE
1  | system   | 시스템    | 0          | 0             | 0              | 0
2  | holding  | 홀딩      | 0          | 0             | 0              | 0
3  | admin    | 관리자    | 0          | 0             | 0              | 0
4  | user1    | 유저1     | 3          | 2             | 11             | 11
5  | user2    | 유저2     | 2          | 3             | 9              | 9
6  | user3    | 유저3     | 1          | 3             | 6              | 6
```

활동 점수 = 글 수 × 3 + 댓글 수 × 1. `MemberEventListener`가 이벤트로 누적 계산한 `ACTUAL_SCORE`와 원천 데이터(`POST_POST`, `POST_POST_COMMENT`)에서 직접 집계한 `EXPECTED_SCORE`가 모두 일치합니다.

### 3) 원본(member_member)과 복제본(post_member) 일치 여부

```sql
SELECT
  m.ID, m.USERNAME,
  m.ACTIVITY_SCORE AS SOURCE_SCORE, pm.ACTIVITY_SCORE AS REPLICA_SCORE,
  CASE WHEN m.USERNAME = pm.USERNAME AND m.NICKNAME = pm.NICKNAME
            AND m.ACTIVITY_SCORE = pm.ACTIVITY_SCORE
       THEN 'MATCH' ELSE 'MISMATCH' END AS RESULT
FROM MEMBER_MEMBER m JOIN POST_MEMBER pm ON m.ID = pm.ID
ORDER BY m.ID;
```
```
ID | USERNAME | SOURCE_SCORE | REPLICA_SCORE | RESULT
1  | system   | 0            | 0             | MATCH
2  | holding  | 0            | 0             | MATCH
3  | admin    | 0            | 0             | MATCH
4  | user1    | 11           | 11            | MATCH
5  | user2    | 9            | 9             | MATCH
6  | user3    | 6            | 6             | MATCH
```

6명 전원 `MATCH` — 이벤트 기반 복제가 정상 동작함을 확인했습니다. 애플리케이션 로그에서도 과정을 그대로 볼 수 있습니다.

회원 가입 시 `post_member`에 INSERT (system 회원, id=1):
```
[Hibernate]
    /* insert for com.back.bounded_context.post.domain.PostMember */insert
    into
        post_member (activity_score, create_date, modify_date, nickname, password, username, id)
    values
        (?, ?, ?, ?, ?, ?, ?)
-- binding: (1:INTEGER)<-[0]  (4:VARCHAR)<-[시스템]  (6:VARCHAR)<-[system]  (7:INTEGER)<-[1]
```

글 작성으로 활동 점수가 바뀌면 `post_member`는 INSERT가 아니라 **SELECT 후 UPDATE**로 갱신됩니다 (user1, id=4):
```
[Hibernate]
    select ... from post_member pm1_0 where pm1_0.id=?    -- merge: 기존 행 존재 여부 확인
-- binding: (1:INTEGER)<-[4] / extracted activity_score=[0], username=[user1] ...

[Hibernate]
    /* update for com.back.bounded_context.post.domain.PostMember */update post_member
    set
        activity_score=?, create_date=?, modify_date=?, nickname=?, password=?, username=?
    where
        id=?
```

### 4) 재실행 시 중복 없음

동일한 `db_dev.mv.db` 파일을 유지한 채 애플리케이션을 재기동했습니다.

- 재기동 로그의 `insert` 문 발생 건수: **0**
- `PostDataInit`이 남기는 `"N번 글이 생성되었습니다"` 디버그 로그 발생 건수: **0**
  → `MemberDataInit.makeBaseMembers()` / `PostDataInit.makeBasePosts()` 가 각각 `count() > 0` 이면 즉시 return 하도록 가드되어 있어, 재실행해도 시드 데이터가 다시 들어가지 않습니다.
- 재기동 후 동일한 카운트 쿼리 재실행 결과, 최초 실행과 완전히 동일:
```
MEMBER_COUNT | POST_COUNT | COMMENT_COUNT | POST_MEMBER_COUNT
6            | 6          | 8             | 6
```

### 5) 보안팁 API 호출 결과

`PostWriteUseCase.write()`는 글을 저장한 직후 `MemberApiClient`를 통해 실제로 자기 자신의 `GET /api/v1/members/randomSecureTip`을 HTTP로 호출합니다. 기동 로그에서 그 결과가 응답 메시지에 그대로 포함된 것을 확인할 수 있습니다.

```
DEBUG c.b.b.post.in.PostDataInit : 1번 글이 생성되었습니다. 보안팁: 비밀번호의 유효기간은 90일 입니다.
DEBUG c.b.b.post.in.PostDataInit : 2번 글이 생성되었습니다. 보안팁: 비밀번호의 유효기간은 90일 입니다.
...
DEBUG c.b.b.post.in.PostDataInit : 6번 글이 생성되었습니다. 보안팁: 비밀번호의 유효기간은 90일 입니다.
```

같은 엔드포인트를 직접 호출한 요청/응답:

요청
```
GET /api/v1/members/randomSecureTip HTTP/1.1
Host: localhost:8080
```

응답
```
HTTP/1.1 200
Content-Type: text/plain;charset=UTF-8
Content-Length: 48

비밀번호의 유효기간은 90일 입니다.
```

`MemberPolicy.PASSWORD_CHANGE_DAYS(=90)` 를 기반으로 만들어지는 문구이며, 현재는 후보 팁이 1종류뿐이라 호출할 때마다 항상 동일한 문자열을 반환합니다.
