package adapters.primary

import adapters.primary.dto.*
import application.usecases.TokenizeUseCase
import domain.TokenizerRegistry
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class WebAdapter(
    private val tokenizeUseCase: TokenizeUseCase
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
                    val metaList = TokenizerRegistry.getAllTokenizerMeta()
                    call.respond(MethodsResponse.from(metaList))
                }
                
                // 토큰화 실행
                post("/tokenize") {
                    val request = call.receive<TokenizeRequest>()
                    val results = tokenizeUseCase.execute(request.text, request.methods)
                    call.respond(TokenizeResponse.from(request.text, results))
                }
            }
        }
    }
}
