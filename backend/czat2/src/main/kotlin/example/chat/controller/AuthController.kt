package com.example.chat.controller

import com.example.chat.model.LoginRequest
import com.example.chat.model.LoginResponse
import com.example.chat.util.JwtUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

// Removed invalid 'post' calls. Logic is already implemented in the AuthController class.

@RestController
@RequestMapping("/auth")
class AuthController {

    // Dla uproszczenia – jeden użytkownik. W praktyce należy korzystać z bazy użytkowników.
    private val validUsername = "user"
    private val validPassword = "password"

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): Mono<ResponseEntity<LoginResponse>> {
        return Mono.fromSupplier {
            if (loginRequest.username == validUsername && loginRequest.password == validPassword) {
                val token = JwtUtil.generateToken(loginRequest.username)
                ResponseEntity.ok(LoginResponse(token))
            } else {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }
        }
    }
}
