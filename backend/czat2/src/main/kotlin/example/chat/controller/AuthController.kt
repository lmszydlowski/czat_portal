package com.example.chat.controller

import com.example.chat.model.LoginRequest
import com.example.chat.model.LoginResponse
import com.example.chat.util.JwtUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Mono
@RestController
@RequestMapping("/auth")
class AuthController {

    // Dla uproszczenia – jeden użytkownik. W praktyce należy korzystać z bazy użytkowników.
    private val validUsername = "user"
    private val validPassword = "password"

    @Autowired
    lateinit var authenticationManager: AuthenticationManager
    @Autowired
    lateinit var jwtUtil: JwtUtil

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): Mono<ResponseEntity<LoginResponse>> {
        return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.username,
                loginRequest.password
            )
        ).map { auth ->
                ResponseEntity.ok(
                    LoginResponse(
                        jwtUtil.generateToken(auth.name)
                    )
                )
            
        }.onErrorResume {
            Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build())
        }
    }
}
// @PostMapping("/register")
// fun register(@RequestBody loginRequest: LoginRequest): Mono<ResponseEntity<String>> {
//     return if (loginRequest.username == validUsername && loginRequest.password == validPassword) {
//         Mono.just(ResponseEntity.ok("User registered successfully"))
//     } else {
//         Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid registration"))
//     }
// }
// @PostMapping("/login")
// fun login(@RequestBody loginRequest: LoginRequest): Mono<ResponseEntity<LoginResponse>> {
//     return if (loginRequest.username == validUsername && loginRequest.password == validPassword) {