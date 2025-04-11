package tg.eplcoursandroid.mazone.entites

data class Plan(
    val id: Int,
    val description: String,
    val price: Double,
    val duration: String // Ex: "1 mois", "1 semaine"
)