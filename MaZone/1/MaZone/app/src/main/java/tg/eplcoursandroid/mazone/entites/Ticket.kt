package tg.eplcoursandroid.mazone.entites

data class Ticket(
    val id: Int,
    val userId: Int,
    val planId: Int,
    val creationDate: String,
    val validity: String
)