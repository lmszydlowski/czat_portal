import example.chat.repository.UserRepository
import example.chat.service.ChatHomeBaseService
import reactor.core.publisher.Mono

class PaymentService(
    private val chatHomeBaseService: ChatHomeBaseService,
    private val userRepository: UserRepository
) {
    fun processPayment(userId: String, amount: Double, currency: String) {
        userRepository.findById(userId.toLong())
            .flatMap { user ->
                val klikId = user?.klikId ?: return@flatMap Mono.error<String>(IllegalArgumentException("Klik ID not found"))
                chatHomeBaseService.sendPostback(klikId, amount, userId, currency, false)
                Mono.empty<Void>()
            }
            .block()
        // Logika przetwarzania płatności
    }
}
