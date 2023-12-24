package cinemaapp

import kotlin.text.StringBuilder
import kotlinx.datetime.Clock
import kotlin.time.Duration
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

const val CINEMA_HALL_WIDTH = 32
const val CINEMA_HALL_LENGTH = 16

@Serializable
class Session(val start: Instant, val end: Instant, val film: Film) {
    @Serializable
    private data class SeatInfo(var ticket: Ticket? = null, var taken: Boolean = false)

    @Serializable
    private val seats: Array<Array<SeatInfo>> = Array(CINEMA_HALL_LENGTH) { Array(CINEMA_HALL_WIDTH) { SeatInfo() } }

    fun showtime(): Duration = end - start
    
    fun verifySeatCoords(row: Int, column: Int): Boolean =
            row.toUInt() < CINEMA_HALL_LENGTH.toUInt() && column.toUInt() < CINEMA_HALL_WIDTH.toUInt()

    fun onSeatSold(ticket: Ticket, row: Int, column: Int) {
        seats[row][column].ticket = ticket
    }

    fun onSeatTaken(row: Int, column: Int): Boolean {
        val seat: SeatInfo = seats[row][column]
        if (seat.ticket == null) {
            return false
        }

        seat.taken = true
        return true
    }

    fun onSeatReturn(row: Int, column: Int): Boolean {
        if (Clock.System.now() >= start) {
            return false
        }

        val seat: SeatInfo = seats[row][column]
        seat.taken = false
        seat.ticket = null
        return true
    }

    fun onSessionDeleted(ticketsReg: TicketsRegistry) {
        for (seatsRow in seats) {
            for (seat in seatsRow) {
                val ticket: Ticket? =  seat.ticket
                if (ticket != null) {
                    ticketsReg.deleteTicketOnSessionDeleted(ticket.ticketNum)
                }
            }
        }
    }
    
    override fun toString(): String {
        val maxColStrLen: Int = CINEMA_HALL_WIDTH.toString().length
        val maxRowStrLen: Int = CINEMA_HALL_LENGTH.toString().length
        val sepLine: String = "+" +
                              "-".repeat(maxRowStrLen + 2) +
                              "+" +
                              ("-".repeat(maxColStrLen + 2) + "+").repeat(CINEMA_HALL_WIDTH) +
                              "\n"

        val infoLine: String = "' ' - free (not sold seat)\n" +
                               "'@' - sold but not taken seats\n" +
                               "'*' - taken seats\n"
        val sb = StringBuilder(infoLine.length + sepLine.length * (CINEMA_HALL_LENGTH * 2 + 3))
        sb.append(infoLine)

        sb.append(sepLine)

        sb.append('|')
        sb.append(" ".repeat(maxRowStrLen + 2))
        sb.append('|')
        for (j in 0..<CINEMA_HALL_WIDTH) {
            sb.append(' ')
            appendWithSpaces(sb, (j + 1).toString(), maxColStrLen)
            sb.append(" |")
        }
        sb.append('\n')

        sb.append(sepLine)

        // Current implementation relies on this
        assert(maxColStrLen >= 2)
        for (i in 0..<CINEMA_HALL_LENGTH) {
            sb.append("| ")
            appendWithSpaces(sb, (i + 1).toString(), maxRowStrLen)
            sb.append(" |")
            for (j in 0..<CINEMA_HALL_WIDTH) {
                sb.append("  ")
                val seat: SeatInfo = seats[i][j]
                val c: Char = if (seat.ticket == null) ' ' else if (!seat.taken) '@' else '*'
                appendWithSpaces(sb, c, maxColStrLen - 1)
                sb.append(" |")
            }
            sb.append('\n')
        }

        sb.append(sepLine)

        return sb.toString()
    }

    private fun appendWithSpaces(sb: StringBuilder, c: Char, fullLen: Int) {
        sb.append(c)
        val len: Int = fullLen - 1
        if (len > 0) {
            sb.append(sb.append(CharArray(len) { ' ' }))
        }
    }

    private fun appendWithSpaces(sb: StringBuilder, str: String, fullLen: Int) {
        sb.append(str)
        val len: Int = fullLen - str.length
        if (len > 0) {
            sb.append(CharArray(len) { ' ' })
        }
    }
}
