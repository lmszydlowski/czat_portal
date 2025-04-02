package example.chat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.http.HttpStatus
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter
import org.springframework.web.server.WebFilter
import reactor.core.publisher.Mono
import java.net.URI
import org.springframework.web.util.UriComponentsBuilder

@SpringBootApplication
@ComponentScan(basePackages = ["com.example.chat", "example.chat.repository"])
@EnableR2dbcRepositories(basePackages = ["example.chat.repository"])
class ReactiveChatApplication {

    @Bean
    fun redirectToHttps(): WebFilter {
        return WebFilter { exchange, chain ->
            val request = exchange.request
            if (request.headers.getFirst("X-Forwarded-Proto") == "http") {
                val redirectUrl = UriComponentsBuilder
                    .fromUriString("https://seksnow.pl")
                    .path(request.path.value())
                    .query(request.uri.query)
                    .build()
                    .toUriString()
                val response = exchange.response
                response.statusCode = HttpStatus.PERMANENT_REDIRECT
                response.headers.location = URI.create(redirectUrl)
                return@WebFilter Mono.empty()
            }
            chain.filter(exchange)
        }
    }
}

// ✅ Correct main function placement
fun main(args: Array<String>) {
    runApplication<ReactiveChatApplication>(*args)
}
