// ANSI 색상 코드
const val RESET = "\u001B[0m"
const val RED = "\u001B[31m"
const val GREEN = "\u001B[32m"
const val YELLOW = "\u001B[33m"
const val BLUE = "\u001B[34m"
const val PURPLE = "\u001B[35m"
const val CYAN = "\u001B[36m"
const val BOLD = "\u001B[1m"

/**
 * 함수 실행 시간을 측정하는 유틸리티 함수
 * @param block 실행할 함수
 * @return 실행 결과와 소요 시간(밀리초)의 Pair
 */
inline fun <T> measureTime(block: () -> T): Pair<T, Long> {
    val startTime = System.currentTimeMillis()
    val result = block()
    val endTime = System.currentTimeMillis()
    return Pair(result, endTime - startTime)
}

/**
 * 프로그램의 진입점. 텍스트 토큰화를 수행하는 메인 함수
 * @param args 명령행 인수 배열. [명령어] [옵션] "텍스트"
 */
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        showHelp()
        return
    }
    
    val command = args[0]
    
    when (command) {
        "tokenize" -> {
            handleTokenizeCommand(args.drop(1))
        }
        "list" -> {
            handleListCommand(args.drop(1))
        }
        "help", "--help", "-h" -> {
            showHelp()
        }
        else -> {
            println("오류: 알 수 없는 명령어 '$command'")
            println("사용 가능한 명령어: tokenize, list, help")
            println("자세한 사용법은 'help' 명령어를 실행하세요.")
        }
    }
}

/**
 * 도움말 출력
 */
fun showHelp() {
    println("=== 텍스트 토큰화 프로그램 ===")
    println()
    println("사용법:")
    println("  tokenize [출력형식] \"텍스트\"  - 텍스트를 토큰화합니다")
    println("  list [출력형식]              - 사용 가능한 토큰화 방법 목록을 출력합니다")
    println("  help                         - 이 도움말을 출력합니다")
    println()
    println("출력형식:")
    println("  text  - 컬러풀한 텍스트 형식 (기본값)")
    println("  json  - JSON 형식")
    println()
    println("예시:")
    println("  tokenize \"안녕하세요!\"")
    println("  tokenize text \"Hello world!\"")
    println("  tokenize json \"안녕하세요!\"")
    println("  list")
    println("  list json")
}

/**
 * tokenize 명령어 처리
 */
fun handleTokenizeCommand(args: List<String>) {
    if (args.isEmpty()) {
        println("오류: 토큰화할 텍스트를 입력해주세요.")
        println("사용법: tokenize [출력형식] \"텍스트\"")
        return
    }
    
    val (outputFormat, input) = if (args.size >= 2 && (args[0] == "text" || args[0] == "json")) {
        Pair(args[0], args.drop(1).joinToString(" "))
    } else {
        Pair("text", args.joinToString(" "))
    }
    
    if (input.isEmpty()) {
        println("오류: 토큰화할 텍스트를 입력해주세요.")
        return
    }
    
    if (outputFormat == "json") {
        outputAsJson(input)
    } else {
        outputAsText(input)
    }
}

/**
 * list 명령어 처리
 */
fun handleListCommand(args: List<String>) {
    val outputFormat = if (args.isNotEmpty() && args[0] == "json") "json" else "text"
    
    if (outputFormat == "json") {
        outputTokenizerListAsJson()
    } else {
        outputTokenizerListAsText()
    }
}

/**
 * 토큰화 방법 목록을 텍스트 형식으로 출력
 */
