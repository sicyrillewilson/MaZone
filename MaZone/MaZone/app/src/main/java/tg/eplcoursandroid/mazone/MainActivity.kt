package tg.eplcoursandroid.mazone

import android.R
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.RealmResults
import tg.eplcoursandroid.mazone.adapters.PlanAdapter
import tg.eplcoursandroid.mazone.applications.PlanApplication
import tg.eplcoursandroid.mazone.databinding.ActivityMainBinding
import tg.eplcoursandroid.mazone.entites.Plan
import tg.eplcoursandroid.mazone.operations.PlanOperation
import tg.eplcoursandroid.mazone.operations.TicketOperation

class MainActivity : AppCompatActivity() {

    // interface utilisateur
    lateinit var ui: ActivityMainBinding

    private val ticketOperation = TicketOperation()
    private val planOperation = PlanOperation()

    // représentation de la base de données Realm
    private lateinit var realm: Realm

    //Liste et Adaptateur privé
    private lateinit var plans: RealmResults<Plan>
    private lateinit var adapter: PlanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        // obtention de realm
        realm = Realm.getDefaultInstance()

        // obtenir la liste des xmens
        plans = realm.where(Plan::class.java).findAllAsync()

        // Ajouter un observateur pour vérifier quand les données sont chargées
        plans.addChangeListener { results ->
            // Afficher le nombre de plans chargés dans les logs
            Log.d("MainActivity", "Plans loaded: ${results.size}")
        }

        // créer l'adaptateur
        adapter = PlanAdapter(this.plans)

        // fournir l'adaptateur au recycler
        ui.recycler.adapter = adapter

        // dimensions constantes
        ui.recycler.setHasFixedSize(true)


        // layout manager
        val Im: RecyclerView.LayoutManager = LinearLayoutManager(this)
        ui.recycler.layoutManager = Im

        /*
        // layout manager grille d'empilement
        val sglm  = StaggeredGridLayoutManager(
                2,  // colonnes
                RecyclerView.VERTICAL
            ) // empilement
        ui.recycler.layoutManager = sglm
         */

        // séparateur
        val dividerItemDecoration = DividerItemDecoration(this,
            DividerItemDecoration.VERTICAL
        )
        ui.recycler.addItemDecoration(dividerItemDecoration)

    }

    override fun onDestroy(){
        realm.close()
        super.onDestroy()
    }

    /*private fun onItemClick(position: Int) {
        // Récupérer le X-Men concerné
        val xmen = plans[position]

        // Changer l'image du X-Men
        if (xmen != null) {
            xmen.idImage = R.drawable.undef
        }

        // Signaler à l'adaptateur que l'élément a changé
        adapter.notifyItemChanged(position)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }*/

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val application = application as PlanApplication
        // selon l'item sélectionné
        val iditem = item.itemId
        if (iditem == R.id.reinit) {
            // vider la liste
            application.initPlan(realm)
            plans = realm.where(Plan::class.java).findAllAsync()  // Recharge les données de la base
            adapter.notifyDataSetChanged()  // Notifie l'adaptateur que les données ont changé
            return true
        }
        if (iditem == R.id.create) {
            // TODO voir exercice suivant
            return true
        }
        return super.onOptionsItemSelected(item)
    }*/

}