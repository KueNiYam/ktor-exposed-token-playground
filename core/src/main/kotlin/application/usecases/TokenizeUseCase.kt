package application.usecases

import domain.*

/**
 * 토큰화 유스케이스 - 핵심 비즈니스 로직
 */
class TokenizeUseCase {
    
    fun executeAll(text: String): List<Pair<TokenizerMeta, TokenizedText>> {
        val tokenizers = TokenizerRegistry.getAllTokenizers()
        return tokenizers.map { tokenizer ->
            tokenizer.meta to tokenizer.tokenize(text)
        }
    }
    
    fun execute(text: String, methodIds: List<Int>?): List<Pair<TokenizerMeta, TokenizedText>> {
        val tokenizers = TokenizerRegistry.getAllTokenizers()
        val selectedTokenizers = if (methodIds != null) {
            tokenizers.filter { it.meta.id in methodIds }
        } else {
            tokenizers
        }
        
        return selectedTokenizers.map { tokenizer ->
            tokenizer.meta to tokenizer.tokenize(text)
        }
    }
}
