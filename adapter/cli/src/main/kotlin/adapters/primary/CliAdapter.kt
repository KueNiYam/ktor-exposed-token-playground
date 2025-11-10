package adapters.primary

import application.usecases.*
import domain.*

/**
 * CLI 어댑터 - 명령줄 인터페이스 처리
 */
class CliAdapter {
    private val tokenizeUseCase = TokenizeUseCase()
    private val listMethodsUseCase = ListMethodsUseCase()
    
    fun handleCommand(args: Array<String>) {
        if (args.isEmpty()) {
            showHelp()
            return
        }
        
        when (args[0]) {
            "tokenize" -> handleTokenizeCommand(args.drop(1))
            "list" -> handleListCommand(args.drop(1))
            "help", "--help", "-h" -> showHelp()
            else -> {
                println("오류: 알 수 없는 명령어 '${args[0]}'")
                println("사용 가능한 명령어: tokenize, list, help")
                println("자세한 사용법은 'help' 명령어를 실행하세요.")
            }
        }
    }
    
    private fun handleTokenizeCommand(args: List<String>) {
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
    
    private fun handleListCommand(args: List<String>) {
        val outputFormat = if (args.isNotEmpty() && args[0] == "json") "json" else "text"
        
        if (outputFormat == "json") {
            outputMethodsAsJson()
        } else {
            outputMethodsAsText()
        }
    }
    
    private fun outputAsText(input: String) {
        println("${BOLD}${CYAN}=== 텍스트 토큰화 프로그램 ===${RESET}")
        println("${YELLOW}입력 텍스트:${RESET} $input")
        println()
        
        val results = tokenizeUseCase.executeAll(input)
        
        results.forEach { (info, result) ->
            println("${BOLD}${BLUE}[${info.id}] ${info.name}:${RESET} ${YELLOW}(${info.description})${RESET}")
            println("   ${GREEN}토큰 개수:${RESET} ${result.tokens.size} ${YELLOW}(${result.executionTimeMs}ms)${RESET}")
            
            val displayTokens = result.tokens.take(20)
            print("   ${GREEN}토큰:${RESET} ")
            displayTokens.forEach { token ->
                when {
                    token.score != null -> print("${CYAN}\"${token.value}\"${RESET}${YELLOW}(${token.score})${RESET} ")
                    token.type != null -> {
                        val color = when (token.type) {
                            "한글" -> BLUE
                            "영어" -> GREEN
                            "숫자" -> YELLOW
                            "특수문자" -> RED
                            else -> CYAN
                        }
                        print("${color}\"${token.value}\"${RESET} ")
                    }
                    else -> print("${CYAN}\"${token.value}\"${RESET} ")
                }
            }
            if (result.tokens.size > 20) {
                print("${YELLOW}... (${result.tokens.size - 20}개 더)${RESET}")
            }
            println()
            println()
        }
    }
    
    private fun outputAsJson(input: String) {
        val results = tokenizeUseCase.executeAll(input)
        
        println("{")
        println("  \"input\": \"${input.replace("\"", "\\\"")}\",")
        println("  \"results\": [")
        
        val jsonResults = results.map { (info, result) ->
            val tokensJson = result.tokens.joinToString(", ") { token ->
                buildString {
                    append("{\"value\": \"${token.value.replace("\"", "\\\"")}\"")
                    token.score?.let { append(", \"score\": $it") }
                    token.type?.let { append(", \"type\": \"$it\"") }
                    append("}")
                }
            }
            
            """    {
      "id": ${info.id},
      "name": "${info.name}",
      "description": "${info.description}",
      "token_count": ${result.tokens.size},
      "execution_time_ms": ${result.executionTimeMs},
      "tokens": [$tokensJson]
    }"""
        }
        
        println(jsonResults.joinToString(",\n"))
        println("  ]")
        println("}")
    }
    
    private fun outputMethodsAsText() {
        println("${BOLD}${CYAN}=== 사용 가능한 토큰화 방법 ===${RESET}")
        println()
        
        val methods = listMethodsUseCase.execute()
        methods.forEach { info ->
            println("${BOLD}${BLUE}[${info.id}] ${info.name}:${RESET} ${YELLOW}${info.description}${RESET}")
        }
        
        println()
        println("${GREEN}총 ${methods.size}가지 토큰화 방법을 지원합니다.${RESET}")
    }
    
    private fun outputMethodsAsJson() {
        val methods = listMethodsUseCase.execute()
        
        println("{")
        println("  \"methods\": [")
        
        val methodsJson = methods.map { info ->
            """    {
      "id": ${info.id},
      "name": "${info.name}",
      "description": "${info.description}"
    }"""
        }
        
        println(methodsJson.joinToString(",\n"))
        println("  ],")
        println("  \"total_count\": ${methods.size}")
        println("}")
    }
    
    private fun showHelp() {
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
    
    companion object {
        // ANSI 색상 코드
        const val RESET = "\u001B[0m"
        const val RED = "\u001B[31m"
        const val GREEN = "\u001B[32m"
        const val YELLOW = "\u001B[33m"
        const val BLUE = "\u001B[34m"
        const val PURPLE = "\u001B[35m"
        const val CYAN = "\u001B[36m"
        const val BOLD = "\u001B[1m"
    }
}
