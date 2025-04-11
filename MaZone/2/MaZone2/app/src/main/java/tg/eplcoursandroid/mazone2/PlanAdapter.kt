package tg.eplcoursandroid.mazone2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tg.eplcoursandroid.mazone2.data.model.Plan

class PlanAdapter(private val plans: List<Plan>) : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plan, parent, false)
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = plans[position]
        holder.name.text = plan.description ?: "Description non disponible"
        holder.price.text = plan.price ?: "Prix non disponible"
    }

    override fun getItemCount() = plans.size

    class PlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.planDescription)
        val price: TextView = itemView.findViewById(R.id.planPrice)
    }
}