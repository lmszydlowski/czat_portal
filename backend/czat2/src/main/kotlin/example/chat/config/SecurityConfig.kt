// ✅ Single securityWebFilterChain with proper configuration
// ✅ Single securityWebFilterChain with proper configuration
import org.springframework.context.annotation.Bean
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.http.HttpStatus
import org.springframework.security.config.web.server.ServerHttpSecurity
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
        .exceptionHandling { exceptions ->
            exceptions.authenticationEntryPoint { exchange, _ ->
                exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                Mono.empty()
            }
        }
        .headers {
            it.hsts { hsts ->
                hsts.maxAge(Duration.ofSeconds(31536000))
                    .includeSubdomains(true)
            }
        }
        .build()
}
