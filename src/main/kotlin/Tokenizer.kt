/**
 * 토큰화 메타데이터
 */
data class TokenizerInfo(
    val id: Int,
    val name: String,
    val description: String
)

/**
 * 토큰 데이터 클래스
 */
data class Token(
    val value: String,
    val score: Number? = null,
    val type: String? = null
)

/**
 * 토큰화 결과
 */
data class TokenizationResult(
    val tokens: List<Token>,
    val executionTimeMs: Long,
    val metadata: Map<String, Any> = emptyMap()
)

/**
 * 토큰화 인터페이스 - sealed interface로 구현
 */
sealed interface Tokenizer {
    val info: TokenizerInfo
    
    fun tokenize(text: String): TokenizationResult {
        val startTime = System.currentTimeMillis()
        val tokens = doTokenize(text)
        val endTime = System.currentTimeMillis()
        
        return TokenizationResult(
            tokens = tokens,
            executionTimeMs = endTime - startTime,
            metadata = getMetadata(tokens)
        )
    }
    
    fun doTokenize(text: String): List<Token>
    fun getMetadata(tokens: List<Token>): Map<String, Any> = emptyMap()
}
