package tg.eplcoursandroid.mazone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import tg.eplcoursandroid.mazone.databinding.PlanBinding
import tg.eplcoursandroid.mazone.entites.Plan
import tg.eplcoursandroid.mazone.viewholder.PlanViewHolder

class PlanAdapter (val plans: RealmResults<Plan>) : RecyclerView.Adapter<PlanViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val ui = PlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanViewHolder(ui)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.plan = plans[position]
        holder.setOnItemClickListener(onItemClickListener)
    }

    override fun getItemCount(): Int {
        return plans.count()
    }

    init {
        plans.addChangeListener { _, changeSet ->
            for (change in changeSet.deletionRanges) {
                notifyItemRangeRemoved(change.startIndex, change.length)
            }
            for (change in changeSet.insertionRanges) {
                notifyItemRangeInserted(change.startIndex, change.length)
            }
            for (change in changeSet.changeRanges) {
                notifyItemRangeChanged(change.startIndex, change.length)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }
}