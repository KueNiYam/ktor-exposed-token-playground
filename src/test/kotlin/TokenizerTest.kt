import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import domain.*
import domain.Tokenizer.TokenizerMeta

class TokenizerTest {
    
    @Test
    fun testWhitespaceTokenizer() {
        val testText = "안녕하세요! Hello World 123"
        val tokenizer = WhitespaceTokenizer(TokenizerMeta(1, "공백 기준", "공백 문자로 단어 분리"))
        val result = tokenizer.tokenize(testText)
        
        assertEquals(4, result.tokens.size)
        assertEquals("안녕하세요!", result.tokens[0].value)
        assertEquals("Hello", result.tokens[1].value)
        assertTrue(result.executionTimeMs >= 0)
    }
    
    @Test
    fun testWordTokenizer() {
        val testText = "안녕하세요! Hello World 123"
        val tokenizer = WordTokenizer(TokenizerMeta(2, "단어 기준", "영문자/숫자/한글만 추출"))
        val result = tokenizer.tokenize(testText)
        
        assertEquals(4, result.tokens.size)
        assertEquals("안녕하세요", result.tokens[0].value)  // 구두점 제외
        assertEquals("Hello", result.tokens[1].value)
    }
    
    @Test
    fun testCharacterTokenizer() {
        val testText = "안녕하세요! Hello World 123"
        val tokenizer = CharacterTokenizer(TokenizerMeta(3, "문자 기준", "각 문자를 개별 토큰으로 분리"))
        val result = tokenizer.tokenize(testText)
        
        assertEquals(testText.length, result.tokens.size)
        assertEquals("안", result.tokens[0].value)
        assertEquals("녕", result.tokens[1].value)
    }
    
    @Test
    fun testFrequencyTokenizer() {
        val tokenizer = FrequencyTokenizer(TokenizerMeta(12, "빈도 기반", "단어 출현 빈도로 중요도 계산"))
        val result = tokenizer.tokenize("hello hello world")
        
        assertEquals(2, result.tokens.size)
        assertEquals("hello", result.tokens[0].value)
        assertEquals(2, result.tokens[0].score)
        assertEquals("world", result.tokens[1].value)
        assertEquals(1, result.tokens[1].score)
        assertEquals(3, result.metadata["total_words"])
        assertEquals(2, result.metadata["unique_words"])
    }
    
    @Test
    fun testLanguageTokenizer() {
        val tokenizer = LanguageTokenizer(TokenizerMeta(14, "언어 혼합", "한글/영어/숫자/특수문자별 분류"))
        val result = tokenizer.tokenize("안녕 Hello 123 !")
        
        assertEquals(4, result.tokens.size)
        assertEquals("한글", result.tokens[0].type)
        assertEquals("영어", result.tokens[1].type)
        assertEquals("숫자", result.tokens[2].type)
        assertEquals("특수문자", result.tokens[3].type)
    }
    
    @Test
    fun testLengthTokenizer() {
        val tokenizer = LengthTokenizer(TokenizerMeta(11, "길이 기준", "고정 길이로 균등 분할"), 3)
        val result = tokenizer.tokenize("abcdef")
        
        assertEquals(2, result.tokens.size)
        assertEquals("abc", result.tokens[0].value)
        assertEquals("def", result.tokens[1].value)
        assertEquals(3, result.metadata["chunk_length"])
    }
    
    @Test
    fun testTokenizerRegistry() {
        val tokenizers = TokenizerRegistry.getAllTokenizers()
        
        assertEquals(10, tokenizers.size)
        assertEquals(10, TokenizerRegistry.getTotalCount())
        
        val info = TokenizerRegistry.getAllTokenizerInfo()
        assertEquals(10, info.size)
        assertEquals(1, info[0].first)  // ID 순으로 정렬됨
    }
    
    @Test
    fun testAllTokenizersExecutionTime() {
        val testText = "안녕하세요! Hello World 123"
        val tokenizers = TokenizerRegistry.getAllTokenizers()
        
        tokenizers.forEach { tokenizer ->
            val result = tokenizer.tokenize(testText)
            assertTrue(result.executionTimeMs >= 0)
            assertTrue(result.tokens.isNotEmpty())
        }
    }
}
