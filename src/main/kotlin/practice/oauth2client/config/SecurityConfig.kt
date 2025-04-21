package practice.oauth2client.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository

import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity // 꼭 필요함!
class SecurityConfig {

    // OAuth2 인증 요청을 쿠키에 저장할 Repository
    @Bean
    fun authorizationRequestRepository(): AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
        return HttpSessionOAuth2AuthorizationRequestRepository()
    }

    // ✅ CORS 설정 추가
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowedOrigins = listOf("http://localhost:9000", "http://localhost:9001")
        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        config.allowedHeaders = listOf("*")
        config.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }


    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // 세션 고정 방지 해제
            .sessionManagement {
                it.sessionFixation().none()  // 세션 ID 고정 방지 해제
            }
            // 요청에 대한 권한 설정
            .authorizeHttpRequests {
                it
                    .requestMatchers("/login", "/oauth2/**").permitAll()  // 로그인 및 OAuth2 관련 요청은 모두 허용
                    .anyRequest().authenticated()  // 그 외의 요청은 인증된 사용자만 접근 가능
            }
            // OAuth2 로그인 설정
            .oauth2Login {
                it.failureHandler { request, response, exception ->
                    exception.printStackTrace()  // 콘솔에 에러 찍힘
                    response.sendRedirect("/login?error")
                }
                it.authorizationEndpoint {
                    it.authorizationRequestRepository(authorizationRequestRepository())  // 인증 요청 저장소 설정
                }
            }
        return http.build()  // 빌드하여 반환
    }
}