fun outputTokenizerListAsText() {
    println("${BOLD}${CYAN}=== 사용 가능한 토큰화 방법 ===${RESET}")
    println()
    
    val tokenizers = listOf(
        Triple(1, "공백 기준", "공백 문자로 단어 분리"),
        Triple(2, "단어 기준", "영문자/숫자/한글만 추출"),
        Triple(3, "문자 기준", "각 문자를 개별 토큰으로 분리"),
        Triple(4, "구두점 포함", "구두점을 별도 토큰으로 처리"),
        Triple(5, "서브워드 (BPE)", "GPT처럼 2-3글자 단위로 분할"),
        Triple(6, "음절 기준", "한글 음절 단위로 분리"),
        Triple(7, "N-gram (2-gram)", "연속된 2글자 조합으로 분할"),
        Triple(8, "형태소 기반", "어미 분리 - 세요, 습니다 등"),
        Triple(9, "바이트 기준", "UTF-8 바이트를 16진수로 표현"),
        Triple(10, "정규표현식 기반", "언어별 패턴 매칭으로 분리"),
        Triple(11, "길이 기준", "고정 길이로 균등 분할"),
        Triple(12, "빈도 기반", "단어 출현 빈도로 중요도 계산"),
        Triple(13, "TF-IDF 기반", "단어 중요도 점수로 필터링"),
        Triple(14, "언어 혼합", "한글/영어/숫자/특수문자별 분류")
    )
    
    tokenizers.forEach { (id, name, description) ->
        println("${BOLD}${BLUE}[$id] $name:${RESET} ${YELLOW}$description${RESET}")
    }
    
    println()
    println("${GREEN}총 ${tokenizers.size}가지 토큰화 방법을 지원합니다.${RESET}")
}

/**
 * 토큰화 방법 목록을 JSON 형식으로 출력
 */
fun outputTokenizerListAsJson() {
    val tokenizers = listOf(
        mapOf("id" to 1, "name" to "공백 기준", "description" to "공백 문자로 단어 분리"),
        mapOf("id" to 2, "name" to "단어 기준", "description" to "영문자/숫자/한글만 추출"),
        mapOf("id" to 3, "name" to "문자 기준", "description" to "각 문자를 개별 토큰으로 분리"),
        mapOf("id" to 4, "name" to "구두점 포함", "description" to "구두점을 별도 토큰으로 처리"),
        mapOf("id" to 5, "name" to "서브워드 (BPE)", "description" to "GPT처럼 2-3글자 단위로 분할"),
        mapOf("id" to 6, "name" to "음절 기준", "description" to "한글 음절 단위로 분리"),
        mapOf("id" to 7, "name" to "N-gram (2-gram)", "description" to "연속된 2글자 조합으로 분할"),
        mapOf("id" to 8, "name" to "형태소 기반", "description" to "어미 분리 - 세요, 습니다 등"),
        mapOf("id" to 9, "name" to "바이트 기준", "description" to "UTF-8 바이트를 16진수로 표현"),
        mapOf("id" to 10, "name" to "정규표현식 기반", "description" to "언어별 패턴 매칭으로 분리"),
        mapOf("id" to 11, "name" to "길이 기준", "description" to "고정 길이로 균등 분할"),
        mapOf("id" to 12, "name" to "빈도 기반", "description" to "단어 출현 빈도로 중요도 계산"),
        mapOf("id" to 13, "name" to "TF-IDF 기반", "description" to "단어 중요도 점수로 필터링"),
        mapOf("id" to 14, "name" to "언어 혼합", "description" to "한글/영어/숫자/특수문자별 분류")
    )
    
    println("{")
    println("  \"tokenizers\": [")
    
    val tokenizerJsons = tokenizers.map { tokenizer ->
        """    {
      "id": ${tokenizer["id"]},
      "name": "${tokenizer["name"]}",
      "description": "${tokenizer["description"]}"
    }"""
    }
    
    println(tokenizerJsons.joinToString(",\n"))
    println("  ],")
    println("  \"total_count\": ${tokenizers.size}")
    println("}")
}

/**
 * 텍스트 형식으로 출력 (기존 방식)
 */
