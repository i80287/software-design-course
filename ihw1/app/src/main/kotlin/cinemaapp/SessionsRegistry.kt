package cinemaapp

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.text.StringBuilder

private const val SESSIONS_REGISTRY_FILESTORAGE = "sessions_reg.json"

private const val SESSION_NUM_COLUMN_NAME = "session number"
private const val FILM_NAME_COLUMN_NAME = "film name"
private const val SESSION_START_COLUMN_NAME = "start time"
private const val SESSION_END_COLUMN_NAME = "end time"
private const val SESSION_SHOWTIME_COLUMN_NAME = "showtime"

const val MIN_SESSION_NUMBER = 1

class SessionsRegistry : Registry() {
    private val sessions: HashMap<Int, Session> = deserialize<HashMap<Int, Session>>(SESSIONS_REGISTRY_FILESTORAGE) ?: HashMap()

    override fun saveToFile() {
        serialize(SESSIONS_REGISTRY_FILESTORAGE, sessions)
    }

    private var lastSessionNum: Int = MIN_SESSION_NUMBER - 1

    init {
        for (sessionNum: Int in sessions.keys) {
            if (sessionNum > lastSessionNum) {
                lastSessionNum = sessionNum
            }
        }
    }

    fun addSession(start: Instant, end: Instant, film: Film): Pair<Int, Session>? {
        for (session: Session in sessions.values) {
            if ((session.start <= start && start <= session.end)
                || (session.start <= end && end <= session.end)) {
                return null
            }
        }

        val newSessionNum: Int = ++lastSessionNum
        val session = Session(start, end, film) 
        sessions[newSessionNum] = session
        return Pair(newSessionNum, session)
    }

    fun deleteSession(sessionNum: Int, ticketsReg: TicketsRegistry): Session? {
        val session: Session? = sessions.remove(sessionNum)
        session?.onSessionDeleted(ticketsReg)
        return session
    }

    fun getSession(sessionNum: Int): Session? {
        return sessions[sessionNum]
    }

    override fun toString(): String {
        var maxSessionNumStrLen: Int = SESSION_NUM_COLUMN_NAME.length
        var maxFilmNameLen: Int = FILM_NAME_COLUMN_NAME.length
        var maxStartTimeStrLen: Int = SESSION_START_COLUMN_NAME.length 
        var maxEndTimeStrLen: Int = SESSION_END_COLUMN_NAME.length
        var maxShowtimeStrLen: Int = SESSION_SHOWTIME_COLUMN_NAME.length

        for (pair in sessions) {
            val sessionNum: Int = pair.key
            val session: Session = pair.value

            val sessionNumStrLen: Int = sessionNum.toString().length
            if (sessionNumStrLen > maxSessionNumStrLen) {
                maxSessionNumStrLen = sessionNumStrLen
            }

            val startTimeStrLen: Int = session.start.toLocalDateTime(TimeZone.currentSystemDefault()).toString().length
            if (startTimeStrLen > maxStartTimeStrLen) {
                maxStartTimeStrLen = startTimeStrLen
            }

            val endTimeStrLen: Int = session.end.toLocalDateTime(TimeZone.currentSystemDefault()).toString().length
            if (endTimeStrLen > maxEndTimeStrLen) {
                maxEndTimeStrLen = endTimeStrLen
            }

            val showtimeStrLen: Int = session.showtime().toString().length
            if (showtimeStrLen > maxShowtimeStrLen) {
                maxShowtimeStrLen = showtimeStrLen
            }

            val filmNameLen: Int = session.film.name.length
            if (filmNameLen > maxFilmNameLen) {
                maxFilmNameLen = filmNameLen
            }
        }

        val sepLine: String = "+-" +
                              "-".repeat(maxSessionNumStrLen) +
                              "-+-" +
                              "-".repeat(maxFilmNameLen) +
                              "-+-" +
                              "-".repeat(maxStartTimeStrLen) +
                              "-+-" +
                              "-".repeat(maxEndTimeStrLen) +
                              "-+-" +
                              "-".repeat(maxShowtimeStrLen) +
                              "-+\n"

        val sb = StringBuilder(sepLine.length * ((sessions.size + 1) * 2 + 1))
        sb.append(sepLine)
        appendHeader(sb, maxSessionNumStrLen, maxFilmNameLen, maxStartTimeStrLen, maxEndTimeStrLen, maxShowtimeStrLen)
        sb.append(sepLine)
        for (pair in sessions) {
            val sessionNum: Int = pair.key
            val session: Session = pair.value
            appendSessionInfo(sb, sessionNum, session, maxSessionNumStrLen, maxFilmNameLen, maxStartTimeStrLen, maxEndTimeStrLen, maxShowtimeStrLen)
            sb.append(sepLine)
        }

        return sb.toString()
    }

    private fun appendHeader(sb: StringBuilder,
                             maxSessionNumStrLen: Int,
                             maxFilmNameLen: Int,
                             maxStartTimeStrLen: Int,
                             maxEndTimeStrLen: Int,
                             maxShowtimeStrLen: Int) {
        sb.append("| ")
        appendWithSpaces(sb, SESSION_NUM_COLUMN_NAME, maxSessionNumStrLen)
        sb.append(" | ")
        appendWithSpaces(sb, FILM_NAME_COLUMN_NAME, maxFilmNameLen)
        sb.append(" | ")
        appendWithSpaces(sb, SESSION_START_COLUMN_NAME, maxStartTimeStrLen)
        sb.append(" | ")
        appendWithSpaces(sb, SESSION_END_COLUMN_NAME, maxEndTimeStrLen)
        sb.append(" | ")
        appendWithSpaces(sb, SESSION_SHOWTIME_COLUMN_NAME, maxShowtimeStrLen)
        sb.append(" |\n")
    }

    private  fun appendSessionInfo(sb: StringBuilder,
                                   sessionNum: Int,
                                   session: Session,
                                   maxSessionNumStrLen: Int,
                                   maxFilmNameLen: Int,
                                   maxStartTimeStrLen: Int,
                                   maxEndTimeStrLen: Int,
                                   maxShowtimeStrLen: Int) {
        sb.append("| ")
        appendWithSpaces(sb, sessionNum.toString(), maxSessionNumStrLen)
        sb.append(" | ")
        appendWithSpaces(sb, session.film.name, maxFilmNameLen)
        sb.append(" | ")
        appendWithSpaces(sb, session.start.toLocalDateTime(TimeZone.currentSystemDefault()).toString(), maxStartTimeStrLen)
        sb.append(" | ")
        appendWithSpaces(sb, session.end.toLocalDateTime(TimeZone.currentSystemDefault()).toString(), maxEndTimeStrLen)
        sb.append(" | ")
        appendWithSpaces(sb, session.showtime().toString(), maxShowtimeStrLen)
        sb.append(" |\n")
    }

    private fun appendWithSpaces(sb: StringBuilder, str: String, fullLen: Int) {
        sb.append(str)
        val len: Int = fullLen - str.length
        if (len > 0) {
            sb.append(CharArray(len) { ' ' })
        }
    }
}
