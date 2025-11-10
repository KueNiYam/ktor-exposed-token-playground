package application.usecases

import domain.*
import infrastructure.TokenizerRegistry

/**
 * 토큰화 방법 목록 조회 유스케이스
 */
class ListMethodsUseCase {
    
    fun execute(): List<TokenizerMeta> {
        return TokenizerRegistry.getAllTokenizers().map { it.meta }
    }
    
    fun getTotalCount(): Int {
        return TokenizerRegistry.getTotalCount()
    }
}
