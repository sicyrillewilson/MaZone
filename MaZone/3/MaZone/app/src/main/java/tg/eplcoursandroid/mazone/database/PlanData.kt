package tg.eplcoursandroid.mazone.database

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import tg.eplcoursandroid.mazone.data.model.Plan

object PlanData {
    fun savePlans(context: Context, plans: List<Plan>) {
        val sharedPreferences = context.getSharedPreferences("PlanPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(plans)
        editor.putString("plans", json)
        editor.apply()
    }


    fun loadPlans(context: Context): List<Plan> {
        val sharedPreferences = context.getSharedPreferences("PlanPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("plans", null)
        val type = object : TypeToken<List<Plan>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

}