fun outputAsText(input: String) {
    println("${BOLD}${CYAN}=== 텍스트 토큰화 프로그램 ===${RESET}")
    println("${YELLOW}입력 텍스트:${RESET} $input")
    println()
    
    println("${BOLD}${BLUE}[1] 공백 기준 토큰화:${RESET} ${YELLOW}(공백 문자로 단어 분리)${RESET}")
    val (whitespaceTokens, time1) = measureTime { tokenizeByWhitespace(input) }
    println("   ${GREEN}토큰 개수:${RESET} ${whitespaceTokens.size} ${YELLOW}(${time1}ms)${RESET}")
    println("   ${GREEN}토큰:${RESET} ${formatTokens(whitespaceTokens)}")
    
    println("\n${BOLD}${BLUE}[2] 단어 기준 토큰화:${RESET} ${YELLOW}(영문자/숫자/한글만 추출)${RESET}")
    val (wordTokens, time2) = measureTime { tokenizeByWord(input) }
    println("   ${GREEN}토큰 개수:${RESET} ${wordTokens.size} ${YELLOW}(${time2}ms)${RESET}")
    println("   ${GREEN}토큰:${RESET} ${formatTokens(wordTokens)}")
    
    println("\n${BOLD}${BLUE}[3] 문자 기준 토큰화:${RESET} ${YELLOW}(각 문자를 개별 토큰으로 분리)${RESET}")
    val (charTokens, time3) = measureTime { tokenizeByCharacter(input) }
    println("   ${GREEN}토큰 개수:${RESET} ${charTokens.size} ${YELLOW}(${time3}ms)${RESET}")
    println("   ${GREEN}토큰:${RESET} ${formatTokens(charTokens)}")
    
    println("\n${BOLD}${BLUE}[4] 구두점 포함 토큰화:${RESET} ${YELLOW}(구두점을 별도 토큰으로 처리)${RESET}")
    val (punctuationTokens, time4) = measureTime { tokenizeWithPunctuation(input) }
    println("   ${GREEN}토큰 개수:${RESET} ${punctuationTokens.size} ${YELLOW}(${time4}ms)${RESET}")
    println("   ${GREEN}토큰:${RESET} ${formatTokens(punctuationTokens)}")
    
    println("\n${BOLD}${BLUE}[5] 서브워드 토큰화 (BPE 스타일):${RESET} ${YELLOW}(GPT처럼 2-3글자 단위로 분할)${RESET}")
    val (subwordTokens, time5) = measureTime { tokenizeSubword(input) }
    println("   ${GREEN}토큰 개수:${RESET} ${subwordTokens.size} ${YELLOW}(${time5}ms)${RESET}")
    println("   ${GREEN}토큰:${RESET} ${formatTokens(subwordTokens)}")
    
    println("\n${BOLD}${BLUE}[6] 음절 기준 토큰화:${RESET} ${YELLOW}(한글 음절 단위로 분리)${RESET}")
    val (syllableTokens, time6) = measureTime { tokenizeBySyllable(input) }
    println("   ${GREEN}토큰 개수:${RESET} ${syllableTokens.size} ${YELLOW}(${time6}ms)${RESET}")
    println("   ${GREEN}토큰:${RESET} ${formatTokens(syllableTokens)}")
    
    println("\n${BOLD}${BLUE}[7] N-gram 토큰화 (2-gram):${RESET} ${YELLOW}(연속된 2글자 조합으로 분할)${RESET}")
    val (ngramTokens, time7) = measureTime { tokenizeNGram(input, 2) }
    println("   ${GREEN}토큰 개수:${RESET} ${ngramTokens.size} ${YELLOW}(${time7}ms)${RESET}")
    println("   ${GREEN}토큰:${RESET} ${formatTokens(ngramTokens)}")
    
    println("\n${BOLD}${BLUE}[8] 형태소 기반 토큰화:${RESET} ${YELLOW}(어미 분리 - 세요, 습니다 등)${RESET}")
    val (morphemeTokens, time8) = measureTime { tokenizeMorpheme(input) }
    println("   ${GREEN}토큰 개수:${RESET} ${morphemeTokens.size} ${YELLOW}(${time8}ms)${RESET}")
    println("   ${GREEN}토큰:${RESET} ${formatTokens(morphemeTokens)}")
    
    println("\n${BOLD}${BLUE}[9] 바이트 기준 토큰화:${RESET} ${YELLOW}(UTF-8 바이트를 16진수로 표현)${RESET}")
    val (byteTokens, time9) = measureTime { tokenizeByByte(input) }
    println("   ${GREEN}토큰 개수:${RESET} ${byteTokens.size} ${YELLOW}(${time9}ms)${RESET}")
    println("   ${GREEN}토큰:${RESET} ${formatTokens(byteTokens.take(20))}${if(byteTokens.size > 20) " ${YELLOW}... (${byteTokens.size - 20}개 더)${RESET}" else ""}")
    
    println("\n${BOLD}${BLUE}[10] 정규표현식 기반 토큰화:${RESET} ${YELLOW}(언어별 패턴 매칭으로 분리)${RESET}")
    val (regexTokens, time10) = measureTime { tokenizeByRegex(input) }
    println("   ${GREEN}토큰 개수:${RESET} ${regexTokens.size} ${YELLOW}(${time10}ms)${RESET}")
    println("   ${GREEN}토큰:${RESET} ${formatTokens(regexTokens)}")
    
    println("\n${BOLD}${BLUE}[11] 길이 기반 토큰화:${RESET} ${YELLOW}(고정 길이로 균등 분할)${RESET}")
    val (lengthTokens, time11) = measureTime { tokenizeByLength(input, 4) }
    println("   ${GREEN}토큰 개수:${RESET} ${lengthTokens.size} ${YELLOW}(${time11}ms)${RESET}")
    println("   ${GREEN}토큰:${RESET} ${formatTokens(lengthTokens)}")
    
    println("\n${BOLD}${BLUE}[12] 빈도 기반 토큰화:${RESET} ${YELLOW}(단어 출현 빈도로 중요도 계산)${RESET}")
    val (frequencyTokens, time12) = measureTime { tokenizeByFrequency(input) }
    println("   ${GREEN}토큰 개수:${RESET} ${frequencyTokens.size} ${YELLOW}(${time12}ms)${RESET}")
    println("   ${PURPLE}점수 의미: 괄호 안 숫자는 출현 횟수 (높을수록 자주 나오는 중요한 단어)${RESET}")
    println("   ${GREEN}토큰:${RESET} ${formatFrequencyTokens(frequencyTokens)}")
    
    println("\n${BOLD}${BLUE}[13] TF-IDF 기반 토큰화:${RESET} ${YELLOW}(단어 중요도 점수로 필터링)${RESET}")
    val (tfidfTokens, time13) = measureTime { tokenizeByTFIDF(input) }
    println("   ${GREEN}토큰 개수:${RESET} ${tfidfTokens.size} ${YELLOW}(${time13}ms)${RESET}")
    println("   ${PURPLE}점수 의미: 괄호 안 숫자는 TF-IDF 점수 (높을수록 특별하고 중요한 단어)${RESET}")
    println("   ${GREEN}토큰:${RESET} ${formatTFIDFTokens(tfidfTokens)}")
    
    println("\n${BOLD}${BLUE}[14] 언어 혼합 토큰화:${RESET} ${YELLOW}(한글/영어/숫자/특수문자별 분류)${RESET}")
    val (mixedTokens, time14) = measureTime { tokenizeByLanguage(input) }
    println("   ${GREEN}토큰 개수:${RESET} ${mixedTokens.size} ${YELLOW}(${time14}ms)${RESET}")
    println("   ${PURPLE}분류 의미: 한글(파랑), 영어(초록), 숫자(노랑), 특수문자(빨강)${RESET}")
    println("   ${GREEN}토큰:${RESET} ${formatLanguageTokens(mixedTokens)}")
    
    val totalTime = time1 + time2 + time3 + time4 + time5 + time6 + time7 + time8 + time9 + time10 + time11 + time12 + time13 + time14
    println("\n${BOLD}${PURPLE}=== 실행 시간 요약 ===${RESET}")
    println("${CYAN}전체 실행 시간: ${totalTime}ms${RESET}")
}

