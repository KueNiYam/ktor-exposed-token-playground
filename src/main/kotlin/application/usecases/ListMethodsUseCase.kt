package application.usecases

import domain.*
import infrastructure.TokenizerRegistry
import adapters.primary.MethodInfo

/**
 * 토큰화 방법 목록 조회 유스케이스
 */
class ListMethodsUseCase {
    
    fun execute(): List<MethodInfo> {
        return TokenizerRegistry.getAllTokenizers().map { tokenizer ->
            MethodInfo(
                id = tokenizer.meta.id,
                name = tokenizer.meta.name,
                description = tokenizer.meta.description
            )
        }
    }
    
    fun getTokenizerMetas(): List<TokenizerMeta> {
        return TokenizerRegistry.getAllTokenizers().map { it.meta }
    }
    
    fun getTotalCount(): Int {
        return TokenizerRegistry.getTotalCount()
    }
}
