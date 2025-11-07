package domain

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
data class TokenizedText(
    val tokens: List<Token>,
    val executionTimeMs: Long,
    val metadata: Map<String, Any> = emptyMap()
)
