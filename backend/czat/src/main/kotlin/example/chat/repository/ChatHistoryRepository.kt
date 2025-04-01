// Add to backend/czat/src/main/kotlin/example/chat/repository/ChatHistoryRepository.kt
package example.chat.repository

import example.chat.model.ChatHistory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import reactor.core.publisher.Flux

@Repository

//@EnableR2dbcRepositories(basePackages = ["example.chat.repository"])
interface ChatHistoryRepository : R2dbcRepository<ChatHistory, Long> {
    fun findByUserId(userId: Long): Flux<ChatHistory>
    fun findAllByOrderByTimestampDesc(): Flux<ChatHistory>
}
