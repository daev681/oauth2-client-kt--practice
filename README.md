# 🔐 OAuth2 Authorization Code Flow with Spring Security (포트 분리 환경)

## 📦 프로젝트 구성

| 앱 역할              | 포트  | 설명                                        |
|---------------------|-------|---------------------------------------------|
| Authorization Server| 9000  | 인증/인가 제공 (Spring Authorization Server) |
| Client Application  | 9001  | 사용자가 접근하는 클라이언트 앱 (Spring Boot) |

---

## 🔄 인증 플로우 (Authorization Code + 세션 기반 로그인 유지)

```plaintext
[사용자]
   |
   | ▶ http://localhost:9001 접속 (리소스 요청)
   v
[Client (9001)]
   |
   | ▶ 인증이 필요한 경우 → Spring Security가 로그인 요청 발생
   v
[Redirect → /oauth2/authorization/client-oidc]
   |
   | ▶ 등록된 redirectUri 및 client_id 기반 Authorization 요청
   v
[Authorization Server (9000)]
   |
   | ▶ 로그인 폼 표시 → 사용자 로그인
   | ▶ 세션 생성 (JSESSIONID) 및 SecurityContext 저장
   |
   | ▶ 로그인 성공 시 Authorization Code 발급
   v
[Redirect → http://localhost:9001/login/oauth2/code/client-oidc?code=xxx&state=yyy]
   |
   | ▶ client가 authorization code + client_secret으로 토큰 요청
   | ▶ access_token, id_token 수신
   |
   | ▶ 내부적으로 Spring Security가 사용자 정보를 가져와 Authentication 객체 생성
   | ▶ SecurityContextHolder에 저장하고, 세션에도 저장 (클라이언트 측 세션 로그인 완료)
   v
[사용자는 이제 인증된 상태로 9001에서 리소스 접근 가능]
