package tg.eplcoursandroid.mazone

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import tg.eplcoursandroid.mazone.adapters.PlanAdapter
import tg.eplcoursandroid.mazone.adapters.TicketAdapter
import tg.eplcoursandroid.mazone.adapters.ViewPagerAdapter
import tg.eplcoursandroid.mazone.databinding.ActivityMainBinding
import tg.eplcoursandroid.mazone.entites.Plan
import tg.eplcoursandroid.mazone.entites.Ticket
import tg.eplcoursandroid.mazone.operations.PlanOperation
import tg.eplcoursandroid.mazone.operations.TicketOperation

class MainActivity : AppCompatActivity() {

    // interface utilisateur
    lateinit var ui: ActivityMainBinding

    private val ticketOperation = TicketOperation()
    private val planOperation = PlanOperation()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        /*// Initialiser les carousels
        val planAdapter = PlanAdapter(planOperation.getAllPlans())
        ui.viewPagerPlans.adapter = planAdapter

        val ticketAdapter = TicketAdapter(ticketOperation.getAllTickets())
        ui.viewPagerTickets.adapter = ticketAdapter

        // Connecter les TabLayouts
        TabLayoutMediator(ui.tabLayoutPlans, ui.viewPagerPlans) { _, _ -> }.attach()
        TabLayoutMediator(ui.tabLayoutTickets, ui.viewPagerTickets) { _, _ -> }.attach()*/

        populatePlansAndTickets()

        setupViewPager()

        //populateSpinners()
    }

    private fun populatePlansAndTickets() {
        // Ajouter des exemples de données
        planOperation.addPlan(Plan(1, "Plan 1 mois", 1000.0, "1 mois"))
        planOperation.addPlan(Plan(2, "Plan 1 semaine", 500.0, "1 semaine"))

        ticketOperation.addTicket(Ticket(1, 1, 1, "2025-01-01", "1 mois"))
    }

    private fun setupViewPager() {
        // Créez un adaptateur pour votre ViewPager2
        val viewPagerAdapter = ViewPagerAdapter(this, planOperation.getAllPlans(), ticketOperation.getAllTickets())
        ui.viewPagerPlans.adapter = viewPagerAdapter

        // Configurez le TabLayout avec le ViewPager2
        TabLayoutMediator(ui.tabLayoutPlans, ui.viewPagerPlans) { tab, position ->
            tab.text = if (position == 0) "Plans" else "Tickets"
        }.attach()
    }

    /*private fun populateSpinners() {
        // Ajouter des exemples de données
        planOperation.addPlan(Plan(1, "Plan 1 mois", 1000.0, "1 mois"))
        planOperation.addPlan(Plan(2, "Plan 1 semaine", 500.0, "1 semaine"))

        ticketOperation.addTicket(Ticket(1, 1, 1, "2025-01-01", "1 mois"))

        // Afficher les plans
        val plans = planOperation.getAllPlans().map { it.description }
        val plansAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, plans)
        ui.spinnerPlans.adapter = plansAdapter

        // Afficher les tickets
        val tickets = ticketOperation.getAllTickets().map { "Ticket ${it.id}" }
        val ticketsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tickets)
        ui.spinnerTickets.adapter = ticketsAdapter
    }*/
}