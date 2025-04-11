package tg.eplcoursandroid.mazone.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.w3c.dom.Element
import org.w3c.dom.Node
import tg.eplcoursandroid.mazone.data.model.Plan
import tg.eplcoursandroid.mazone.data.model.Ticket
import tg.eplcoursandroid.mazone.database.PlanData
import tg.eplcoursandroid.mazone.database.TicketData
import tg.eplcoursandroid.mazone.databinding.FragmentAccueilBinding
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class AccueilFragment : Fragment() {

    private var _binding: FragmentAccueilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccueilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val plans: MutableList<Plan> = PlanData.loadPlans(requireContext()).toMutableList()

        var ticketsUtilisable : MutableList<Ticket> = ticketsActifsUnused()
        if (ticketsUtilisable.isEmpty()) {
            ticketsUtilisable = ticketsActifsUsed()
        }

        if (ticketsUtilisable.isNotEmpty()) {
            var ticketCourant: Ticket = ticketsUtilisable.get(0)
            setTicketUtilisable(ticketCourant)
        }

        if (plans.isNotEmpty()) {
            var meilleurPlan: Plan = plans.get(0)
            var abordablePlan: Plan = plans.get(0)
            for (plan in plans) {
                if (meilleurPlan.price.toInt() < plan.price.toInt()) {
                    meilleurPlan = plan
                }
                if (plan.price.toInt() < abordablePlan.price.toInt()) {
                    abordablePlan = plan
                }
            }

            setMeilleurPlan(meilleurPlan)
            setAbordablePlan(abordablePlan)
            binding.payerMO.setOnClickListener{ payerMOClick(meilleurPlan) }
        }
        binding.login.setOnClickListener(this::onLoginClick)
        binding.logout.setOnClickListener(this::onLogoutClick)
        binding.status.setOnClickListener(this::onStatusClick)

    }

    fun setTicketUtilisable(ticketCourant: Ticket) {
        binding.descriptionTextViewAU.setText(ticketCourant.plan.description)
        binding.usernameTextViewAU.setText(ticketCourant.username)
        binding.passwordTextViewAU.setText(ticketCourant.password)
        binding.priceTextViewAU.setText(ticketCourant.plan.price)
        binding.timeHHMMTextViewAU.setText(ticketCourant.plan.timeHHMM)
        binding.timeLeftTextViewAU.setText(convertSecondsToHHMMSS(ticketCourant.timeLeft.toInt()))
    }

    fun convertSecondsToHHMMSS(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60

        //return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
        return String.format("%02d:%02d", hours, minutes, remainingSeconds)
    }

    fun setMeilleurPlan(meilleurPlan: Plan) {
        binding.descriptionTextViewMO.setText(meilleurPlan.description)
        binding.priceTextViewMO.setText(meilleurPlan.price)
        binding.timeHHMMTextViewMO.setText(meilleurPlan.timeHHMM)
    }

    fun setAbordablePlan(meilleurPlan: Plan) {
        binding.descriptionTextViewAO.setText(meilleurPlan.description)
        binding.priceTextViewAO.setText(meilleurPlan.price)
        binding.timeHHMMTextViewAO.setText(meilleurPlan.timeHHMM)
    }

    fun payerMOClick(plan: Plan) {
        payer(plan)
    }

    fun onPaymentConfirmed(plan: Plan) {
        Toast.makeText(requireContext(), "Votre ticket sera bientôt généré.", Toast.LENGTH_LONG).show()
        generateTicket(plan)
    }

    private fun getOperatorName(): String? {
        val telephonyManager = ContextCompat.getSystemService(requireContext(), TelephonyManager::class.java)
        return telephonyManager?.networkOperatorName
    }

    fun payer(plan: Plan) {
        val operatorName = getOperatorName()
        val ussdCode = when {
            operatorName?.contains("Moov", ignoreCase = true) == true -> "*155*1*1*96343739*96343739*${plan.price}*1#" // Code pour Moov
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
        this.onPaymentConfirmed(plan)
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

    fun ticketsActifs(): MutableList<Ticket>{
        val tickets = TicketData.loadTickets(requireContext()).toMutableList()
        val ticketsActifs: MutableList<Ticket> = mutableListOf()

        for (ticket in tickets) {
            if (ticket.timeLeft.toInt() > 0) {
                ticketsActifs.add(ticket)
            }
        }
        return ticketsActifs
    }

    fun ticketsActifsUnused(): MutableList<Ticket>{
        val tickets = ticketsActifs()
        val ticketsActifsUnused: MutableList<Ticket> = mutableListOf()

        for (ticket in tickets) {
            if (ticket.timeLeft.toInt() == ticket.plan.time.toInt()) {
                ticketsActifsUnused.add(ticket)
            }
        }
        return ticketsActifsUnused
    }

    fun ticketsActifsUsed(): MutableList<Ticket>{
        val tickets = ticketsActifs()
        val ticketsActifsUsed: MutableList<Ticket> = mutableListOf()

        for (ticket in tickets) {
            if (ticket.timeLeft.toInt() != ticket.plan.time.toInt()) {
                ticketsActifsUsed.add(ticket)
            }
        }
        return ticketsActifsUsed
    }

    fun onLoginClick(view: View?) {
        val url = "http://192.168.137.1/login"
        openBrowser(url)
    }

    fun onLogoutClick(view: View?) {
        val url = "http://192.168.137.1/logout"
        openBrowser(url)
    }

    fun onStatusClick(view: View?) {
        val url = "http://192.168.137.1:881"
        openBrowser(url)
    }

    private fun openBrowser(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Impossible d'ouvrir l'URL : ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}