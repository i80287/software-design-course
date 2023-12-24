package cinemaapp

import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime
import kotlinx.datetime.TimeZone
import java.lang.Exception
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class App {
    private val sessionsReg = SessionsRegistry()
    private val ticketsReg = TicketsRegistry()
    private val staffReg = StaffRegistry()
    private val filmsReg = FilmsRegistry()
    private val interactor = UserInteractor(staffReg)

    private enum class FilmEditOptions(val value: Int) { EditName(1), EditDescription(2), EditDuration(3), DeleteFilm(4) }

    fun run() {
        if (!interactor.loginOrRegisterUser()) {
            exitCommand()
            return
        }

        while (true) {
            when (interactor.nextCommand()) {
                UserInteractor.UserCommand.SellTicket -> sellTicketCommand()
                UserInteractor.UserCommand.ReturnTicket -> returnTicketCommand()
                UserInteractor.UserCommand.ListSeats -> listSeatsCommand()
                UserInteractor.UserCommand.AddFilm -> addFilmCommand()
                UserInteractor.UserCommand.EditFilm -> editFilmCommand()
                UserInteractor.UserCommand.MarkTakenSeat -> markTakenSeatCommand()
                UserInteractor.UserCommand.AddSession -> addSessionCommand()
                UserInteractor.UserCommand.DeleteSession -> deleteSessionCommand()
                UserInteractor.UserCommand.ListSessions -> listSessionsCommand()
                UserInteractor.UserCommand.Exit -> {
                    exitCommand()
                    return
                }
            }
        }
    }

    fun saveStateOnExit() {
        sessionsReg.saveToFile()
        ticketsReg.saveToFile()
        staffReg.saveToFile()
        filmsReg.saveToFile()
    }

    private fun sellTicketCommand() {
        val (sessionNumber: Int, session: Session) = getSession() ?: return
        val (row: Int, column: Int) = getSeatCoords(session) ?: return
        val ticket: Ticket = ticketsReg.newTicket(row, column, sessionNumber)
        session.onSeatSold(ticket, row, column)
        interactor.notify("You marked sold seat in the ${row + 1} row and ${column + 1} column. Ticket number is ${ticket.ticketNum}")
    }

    private fun returnTicketCommand() {
        val ticketNum = interactor.requestIntAtLeast(MIN_TICKET_NUMBER, "Enter ticket number")
        val msg: String = if (ticketsReg.deleteTicket(ticketNum, sessionsReg))
            "You returned ticket with number $ticketNum"
        else
            "Ticket with number $ticketNum does not exist or you can not return it now"
        interactor.notify(msg)
    }

    private fun listSeatsCommand() {
        val (_, session: Session) = getSession() ?: return
        interactor.notify(session.toString())
    } 
    
    private fun addFilmCommand() {
        val filmName: String = interactor.requestNonEmptyString("Enter film name")
        if (filmsReg.getFilm(filmName) != null) {
            interactor.notify("Film \"$filmName\" already exists")
            return
        }

        val filmDescription: String = interactor.requestString("Enter film description (press Enter for empty)")
        val durationInMinutes: Int = interactor.requestPositiveInt("Enter the new film duration in minutes")
        val newFilm: Film = filmsReg.addFilm(filmName, filmDescription, durationInMinutes.minutes)
        interactor.notify("Added film:\n${newFilm}")
    }

    private fun editFilmCommand() {
        val filmName: String = interactor.requestNonEmptyString("Enter film name")
        val film: Film? = filmsReg.getFilm(filmName)
        if (film == null) {
            interactor.notify("Film \"$filmName\" not found")
            return
        }

        interactor.notify("Selected film info:\n${film}")

        assert(FilmEditOptions.EditName.value == 1
               && FilmEditOptions.EditDescription.value == 2
               && FilmEditOptions.EditDuration.value == 3
               && FilmEditOptions.DeleteFilm.value == 4)
        val prompt: String =
            "Edit options:\n" +
            "   ${FilmEditOptions.EditName.value}. Edit name\n" +
            "   ${FilmEditOptions.EditDescription.value}. Edit description\n" +
            "   ${FilmEditOptions.EditDuration.value}. Edit duration\n" +
            "   ${FilmEditOptions.DeleteFilm.value}. Delete this film\n"

        val option: Int = interactor.requestIntBetween(FilmEditOptions.EditName.value, FilmEditOptions.DeleteFilm.value, prompt)
        when(option) {
            FilmEditOptions.EditName.value -> {
                val newName: String = interactor.requestNonEmptyString("Enter new film name")
                if (!filmsReg.renameFilm(film.name, newName)) {
                    interactor.notify("Film with name $newName already exists")
                    return
                }
            }
            FilmEditOptions.EditDescription.value -> {
                film.description = interactor.requestNonEmptyString("Enter new film description")
            }
            FilmEditOptions.EditDuration.value -> {
                val durationInMinutes: Int = interactor.requestPositiveInt("Enter the new film duration in minutes")
                film.duration = durationInMinutes.minutes
            }
            FilmEditOptions.DeleteFilm.value -> {
                filmsReg.deleteFilm(filmName)
                return
            }
        }

        interactor.notify("New film info:\n${film}")
    }

    private fun markTakenSeatCommand() {
        val (_, session: Session) = getSession() ?: return
        val (row: Int, column: Int) = getSeatCoords(session) ?: return
        if (!session.onSeatTaken(row, column)) {
            interactor.notify("This seat is not sold and thus can not be taken")
            return
        }

        interactor.notify("You marked seat at row ${row + 1} and column ${column + 1} as taken")
    }

    private fun addSessionCommand() {
        val start: LocalDateTime? = interactor.requestDate("Enter session start date")
        if (start == null) {
            interactor.notify("Incorrect time format")
            return
        }

        val end: LocalDateTime? = interactor.requestDate("Enter session end date")
        if (end == null) {
            interactor.notify("Incorrect time format")
            return
        }

        if (start >= end) {
            interactor.notify("Start should earlier than end")
            return
        }

        val filmName: String = interactor.requestNonEmptyString("Enter film name")
        val film: Film? = filmsReg.getFilm(filmName)
        if (film == null) {
            interactor.notify("Film \"$filmName\" not found")
            return
        }

        val startInstant: Instant = start
            .toKotlinLocalDateTime()
            .toInstant(TimeZone.currentSystemDefault())
        val endInstant: Instant = end
            .toKotlinLocalDateTime()
            .toInstant(TimeZone.currentSystemDefault())
        val sessionsShowtime: Duration = endInstant - startInstant
        if (film.duration > sessionsShowtime) {
            interactor.notify("Film duration ${film.duration} can not exceed cinema session duration $sessionsShowtime")
            return
        }

        val sessionPair: Pair<Int, Session>?  = sessionsReg.addSession(startInstant, endInstant, film)
        if (sessionPair == null) {
            interactor.notify("Session can not overlap with other sessions (incorrect time)")
            return
        }

        interactor.notify("You added session with number ${sessionPair.first}")
    }

    private fun deleteSessionCommand() {
        val sessionNum: Int = interactor.requestIntAtLeast(MIN_SESSION_NUMBER, "Enter sessions number")
        if (sessionsReg.deleteSession(sessionNum, ticketsReg) == null) {
            interactor.notify("Session with number $sessionNum does not exist")
            return
        }

        interactor.notify("You deleted session with number $sessionNum")
    }

    private fun listSessionsCommand() {
        interactor.notify("Active sessions:\n$sessionsReg")
    }

    private fun exitCommand() {
        interactor.notifyExit()
    }

    private fun getSession(): Pair<Int, Session>? {
        val sessionNum: Int = interactor.requestIntAtLeast(MIN_SESSION_NUMBER, "Enter sessions number")
        val session: Session? = sessionsReg.getSession(sessionNum)
        if (session == null) {
            interactor.notify("Session with number $sessionNum does not exist")
            return null
        }

        return Pair(sessionNum, session)
    }

    private fun getSeatCoords(session: Session): Pair<Int, Int>? {
        val row: Int = interactor.requestPositiveInt("Enter row of the seat") - 1
        val column: Int = interactor.requestPositiveInt("Enter column of the seat") - 1
        if (!session.verifySeatCoords(row, column)) {
            interactor.notify("Incorrect seat coordinates")
            return null
        }

        return Pair(row, column)
    }
}

fun main() {
    val app = App()
    try {
        app.run()
    } catch (ex: Exception) {
        println("An error occured during execution. Exiting app...")
    } finally {
        app.saveStateOnExit()
    }
}