/**
 * 공백 문자를 기준으로 텍스트를 토큰화
 * @param text 토큰화할 입력 텍스트
 * @return 공백으로 분리된 토큰 리스트
 */
fun tokenizeByWhitespace(text: String): List<String> {
    return text.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
}

/**
 * 단어 단위로 텍스트를 토큰화 (영문자, 숫자, 한글만 추출)
 * @param text 토큰화할 입력 텍스트
 * @return 단어 단위로 분리된 토큰 리스트
 */
fun tokenizeByWord(text: String): List<String> {
    return Regex("[a-zA-Z0-9가-힣]+").findAll(text)
        .map { it.value }
        .toList()
}

/**
 * 문자 단위로 텍스트를 토큰화
 * @param text 토큰화할 입력 텍스트
 * @return 각 문자를 개별 토큰으로 분리한 리스트
 */
fun tokenizeByCharacter(text: String): List<String> {
    return text.map { it.toString() }
}

/**
 * JSON 형식으로 출력
 */
fun outputAsJson(input: String) {
    println("{")
    println("  \"input\": \"${input.replace("\"", "\\\"")}\",")
    println("  \"tokenization_results\": [")
    
    val results = mutableListOf<String>()
    
    // 각 토큰화 방법 실행
    val (whitespaceTokens, time1) = measureTime { tokenizeByWhitespace(input) }
    results.add(createJsonResult(1, "공백 기준", "공백 문자로 단어 분리", whitespaceTokens.map { it }, time1))
    
    val (wordTokens, time2) = measureTime { tokenizeByWord(input) }
    results.add(createJsonResult(2, "단어 기준", "영문자/숫자/한글만 추출", wordTokens.map { it }, time2))
    
    val (charTokens, time3) = measureTime { tokenizeByCharacter(input) }
    results.add(createJsonResult(3, "문자 기준", "각 문자를 개별 토큰으로 분리", charTokens.map { it }, time3))
    
    val (punctuationTokens, time4) = measureTime { tokenizeWithPunctuation(input) }
    results.add(createJsonResult(4, "구두점 포함", "구두점을 별도 토큰으로 처리", punctuationTokens.map { it }, time4))
    
    val (subwordTokens, time5) = measureTime { tokenizeSubword(input) }
    results.add(createJsonResult(5, "서브워드 (BPE)", "GPT처럼 2-3글자 단위로 분할", subwordTokens.map { it }, time5))
    
    val (syllableTokens, time6) = measureTime { tokenizeBySyllable(input) }
    results.add(createJsonResult(6, "음절 기준", "한글 음절 단위로 분리", syllableTokens.map { it }, time6))
    
    val (ngramTokens, time7) = measureTime { tokenizeNGram(input, 2) }
    results.add(createJsonResult(7, "N-gram (2-gram)", "연속된 2글자 조합으로 분할", ngramTokens.map { it }, time7))
    
    val (morphemeTokens, time8) = measureTime { tokenizeMorpheme(input) }
    results.add(createJsonResult(8, "형태소 기반", "어미 분리 - 세요, 습니다 등", morphemeTokens.map { it }, time8))
    
    val (byteTokens, time9) = measureTime { tokenizeByByte(input) }
    results.add(createJsonResult(9, "바이트 기준", "UTF-8 바이트를 16진수로 표현", byteTokens.map { it }, time9))
    
    val (regexTokens, time10) = measureTime { tokenizeByRegex(input) }
    results.add(createJsonResult(10, "정규표현식 기반", "언어별 패턴 매칭으로 분리", regexTokens.map { it }, time10))
    
    val (lengthTokens, time11) = measureTime { tokenizeByLength(input, 4) }
    results.add(createJsonResult(11, "길이 기준", "고정 길이로 균등 분할", lengthTokens.map { it }, time11))
    
    val (frequencyTokens, time12) = measureTime { tokenizeByFrequency(input) }
    results.add(createJsonResultWithScores(12, "빈도 기반", "단어 출현 빈도로 중요도 계산", frequencyTokens, time12))
    
    val (tfidfTokens, time13) = measureTime { tokenizeByTFIDF(input) }
    results.add(createJsonResultWithScores(13, "TF-IDF 기반", "단어 중요도 점수로 필터링", tfidfTokens, time13))
    
    val (mixedTokens, time14) = measureTime { tokenizeByLanguage(input) }
    results.add(createJsonResultWithTypes(14, "언어 혼합", "한글/영어/숫자/특수문자별 분류", mixedTokens, time14))
    
    println(results.joinToString(",\n"))
    
    val totalTime = time1 + time2 + time3 + time4 + time5 + time6 + time7 + time8 + time9 + time10 + time11 + time12 + time13 + time14
    println("  ],")
    println("  \"total_execution_time_ms\": $totalTime")
    println("}")
}

