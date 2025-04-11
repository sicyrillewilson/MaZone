package tg.eplcoursandroid.mazone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tg.eplcoursandroid.mazone.R
import tg.eplcoursandroid.mazone.entites.Ticket

class TicketAdapter(private val tickets: List<Ticket>) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    class TicketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ticketInfo: TextView = view.findViewById(R.id.ticketInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = tickets[position]
        holder.ticketInfo.text = "Ticket ID: ${ticket.id}, Validité: ${ticket.validity}"
    }

    override fun getItemCount(): Int = tickets.size
}
