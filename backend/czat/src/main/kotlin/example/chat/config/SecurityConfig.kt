@Configuration
@EnableWebFluxSecurity
class SecurityConfig(private val jwtUtil: JwtUtil) {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf { csrf -> csrf.disable() }
            .httpBasic { basic -> basic.disable() }
            .formLogin { form -> form.disable() }
            .cors { cors -> 
                cors.configurationSource {
                    CorsConfiguration().apply {
                        allowedOrigins = listOf(
                            "http://localhost:3000", 
                            "https://sexnow.pl",
                            "https://www.sexnow.pl"
                        )
                        allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        allowedHeaders = listOf(
                            HttpHeaders.AUTHORIZATION,
                            HttpHeaders.CONTENT_TYPE,
                            "klikId",
                            "X-Requested-With"
                        )
                        exposeHeaders = listOf(
                            HttpHeaders.AUTHORIZATION,
                            "X-Registration-Status"
                        )
                        allowCredentials = true
                        maxAge = 3600
                    }
                }
            }
            .authorizeExchange { exchanges ->
                exchanges
                    .pathMatchers(HttpMethod.POST, "/auth/register").permitAll()
                    .pathMatchers(HttpMethod.GET, "/auth/verify-email/**").permitAll()
                    .pathMatchers(HttpMethod.POST, "/auth/login").permitAll()
                    .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .pathMatchers("/admin/**").hasRole("ADMIN")
                    .pathMatchers("/payment/**").authenticated()
                    .pathMatchers("/api/**").authenticated()
                    .anyExchange().authenticated()
            }
            .exceptionHandling { exceptions ->
                exceptions
                    .authenticationEntryPoint { exchange, _ ->
                        exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                        Mono.empty()
                    }
                    .accessDeniedHandler { exchange, _ ->
                        exchange.response.statusCode = HttpStatus.FORBIDDEN
                        Mono.empty()
                    }
            }
            .headers { headers ->
                headers
                    .contentSecurityPolicy { csp ->
                        csp.policyDirectives = "default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src 'self' data:;"
                    }
                    .xssProtection { XssProtectionConfig::enable }
                    .frameOptions { FrameOptionsConfig::disable }
            }
            .build()
    }

    @Bean
    fun reactiveAuthenticationManager(): ReactiveAuthenticationManager {
        return ReactiveAuthenticationManager { authentication ->
            val token = authentication.credentials.toString()
            if (jwtUtil.validateToken(token)) {
                val claims = jwtUtil.getClaims(token)
                val authorities = claims["roles"]?.let { roles ->
                    (roles as List<*>).map {
                        SimpleGrantedAuthority(it.toString())
                    }
                } ?: emptyList()
                
                Mono.just(UsernamePasswordAuthenticationToken(
                    claims.subject,
                    token,
                    authorities
                ))
            } else {
                Mono.error(JwtAuthenticationException("Invalid or expired token"))
            }
        }
    }

    @Bean
    fun securityContextRepository(): ServerSecurityContextRepository {
        return object : ServerSecurityContextRepository {
            override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
                return Mono.justOrEmpty(exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION))
                    .filter { it.startsWith("Bearer ") }
                    .map { it.substring(7) }
                    .flatMap { token ->
                        if (jwtUtil.validateToken(token)) {
                            val claims = jwtUtil.getClaims(token)
                            val authorities = claims["roles"]?.let { roles ->
                                (roles as List<*>).map {
                                    SimpleGrantedAuthority(it.toString())
                                }
                            } ?: emptyList()
                            
                            Mono.just(SecurityContextImpl(
                                UsernamePasswordAuthenticationToken(
                                    claims.subject,
                                    token,
                                    authorities
                                )
                            ))
                        } else {
                            Mono.empty()
                        }
                    }
            }

            override fun save(exchange: ServerWebExchange, context: SecurityContext?): Mono<Void> {
                return Mono.empty()
            }
        }
    }
}

class JwtAuthenticationException(message: String) : AuthenticationException(message)