/**
 * 기본 JSON 결과 생성
 */
fun createJsonResult(id: Int, name: String, description: String, tokens: List<String>, timeMs: Long): String {
    val tokensJson = tokens.joinToString(", ") { "\"${it.replace("\"", "\\\"")}\"" }
    return """    {
      "id": $id,
      "name": "$name",
      "description": "$description",
      "token_count": ${tokens.size},
      "execution_time_ms": $timeMs,
      "tokens": [$tokensJson]
    }"""
}

/**
 * 점수가 있는 JSON 결과 생성
 */
fun createJsonResultWithScores(id: Int, name: String, description: String, tokens: List<Pair<String, Number>>, timeMs: Long): String {
    val tokensJson = tokens.joinToString(", ") { (token, score) ->
        "{\"token\": \"${token.replace("\"", "\\\"")}\"," +
        " \"score\": ${if (score is Double) "%.2f".format(score) else score}}"
    }
    return """    {
      "id": $id,
      "name": "$name",
      "description": "$description",
      "token_count": ${tokens.size},
      "execution_time_ms": $timeMs,
      "tokens": [$tokensJson]
    }"""
}

/**
 * 타입이 있는 JSON 결과 생성
 */
fun createJsonResultWithTypes(id: Int, name: String, description: String, tokens: List<Pair<String, String>>, timeMs: Long): String {
    val tokensJson = tokens.joinToString(", ") { (token, type) ->
        "{\"token\": \"${token.replace("\"", "\\\"")}\"," +
        " \"type\": \"$type\"}"
    }
    return """    {
      "id": $id,
      "name": "$name",
      "description": "$description",
      "token_count": ${tokens.size},
      "execution_time_ms": $timeMs,
      "tokens": [$tokensJson]
    }"""
}

