package com.example.chat.config

import com.example.chat.util.JwtUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import org.springframework.security.core.context.SecurityContextImpl as SecurityContextImpl1

@Configuration
@EnableWebFluxSecurity // Enable WebFlux Security
class SecurityConfig {

    @Bean
    fun authenticationManager(): ReactiveAuthenticationManager {
        return ReactiveAuthenticationManager { authentication ->
            val token = authentication.credentials.toString()
            if (JwtUtil.validateToken(token)) {
                val claims = JwtUtil.getClaims(token)
                val username = claims.subject
                val roles = claims.get("roles", List::class.java)?.map { it.toString() } ?: emptyList()
                if (username != null) {
                    val authorities = roles.map { SimpleGrantedAuthority(it) }
                    val auth = UsernamePasswordAuthenticationToken(username, token, authorities)
                    Mono.just(auth)
                } else {
                    Mono.empty()
                }
            } else {
                Mono.empty()
            }
        }
    }

    @Bean
    fun securityContextRepository(authManager: ReactiveAuthenticationManager): ServerSecurityContextRepository {
        return object : ServerSecurityContextRepository {
            override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
                val token = exchange.request.headers[HttpHeaders.AUTHORIZATION]
                    ?.firstOrNull()
                    ?.removePrefix("Bearer ")
                return if (token != null && JwtUtil.validateToken(token)) {
                    val claims = JwtUtil.getClaims(token)
                    val username = claims.subject
                    val authorities = claims.get("roles", List::class.java)
                        ?.map { SimpleGrantedAuthority(it.toString()) }
                        ?: emptyList()
                    if (username != null) {
                        val auth = UsernamePasswordAuthenticationToken(username, token, authorities)
                        Mono.just(SecurityContextImpl1(auth))
                    } else {
                        Mono.empty()
                    }
                } else {
                    Mono.empty()
                }
            }

            override fun save(exchange: ServerWebExchange, context: SecurityContext?): Mono<Void> {
                return Mono.empty() // Stateless, no need to save
            }
        }
    }

    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        securityContextRepository: ServerSecurityContextRepository
    ): SecurityWebFilterChain {
        return http
            .csrf { it.disable() } // Disable CSRF for stateless JWT
            .httpBasic { it.disable() } // No HTTP Basic
            .formLogin { it.disable() } // No form login
            .securityContextRepository(securityContextRepository)
            .authorizeExchange { exchange ->
                exchange
                    .pathMatchers("/auth/login", "/auth/register").permitAll()
                    .pathMatchers("/admin/**").hasRole("ADMIN")
                    .pathMatchers("/chat/**").authenticated()
                    .anyExchange().authenticated()
            }
            .exceptionHandling { exceptions ->
                exceptions.authenticationEntryPoint { exchange, _ ->
                    exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                    Mono.empty()
                }
            }
            .build()
    }
}