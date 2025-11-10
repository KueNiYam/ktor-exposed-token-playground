import adapters.primary.CliAdapter

fun main(args: Array<String>) {
    val cliAdapter = CliAdapter()
    cliAdapter.handleCommand(args)
}
