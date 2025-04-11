package tg.eplcoursandroid.mazone.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tg.eplcoursandroid.mazone.R
import tg.eplcoursandroid.mazone.data.model.Plan

class TicketsFragmentAdapter(private val plans: List<Plan>,
                             private val onPayClick: (Plan) -> Unit
) : RecyclerView.Adapter<TicketsFragmentAdapter.TicketFragmentViewHolder>() {

    // ViewHolder inner class
    class TicketFragmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextViewPT)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextViewPT)
        val timeHHMMTextView: TextView = itemView.findViewById(R.id.timeHHMMTextViewPT)
        val payerButton: Button = itemView.findViewById(R.id.payerPT)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketFragmentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_plan_ticket, parent, false)
        return TicketFragmentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TicketFragmentViewHolder, position: Int) {
        val currentPlan = plans[position]
        holder.descriptionTextView.text = currentPlan.description
        holder.priceTextView.text = currentPlan.price
        holder.timeHHMMTextView.text = currentPlan.timeHHMM
        holder.payerButton.setOnClickListener {
            onPayClick(currentPlan)
        }
    }

    fun convertSecondsToHHMMSS(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    }

    override fun getItemCount(): Int = plans.size
}