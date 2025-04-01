package example.chat.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("payment_information")
data class PaymentInformation(
    @Id val id: Long? = null,
    val userId: Long,
    val cardType: String? = null,
    val lastFourDigits: String? = null,
    val cardholderName: String? = null,
    val expiryDate: String? = null,
    val billingAddress: String? = null,
    val isDefault: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
