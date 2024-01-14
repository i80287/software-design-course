package task3

import kotlinx.datetime.Clock

@kotlinx.serialization.ExperimentalSerializationApi
fun main() {
    Logger.log(LogSeverity.INFO, "info source", Clock.System.now(), "info description")
    Logger.log(LogSeverity.WARNING, "warning source", Clock.System.now(), "warning description")
    Logger.log(LogSeverity.ERROR, "error source", Clock.System.now(), "error description")
}
