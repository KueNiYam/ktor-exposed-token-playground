import adapters.primary.CliAdapter
import application.usecases.TokenizeUseCase

fun main(args: Array<String>) {
    val tokenizerPort = TokenizeUseCase()
    val cliAdapter = CliAdapter(tokenizerPort)
    cliAdapter.handleCommand(args)
}
