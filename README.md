## Kateboo

### 서비스 소개

kateboo는 카카오테크 부트캠프 인원들이 편하게 자신이 공부한 내용들을 공유하고,
학습하고 의견을 주고받을 수 있도록 하기위해서 만든 커뮤니티 게시판입니다.



https://github.com/user-attachments/assets/ff1c53dc-95cc-4e11-8412-ddb9417c50b0



---

### ERD 설계

<img width="1900" height="1212" alt="Community" src="https://github.com/user-attachments/assets/4f70ca46-c75f-4c39-94f1-61f72cdd1e3e" />


ERD는 다음 사진과 같이 구성되어 있습니다.

같은 Aggregate인 Board,Like,Comment를 하나로 묶어 JPA refactoring 시 양방향 매핑을 통해 강하게 결합할 수 있도록 했습니다.

또한 Image테이블을 각각 도메인에서 활용하기 위해 UserImage, BoardImage 매핑 테이블을 만들어서 활용을 용이하게 할 수 있도록 했습니다.

마지막으로 index로 자주 활용될만한 컬럼을 뽑아 index로 설정한 뒤 ERD 설계를 마쳤습니다.

---

### 주요 구현 내용

1. **도메인 이벤트 도입을 통해 SRP 준수하도록 구현**

    ```java
        @Transactional
        public void deleteBoard(Long userId, Long boardId) {
            Board board = boardService.findById(boardId);
            validateUser(board, userId);
            boardService.deleteBoard(boardId);
            commentService.deleteByBoardId(boardId);
            likeService.deleteByBoardId(boardId);
        }
    ```

   위 코드는 BoardCommandFacade 클래스 파일에서 게시글을 삭제하는 로직입니다.

   Board 삭제 시 관련된 댓글, 좋아요를 삭제하는것이 필요했지만 해당 내용을 BoardCommandFacade에서 진행하게 될 경우 세 개의 도메인에 대해 삭제 로직을 진행해야해, 단일 책임 원칙을 위반하는 문제가 발생했습니다.

   위 내용을 해결하기 위해 도메인 이벤트를 도입해 아래와 같이 게시글 삭제 시 삭제로직만을 실행하고

   boardService 내부에서 board삭제 실제로 진행 후, BoardDeletedEvent를 발행하도록 해 단일 책임 원칙을 준수하되, 관련된 좋아요와 댓글도 삭제할 수 있도록 했습니다.

    ```java
        //BoardCommandFacade
        @Transactional
        public void deleteBoard(Long userId, Long boardId) {
            Board board = boardService.findById(boardId);
            validateUser(board, userId);
            boardService.deleteBoard(boardId);
        }
    ```

    ```java
        //BoardService
        public void deleteBoard(Long boardId) {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_BOARD));
            boardRepository.deleteById(boardId);
            eventPublisher.publishEvent(new BoardDeletedEvent(boardId));
        }
    ```

    ```java
    @Component
    public class CommentBoardEventHandler {
        private final CommentService commentService;
    
        CommentBoardEventHandler(CommentService commentService) {
            this.commentService = commentService;
        }
    
        @EventListener
        public void handle(BoardDeletedEvent event) {
            commentService.deleteByBoardId(event.boardId());
        }
    }
    ```

