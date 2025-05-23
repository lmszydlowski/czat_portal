package example.chat.service

import example.chat.model.PaymentInformation
import example.chat.model.User
import example.chat.model.UserProfile
import example.chat.repository.PaymentInformationRepository
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
    private val paymentInfoRepository: PaymentInformationRepository,
    private val passwordEncoder: PasswordEncoder
) {
    
    data class RegistrationRequest(
    @field:Size(min=3, max=20)
    val username: String,
    
    @field:Email
    val email: String,
    
    @field:Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
    val password: String,
    
    // Add validation for payment fields
    @field:CreditCardNumber
    val cardNumber: String,
    
    @field:Pattern(regexp = "^(0[1-9]|1[0-2])\\/([0-9]{2})\$")
    val expiryDate: String
)
    
    fun registerUser(request: RegistrationRequest): Mono<User> {
        return userRepository.existsByEmail(request.email)
            .flatMap { emailExists ->
                if (emailExists) {
                    Mono.error(IllegalArgumentException("Email already in use"))
                } else {
                    userRepository.existsByUsername(request.username)
                }
            }
            .flatMap { usernameExists ->
                if (usernameExists) {
                    Mono.error(IllegalArgumentException("Username already taken"))
                } else {
                    createUser(request)
                }
            }
    }


    private fun createUser(request: UserRegistrationService.RegistrationRequest): Mono<User> {
        val user = User(
            username = request.username,
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password),
            createdAt = LocalDateTime.now(),
            klikId = "KLIK-" + System.currentTimeMillis().toString() + "-" + (1000..9999).random()
        )

        return userRepository.save(user)
            .flatMap { savedUser ->
                val profile = UserProfile(
                    userId = savedUser.id!!,
                    fullName = request.fullName
                )

                Mono.fromCallable { userProfileRepository.save(profile) }
                    .then(savePaymentInfo(savedUser.id, request))
                    .thenReturn(savedUser)
            }
    }

    private fun savePaymentInfo(userId: Long, request: UserRegistrationService.RegistrationRequest): Mono<PaymentInformation> {
        // In a real application, you would use a payment processor API
        // and encrypt sensitive data before storing
        val lastFour = request.cardNumber.takeLast(4)
        
        val paymentInfo = PaymentInformation(
            userId = userId,
            cardType = request.cardType,
            lastFourDigits = lastFour,
            cardholderName = request.cardholderName,
            expiryDate = request.expiryDate,
            billingAddress = request.billingAddress,
            isDefault = true
        )
        
        return paymentInfoRepository.save(paymentInfo)
    }
}
