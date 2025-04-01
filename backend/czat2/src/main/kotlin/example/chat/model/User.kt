package example.chat.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("users")
data class User(
    @Id val id: Long? = null,
    val username: String,
    val email: String,
    val passwordHash: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val lastLogin: LocalDateTime? = null,
    val isActive: Boolean = true,
    val isAdmin: Boolean = false,
    val klikId: String,
)