---
2. **JPA N+1 문제에 대해 IN 쿼리 활용해 쿼리 횟수 최적화**

   게시글을 조회할 경우 응답으로 게시글 작성자 정보, 댓글 정보, 좋아요 정보를 함께 반환해야합니다.

   이 과정에서 게시글을 하나 조회할때마다 관련해서 쿼리가 4개씩 더 발생해 쿼리가 과도하게 많아지는 N+1문제가 발생했습니다.

   이를 해결하기 위해 관련 게시글을 전부 조회한 뒤, 아래 코드와 같이 Id를 List로 뽑았습니다.

    ```java
    Page<Board> boards = boardService.findPage(title, content, page,size);
    
    List<Long> boardIds = boards.stream().map(Board::getId).toList();
    ```

   이를 바탕으로 아래와 같이 해당 id리스트에서 관련 도메인 내용을 조회할 수 있도록 해 각 도메인마다 IN 쿼리 한번으로 해결할 수 있도록 만들어 쿼리가 과도하게 생성되는것을 막았습니다.

    ```java
    Map<Long, List<Like>> likeMap = likeService.findAllByPagedBoardIds(boardIds)
            .stream()
            .collect(Collectors.groupingBy(Like::getBoardId));
    
    Map<Long, List<Comment>> commentMap = commentService.findAllByPagedBoardIds(boardIds)
            .stream()
            .collect(Collectors.groupingBy(Comment::getBoardId));
    
    List<BoardInfoResponse> responses = boards.stream()
            .map(board -> BoardInfoResponse.of(
                    board,
                    likeMap.getOrDefault(board.getId(), List.of()).size(),
                    board.getVisitors(),
                    commentMap.getOrDefault(board.getId(), List.of()).size(),
                    board.getUser(),
                    board.getUser().getUserImage().getImage()
            )).toList();
    ```

   IN 쿼리의 경우 IN 쿼리를 통해 확인해야하는 항목이 많아지면 안티패턴입니다만, 현재는 Pagination을 통해 한번에 조회하는 객체의 개수 자체가 일정하게 정해져있어 page size가 과도하게 커지지않을 경우 안티패턴을 통해 성능이 다시 안좋아지는것을 막을 수 있습니다.

---
3. **JwtCustomFilter 구현을 통해 인증/인가 로직 구현**

   초기 인증/인가 구현 방향은 Spring Security를 사용하지 않는 것이었습니다.

   따라서 관련된 로직을 직접 구현해야하는 상황이었고, 인증/인가 로직은 비즈니스 로직과 연관되지않도록 DispatcherServlet으로 요청이 들어가기전에 Filter를 구현해서 먼저 걸러주는것이 맞다고 생각했습니다.

   이에 따라 JWT를 활용한 Stateless 인증/인가를 구현하기 위해 JwtCustomFilter를 구현하게 되었습니다.

   추가적으로 tokenBlackList를 ConcurrentMap을 통해 구현 해 로그아웃 시 서버에서 토큰에 대한 인증 여부를 통제할 수 있도록 구현한 뒤, ExcludePathMatcher를 구현 해 제외할 로직을 명시했습니다.

---
4. **Custom Filter Chain 구현**

   JwtCustomFilter 구현 후, API들을 fetch 하는 과정에서 아래 사진과 같이 인증 문제가 발생했습니다.

   <img width="729" height="47" alt="스크린샷 2025-12-08 오후 11 00 28" src="https://github.com/user-attachments/assets/c6e1fcea-5035-45bc-8b33-f1a1e8de6910" />


   클라이언트는 실제 API요청을 보내기전에 해당 리소스가 실제로 있는지 확인하기 위해 아래 사진과 같이 Preflight요청을 보내게 됩니다.

   <img width="763" height="518" alt="스크린샷 2025-12-09 오전 12 50 23" src="https://github.com/user-attachments/assets/1e8e02fc-4038-4ca0-9447-491cd9e858f8" />

    
   해당 내용에 토큰이 첨부되어있을리가 없으니 요청이 DispatcherServlet을 통해 Controller로 들어가기 전 JwtCustomFilter에 의해 걸러진 것 입니다.

   처음에는 사전 요청이니까 통과시켜도 괜찮지않을까? 라는 생각에 ExcludePathMatcher에 OPTIONS 요청을 허용하도록 설정해두었지만, 헤더가 설정되지않아 preflight 요청이 정상적으로 처리되지않았고 에러는 여전히 발생했습니다.

   이 과정에서 최상단에 CorsFilter를 설정해서 preflight를 처리해주면 되겠다는 생각이 들었고, 관련 필터를 구현 해 OPTIONS 요청에 대해 헤더를 설정할 수 있도록 처리한 뒤, Order(0)를 통해 최상단에 CorsFilter가 존재하고 처리가 완료된 요청을 JwtFilter로 넘길 수 있도록 Custom Filter Chain을 구현했습니다.

