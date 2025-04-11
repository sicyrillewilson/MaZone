package tg.eplcoursandroid.mazone2.api

import retrofit2.http.GET
import tg.eplcoursandroid.mazone2.data.model.Plan
import tg.eplcoursandroid.mazone2.data.model.Ticket

interface ApiService {
    @GET("getpriceplans?pass=SECRETPASS") // Remplacez par l'URL réelle
    suspend fun getPlans(): List<Plan>

    @GET("tickets.xml") // Remplacez par l'URL réelle
    suspend fun getTickets(): List<Ticket>
}