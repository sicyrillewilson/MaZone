package tg.eplcoursandroid.mazone.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tg.eplcoursandroid.mazone.R
import tg.eplcoursandroid.mazone.data.model.Plan

class PlanAdapter(private val plans: List<Plan>) : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    // ViewHolder inner class
    class PlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val idTextView: TextView = itemView.findViewById(R.id.idTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val timeHHMMTextView: TextView = itemView.findViewById(R.id.timeHHMMTextView)
        val unlimitedTimeTextView: TextView = itemView.findViewById(R.id.unlimitedTimeTextView)
        val enabledTextView: TextView = itemView.findViewById(R.id.enabledTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_plan, parent, false)
        return PlanViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        //if (plans.isNotEmpty()) {
            val currentPlan = plans[position]
            holder.descriptionTextView.text = currentPlan.description
            holder.priceTextView.text = currentPlan.price
            holder.idTextView.text = currentPlan.id
            holder.timeTextView.text = currentPlan.time
            holder.timeHHMMTextView.text = currentPlan.timeHHMM
            holder.unlimitedTimeTextView.text = currentPlan.unlimitedTime
            holder.enabledTextView.text = currentPlan.enabled
        //}
    }

    override fun getItemCount(): Int = plans.size
}