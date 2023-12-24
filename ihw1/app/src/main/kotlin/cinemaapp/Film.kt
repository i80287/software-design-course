package cinemaapp

import kotlin.text.StringBuilder
import kotlin.time.Duration
import kotlinx.serialization.Serializable
import kotlin.math.max

private const val NAME_COLUMN_NAME = "name"
private const val DESCRIPTION_COLUMN_NAME = "description"
private const val DURATION_COLUMN_NAME = "duration" 

@Serializable
data class Film(var name: String, var description: String, var duration: Duration) {
    override fun toString(): String {
        val durationStr: String = duration.toString()
        val nameColumnLength: Int = max(NAME_COLUMN_NAME.length, name.length)
        val descriptionColumnLength: Int = max(DESCRIPTION_COLUMN_NAME.length, description.length)
        val durationColumnLength: Int = max(DURATION_COLUMN_NAME.length, durationStr.length)
        val sepLine: String = "+-" +
                              "-".repeat(nameColumnLength) +
                              "-+-" +
                              "-".repeat(descriptionColumnLength) +
                              "-+-" +
                              "-".repeat(durationColumnLength) +
                              "-+\n"

        val capacity: Int = sepLine.length * 5
        val sb = StringBuilder(capacity)

        // First line
        sb.append(sepLine)

        // Second line
        appendFilmLine(sb,
                       NAME_COLUMN_NAME,
                       nameColumnLength,
                       DESCRIPTION_COLUMN_NAME,
                       descriptionColumnLength,
                       DURATION_COLUMN_NAME,
                       durationColumnLength)

        // Third line
        sb.append(sepLine)

        // Fourth line
        appendFilmLine(sb,
                       name,
                       nameColumnLength,
                       description,
                       descriptionColumnLength,
                       durationStr,
                       durationColumnLength)

        // Fifth line
        sb.append(sepLine)

        return sb.toString()
    }

    private fun appendFilmLine(sb: StringBuilder,
                          column1: String,
                               column1FullLength: Int,
                          column2: String,
                               column2FullLength: Int,
                          column3: String,
                          column3FullLength: Int) {
        sb.append("| ")

        sb.append(column1)
            val freeAppendLen1: Int = column1FullLength - column1.length
        if (freeAppendLen1 > 0) {
            sb.append(CharArray(freeAppendLen1) {' '})
        }

        sb.append(" | ")

        sb.append(column2)
        val freeAppendLen2: Int = column2FullLength - column2.length
        if (freeAppendLen2 > 0) {
            sb.append(CharArray(freeAppendLen2) {' '})
        }

        sb.append(" | ")

        sb.append(column3)
        val freeAppendLen3: Int = column3FullLength - column3.length
        if (freeAppendLen3 > 0) {
            sb.append(CharArray(freeAppendLen3) {' '})
        }

        sb.append(" |\n")
    }
}
