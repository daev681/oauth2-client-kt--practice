
# OAuth2 클라이언트 및 인증 서버 구현 (세션 기반) 

이 프로젝트는 **OAuth2 인증 서버** (9000번 포트)와 **OAuth2 클라이언트** (9001번 포트)를 세션 기반으로 구현한 예제입니다. 클라이언트는 인증 서버를 통해 사용자 인증을 받고, 세션에 인증 정보를 저장하여 후속 요청에서 이를 사용합니다.

## 구성 

 
- **9000번 포트** : OAuth2 인증 서버 (세션 기반)
 
- **9001번 포트** : OAuth2 클라이언트 (세션 기반)



---



## OAuth2 인증 서버 (9000) 

OAuth2 인증 서버는 인증 및 토큰 발급을 담당합니다. 클라이언트가 인증을 요청하면 인증 서버가 사용자를 인증하고, **Authorization Code** 를 클라이언트로 반환합니다. 클라이언트는 이 코드를 사용하여 액세스 토큰을 요청할 수 있습니다.

### 주요 설정 



```yaml
server:
  port: 9000

spring:
  security:
    oauth2:
      authorization-server:
        client:
          registration:
            client-oidc:
              client-id: client
              client-secret: secret
              redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
              authorization-grant-type: authorization_code
              scope: openid,read
        provider:
          client-oidc:
            authorization-uri: http://localhost:9000/oauth2/authorize
            token-uri: http://localhost:9000/oauth2/token
            user-info-uri: http://localhost:9000/userinfo
            user-name-attribute: sub
  servlet:
    session:
      cookie:
        same-site: lax
        secure: false
```


### 설정 설명 

 
- `client-id`, `client-secret`: OAuth2 인증 클라이언트 정보
 
- `authorization-uri`, `token-uri`, `user-info-uri`: 인증 서버에서 제공하는 URI
 
- `same-site`: 쿠키의 `SameSite` 정책을 `lax`로 설정하여 CSRF 공격 방지
 
- `secure: false`: 개발 환경에서만 사용하며, 배포 환경에서는 `true`로 변경 필요



---



## OAuth2 클라이언트 (9001) 


OAuth2 클라이언트는 인증 서버와 상호작용하여 사용자를 인증하고, 세션에 인증 정보를 저장하여 후속 요청에서 사용합니다.


### 주요 설정 



```yaml
server:
  port: 9001

spring:
  security:
    oauth2:
      client:
        registration:
          client-oidc:
            client-id: client
            client-secret: secret
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            authorization-grant-type: authorization_code
            scope: openid,read
        provider:
          client-oidc:
            authorization-uri: http://localhost:9000/oauth2/authorize
            token-uri: http://localhost:9000/oauth2/token
            user-info-uri: http://localhost:9000/userinfo
            user-name-attribute: sub
  servlet:
    session:
      cookie:
        same-site: lax
        secure: false

logging:
  level:
    org.springframework.security: DEBUG
    org.springframework.web: DEBUG
```


### 설정 설명 

 
- `client-id`, `client-secret`: OAuth2 인증 서버에서 제공하는 클라이언트 정보
 
- `authorization-grant-type`: 인증 방식으로 `authorization_code` 사용
 
- `scope`: 인증 요청에서 `openid`와 `read` 스코프 요청
 
- `user-info-uri`: 사용자의 정보를 요청할 URI
 
- `user-name-attribute`: 사용자 이름을 가져올 필드로 `sub`를 사용



---



## 세션 방식 인증 

이 프로젝트에서는 **세션 방식** 으로 OAuth2 인증을 구현했습니다. 클라이언트는 인증 후 **세션** 에 액세스 토큰을 저장하고, 후속 요청에서 해당 토큰을 사용하여 인증된 요청을 처리합니다.
 
- **세션 쿠키 설정** : 클라이언트와 서버 간 세션 정보를 쿠키를 통해 전달합니다.
 
- **토큰 저장** : 세션에 저장된 액세스 토큰을 사용하여 인증된 API 요청을 보냅니다.



---



## 문제점 및 해결하지 못한 사항 

 
- **세션에서 토큰 관리** : 세션에 저장된 토큰을 어떻게 안전하게 관리할지에 대한 고민이 있었습니다. 세션에서 토큰을 사용하지만 보안상 취약할 수 있기 때문에, 향후 토큰 저장 방식을 개선할 필요가 있습니다.
 
- **토큰 갱신 및 만료 처리** : 세션에서 토큰 만료 처리를 어떻게 구현할지에 대한 논의가 필요하며, 이를 위한 토큰 갱신 로직을 추가해야 했으나, 이 부분은 구현하지 못했습니다.



---



## 결론 

이 프로젝트는 **OAuth2 클라이언트** 와 **OAuth2 인증 서버** 를 세션 기반 방식으로 구현한 예제입니다. 이후, 세션 방식 대신 **토큰 기반 인증**  방식으로의 전환을 고려해야 할 필요가 있으며, 보안 및 토큰 관리 측면에서 추가적인 개선이 필요합니다.


---



## 실행 방법 

 
2. **9000번 포트 (OAuth2 인증 서버)**  실행
 
4. **9001번 포트 (OAuth2 클라이언트)**  실행
 
6. 브라우저에서 **9001번 포트** 로 접속하여 인증을 진행합니다.

