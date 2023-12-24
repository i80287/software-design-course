package cinemaapp

private const val TICKETS_REGISTRY_FILESTORAGE = "tickets_reg.json"

const val MIN_TICKET_NUMBER = 1

class TicketsRegistry : Registry() {
    private val tickets: HashMap<Int, Ticket> = deserialize<HashMap<Int, Ticket>>(TICKETS_REGISTRY_FILESTORAGE) ?: HashMap()

    private var lastTicketNum: Int = MIN_TICKET_NUMBER - 1

    init {
        for (ticketNum: Int in tickets.keys) {
            if (ticketNum > lastTicketNum) {
                lastTicketNum = ticketNum
            }
        }
    }

    override fun saveToFile() {
        serialize(TICKETS_REGISTRY_FILESTORAGE, tickets)
    }

    fun newTicket(row: Int, column: Int, sessionNumber: Int): Ticket {
        val ticketNum: Int = ++lastTicketNum
        val newTicket = Ticket(ticketNum, sessionNumber, row, column)
        tickets[ticketNum] = newTicket
        return newTicket
    }

    fun deleteTicket(ticketNumber: Int, sessionsReg: SessionsRegistry): Boolean {
        val removedTicket: Ticket = tickets.remove(ticketNumber) ?: return false
        val ticketSession: Session = sessionsReg.getSession(removedTicket.sessionNumber) ?: return false
        return ticketSession.onSeatReturn(removedTicket.row, removedTicket.column)
    }

    fun deleteTicketOnSessionDeleted(ticketNumber: Int) {
        tickets.remove(ticketNumber)
    }
}
