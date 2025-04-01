// Add to backend/czat/src/main/kotlin/example/chat/model/ChatHistory.kt
package example.chat.controller

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("chat_history")
 data class ChatHistory(
    @Id val id: Long? = null,
    val userId: Long,
    val messageId: String,
    val content: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val isFromUser: Boolean
)
