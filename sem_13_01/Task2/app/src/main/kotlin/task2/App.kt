package task2

import java.lang.Exception
import java.nio.file.Path

const val REPORT_STORAGE_FILENAME = "report.json"

fun readString(prompt: String): String {
    print("$prompt\n> ")
    return readlnOrNull() ?: ""
}

fun readUInt(prompt: String): UInt? {
    print("$prompt\n> ")
    return readlnOrNull()?.toUIntOrNull()
}

fun main() {
    val rb: ReportBuilder = ReportBuilder(Report.readReportType())
        .withName(readString(("Input report name or press Enter for empty")))
        .withTask(readString(("Input report task or press Enter for empty")))
        .withAnnotation(readString(("Input report annotation or press Enter for empty")))
        .withHeader(readString(("Input report header or press Enter for empty")))
        .withBody(readString(("Input report body or press Enter for empty")))
        .withAppendix(readString(("Input report appendix or press Enter for empty")))

    val sourcesSize: UInt? = readUInt("Input number of sources or press Enter for empty")
    if (sourcesSize != null) {
        rb.reserveSources(sourcesSize.toInt())
        for (i in 1..sourcesSize.toInt()) {
            rb.addSource(readString("Input $i-th source for the report"))
        }
    }

    val report: Report = rb.build()
    try {
        val dstPath: Path = report.serialize(REPORT_STORAGE_FILENAME)
        println("Serialized report to the $dstPath")
    } catch (ex: Exception) {
        println("An error occured during the report serialization: ${ex.message}")
    }
}
