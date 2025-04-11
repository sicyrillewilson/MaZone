package tg.eplcoursandroid.mazone.ui

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.w3c.dom.Element
import org.w3c.dom.Node
import tg.eplcoursandroid.mazone.data.model.Plan
import tg.eplcoursandroid.mazone.database.PlanData
import tg.eplcoursandroid.mazone.databinding.ActivityMain2Binding
import tg.eplcoursandroid.mazone.viewmodel.PlanAdapter
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class AccueilFragment2 : Fragment() {

    private var _binding: ActivityMain2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityMain2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Charger les plans sauvegardés
        val plans: MutableList<Plan> = PlanData.loadPlans(requireContext()).toMutableList()

        // Configurer le RecyclerView avec les plans chargés
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = PlanAdapter(plans)
        binding.recyclerView.setHasFixedSize(true)

        binding.buttonRefresh.setOnClickListener { onButtonRefresh() }
    }

    private fun onButtonRefresh() {
        getPlans()
    }

    fun getPlans() : MutableList<Plan> {
        val apiUrl = "http://192.168.137.1:82/getpriceplans?pass=SECRETPASS"
        val threadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(threadPolicy)
        var plans: MutableList<Plan> = mutableListOf()

        try {
            val url = URL(apiUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/xml")

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }

                requireActivity().runOnUiThread {
                    binding.message.text = response
                }

                plans = parseAndDisplayGetPlanPrice(response)
                return plans

            } else {
                requireActivity().runOnUiThread {
                    binding.message.text = "Erreur : ${connection.responseCode}"
                }
            }
            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
            requireActivity().runOnUiThread {
                binding.message.text = "Erreur : ${e.message}"
            }
        }
        return plans
    }

    private fun parseAndDisplayGetPlanPrice(xml: String): MutableList<Plan> {
        val plans: MutableList<Plan> = mutableListOf()

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

                    val plan = Plan(
                        description = element.getElementsByTagName("Description").item(0)?.textContent ?: "N/A",
                        price = element.getElementsByTagName("Price").item(0)?.textContent ?: "N/A",
                        id = element.getElementsByTagName("ID").item(0)?.textContent ?: "N/A",
                        time = element.getElementsByTagName("Time").item(0)?.textContent ?: "N/A",
                        timeHHMM = element.getElementsByTagName("TimeHHMM").item(0)?.textContent ?: "N/A",
                        unlimitedTime = element.getElementsByTagName("UnlimitedTime").item(0)?.textContent ?: "N/A",
                        enabled = element.getElementsByTagName("Enabled").item(0)?.textContent ?: "N/A"
                    )

                    plans.add(plan)
                }
            }

            PlanData.savePlans(requireContext(), plans)
            binding.recyclerView.adapter = PlanAdapter(plans)
            requireActivity().runOnUiThread {
                binding.message.text = "Plans updated successfully"
            }

            return plans
        } catch (e: Exception) {
            e.printStackTrace()
            requireActivity().runOnUiThread {
                binding.message.text = "Erreur lors de l'analyse du XML : ${e.message}"
            }
        }

        return plans
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}