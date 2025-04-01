package example.chat.service

import example.chat.model.User
import example.chat.model.UserProfile
import example.chat.repository.UserRepository
import example.chat.repository.UserProfileRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class UserRegistrationService(
    private val userRepository: UserRepository,
    private val userProfileRepository: UserProfileRepository,
    private val passwordEncoder: PasswordEncoder
) {

    data class RegistrationRequest(
        val username: String,
        val email: String,
        val password: String,
        val klikId: String? = null  // Changed to String and made nullable
    )

    fun registerUser(request: RegistrationRequest): Mono<User> {
        return userRepository.existsByEmail(request.email)
            .flatMap { emailExists ->
                if (emailExists) {
                    Mono.error(IllegalArgumentException("Email already in use"))
                } else {
                    userRepository.existsByUsername(request.username)
                        .flatMap { usernameExists ->
                            if (usernameExists) {
                                Mono.error(IllegalArgumentException("Username already taken"))
                            } else {
                                createUser(request)
                            }
                        }
                }
            }
    }

    private fun createUser(request: RegistrationRequest): Mono<User> {
        val user = User(
            username = request.username,
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password),
            createdAt = LocalDateTime.now(),
            klikId = request.klikId  // Use klikId from request instead of generating
        )

        return userRepository.save(user)
            .flatMap { savedUser ->
                val profile = UserProfile(
                    userId = savedUser.id!!,
                    fullName = "" // Initialize empty profile
                )
                userProfileRepository.save(profile).thenReturn(savedUser)
            }
    }
}
