package adapters.primary

import application.usecases.TokenizeUseCase
import application.usecases.ListMethodsUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

class WebAdapter(
    private val tokenizeUseCase: TokenizeUseCase,
    private val listMethodsUseCase: ListMethodsUseCase
) {
    
    fun configureRouting(application: Application) {
        application.routing {
            route("/api") {
                // 헬스체크
                get("/health") {
                    call.respond(HttpStatusCode.OK, mapOf("status" to "healthy"))
                }
                
                // 토큰화 방법 목록
                get("/methods") {
                    val methods = listMethodsUseCase.execute()
                    call.respond(MethodsResponse(methods, methods.size))
                }
                
                // 토큰화 실행
                post("/tokenize") {
                    val request = call.receive<TokenizeRequest>()
                    val results = tokenizeUseCase.execute(request.text, request.methods)
                    call.respond(TokenizeResponse(request.text, results))
                }
            }
        }
    }
}

@Serializable
data class TokenizeRequest(
    val text: String,
    val methods: List<Int>? = null
)

@Serializable
data class TokenizeResponse(
    val input: String,
    val results: List<TokenizeResult>
)

@Serializable
data class TokenizeResult(
    val id: Int,
    val name: String,
    val description: String,
    val token_count: Int,
    val execution_time_ms: Long,
    val tokens: List<TokenData>
)

@Serializable
data class TokenData(
    val value: String,
    val score: Double? = null,
    val type: String? = null
)

@Serializable
data class MethodsResponse(
    val methods: List<MethodInfo>,
    val total_count: Int
)

@Serializable
data class MethodInfo(
    val id: Int,
    val name: String,
    val description: String
)
