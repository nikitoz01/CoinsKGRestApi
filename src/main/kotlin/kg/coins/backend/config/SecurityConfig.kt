package kg.coins.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain


@Configuration
class SecurityConfig {
    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http.authorizeExchange()
            .pathMatchers("/categories**","/coins**","/images**","/**")
            .permitAll()
            .and()
            .httpBasic()
            .disable()
            .csrf()
            .disable()
            .formLogin()
            .disable()
            .logout()
            .disable()
            .build()
    }
}