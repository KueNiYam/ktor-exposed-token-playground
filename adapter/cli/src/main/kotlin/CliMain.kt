import adapters.primary.CliAdapter
import application.usecases.TokenizeUseCase

fun main(args: Array<String>) {
    val tokenizeUseCase = TokenizeUseCase()
    val cliAdapter = CliAdapter(tokenizeUseCase)
    cliAdapter.handleCommand(args)
}
