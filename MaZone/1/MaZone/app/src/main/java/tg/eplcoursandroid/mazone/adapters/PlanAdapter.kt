package tg.eplcoursandroid.mazone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tg.eplcoursandroid.mazone.R
import tg.eplcoursandroid.mazone.entites.Plan

class PlanAdapter(private val plans: List<Plan>) : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    class PlanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val description: TextView = view.findViewById(R.id.planDescription)
        val price: TextView = view.findViewById(R.id.planPrice)
        val duration: TextView = view.findViewById(R.id.planDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plan, parent, false)
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = plans[position]
        holder.description.text = plan.description
        holder.price.text = "${plan.price} FCFA"
        holder.duration.text = plan.duration
    }

    override fun getItemCount(): Int = plans.size
}