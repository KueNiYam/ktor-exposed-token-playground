package application.usecases

import domain.*
import infrastructure.TokenizerRegistry

/**
 * 토큰화 유스케이스 - 핵심 비즈니스 로직
 */
class TokenizeUseCase {
    
    fun execute(text: String, methodName: String? = null): TokenizedText {
        val tokenizers = TokenizerRegistry.getAllTokenizers()
        
        val selectedTokenizer = if (methodName != null) {
            tokenizers.find { it.meta.name == methodName }
                ?: throw IllegalArgumentException("토큰화 방법을 찾을 수 없습니다: $methodName")
        } else {
            tokenizers.first() // 기본값: 첫 번째 토큰화 방법
        }
        
        return selectedTokenizer.tokenize(text)
    }
    
    fun executeAll(text: String): List<Pair<TokenizerMeta, TokenizedText>> {
        val tokenizers = TokenizerRegistry.getAllTokenizers()
        return tokenizers.map { tokenizer ->
            tokenizer.meta to tokenizer.tokenize(text)
        }
    }
    
    fun execute(text: String, methodIds: List<Int>?): List<Triple<TokenizerMeta, TokenizedText, Map<String, Any>>> {
        val tokenizers = TokenizerRegistry.getAllTokenizers()
        val selectedTokenizers = if (methodIds != null) {
            tokenizers.filter { it.meta.id in methodIds }
        } else {
            tokenizers
        }
        
        return selectedTokenizers.map { tokenizer ->
            val result = tokenizer.tokenize(text)
            Triple(tokenizer.meta, result, emptyMap<String, Any>())
        }
    }
}
