package tg.eplcoursandroid.mazone.data.model

data class Ticket(
    val username: String,
    val password: String,
    var timeLeft: String,
    val unlimitedQuota: String,
    val quota: String,
    val plan: Plan
)