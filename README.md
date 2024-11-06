# Planner Project

&nbsp;   
&nbsp;    

## Project Overview
> - 기여도 : 100%
> - 담당 기능 : 회원, 관리자

&nbsp;   
&nbsp;    

## Project Tech Stack
> - **Framework** : Spring Boot (3.3.0)
> - **IDE** : STS 4 (Eclipse)
> - **View** : Thymeleaf
> - **DB** : OracleDB
> - **SQLMapper** : MyBatis
> - **Language** : Java(JDK 21)
> - **Build Tool** : Gradle

&nbsp;   
&nbsp;    

## Project Architecture
```text
 src
  ├─config
  │      AppConfig.java : 애플리케이션 전반에서 필요로 하는 빈(Bean)을 설정하고 관리하기 위한 구성 파일
  │      EmailConfig.java : 애플리케이션에서 이메일 발송을 위한 STMP 설정 파일
  │      MvcConfig.java : 실제 이미지 파일이 저장된 폴더를 애플리케이션에서 접근할 수 있도록 설정한 파일
  │      SecurityConfig.java : Spring Security 설정 파일
  │      StatusConfig.java : FriendRoleUtils를 애플리케이션 전역에서 사용 가능한 빈(Bean)으로 설정하고 관리하기 위한 구성 파일
  │      WebConfig.java : 커스텀 어노테이션 사용을 위한 설정 파일
  │
  ├─controller
  │      FriendController.java : 친구 관련 요청 처리 컨트롤러
  │      MapLikeController.java : 장소 즐겨찾기 관련 요청 처리 컨트롤러
  │      MemberController.java : 회원 관련 요청 처리 컨트롤러
  │      NoticeCotroller.java : 공지사항 관련 요청 처리 컨트롤러
  │      OAuth2Controller.java : 소셜로그인 관련 요청 처리 컨트롤러
  │      PlannerController.java : 플래너 메인페이지 관련 요청 처리 컨트롤러
  │      ReplyController.java : 댓글 관련 요청 처리 컨트롤러
  │      ScheduleController.java : 일정 관련 요청 처리 컨트롤러
  │      TeamBoardController.java : 그룹 게시판 관련 요청 처리 컨트롤러
  │      TeamController.java : 그룹 관련 요청 처리 컨트롤러
  │      TeamMemberController.java : 그룹 회원 관련 요청 처리 컨트롤러
  │      TeamScheduleController.java : 그룹 일정 관련 요청 처리 컨트롤러 
  │      VoteController.java : 투표 관련 요청 처리 컨트롤러
  │
  ├─dto
  │  ├─request
  │  │  ├─admin
  │  │  │      NoticeDTO.java : 공지사항 DTO
  │  │  │      ReqNoticeImg.java : 공지사항 이미지 요청 DTO
  │  │  │
  │  │  ├─friend
  │  │  │      FriendDTO.java : 친구 관련 DTO
  │  │  │      FriendRequestDTO.java 친구신청 DTO
  │  │  │
  │  │  ├─member
  │  │  │      MemberDTO.java : 회원 DTO
  │  │  │      ReqChangePassword.java : 비밀번호 변경 요청 DTO
  │  │  │      ReqMemberRestore.java : 회원 복구 요청 DTO
  │  │  │      ReqMemberUpdate.java : 회원 수정 요청 DTO
  │  │  │      ReqOAuth2MemberAdd.java : 소셜로그인 회원가입 요청 DTO
  │  │  │      ReqOAuth2Signup.java : 소셜로그인 추가 회원가입 요청 DTO
  │  │  │
  │  │  ├─schedule
  │  │  │      CalendarDTO.java : 달력 DTO
  │  │  │      CalendarPrintDTO.java : 달력 안 일정 DTO
  │  │  │      MapDTO.java : 장소 DTO
  │  │  │      MapLikeDTO.java : 즐겨찾기 장소 DTO
  │  │  │      ScheduleDTO.java : 일정 DTO
  │  │  │      ScheduleSearchDTO.java : 일정 검색 DTO
  │  │  │      TodayInfo.java : 오늘의 일정 DTO
  │  │  │
  │  │  └─team
  │  │      │  MyTeamListDTO.java : 나의 그룹 목록 DTO
  │  │      │  TeamDTO.java : 그룹 DTO
  │  │      │  TeamInfoDTO.java : 그룹 정보 DTO
  │  │      │  TeamMemberDTO.java : 그룹멤버 DTO
  │  │      │  TeamMyInfoDTO.java : 그룹 내 정보 DTO
  │  │      │
  │  │      ├─board
  │  │      │      ReplyDTO.java : 댓글 DTO
  │  │      │      ReplyViewDTO.java : 댓글 상세 DTO
  │  │      │      TeamBoardDTO.java : 그룹 게시판 DTO
  │  │      │      TeamBoardListDTO.java : 그룹 게시판 목록 DTO
  │  │      │      TeamBoardUpdateDTO.java : 그룹 게시판 수정 DTO
  │  │      │
  │  │      └─vote
  │  │              VoteDTO.java : 투표 DTO
  │  │              VoteInfoDTO.java : 투표 정보 DTO
  │  │              VoteItemDTO.java : 투표 항목 DTO
  │  │              VoteMemberDTO.java : 투표 인원 DTO
  │  │              VoteMemberInsertDTO.java : 투표 한 인원 DTO
  │  │
  │  └─response
  │      └─member
  │              ResEmailAuthDetail.java : 이메일 인증 코드 응답 DTO
  │              ResMemberDetail.java : 회원 상세 정보 응답 DTO
  │
  ├─enums
  │      CodeStatus.java : 회원 인증 상태를 나타내는 enum
  │      ErrorType.java : 동기/비동기 예외를 나타내는 enum
  │      FriendRole.java : 친구 상태를 나타내는 enum
  │      Gender.java : 회원의 성별을 나타내는 enum
  │      Masking.java : 나타내면 안되는 정보를 *로 나타내는 enum
  │      MemberRole.java : 회원 권한을 나타내는 enum
  │      MemberStatus.java : 회원 상태를 나타내는 enum
  │      OAuthType.java : 소셜 로그인의 타입을 나타내는 enum
  │      TM_Grade.java : 그룹내 등급을 나타내는 enum
  │
  ├─exception
  │      CustomException.java : 커스텀 예외 클래스(동기)
  │      ErrorCode.java : 내가 원하는 상태코드와 에러메세지를 나타낸 enum
  │      ErrorResponse.java : 발생한 커스텀 에러와 응답 데이터 포맷을 설정해주는 파일
  │      GlobalExceptionHandler.java : 전역에서 발생한 커스텀 예외 및 유효성 검사 예외 등 모든 예외를 핸들링하는 클래스(동기)
  │      RestCustomException.java : 커스텀 예외 클래스(동기)
  │      RestGlobalExceptionHandler.java : 전역에서 발생한 커스텀 예외 및 유효성 검사 예외 등 모든 예외를 핸들링하는 클래스(비동기)
  │
  ├─handler
  │      CustomAccessDeniedHandler.java : 스프링시큐리티에서 인가 실패 시 발동하는 핸들러
  │
  ├─mapper
  │      EmailMapper.java : 이메일 관련 데이터베이스 접근 담당
  │      FriendMapper.java : 친구 관련 데이터베이스 접근 담당
  │      MapLikeMapper.java : 장소 즐겨찾기 관련 데이터베이스 접근 담당
  │      MapRepository.java : 장소 관련 데이터베이스 접근 담당
  │      MemberMapper.java 회원 관련 데이터베이스 접근 담당
  │      NoticeMapper.java : 공지사항 관련 데이터베이스 접근 담당
  │      ReplyMapper.java : 댓글 관련 데이터베이스 접근 담당
  │      ScheduleMapper.java : 일정 관련 데이터베이스 접근 담당
  │      TeamBoardMapper.java : 그룹 게시판 관련 데이터베이스 접근 담당
  │      TeamMapper.java : 그룹 관련 데이터베이스 접근 담당
  │      TeamMemberMapper.java : 그룹 회원 관련 데이터베이스 접근 담당
  │      VoteMapper.java : 투표 관련 데이터베이스 접근 담당
  │
  ├─oauth
  │  │  TokenRedirect.java : 로그인의 결과에 따른 URL을 나타내는 enum
  │  │
  │  ├─exception
  │  │      OAuth2AuthenticationProcessingException.java : 인증 관련 예외
  │  │
  │  ├─handler
  │  │      OAuth2AuthenticationFailureHandler.java : 소셜로그인 인증 실패 핸들러
  │  │      OAuth2AuthenticationSuccessHandler.java : 소셜 로그인 인증 성공 핸들러
  │  │
  │  ├─service
  │  │      CustomOAuth2UserService.java : 시큐리티 소셜로그인 관련 비즈니스 로직
  │  │      CustomUserDetailService.java : 시큐리티 일반로그인 관련 비즈니스 로직  
  │  │      OAuth2Service.java : 소셜로그인 관련 비즈니스 로직
  │  │      OAuth2UserPrincipal.java : OAuth2User와 UserDetails 인터페이스를 구현하여 사용자 정보를 담은 클래스
  │  │
  │  └─user
  │          CustomUserDetail.java : 로그인 후 유저 정보를 시큐리티에 저장하기 위한 커스텀 클래스
  │          GoogleOAuth2UserInfo.java : 구글 소셜로그인 회원 정보 
  │          KakaoOAuth2UserInfo.java : 카카오 소셜로그인 회원 정보
  │          OAuth2Provider.java : 구글인지 카카오인지 들어오는 registerID에 따라 나타내는 enum
  │          OAuth2UserInfo.java : 구글과 카카오 등 다른 소셜로그인 회원 정보를 일정하게 통합하기 위한 공통 최상위 인터페이스
  │          OAuth2UserInfoFactory.java : 어떤 소셜로그인이냐의 따라서 유저정보를 반환하는 클래스
  │
  ├─service
  │      EmailService.java : 이메일 관련 비즈니스 로직 처리
  │      FriendService.java : 친구 관련 비즈니스 로직 처리
  │      MapLikeService.java : 장소 즐겨찾기 관련 비즈니스 로직 처리
  │      MapService.java : 장소 관련 비즈니스 로직 처리
  │      MemberService.java : 회원 관련 비즈니스 로직 처리
  │      NoticeService.java : 공지 관련 비즈니스 로직 처리
  │      ReplyService.java : 댓글 관련 비즈니스 로직 처리
  │      ScheduleService.java : 일정 관련 비즈니스 로직 처리
  │      TeamBoardService.java : 그룹 게시판 관련 비즈니스 로직 처리
  │      TeamMemberService.java : 그룹 회원 관련 비즈니스 로직 처리
  │      TeamService.java : 그룹 관련 비즈니스 로직 처리
  │      VoteService.java : 투표 관련 비즈니스 로직 처리
  │
  └─util
          CommonUtils.java : 전역에서 사용하는 공통된 메서드를 모아놓은 유틸 클래스
          FriendRoleUtils.java : 역할, 마스킹, 성별 등의 정보를 변환해주는 유틸리티 클래스
          Scheduler.java : 스케쥴링 기능을 정의한 유틸 클래스
          UserData.java : 커스텀 어노테이션 클래스
          UserDataResolver.java : UserData 어노테이션을 사용했을 때 사용자 인증 정보를 전달하는 역할
```

