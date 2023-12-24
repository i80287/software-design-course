package cinemaapp

import kotlinx.serialization.Serializable

@Serializable
data class Ticket(val ticketNum: Int, val sessionNumber: Int, val row: Int, val column: Int)
