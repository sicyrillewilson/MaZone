package tg.eplcoursandroid.mazone.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tg.eplcoursandroid.mazone.R
import tg.eplcoursandroid.mazone.data.model.Ticket

class ActifsUnusedAdapter(private val tickets: List<Ticket>) : RecyclerView.Adapter<ActifsUnusedAdapter.ActifsUnusedAdapterViewHolder>() {

    // ViewHolder inner class
    class ActifsUnusedAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextViewAUN)
        val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextViewAUN)
        val passwordTextView: TextView = itemView.findViewById(R.id.passwordTextViewAUN)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextViewAUN)
        val timeHHMMTextView: TextView = itemView.findViewById(R.id.timeHHMMTextViewAUN)
        val timeLeftTextView: TextView = itemView.findViewById(R.id.timeLeftTextViewAUN)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActifsUnusedAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_actif_unused, parent, false)
        return ActifsUnusedAdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ActifsUnusedAdapterViewHolder, position: Int) {
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