/**
 * 토큰 리스트를 가독성 좋게 포맷팅
 * @param tokens 포맷팅할 토큰 리스트
 * @return 따옴표로 감싸고 구분자로 분리된 문자열
 */
fun formatTokens(tokens: List<String>): String {
    return tokens.joinToString(" ${PURPLE}|${RESET} ") { token ->
        when {
            token.isBlank() -> "${YELLOW}\"${token.replace(" ", "␣")}\"${RESET}"
            token.length == 1 && !token[0].isLetterOrDigit() -> "${RED}\"$token\"${RESET}"
            else -> "${CYAN}\"$token\"${RESET}"
        }
    }
}

/**
 * 서브워드 토큰화 (BPE 스타일 - 간단한 구현)
 * @param text 토큰화할 입력 텍스트
 * @return 서브워드 단위로 분리된 토큰 리스트
 */
fun tokenizeSubword(text: String): List<String> {
    val tokens = mutableListOf<String>()
    val words = text.split(Regex("\\s+")).filter { it.isNotEmpty() }
    
    for (word in words) {
        // 간단한 BPE 스타일: 2-3글자씩 분할
        var remaining = word
        while (remaining.isNotEmpty()) {
            when {
                remaining.length >= 3 -> {
                    tokens.add(remaining.take(3))
                    remaining = remaining.drop(3)
                }
                remaining.length >= 2 -> {
                    tokens.add(remaining.take(2))
                    remaining = remaining.drop(2)
                }
                else -> {
                    tokens.add(remaining)
                    remaining = ""
                }
            }
        }
    }
    return tokens
}

/**
 * 음절 기준 토큰화 (한글 특화)
 * @param text 토큰화할 입력 텍스트
 * @return 음절 단위로 분리된 토큰 리스트
 */
fun tokenizeBySyllable(text: String): List<String> {
    val tokens = mutableListOf<String>()
    var currentSyllable = StringBuilder()
    
    for (char in text) {
        when {
            char.isWhitespace() -> {
                if (currentSyllable.isNotEmpty()) {
                    tokens.add(currentSyllable.toString())
                    currentSyllable.clear()
                }
            }
            char in ",.!?;:()[]{}\"'-" -> {
                if (currentSyllable.isNotEmpty()) {
                    tokens.add(currentSyllable.toString())
                    currentSyllable.clear()
                }
                tokens.add(char.toString())
            }
            char in '가'..'힣' -> {
                // 한글 완성형 문자는 하나의 음절
                if (currentSyllable.isNotEmpty() && !currentSyllable.last().isLetter()) {
                    tokens.add(currentSyllable.toString())
                    currentSyllable.clear()
                }
                if (currentSyllable.isNotEmpty()) {
                    tokens.add(currentSyllable.toString())
                    currentSyllable.clear()
                }
                tokens.add(char.toString())
            }
            else -> {
                currentSyllable.append(char)
            }
        }
    }
    
    if (currentSyllable.isNotEmpty()) {
        tokens.add(currentSyllable.toString())
    }
    
    return tokens
}

