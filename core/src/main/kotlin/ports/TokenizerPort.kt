package ports

import domain.TokenizerMeta
import domain.TokenizedText

/**
 * 제공하는 토큰화 기능에 대한 포트
 */
interface TokenizerPort {
    
    /**
     * 모든 토큰화 방법으로 텍스트를 토큰화
     */
    fun tokenizeAll(text: String): List<Pair<TokenizerMeta, TokenizedText>>
    
    /**
     * 특정 방법으로 텍스트를 토큰화
     */
    fun tokenizeWith(text: String, methodId: Int): Pair<TokenizerMeta, TokenizedText>
    
    /**
     * 사용 가능한 토큰화 방법 목록 조회
     */
    fun getAvailableMethods(): List<TokenizerMeta>
}
