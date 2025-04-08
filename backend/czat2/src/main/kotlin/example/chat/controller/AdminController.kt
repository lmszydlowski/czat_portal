// Add to backend/czat/src/main/kotlin/example/chat/controller/AdminController.kt
package example.chat.controller
import example.chat.model.ChatHistory
import example.chat.repository.ChatHistoryRepository
import example.chat.repository.UserRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

get("/users") { /* User management logic */ }
get("/chats") { /* Chat monitoring logic */ }
get("/payments") { /* Payment tracking logic */ }

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
class AdminController(
    private val chatHistoryRepository: ChatHistoryRepository,
    private val userRepository: UserRepository
) {
    @GetMapping("/chat-history")
    fun getAllChatHistory(): Flux<example.chat.model.ChatHistory> {
        return chatHistoryRepository.findAllByOrderByTimestampDesc()
    }
    
    @GetMapping("/chat-history/{userId}")
    fun getUserChatHistory(@PathVariable userId: Long): Flux<ChatHistory> {
        return chatHistoryRepository.findByUserId(userId)
    }
    
    @GetMapping("/users")
    fun getAllUsers() = userRepository.findAll()
}