/**
 * N-gram 기반 토큰화
 * @param text 토큰화할 입력 텍스트
 * @param n N-gram의 크기
 * @return N-gram 단위로 분리된 토큰 리스트
 */
fun tokenizeNGram(text: String, n: Int): List<String> {
    val cleanText = text.replace(Regex("\\s+"), "")
    return (0..cleanText.length - n).map { i ->
        cleanText.substring(i, i + n)
    }
}

/**
 * 형태소 기반 토큰화 (간단한 규칙 기반)
 * @param text 토큰화할 입력 텍스트
 * @return 형태소 단위로 분리된 토큰 리스트
 */
fun tokenizeMorpheme(text: String): List<String> {
    val tokens = mutableListOf<String>()
    val words = text.split(Regex("\\s+")).filter { it.isNotEmpty() }
    
    for (word in words) {
        val cleanWord = word.replace(Regex("[,.!?;:()\\[\\]{}\"'-]"), "")
        when {
            cleanWord.endsWith("습니다") -> {
                tokens.add(cleanWord.dropLast(3))
                tokens.add("습니다")
            }
            cleanWord.endsWith("세요") -> {
                tokens.add(cleanWord.dropLast(2))
                tokens.add("세요")
            }
            cleanWord.endsWith("ing") -> {
                tokens.add(cleanWord.dropLast(3))
                tokens.add("ing")
            }
            cleanWord.endsWith("ed") -> {
                tokens.add(cleanWord.dropLast(2))
                tokens.add("ed")
            }
            else -> tokens.add(cleanWord)
        }
        
        // 구두점 추가
        val punctuation = word.filter { it in ",.!?;:()[]{}\"'-" }
        if (punctuation.isNotEmpty()) {
            tokens.addAll(punctuation.map { it.toString() })
        }
    }
    
    return tokens.filter { it.isNotEmpty() }
}

/**
 * 바이트 기준 토큰화
 * @param text 토큰화할 입력 텍스트
 * @return 바이트 단위로 분리된 토큰 리스트
 */
fun tokenizeByByte(text: String): List<String> {
    return text.toByteArray(Charsets.UTF_8).map { byte ->
        String.format("%02X", byte)
    }
}

/**
 * 정규표현식 기반 토큰화
 * @param text 토큰화할 입력 텍스트
 * @return 정규표현식 패턴으로 분리된 토큰 리스트
 */
fun tokenizeByRegex(text: String): List<String> {
    val patterns = listOf(
        Regex("[가-힣]+"),      // 한글
        Regex("[a-zA-Z]+"),     // 영문
        Regex("\\d+"),          // 숫자
        Regex("[,.!?;:]+"),     // 구두점
        Regex("\\s+")           // 공백
    )
    
    val tokens = mutableListOf<String>()
    var remaining = text
    
    while (remaining.isNotEmpty()) {
        var matched = false
        for (pattern in patterns) {
            val match = pattern.find(remaining)
            if (match != null && match.range.first == 0) {
                if (match.value.trim().isNotEmpty()) {
                    tokens.add(match.value)
                }
                remaining = remaining.substring(match.range.last + 1)
                matched = true
                break
            }
        }
        if (!matched) {
            tokens.add(remaining.first().toString())
            remaining = remaining.drop(1)
        }
    }
    
    return tokens.filter { it.isNotBlank() }
}

/**
 * 길이 기반 토큰화
 * @param text 토큰화할 입력 텍스트
 * @param length 각 토큰의 길이
 * @return 고정 길이로 분리된 토큰 리스트
 */
fun tokenizeByLength(text: String, length: Int): List<String> {
    val cleanText = text.replace(Regex("\\s+"), " ")
    return (0 until cleanText.length step length).map { i ->
        cleanText.substring(i, minOf(i + length, cleanText.length))
    }
}

/**
 * 빈도 기반 토큰화
 * @param text 토큰화할 입력 텍스트
 * @return 단어와 빈도수의 Pair 리스트
 */
