package example.chat.controller

import com.example.chat.model.LoginResponse
import example.chat.service.UserRegistrationService.RegistrationRequest
import com.example.chat.util.JwtUtil
import example.chat.model.User
import example.chat.service.ChatHomeBaseService
import example.chat.service.UserRegistrationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/auth")
class RegistrationController(
    private val userRegistrationService: UserRegistrationService,
    private val chatHomeBaseService: ChatHomeBaseService,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerUser(@RequestBody request: RegistrationRequest): Mono<Map<String, String>> {
        return Mono.fromCallable { userRegistrationService.registerUser(request = request) }
            .map { userMono: Mono<User> ->
                userMono.block()?.let { user ->
                    mapOf("message" to "User registered successfully", "userId" to user.id.toString())
                } ?: throw IllegalStateException("User registration failed")
            }
    }

    @PostMapping("/register/login-response")
    fun registerAndReturnToken(@RequestBody registrationRequest: RegistrationRequest): ResponseEntity<LoginResponse> {
        val user = userRegistrationService.registerUser(registrationRequest)
        val token = jwtUtil.generateToken(user.block()?.username ?: throw IllegalStateException("Missing username"))
        return ResponseEntity.ok(LoginResponse(token))
    }

    @PostMapping("/register/with-klik-id")
    fun registerWithKlikId(@RequestBody registrationRequest: RegistrationRequest): ResponseEntity<Any> {
        val klikId = registrationRequest.klikId
        // Zapisz klikId w obiekcie User
        val newUser = userRegistrationService.registerUser(registrationRequest)
        chatHomeBaseService.sendPostback(klikId.toString(), 0.0, newUser.block()?.id?.toString() ?: throw IllegalStateException("Missing ID"), "PLN", true)
        
        return ResponseEntity.ok(
            mapOf(
                "message" to "User registered with Klik ID successfully",
                "userId" to (newUser.block()?.id?.toString() ?: throw IllegalStateException("Missing ID"))
            )
        )
    }
}
