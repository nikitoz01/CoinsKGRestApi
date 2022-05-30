package kg.coins.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain


@Configuration
//@EnableWebFluxSecurity
//@EnableReactiveMethodSecurity
class SecurityConfig {
    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http.authorizeExchange()
            .pathMatchers(HttpMethod.GET,"/categories/**","/coins/**","/categories**","/coins**","/images**")
            .permitAll()
//            .pathMatchers(HttpMethod.POST,"/categories**","/coins**","/images**","/**","/categories/**","/coins/**")
//            .authenticated()
//            .pathMatchers(HttpMethod.DELETE,"/categories**","/coins**","/images**","/**","/categories/**","/coins/**")
//            .authenticated()
            .anyExchange()
            .authenticated()
            .and()
            .httpBasic()
            .and()
//            .disable()
            .csrf()
            .disable()
            .cors()
            .disable()
            .formLogin()
            .disable()
            .logout()
            .disable()
            .build()
    }
}