/**
 * 토큰화 레지스트리 - 모든 토큰화 구현체를 관리
 */
object TokenizerRegistry {
    private val tokenizers = listOf<Tokenizer>(
        WhitespaceTokenizer(TokenizerInfo(1, "공백 기준", "공백 문자로 단어 분리")),
        WordTokenizer(TokenizerInfo(2, "단어 기준", "영문자/숫자/한글만 추출")),
        CharacterTokenizer(TokenizerInfo(3, "문자 기준", "각 문자를 개별 토큰으로 분리")),
        PunctuationTokenizer(TokenizerInfo(4, "구두점 포함", "구두점을 별도 토큰으로 처리")),
        SubwordTokenizer(TokenizerInfo(5, "서브워드 (BPE)", "GPT처럼 2-3글자 단위로 분할")),
        ByteTokenizer(TokenizerInfo(9, "바이트 기준", "UTF-8 바이트를 16진수로 표현")),
        LengthTokenizer(TokenizerInfo(11, "길이 기준", "고정 길이로 균등 분할")),
        FrequencyTokenizer(TokenizerInfo(12, "빈도 기반", "단어 출현 빈도로 중요도 계산")),
        TFIDFTokenizer(TokenizerInfo(13, "TF-IDF 기반", "단어 중요도 점수로 필터링")),
        LanguageTokenizer(TokenizerInfo(14, "언어 혼합", "한글/영어/숫자/특수문자별 분류"))
    )
    
    fun getAllTokenizers(): List<Tokenizer> = tokenizers
    fun getTotalCount(): Int = tokenizers.size
    
    /**
     * 모든 토큰화 방법의 정보를 반환
     */
    fun getAllTokenizerInfo(): List<Triple<Int, String, String>> {
        return tokenizers.map { Triple(it.info.id, it.info.name, it.info.description) }
            .sortedBy { it.first }
    }
}
