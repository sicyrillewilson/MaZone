package tg.eplcoursandroid.mazone.operations

import tg.eplcoursandroid.mazone.entites.Plan

class PlanOperation {
    private val plans = mutableListOf<Plan>()

    fun addPlan(plan: Plan) {
        plans.add(plan)
    }

    fun getAllPlans(): List<Plan> = plans
}