&nbsp;   
&nbsp;    

## 아쉬운 점
> 이번 팀 프로젝트에서는 Git을 도입해 협업을 시도했으나, 팀원 간 학습 수준의 차이로 인해 충분히 활용하지 못하고 중간에 포기하게 되어 아쉬움이 남습니다.     
> Git을 통해 체계적인 협업과 코드 관리를 경험하지 못한 점은 이번 프로젝트에서 더욱 아쉽게 느껴졌습니다.     
> 또한, Spring Security와 소셜 로그인 로직을 충분히 숙지하지 못한 상태에서 구현을 시도하다 보니, 테스트나 커스터마이징 과정에서 많은 시간을 소모했고,    
> 그로 인해 여러 어려움이 발생했습니다. 사전에 더 깊이 공부했다면 문제 해결이 훨씬 더 수월했을 것이고,    
> 이를 통해 프로젝트 완성도도 높일 수 있었을 것이라는 아쉬움이 큽니다.    
> 비즈니스 로직이 복잡해지면서 리팩토링을 진행했지만, 디자인 패턴이나 클린 코드 원칙에 대해 더 깊이 이해하고 있었다면,     
> 리팩토링 과정에서 더 효율적이고 유지보수성 높은 코드를 작성할 수 있었을 것입니다.      
> 또한, 이메일 인증 시스템에서는 인증 코드를 RDB에 저장하는 방식을 사용했는데, 유효시간이 지난 데이터를 관리하는 데 불편함을 느꼈습니다.     
> 추후에는 Redis 같은 인메모리 데이터베이스를 활용해 데이터 유효시간을 설정하고, 보다 효율적으로 관리할 계획입니다.      
> 특히, 팀 내에서 관리자 파트를 맡았던 팀원이 중도에 포기하면서, 갑작스럽게 제가 그 역할을 맡게 되었습니다.      
> 시간적 여유가 부족해 관리자 기능을 충분히 구현하지 못해 아쉬움이 컸습니다.     
> 더불어 일정 관리 파트에서 사용된 카카오맵 기능도 수정해야 했는데, 카카오맵을 처음 다뤄보는 상황이라 충분히 기능을 활용하지 못한 점이 아쉬웠습니다.      
> 이후에 더 깊이 공부하여 이러한 기술들을 완벽히 익히고, 향후 프로젝트에서 적용할 계획입니다.     
