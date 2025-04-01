package example.chat.repository

import example.chat.model.User
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserRepository : R2dbcRepository<User, Long> {
    fun findByEmail(email: String): Mono<User>
    fun findByUsername(username: String): Mono<User>
    fun existsByEmail(email: String): Mono<Boolean>
    fun existsByUsername(username: String): Mono<Boolean>
}
