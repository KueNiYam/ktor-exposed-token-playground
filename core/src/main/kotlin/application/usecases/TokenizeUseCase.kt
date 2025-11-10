package application.usecases

import domain.*
import ports.TokenizerPort

/**
 * 토큰화 유스케이스 - TokenizerPort 구현
 */
class TokenizeUseCase : TokenizerPort {
    
    override fun tokenizeAll(text: String): List<Pair<TokenizerMeta, TokenizedText>> {
        val tokenizers = TokenizerRegistry.getAllTokenizers()
        return tokenizers.map { tokenizer ->
            tokenizer.meta to tokenizer.tokenize(text)
        }
    }
    
    override fun tokenizeWith(text: String, methodId: Int): Pair<TokenizerMeta, TokenizedText> {
        val tokenizers = TokenizerRegistry.getAllTokenizers()
        val tokenizer = tokenizers.find { it.meta.id == methodId }
            ?: throw IllegalArgumentException("토큰화 방법을 찾을 수 없습니다: $methodId")
        
        return tokenizer.meta to tokenizer.tokenize(text)
    }
    
    override fun getAvailableMethods(): List<TokenizerMeta> {
        return TokenizerRegistry.getAllTokenizerMeta()
    }
}
