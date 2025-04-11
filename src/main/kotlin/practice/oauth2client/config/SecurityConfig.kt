package practice.oauth2client.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

import org.springframework.security.config.annotation.web.invoke


@Configuration
@EnableWebSecurity // 꼭 필요함!
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize("/login", permitAll)
                authorize("/oauth2/**", permitAll) // ✅ 요거 추가!
                authorize(anyRequest, authenticated)
            }
            oauth2Login { }
        }
        return http.build()
    }
}
