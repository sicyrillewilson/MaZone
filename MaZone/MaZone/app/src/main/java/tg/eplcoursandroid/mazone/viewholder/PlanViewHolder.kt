package tg.eplcoursandroid.mazone.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tg.eplcoursandroid.mazone.adapters.PlanAdapter
import tg.eplcoursandroid.mazone.databinding.PlanBinding
import tg.eplcoursandroid.mazone.entites.Plan

class PlanViewHolder  (val ui: PlanBinding): RecyclerView.ViewHolder(ui.root){

    private var onItemClickListener: PlanAdapter.OnItemClickListener? = null

    init {
        itemView.setOnClickListener(this::onClick)
    }

    var plan: Plan?
        get() = null
        set(plan) {
            if (plan == null) return
            ui.description.text = plan.description
            ui.prix.text = plan.price
            ui.id.text = plan.identifiant
            ui.duree.text = plan.time
            ui.heureHHMM.text = plan.timeHHMM
            ui.tempsIllimite.text = plan.unlimitedTime
            ui.active.text = plan.enabled
        }

    fun setOnItemClickListener(onItemClickListener: PlanAdapter.OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    private fun onClick(v: View) {
        onItemClickListener?.onItemClick(bindingAdapterPosition)
    }
}