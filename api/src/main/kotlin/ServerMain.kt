import adapters.primary.WebAdapter
import application.usecases.TokenizeUseCase
import application.usecases.ListMethodsUseCase
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureServer()
    }.start(wait = true)
}

fun Application.configureServer() {
    // JSON 직렬화 설정
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
    
    // CORS 설정
    install(CORS) {
        anyHost()
        allowHeader("Content-Type")
    }
    
    // 의존성 주입
    val tokenizeUseCase = TokenizeUseCase()
    val listMethodsUseCase = ListMethodsUseCase()
    val webAdapter = WebAdapter(tokenizeUseCase, listMethodsUseCase)
    
    // 라우팅 설정
    webAdapter.configureRouting(this)
    
    println("🚀 토큰화 API 서버가 http://localhost:8080 에서 실행 중입니다")
    println("📖 API 문서: http://localhost:8080/api/health")
}
