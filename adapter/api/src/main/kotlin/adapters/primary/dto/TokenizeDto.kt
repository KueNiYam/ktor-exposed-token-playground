package adapters.primary.dto

import domain.Token
import domain.TokenizedText
import domain.TokenizerMeta
import kotlinx.serialization.Serializable

@Serializable
data class TokenizeRequest(
    val text: String,
    val methods: List<Int>? = null
)

@Serializable
data class TokenizeResponse(
    val input: String,
    val results: List<TokenizeResult>
) {
    @Serializable
    data class TokenizeResult(
        val id: Int,
        val name: String,
        val description: String,
        val token_count: Int,
        val execution_time_ms: Long,
        val tokens: List<TokenData>
    ) {
        @Serializable
        data class TokenData(
            val value: String,
            val score: Double? = null,
            val type: String? = null
        ) {
            companion object {
                fun from(token: Token): TokenData {
                    return TokenData(
                        value = token.value,
                        score = token.score?.toDouble(),
                        type = token.type
                    )
                }
            }
        }
        
        companion object {
            fun from(meta: TokenizerMeta, result: TokenizedText): TokenizeResult {
                return TokenizeResult(
                    id = meta.id,
                    name = meta.name,
                    description = meta.description,
                    token_count = result.tokens.size,
                    execution_time_ms = result.executionTimeMs,
                    tokens = result.tokens.map { TokenData.from(it) }
                )
            }
        }
    }
    
    companion object {
        fun from(input: String, results: List<Pair<TokenizerMeta, TokenizedText>>): TokenizeResponse {
            return TokenizeResponse(
                input = input,
                results = results.map { (meta, result) ->
                    TokenizeResult.from(meta, result)
                }
            )
        }
    }
}
