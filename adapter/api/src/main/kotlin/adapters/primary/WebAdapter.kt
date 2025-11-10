package adapters.primary

import adapters.primary.dto.*
import ports.TokenizerPort
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class WebAdapter(
    private val tokenizerPort: TokenizerPort
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
                    val metaList = tokenizerPort.getAvailableMethods()
                    call.respond(MethodsResponse.from(metaList))
                }
                
                // 토큰화 실행
                post("/tokenize") {
                    val request = call.receive<TokenizeRequest>()
                    
                    if (request.method == null) {
                        // 모든 방법으로 토큰화
                        val results = tokenizerPort.tokenizeAll(request.text)
                        call.respond(TokenizeResponse.from(request.text, results))
                    } else {
                        // 특정 방법으로 토큰화
                        val result = tokenizerPort.tokenizeWith(request.text, request.method)
                        call.respond(TokenizeResponse.from(request.text, result))
                    }
                }
            }
        }
    }
}
