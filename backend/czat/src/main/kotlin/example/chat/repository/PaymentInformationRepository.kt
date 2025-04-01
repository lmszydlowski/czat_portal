package example.chat.repository

import example.chat.model.PaymentInformation
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface PaymentInformationRepository : R2dbcRepository<PaymentInformation, Long> {
    fun findByUserId(userId: Long): Flux<PaymentInformation>
    fun findByUserIdAndIsDefault(userId: Long, isDefault: Boolean): Flux<PaymentInformation>
}
