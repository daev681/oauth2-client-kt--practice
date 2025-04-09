package practice.oauth2client.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

import org.springframework.security.config.annotation.web.invoke


@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize("/login", permitAll)
                authorize(anyRequest, authenticated)
            }

            oauth2Login {  // Deprecated이긴 하나, 아직까지는 필요한 부분
                // 여기서 커스터마이징도 가능
            }
        }
        return http.build()
    }
}