---
5. **loadUserByUserName 캐싱**

   이전에 Custom으로 구현했던 인증/인가 로직을 Spring security로 바꾸기로 결정해 관련 로직을 구현하던 중 의문이 들었습니다.

   JWT 활용한 Stateless를 구현하고 있었기때문에 Security Context가 매 요청마다 초기화되었고, 이로 인해 인증된 사용자가 맞는지 확인하기 위해 UserDetailsService에서 유저 조회 쿼리가 하나씩 발생했습니다.

   매 요청마다 유저 확인을 위해 쿼리를 하나씩 보내는것이 비효율적이라 생각했고,

   아래 코드와 같이 loadUserByUserName 메소드를 @Override하는 과정에서 userId를 key로 하는 캐싱을 구현 해 쿼리를 줄일 수 있도록 구현했습니다.

    ```java
        @Override
        @Cacheable(value = "userDetailsCache", key = "#userId")
        public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
            User user = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));
    
            return new CustomUserDetails(user);
        }
    ```

---
6. **Jacoco를 활용한 테스트 커버리지 측정**

   작성했던 비즈니스 로직을 비롯해 Controller를 통해 들어오는 요청에 대해 unit test 코드를 작성해 내부 로직의 동작여부를 확인했습니다.

   branch와 메소드의 coverageVerification 통과 기준을 80%로 설정해둔 뒤에, 작성한 로직들에 대해 테스트 커버리지를 아래 사진과 같이 측정하였습니다.

   <img width="1103" height="326" alt="스크린샷 2025-12-08 오전 8 14 52" src="https://github.com/user-attachments/assets/3c557be1-3b3c-4e1c-b339-8d3918547fa7" />


   기존 Mockito 방식에 비해 BDDMockito 방식이 give-when-then을 확인하기에 용이하다 생각해 BDDMockito를 활용해 테스트 코드를 작성했습니다.

   @DataJpaTest를 통해 Repository의 쿼리가 실제로 잘 동작하는지 확인했으며, Service와 Facade에 대해 branch를 나누어 내부 로직이 잘 동작하는지 확인했습니다.

   Controller의 경우 inside server test의 MockMVC in Standalone Mode방법을 활용해 내부로직이 잘 동작하는지 확인했으며

   <img width="715" height="410" alt="스크린샷 2025-12-08 오후 2 49 18" src="https://github.com/user-attachments/assets/059a8b24-996c-4781-8295-9be4ffa3059b" />


   추후에 배포 이전에 통합테스트와 E2E테스트를 통해 outside server test를 구현해 외부에서 들어오는 요청에 대한 검증 여부를 테스트할 예정입니다.

   <img width="688" height="546" alt="스크린샷 2025-12-08 오후 2 53 10" src="https://github.com/user-attachments/assets/3800d961-6ac7-46ca-b16a-954530789c96" />


   @DataJpaTest의 경우 JPA관련 전체 repository bean들을 불러와서 repository 계층에 대한 검증을 진행하고,  저는 QueryDsl을 활용해서 로직을 작성해둔 Repository들이 있기에 아래와 같이 QueryDslTestConfig를 설정해 EntityManager를 주입받은 jpaQueryFactory를 생성할 수 있도록 해 오류가 발생하지않도록 한 뒤, 각 Repository 테스트 코드에 설정해주었습니다.

    ```java
    @TestConfiguration
    public class QueryDslTestConfig {
        @PersistenceContext
        private EntityManager em;
    
        @Bean
        public JPAQueryFactory jpaQueryFactory() {
            return new JPAQueryFactory(em);
        }
    }
    ```

   또한 proxy의 경우 내부 객체 정보는 비어있더라도, 객체의 Id는 가지고있기때문에 fetch join 검증시에 정말 객체 정보가 잘 불러와졌는지 확인하기 위해 아래와같이 proxy인지 실제 확인하는 코드를 repository assertThat코드에 삽입했습니다.

    ```java
    assertThat(found.getUser().getClass().getName())
    		.doesNotContain("HibernateProxy");
    ```
