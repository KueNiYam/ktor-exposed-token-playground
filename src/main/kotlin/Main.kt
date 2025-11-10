import adapters.primary.CliAdapter

/**
 * 프로그램 진입점
 */
fun main(args: Array<String>) {
    val cliAdapter = CliAdapter()
    cliAdapter.handleCommand(args)
}