fun tokenizeByFrequency(text: String): List<Pair<String, Int>> {
    val words = text.split(Regex("\\s+")).filter { it.isNotEmpty() }
        .map { it.replace(Regex("[,.!?;:()\\[\\]{}\"'-]"), "") }
        .filter { it.isNotEmpty() }
    
    val frequency = mutableMapOf<String, Int>()
    for (word in words) {
        frequency[word] = frequency.getOrDefault(word, 0) + 1
    }
    
    return frequency.toList().sortedByDescending { it.second }
}

/**
 * TF-IDF 기반 토큰화
 * @param text 토큰화할 입력 텍스트
 * @return 단어와 TF-IDF 점수의 Pair 리스트
 */
fun tokenizeByTFIDF(text: String): List<Pair<String, Double>> {
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
        // TF: 전체 문서에서의 빈도
        val tf = sentenceWords.flatten().count { it == word }.toDouble()
        
        // IDF: 역문서 빈도
        val df = sentenceWords.count { it.contains(word) }.toDouble()
        val idf = if (df > 0) kotlin.math.ln(sentences.size / df) else 0.0
        
        tfidfScores[word] = tf * idf
    }
    
    return tfidfScores.toList()
        .filter { it.second > 0.0 }
        .sortedByDescending { it.second }
}

/**
 * 빈도 토큰 포맷팅
 */
fun formatFrequencyTokens(tokens: List<Pair<String, Int>>): String {
    return tokens.take(10).joinToString(" ${PURPLE}|${RESET} ") { (word, freq) ->
        "${CYAN}\"$word\"${RESET}${YELLOW}($freq)${RESET}"
    }
}

/**
 * TF-IDF 토큰 포맷팅
 */
fun formatTFIDFTokens(tokens: List<Pair<String, Double>>): String {
    return tokens.take(10).joinToString(" ${PURPLE}|${RESET} ") { (word, score) ->
        "${CYAN}\"$word\"${RESET}${YELLOW}(%.2f)${RESET}".format(score)
    }
}

/**
 * 언어 혼합 토큰화 (한글/영어/숫자/특수문자별 분류)
 * @param text 토큰화할 입력 텍스트
 * @return 토큰과 언어 타입의 Pair 리스트
 */
fun tokenizeByLanguage(text: String): List<Pair<String, String>> {
    val tokens = mutableListOf<Pair<String, String>>()
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
                tokens.add(Pair(currentToken.toString(), currentType))
                currentToken.clear()
                currentType = ""
            }
        } else if (currentType == "" || currentType == charType) {
            currentToken.append(char)
            currentType = charType
        } else {
            if (currentToken.isNotEmpty()) {
                tokens.add(Pair(currentToken.toString(), currentType))
            }
            currentToken.clear()
            currentToken.append(char)
            currentType = charType
        }
    }
    
    if (currentToken.isNotEmpty()) {
        tokens.add(Pair(currentToken.toString(), currentType))
    }
    
    return tokens
}

/**
 * 언어 혼합 토큰 포맷팅
 */
fun formatLanguageTokens(tokens: List<Pair<String, String>>): String {
    return tokens.joinToString(" ${PURPLE}|${RESET} ") { (token, type) ->
        val color = when (type) {
            "한글" -> BLUE
            "영어" -> GREEN
            "숫자" -> YELLOW
            "특수문자" -> RED
            else -> CYAN
        }
        "${color}\"$token\"${RESET}"
    }
}

/**
 * 구두점을 별도 토큰으로 분리하여 텍스트를 토큰화
 * @param text 토큰화할 입력 텍스트
 * @return 단어와 구두점이 분리된 토큰 리스트
 */
fun tokenizeWithPunctuation(text: String): List<String> {
    val tokens = mutableListOf<String>()
    val currentToken = StringBuilder()
    
    for (char in text) {
        when {
            char.isWhitespace() -> {
                if (currentToken.isNotEmpty()) {
                    tokens.add(currentToken.toString())
                    currentToken.clear()
                }
            }
            char in ",.!?;:()[]{}\"'-" -> {
                if (currentToken.isNotEmpty()) {
                    tokens.add(currentToken.toString())
                    currentToken.clear()
                }
                tokens.add(char.toString())
            }
            else -> {
                currentToken.append(char)
            }
        }
    }
    
    if (currentToken.isNotEmpty()) {
        tokens.add(currentToken.toString())
    }
    
    return tokens
}
