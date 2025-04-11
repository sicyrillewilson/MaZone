package tg.eplcoursandroid.mazone.ui

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.provider.Telephony
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.TELEPHONY_SERVICE
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.w3c.dom.Element
import org.w3c.dom.Node
import tg.eplcoursandroid.mazone.data.model.Plan
import tg.eplcoursandroid.mazone.data.model.Ticket
import tg.eplcoursandroid.mazone.data.network.PaymentConfirmationListener
import tg.eplcoursandroid.mazone.database.PlanData
import tg.eplcoursandroid.mazone.database.TicketData
import tg.eplcoursandroid.mazone.databinding.FragmentTicketsBinding
import tg.eplcoursandroid.mazone.viewmodel.PlanAdapter
import tg.eplcoursandroid.mazone.viewmodel.TicketsFragmentAdapter
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class TicketsFragment : Fragment()/*, PaymentConfirmationListener */{
    private var _binding: FragmentTicketsBinding? = null
    private val binding get() = _binding!!
    private var plan : Plan? = null
//    //
//    private val smsReceiver = SmsReceiver()
//    //

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        //
//        smsReceiver.listener = this
//        //

        getPlans()
        // Charger les plans sauvegardés
        val plans: MutableList<Plan> = PlanData.loadPlans(requireContext()).toMutableList()

//        for (plan in plans){
//            Toast.makeText(requireContext(), "${plan.price}", Toast.LENGTH_LONG).show()
//        }

        // Configurer le RecyclerView avec les plans chargés
        //if (plans.isNotEmpty()) {
            binding.recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.recyclerView.adapter = TicketsFragmentAdapter(plans) { plan ->
                payer(plan)
            }
        //}

    }

//    override fun onPaymentConfirmed() {
//        Toast.makeText(requireContext(), "Votre ticket sera bientôt généré.", Toast.LENGTH_LONG).show()
//        generateTicket(plan!!)
//    }

//    override fun onResume() {
//        super.onResume()
//        val smsReceiver = SmsReceiver()
//        smsReceiver.listener = this
//        val intentFilter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
//        requireContext().registerReceiver(smsReceiver, intentFilter)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        requireContext().unregisterReceiver(smsReceiver)
//    }

    fun onPaymentConfirmed() {
        Toast.makeText(requireContext(), "Votre ticket sera bientôt généré.", Toast.LENGTH_LONG).show()
        generateTicket(plan!!)
    }

    private fun getOperatorName(): String? {
        val telephonyManager = ContextCompat.getSystemService(requireContext(), TelephonyManager::class.java)
        return telephonyManager?.networkOperatorName
    }

    fun payer(plan: Plan) {
        this.plan = plan
        val operatorName = getOperatorName()
        val ussdCode = when {
            operatorName?.contains("Moov", ignoreCase = true) == true -> "*155*1*1*96343739*96343739*${plan.price}*1#" // Code pour Moov
            //operatorName?.contains("Moov", ignoreCase = true) == true -> "*155*1*1*96121227*96121227*${plan.price}*1#" // Code pour Moov
            operatorName?.contains("Togocom", ignoreCase = true) == true -> "*145*1*${plan.price}*70903842*2#" // Code pour Togocom
            operatorName?.contains("Yas", ignoreCase = true) == true -> "*145*1*${plan.price}*70903842*2#" // Code pour Yas
            else -> null
        }

        if (ussdCode != null) {
            val intentUSSD = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Uri.encode(ussdCode)))
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), 1)
            } else {
                startActivity(intentUSSD)
            }
        } else {
            Toast.makeText(requireContext(), "Opérateur non pris en charge ou non détecté.", Toast.LENGTH_LONG).show()
        }
        this.onPaymentConfirmed()
    }

    fun generateTicket(plan: Plan) {
        val apiUrl = "http://192.168.137.1:82/generateaccounts?account=USER&number=2&priceplan=${plan.id}&type=0&sell=1&autologin=1&inactivity=1&pass=SECRETPASS"
        val threadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(threadPolicy)

        try {
            val url = URL(apiUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/xml")

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }

                parseAndDisplayXmlResponse(response, plan)
            }
            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun parseAndDisplayXmlResponse(xml: String, plan: Plan) {
        var ticket: Ticket? = null

        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val inputStream = xml.byteInputStream()
            val doc = builder.parse(inputStream)
            doc.documentElement.normalize()

            val accountNodes = doc.getElementsByTagName("Account")
            val node = accountNodes.item(1)
            if (node.nodeType == Node.ELEMENT_NODE) {
                val element = node as Element

                ticket = Ticket(
                username = element.getElementsByTagName("Username").item(0).textContent,
                password = element.getElementsByTagName("Password").item(0).textContent,
                timeLeft = element.getElementsByTagName("TimeLeft").item(0).textContent,
                unlimitedQuota = element.getElementsByTagName("UnlimitedQuota").item(0).textContent,
                quota = element.getElementsByTagName("Quota").item(0).textContent,
                plan = plan)
            }

            if (ticket != null) {
                TicketData.addTicket(requireContext(),ticket)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /*fun getPlans() : MutableList<Plan> {
        //Toast.makeText(requireContext(), "try préstarting", Toast.LENGTH_LONG).show()
        val apiUrl = "http://192.168.137.1:82/getpriceplans?pass=SECRETPASS"
        val threadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(threadPolicy)
        var plans: MutableList<Plan> = mutableListOf()

        try {
            val url = URL(apiUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/xml")
            //Toast.makeText(requireContext(), "try starting", Toast.LENGTH_LONG).show()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                Toast.makeText(requireContext(), "try responsing", Toast.LENGTH_LONG).show()
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }

                plans = parseAndDisplayGetPlanPrice(response)
                return plans

            }
            connection.disconnect()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "try crashing", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
        return plans
    }*/

    fun getPlans() : MutableList<Plan> {
        val apiUrl = "http://192.168.137.1:82/getpriceplans?pass=SECRETPASS"
        val plans: MutableList<Plan> = mutableListOf()
        val handler = Handler()
        val timeout = 5000L // 5 seconds timeout

        val thread = Thread {
            try {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Accept", "application/xml")
                connection.connectTimeout = timeout.toInt()
                connection.readTimeout = timeout.toInt()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    val fetchedPlans = parseAndDisplayGetPlanPrice(response)
                    plans.addAll(fetchedPlans)
                }
                connection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        handler.postDelayed({
            if (thread.isAlive) {
                thread.interrupt()
                Toast.makeText(requireContext(), "Veillez-vous connecter au Wifi Zone !", Toast.LENGTH_LONG).show()
            }
        }, timeout)

        thread.start()

        try {
            thread.join() // Wait for the thread to finish
        } catch (e: InterruptedException) {
            e.printStackTrace()
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

            return plans
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return plans
    }
}