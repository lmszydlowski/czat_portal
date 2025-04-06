// ✅ Single securityWebFilterChain with proper configuration
// ✅ Single securityWebFilterChain with proper configuration
// Removed conflicting import
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.web.server.ServerWebExchange
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.http.HttpStatus
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.AuthenticationException
import java.time.Duration
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono
@Bean
fun securityWebFilterChain(
    http: ServerHttpSecurity,
    securityContextRepository: ServerSecurityContextRepository
): SecurityWebFilterChain {
    return http
        .csrf { it.disable() }
        .httpBasic { it.disable() }
        .formLogin { it.disable() }
        .securityContextRepository(securityContextRepository)
        .authorizeExchange { exchange ->
            exchange
                .pathMatchers("/auth/login", "/auth/register").permitAll()
                .pathMatchers("/admin/**").hasRole("ADMIN")
                .pathMatchers("/chat/**").authenticated()
                .anyExchange().authenticated()
        }
        .exceptionHandling { exceptions: ServerHttpSecurity.ExceptionHandlingSpec ->
            exceptions.authenticationEntryPoint { exchange: ServerWebExchange, _: AuthenticationException ->
                exchange.response.setStatusCode(HttpStatus.UNAUTHORIZED)
                Mono.empty()
            }
        }
        .headers {
            it.hsts { hsts: ServerHttpSecurity.HeaderSpec.HstsSpec ->
                hsts.maxAge(Duration.ofSeconds(31536000))
                    .includeSubdomains(true)
            }
        }
        .build()
}
