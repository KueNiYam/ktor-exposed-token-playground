/**
 * 공백 기준 토큰화
 */
data class WhitespaceTokenizer(override val info: TokenizerInfo) : Tokenizer {
    override fun doTokenize(text: String): List<Token> {
        return text.trim().split(Regex("\\s+"))
            .filter { it.isNotEmpty() }
            .map { Token(it) }
    }
}

/**
 * 단어 기준 토큰화
 */
data class WordTokenizer(override val info: TokenizerInfo) : Tokenizer {
    override fun doTokenize(text: String): List<Token> {
        return Regex("[a-zA-Z0-9가-힣]+").findAll(text)
            .map { Token(it.value) }
            .toList()
    }
}

/**
 * 문자 기준 토큰화
 */
data class CharacterTokenizer(override val info: TokenizerInfo) : Tokenizer {
    override fun doTokenize(text: String): List<Token> {
        return text.map { Token(it.toString()) }
    }
}

/**
 * 구두점 포함 토큰화
 */
data class PunctuationTokenizer(override val info: TokenizerInfo) : Tokenizer {
    override fun doTokenize(text: String): List<Token> {
        val tokens = mutableListOf<Token>()
        val currentToken = StringBuilder()
        
        for (char in text) {
            when {
                char.isWhitespace() -> {
                    if (currentToken.isNotEmpty()) {
                        tokens.add(Token(currentToken.toString()))
                        currentToken.clear()
                    }
                }
                char in ",.!?;:()[]{}\"'-" -> {
                    if (currentToken.isNotEmpty()) {
                        tokens.add(Token(currentToken.toString()))
                        currentToken.clear()
                    }
                    tokens.add(Token(char.toString()))
                }
                else -> currentToken.append(char)
            }
        }
        
        if (currentToken.isNotEmpty()) {
            tokens.add(Token(currentToken.toString()))
        }
        
        return tokens
    }
}

/**
 * 서브워드 토큰화 (BPE 스타일)
 */
data class SubwordTokenizer(override val info: TokenizerInfo) : Tokenizer {
    override fun doTokenize(text: String): List<Token> {
        val tokens = mutableListOf<Token>()
        val words = text.split(Regex("\\s+")).filter { it.isNotEmpty() }
        
        for (word in words) {
            var remaining = word
            while (remaining.isNotEmpty()) {
                when {
                    remaining.length >= 3 -> {
                        tokens.add(Token(remaining.take(3)))
                        remaining = remaining.drop(3)
                    }
                    remaining.length >= 2 -> {
                        tokens.add(Token(remaining.take(2)))
                        remaining = remaining.drop(2)
                    }
                    else -> {
                        tokens.add(Token(remaining))
                        remaining = ""
                    }
                }
            }
        }
        return tokens
    }
}

/**
 * 바이트 기준 토큰화
 */
data class ByteTokenizer(override val info: TokenizerInfo) : Tokenizer {
    override fun doTokenize(text: String): List<Token> {
        return text.toByteArray(Charsets.UTF_8).map { byte ->
            Token(String.format("%02X", byte))
        }
    }
}

/**
 * 길이 기준 토큰화
 */
data class LengthTokenizer(
    override val info: TokenizerInfo, 
    private val length: Int = 4
) : Tokenizer {
    override fun doTokenize(text: String): List<Token> {
        val cleanText = text.replace(Regex("\\s+"), " ")
        return (0 until cleanText.length step length).map { i ->
            Token(cleanText.substring(i, minOf(i + length, cleanText.length)))
        }
    }
    
    override fun getMetadata(tokens: List<Token>): Map<String, Any> {
        return mapOf("chunk_length" to length)
    }
}

/**
 * 빈도 기반 토큰화
 */
data class FrequencyTokenizer(override val info: TokenizerInfo) : Tokenizer {
    override fun doTokenize(text: String): List<Token> {
        val words = text.split(Regex("\\s+")).filter { it.isNotEmpty() }
            .map { it.replace(Regex("[,.!?;:()\\[\\]{}\"'-]"), "") }
            .filter { it.isNotEmpty() }
        
        val frequency = mutableMapOf<String, Int>()
        for (word in words) {
            frequency[word] = frequency.getOrDefault(word, 0) + 1
        }
        
        return frequency.toList()
            .sortedByDescending { it.second }
            .map { (word, freq) -> Token(word, freq) }
    }
    
    override fun getMetadata(tokens: List<Token>): Map<String, Any> {
        val totalWords = tokens.sumOf { it.score?.toInt() ?: 0 }
        return mapOf("total_words" to totalWords, "unique_words" to tokens.size)
    }
}

/**
 * TF-IDF 기반 토큰화
 */
data class TFIDFTokenizer(override val info: TokenizerInfo) : Tokenizer {
    override fun doTokenize(text: String): List<Token> {
        val sentences = text.split(Regex("[.!?]+")).filter { it.trim().isNotEmpty() }
        if (sentences.isEmpty()) return emptyList()
        
        val allWords = mutableSetOf<String>()
        val sentenceWords = sentences.map { sentence ->
            sentence.split(Regex("\\s+")).filter { it.isNotEmpty() }
                .map { it.replace(Regex("[,.!?;:()\\[\\]{}\"'-]"), "") }
                .filter { it.isNotEmpty() }
                .also { words -> allWords.addAll(words) }
        }
        
        val tfidfScores = mutableMapOf<String, Double>()
        
        for (word in allWords) {
            val tf = sentenceWords.flatten().count { it == word }.toDouble()
            val df = sentenceWords.count { it.contains(word) }.toDouble()
            val idf = if (df > 0) kotlin.math.ln(sentences.size / df) else 0.0
            tfidfScores[word] = tf * idf
        }
        
        return tfidfScores.toList()
            .filter { it.second > 0.0 }
            .sortedByDescending { it.second }
            .map { (word, score) -> Token(word, score) }
    }
    
    override fun getMetadata(tokens: List<Token>): Map<String, Any> {
        return mapOf("vocabulary_size" to tokens.size)
    }
}

/**
 * 언어 혼합 토큰화
 */
data class LanguageTokenizer(override val info: TokenizerInfo) : Tokenizer {
    override fun doTokenize(text: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var currentToken = StringBuilder()
        var currentType = ""
        
        for (char in text) {
            val charType = when {
                char in '가'..'힣' || char in 'ㄱ'..'ㅎ' || char in 'ㅏ'..'ㅣ' -> "한글"
                char in 'a'..'z' || char in 'A'..'Z' -> "영어"
                char in '0'..'9' -> "숫자"
                char.isWhitespace() -> "공백"
                else -> "특수문자"
            }
            
            if (charType == "공백") {
                if (currentToken.isNotEmpty()) {
                    tokens.add(Token(currentToken.toString(), type = currentType))
                    currentToken.clear()
                    currentType = ""
                }
            } else if (currentType == "" || currentType == charType) {
                currentToken.append(char)
                currentType = charType
            } else {
                if (currentToken.isNotEmpty()) {
                    tokens.add(Token(currentToken.toString(), type = currentType))
                }
                currentToken.clear()
                currentToken.append(char)
                currentType = charType
            }
        }
        
        if (currentToken.isNotEmpty()) {
            tokens.add(Token(currentToken.toString(), type = currentType))
        }
        
        return tokens
    }
    
    override fun getMetadata(tokens: List<Token>): Map<String, Any> {
        val typeCounts = tokens.groupBy { it.type }.mapValues { it.value.size }
        return mapOf("type_distribution" to typeCounts)
    }
}
