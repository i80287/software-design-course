package task2

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.writeText

import kotlinx.serialization.Serializable

@Serializable
data class Report(val name: String, val type: ReportType, val task: String, val annotation: String, val header: String, val body: String, val sources: ArrayList<String>, val appendix: String) {
    companion object {
        fun readReportType(): ReportType {
            while (true) {
                print("Input report type as a number (press Enter for default 1. Research report): \n" +
                      " 1. Research report\n" +
                      " 2. Course work report\n" +
                      " 3. Measurements report\n" +
                      "> ")
                val input: String? = readlnOrNull()
                if (input.isNullOrEmpty()) {
                    return ReportType.RESEARCH_REPORT
                }
                when (input.toUIntOrNull() ?: continue) {
                    1u -> return ReportType.RESEARCH_REPORT
                    2u -> return ReportType.COURSE_WORK_REPORT
                    3u -> return ReportType.MEASUREMENTS_REPORT
                    else -> println("Expected number 1, 2 or 3. Please, try again")
                }
            }
        }
    }

    fun serialize(fileName: String): Path {
        val dirPath: Path = Paths.get(".")
        if (!dirPath.exists()) {
            Files.createDirectory(dirPath)
        }

        val filePath: Path = dirPath.resolve(fileName)
        filePath.writeText(Json.encodeToString(this))
        return filePath.toRealPath()
    }
}
