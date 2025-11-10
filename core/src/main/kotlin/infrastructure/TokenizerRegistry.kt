package infrastructure

import domain.*

/**
 * 토큰화 레지스트리 - 모든 토큰화 구현체를 관리
 */
object TokenizerRegistry {
    private val tokenizers = listOf(
        WhitespaceTokenizer(TokenizerMeta(1, "공백 기준", "공백 문자로 단어 분리")),
        WordTokenizer(TokenizerMeta(2, "단어 기준", "영문자/숫자/한글만 추출")),
        CharacterTokenizer(TokenizerMeta(3, "문자 기준", "각 문자를 개별 토큰으로 분리")),
        PunctuationTokenizer(TokenizerMeta(4, "구두점 포함", "구두점을 별도 토큰으로 처리")),
        SubwordTokenizer(TokenizerMeta(5, "서브워드 (BPE)", "GPT처럼 2-3글자 단위로 분할")),
        ByteTokenizer(TokenizerMeta(9, "바이트 기준", "UTF-8 바이트를 16진수로 표현")),
        LengthTokenizer(TokenizerMeta(11, "길이 기준", "고정 길이로 균등 분할")),
        FrequencyTokenizer(TokenizerMeta(12, "빈도 기반", "단어 출현 빈도로 중요도 계산")),
        TFIDFTokenizer(TokenizerMeta(13, "TF-IDF 기반", "단어 중요도 점수로 필터링")),
        LanguageTokenizer(TokenizerMeta(14, "언어 혼합", "한글/영어/숫자/특수문자별 분류"))
    )

    fun getAllTokenizers(): List<Tokenizer> = tokenizers
    fun getTotalCount(): Int = tokenizers.size

    /**
     * 모든 토큰화 방법의 정보를 반환
     */
    fun getAllTokenizerMeta(): List<TokenizerMeta> {
        return tokenizers.map { it.meta }
            .sortedBy { it.id }
    }
}
