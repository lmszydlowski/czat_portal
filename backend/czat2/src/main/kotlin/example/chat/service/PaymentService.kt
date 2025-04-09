import example.chat.repository.UserRepository
import example.chat.service.ChatHomeBaseService
import reactor.core.publisher.Mono

class PaymentService(
    private val chatHomeBaseService: ChatHomeBaseService,
    private val userRepository: UserRepository
) {
    fun processPayment(userId: String, amount: Double, currency: String): Mono<Void> {
        return userRepository.findById(userId.toLong())
            .flatMap { user ->
                user?.klikId?.let { klikId ->
                    chatHomeBaseService.sendPostback(klikId, amount, userId, currency, false)
                } ?: Mono.error(IllegalArgumentException("Invalid user Klik ID"))
            }
            .onErrorResume { e ->
                logger.error("Payment failed for user $userId", e)
                Mono.empty()
            }
    }
