package example.chat.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("user_profiles")
data class UserProfile(
    @Id val id: Long? = null,
    val userId: Long,
    val fullName: String? = null,
    val bio: String? = null,
    val profileImageUrl: String? = null
)
