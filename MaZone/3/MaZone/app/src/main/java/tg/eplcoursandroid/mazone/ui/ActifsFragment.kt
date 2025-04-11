package tg.eplcoursandroid.mazone.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.w3c.dom.Element
import org.w3c.dom.Node
import tg.eplcoursandroid.mazone.data.model.Plan
import tg.eplcoursandroid.mazone.data.model.Ticket
import tg.eplcoursandroid.mazone.database.TicketData
import tg.eplcoursandroid.mazone.databinding.FragmentActifsBinding
import tg.eplcoursandroid.mazone.viewmodel.ActifsUnusedAdapter
import tg.eplcoursandroid.mazone.viewmodel.ActifsUsedAdapter
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class ActifsFragment : Fragment() {
    private var _binding: FragmentActifsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActifsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateTickets()

        // Charger les plans sauvegardés
        val ticketsActifsUnused : MutableList<Ticket> = ticketsActifsUnused()
        val ticketsActifsUsed : MutableList<Ticket> = ticketsActifsUsed()

        // Configurer le RecyclerView avec les plans chargés
        binding.recyclerViewUnused.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewUnused.adapter = ActifsUnusedAdapter(ticketsActifsUnused)
        binding.recyclerViewUnused.setHasFixedSize(true)

        // Configurer le RecyclerView avec les plans chargés
        binding.recyclerViewUsed.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewUsed.adapter = ActifsUsedAdapter(ticketsActifsUsed)
        binding.recyclerViewUsed.setHasFixedSize(true)

        binding.login.setOnClickListener(this::onLoginClick)
        binding.logout.setOnClickListener(this::onLogoutClick)
        binding.status.setOnClickListener(this::onStatusClick)

    }

    fun updateTickets() {
        var tickets: MutableList<Ticket> = TicketData.loadTickets(this.requireContext()).toMutableList()
        for (ticket in tickets) {
            viewAccount(ticket)
        }
        TicketData.saveTickets(requireContext(), tickets)
    }
    fun viewAccount (ticket : Ticket) {
        val apiUrl = "http://192.168.137.1:82/viewaccount?account=${ticket.username}&pass=SECRETPASS"
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
                parseAndDisplayViewAccount(response, ticket)
            }
            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun parseAndDisplayViewAccount(xml: String, ticket: Ticket) {
        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val inputStream = xml.byteInputStream()
            val doc = builder.parse(inputStream)
            doc.documentElement.normalize()

            val rootElement = doc.documentElement

            // Parcourir tous les comptes
            val accountNodes = rootElement.getElementsByTagName("Account")
            for (i in 0 until accountNodes.length) {
                val accountNode = accountNodes.item(i)
                if (accountNode.nodeType == Node.ELEMENT_NODE) {
                    val accountElement = accountNode as Element
                    ticket.timeLeft = accountElement.getElementsByTagName("TimeLeft").item(0).textContent ?: "N/A"
                }
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