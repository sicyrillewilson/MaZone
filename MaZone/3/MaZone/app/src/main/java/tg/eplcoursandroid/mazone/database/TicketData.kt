package tg.eplcoursandroid.mazone.database

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import tg.eplcoursandroid.mazone.data.model.Ticket

object TicketData {
    fun addTicket(context: Context, ticket: Ticket){
        val tickets : MutableList<Ticket> = loadTickets(context).toMutableList()
        tickets.add(ticket)
        saveTickets(context, tickets)
    }

    fun saveTickets(context: Context, tickets: List<Ticket>) {
        val sharedPreferences = context.getSharedPreferences("TicketPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(tickets)
        editor.putString("tickets", json)
        editor.apply()
    }


    fun loadTickets(context: Context): List<Ticket> {
        val sharedPreferences = context.getSharedPreferences("TicketPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("tickets", null)
        val type = object : TypeToken<List<Ticket>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }
}