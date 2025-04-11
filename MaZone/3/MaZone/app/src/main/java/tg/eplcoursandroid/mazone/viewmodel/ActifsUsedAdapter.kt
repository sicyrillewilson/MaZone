package tg.eplcoursandroid.mazone.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tg.eplcoursandroid.mazone.R
import tg.eplcoursandroid.mazone.data.model.Ticket

class ActifsUsedAdapter(private val tickets: List<Ticket>) : RecyclerView.Adapter<ActifsUsedAdapter.TicketFragmentViewHolder>() {

    // ViewHolder inner class
    class TicketFragmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextViewAU)
        val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextViewAU)
        val passwordTextView: TextView = itemView.findViewById(R.id.passwordTextViewAU)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextViewAU)
        val timeHHMMTextView: TextView = itemView.findViewById(R.id.timeHHMMTextViewAU)
        val timeLeftTextView: TextView = itemView.findViewById(R.id.timeLeftTextViewAU)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketFragmentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_actif_used, parent, false)
        return TicketFragmentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TicketFragmentViewHolder, position: Int) {
        val currentTicket = tickets[position]
        holder.descriptionTextView.text = currentTicket.plan.description
        holder.usernameTextView.text = currentTicket.username
        holder.passwordTextView.text = currentTicket.password
        holder.priceTextView.text = currentTicket.plan.price
        holder.timeHHMMTextView.text = currentTicket.plan.timeHHMM
        holder.timeLeftTextView.text = convertSecondsToHHMMSS(currentTicket.timeLeft.toInt())
    }

    fun convertSecondsToHHMMSS(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60

        //return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
        return String.format("%02d:%02d", hours, minutes, remainingSeconds)
    }

    override fun getItemCount(): Int = tickets.size
}