package com.example.chat.controller

import com.example.chat.model.ChatMessage
import example.chat.model.ChatHistory
import example.chat.repository.ChatHistoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
// import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.*
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import java.time.LocalDateTime
import java.util.UUID

// WebSocket Controller for real-time chat
@Controller
class ChatController {
    @Autowired
    private lateinit var chatHistoryRepository: ChatHistoryRepository

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    fun handleChat(chatMessage: ChatMessage): ChatMessage {
        val chatHistory = ChatHistory(
            userId = chatMessage.userId ?: 0L,
            messageId = chatMessage.id ?: UUID.randomUUID().toString(),
            content = chatMessage.content,
            timestamp = LocalDateTime.now(),
            isFromUser = true
        )
        chatHistoryRepository.save(chatHistory).subscribe()
        return chatMessage
    }

    @GetMapping("/api/chat/history")
    fun getChatHistory(): Flux<ChatHistory> {
        return chatHistoryRepository.findAllByOrderByTimestampDesc()
    }
}

// REST Controller for chat API (SSE and POST)
@RestController
@RequestMapping("/api/chat")
class ChatApiController(
    private val chatHistoryRepository: ChatHistoryRepository // Only this dependency
) {

    // Multicast sink for broadcasting messages
    private val chatSink: Sinks.Many<ChatMessage> =
        Sinks.many().multicast().directBestEffort()

    // Endpoint for subscribing to messages via Server-Sent Events (SSE)
    @GetMapping(produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun streamChat(): Flux<ChatMessage> {
        return chatSink.asFlux()
    }

    // Endpoint for posting messages
    @PostMapping
    // fun sendMessage(@RequestBody message: ChatHistory): ResponseEntity<ChatMessage> {
    //     val savedMessage = chatHistoryRepository.save(message)
    //     return ResponseEntity.ok(savedMessage)
    // }
    fun postMessage(
        @RequestBody message: ChatMessage,
        @AuthenticationPrincipal userId: Long
    ): Mono<ChatMessage> {
        val messageWithId = message.copy(id = UUID.randomUUID().toString())

        val chatHistory = ChatHistory(
            userId = userId,
            messageId = messageWithId.id ?: throw IllegalArgumentException("Message ID cannot be null"),
            content = messageWithId.content,
            timestamp = LocalDateTime.now(),
            isFromUser = true
        )

        return chatHistoryRepository.save(chatHistory)
            .doOnSuccess { chatSink.tryEmitNext(messageWithId) }
            .thenReturn(messageWithId)
    }

    // Endpoint to get chat history
    @GetMapping("/history")
    fun getHistory(): Flux<ChatHistory> {
        return chatHistoryRepository.findAllByOrderByTimestampDesc()
    }
}