package tg.eplcoursandroid.mazone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tg.eplcoursandroid.mazone.entites.Plan
import tg.eplcoursandroid.mazone.entites.Ticket
import tg.eplcoursandroid.mazone.R

class ViewPagerAdapter(
    private val context: Context,
    private val plans: List<Plan>,
    private val tickets: List<Ticket>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_TYPE_PLAN = 0
    private val ITEM_TYPE_TICKET = 1

    override fun getItemCount(): Int = 2 // Un pour les plans, un pour les tickets

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ITEM_TYPE_PLAN else ITEM_TYPE_TICKET
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)

        return when (viewType) {
            ITEM_TYPE_PLAN -> {
                val view = inflater.inflate(R.layout.item_plan, parent, false)
                PlanViewHolder(view)
            }
            ITEM_TYPE_TICKET -> {
                val view = inflater.inflate(R.layout.item_ticket, parent, false)
                TicketViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PlanViewHolder -> {
                val plan = plans[position]
                holder.planDescription.text = plan.description
            }
            is TicketViewHolder -> {
                val ticket = tickets[position]
                holder.ticketId.text = "Ticket ID: ${ticket.id}"
            }
        }
    }

    // ViewHolder pour Plan
    class PlanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val planDescription: TextView = view.findViewById(R.id.planDescription)
    }

    // ViewHolder pour Ticket
    class TicketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ticketId: TextView = view.findViewById(R.id.ticketId)
    }
}