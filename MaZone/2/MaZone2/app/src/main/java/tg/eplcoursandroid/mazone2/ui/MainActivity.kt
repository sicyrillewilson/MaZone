package tg.eplcoursandroid.mazone2.ui

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tg.eplcoursandroid.mazone2.api.RetrofitInstance
import androidx.lifecycle.lifecycleScope
import tg.eplcoursandroid.mazone2.data.model.Plan
import org.w3c.dom.Element
import org.w3c.dom.Node
import tg.eplcoursandroid.mazone2.PlanAdapter
import tg.eplcoursandroid.mazone2.R
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: PlanAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Charger les plans et tickets depuis l'API
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                // Charger les plans
                val plans = RetrofitInstance.api.getPlans()

                // Adapter pour afficher les plans
                adapter = PlanAdapter(plans)
                recyclerView.adapter = adapter

                // Récupérer les tickets (facultatif)
                val tickets = RetrofitInstance.api.getTickets()
                Log.d("MainActivity", "Tickets fetched: ${tickets.size}")

            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching data", e)
            }
        }

        onBouton2Click()
    }

    fun onBouton2Click() {
        val apiUrl = "http://192.168.137.1:82/getpriceplans?pass=SECRETPASS"
        val threadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(threadPolicy) // À éviter en production

        try {
            val url = URL(apiUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/xml")

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }

                // Appel de parseAndCreatePlanList pour créer la liste des plans
                val plans = parseAndCreatePlanList(response)

                // Mise à jour de l'interface utilisateur pour afficher les plans
                runOnUiThread {
                    setupRecyclerView(plans)
                }
            } else {
                Log.d("MainActivity", "Erreur : ${connection.responseCode}")
                /*runOnUiThread {
                    R.id.planDescription
                    ui.message.text = "Erreur : ${connection.responseCode}"
                }*/
            }
            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
            /*runOnUiThread {
                ui.message.text = "Erreur : ${e.message}"
            }*/
        }
    }

    fun parseAndCreatePlanList(xml: String): List<Plan> {
        val plans = mutableListOf<Plan>()

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

                    val description = element.getElementsByTagName("Description").item(0)?.textContent ?: "N/A"
                    val price = element.getElementsByTagName("Price").item(0)?.textContent ?: "N/A"
                    val id = element.getElementsByTagName("ID").item(0)?.textContent ?: "N/A"
                    val time = element.getElementsByTagName("Time").item(0)?.textContent ?: "N/A"
                    val timeHHMM = element.getElementsByTagName("TimeHHMM").item(0)?.textContent ?: "N/A"
                    val unlimitedTime = element.getElementsByTagName("UnlimitedTime").item(0)?.textContent ?: "N/A"
                    val enabled = element.getElementsByTagName("Enabled").item(0)?.textContent ?: "N/A"

                    // Ajouter un objet Plan à la liste
                    plans.add(
                        Plan(
                            identifiant = id,
                            description = description,
                            price = price,
                            time = time,
                            timeHHMM = timeHHMM,
                            unlimitedTime = unlimitedTime,
                            enabled = enabled
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return plans
    }

    fun setupRecyclerView(plans: List<Plan>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = PlanAdapter(plans)
        recyclerView.adapter = adapter
    }

}