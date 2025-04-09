package example.chat.service


import com.example.chat.model.ChatMessage
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
@Component
class ChatHomeBaseService {

    private val messages = mutableListOf<ChatMessage>()
    private var userStatus = "Offline"

    fun sendMessage(message: ChatMessage) {
        messages.add(message)
        // Implement logic to send message to Chat Home Base API
    }

    fun getMessages(): List<ChatMessage> {
        return messages
    }

    fun getUserStatus(): String {
        return userStatus
    }

    fun updateUserStatus(status: String) {
        userStatus = status
        // Implement logic to update status on Chat Home Base API
    }
    
    //fun sendPostback(klikId: String, wartosc: Double, leadId: String, waluta: String, noweKonto: Boolean) {
        //val url = "https://api.conversand.com/SIECI/postback.php?id_sieci=67dc17bbb8ad1918e3307938&haslo=sexdatenow&klik_id=$klikId&kwota=$wartosc&lead_id=$leadId&waluta=$waluta&nowe_konto=${if (noweKonto) 1 else 0}"
        // TODO: Implement HTTP request to the URL
        // For now, mark as intentionally unused with a comment
        // or use it in a logger: logger.debug("Sending postback to URL: $url")
    }

    @Value("\${chatbase.api.key}")
lateinit var apiKey: String

fun sendPostback(klikId: String, wartosc: Double, leadId: String, 
                waluta: String, noweKonto: Boolean): Mono<ResponseEntity<String>> {
    val webClient = WebClient.builder()
        .baseUrl("https://api.conversand.com")
        .build()

    return webClient.post()
        .uri("/SIECI/postback.php?id_sieci=67dc17bbb8ad1918e3307938&haslo={apiKey}", apiKey)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(mapOf(
            "klik_id" to klikId,
            "kwota" to wartosc,
            "lead_id" to leadId,
            "waluta" to waluta,
            "nowe_konto" to if(noweKonto) 1 else 0
        ))
        .retrieve()
        .toEntity(String::class.java)
}
}
