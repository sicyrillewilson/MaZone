package tg.eplcoursandroid.mazone.data.repository

import android.content.Context
import org.w3c.dom.Element
import org.w3c.dom.Node
import tg.eplcoursandroid.mazone.data.model.Plan
import tg.eplcoursandroid.mazone.database.PlanData
import javax.xml.parsers.DocumentBuilderFactory

object PlanRepository {
    fun parseAndDisplayGetPlanPrice(context: Context, xml: String) {
        var plans : MutableList<Plan> = mutableListOf()

        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val inputStream = xml.byteInputStream()
            val doc = builder.parse(inputStream)
            doc.documentElement.normalize()

            val pricePlanNodes = doc.getElementsByTagName("PricePlan")
            for (i in 0 until pricePlanNodes.length) {
                val node = pricePlanNodes.item(i)
                if (node.nodeType == Node.ELEMENT_NODE) {
                    val element = node as Element

                    var plan = Plan(
                        description = element.getElementsByTagName("Description").item(0)?.textContent.toString() ?: "N/A",
                        price = element.getElementsByTagName("Price").item(0)?.textContent ?: "N/A",
                        id = element.getElementsByTagName("ID").item(0)?.textContent ?: "N/A",
                        time = element.getElementsByTagName("Time").item(0)?.textContent ?: "N/A",
                        timeHHMM = element.getElementsByTagName("TimeHHMM").item(0)?.textContent ?: "N/A",
                        unlimitedTime = element.getElementsByTagName("UnlimitedTime").item(0)?.textContent ?: "N/A",
                        enabled = element.getElementsByTagName("Enabled").item(0)?.textContent ?: "N/A")

                    plans.add(plan)
                }
            }

            PlanData.savePlans(context, plans)  // Sauvegarde la liste des plans
            // Affichage des informations formatées
            /*runOnUiThread {
                ui.message.text = stringBuilder.toString()
                /*val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this)*/
                ui.recyclerView.adapter = PlanAdapter(plans)
            }*/

        } catch (e: Exception) {
            e.printStackTrace()
            /*runOnUiThread {
                ui.message.text = "Erreur lors de l'analyse du XML : ${e.message}"
            }*/
        }
    }
}