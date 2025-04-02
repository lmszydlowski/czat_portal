// ✅ Single securityWebFilterChain with proper configuration
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
            it.httpStrictTransportSecurity { hsts ->
                hsts.maxAgeInSeconds(31536000)
                    .includeSubdomains(true)
            }
        }
        .build()
}
