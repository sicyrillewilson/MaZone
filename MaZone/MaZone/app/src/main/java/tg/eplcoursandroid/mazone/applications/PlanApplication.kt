package tg.eplcoursandroid.mazone.applications

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import tg.eplcoursandroid.mazone.databinding.ActivityMainBinding
import tg.eplcoursandroid.mazone.entites.Plan
import android.os.StrictMode
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class PlanApplication : Application() {

    /*lateinit var realm: Realm*/

    lateinit var ui: ActivityMainBinding

    override fun onCreate() {
        super.onCreate()
        Realm.init (this)
        val config = RealmConfiguration.Builder()
            .name("my-realm")
            .deleteRealmIfMigrationNeeded()
            .compactOnLaunch()
            .build()
        // set this config as the default realm
        Realm.setDefaultConfiguration(config)


        // si nécessaire pour d'autres initialisations , récupérer la base
        val realm = Realm.getDefaultInstance()
        //initXMens(realm)
        if (!estInitialise(realm)) {
            initPlan(realm)
        }
    }

    fun initPlan(realm: Realm) {
            //val apiUrl = "http://192.168.137.1:82/viewaccount?account=USER9&pass=SECRETPASS"
            val apiUrl = "http://192.168.137.1:82/getpriceplans?pass=SECRETPASS"
            val threadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(threadPolicy) // Pour permettre les requêtes réseau sur le thread principal (à éviter en production)

            try {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Accept", "application/xml") // Spécifie que la réponse doit être en XML

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    // Affiche la réponse dans le TextView
                    /*android.os.Handler(android.os.Looper.getMainLooper()).post {
                        ui.message.text = response
                    }*/

                    /*// Affiche la réponse dans le TextView
                    runOnUiThread {
                        ui.message.text = response
                    }*/
                    // Appel de parseAndDisplayGetPlanPrice avec la réponse XML
                    //parseAndDisplayGetPlanPrice(response)

                    val stringBuilder = StringBuilder()

                    try {
                        val factory = DocumentBuilderFactory.newInstance()
                        val builder = factory.newDocumentBuilder()
                        val inputStream = response.byteInputStream()
                        val doc = builder.parse(inputStream)
                        doc.documentElement.normalize()

                        val pricePlanNodes = doc.getElementsByTagName("PricePlan")
                        for (i in 0 until pricePlanNodes.length) {
                            val node = pricePlanNodes.item(i)
                            //val node = pricePlanNodes.item(1)
                            if (node.nodeType == Node.ELEMENT_NODE) {
                                val element = node as Element

                                val description = element.getElementsByTagName("Description").item(0)?.textContent ?: "N/A"
                                val price = element.getElementsByTagName("Price").item(0)?.textContent ?: "N/A"
                                val id = element.getElementsByTagName("ID").item(0)?.textContent ?: "N/A"
                                val time = element.getElementsByTagName("Time").item(0)?.textContent ?: "N/A"
                                val timeHHMM = element.getElementsByTagName("TimeHHMM").item(0)?.textContent ?: "N/A"
                                val unlimitedTime = element.getElementsByTagName("UnlimitedTime").item(0)?.textContent ?: "N/A"
                                val enabled = element.getElementsByTagName("Enabled").item(0)?.textContent ?: "N/A"

                                realm.executeTransactionAsync {
                                    Plan.create(
                                        realm = it,
                                        description = description,
                                        price = price,
                                        identifiant = id,
                                        time = time,
                                        timeHHMM = timeHHMM,
                                        unlimitedTime = unlimitedTime,
                                        enabled = enabled
                                    )

                                }
                                /*// Ajout des informations formatées dans le StringBuilder
                                //stringBuilder.append("Plan de prix #${i + 1} :\n")
                                stringBuilder.append("Plan de prix #${1} :\n")
                                stringBuilder.append("Description : $description\n")
                                stringBuilder.append("Prix : $price\n")
                                stringBuilder.append("ID : $id\n")
                                stringBuilder.append("Durée : $time\n")
                                stringBuilder.append("Heure (HH:MM) : $timeHHMM\n")
                                stringBuilder.append("Temps illimité : $unlimitedTime\n")
                                stringBuilder.append("Activé : $enabled\n")
                                stringBuilder.append("\n")*/
                            }
                        }

                        // Affichage des informations formatées
                        /*android.os.Handler(android.os.Looper.getMainLooper()).post {
                            ui.message.text = stringBuilder.toString()
                        }*/

                    } catch (e: Exception) {
                        e.printStackTrace()
                        /*android.os.Handler(android.os.Looper.getMainLooper()).post {
                            ui.message.text = "Erreur lors de l'analyse du XML : ${e.message}"
                        }*/
                    }

                } else {
                    /*android.os.Handler(android.os.Looper.getMainLooper()).post {
                        ui.message.text = "Erreur : ${connection.responseCode}"
                    }*/
                }
                connection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                /*android.os.Handler(android.os.Looper.getMainLooper()).post {
                    ui.message.text = "Erreur : ${e.message}"
                }*/
            }

    }

    /*
    fun parseAndDisplayGetPlanPrice(xml: String) {
        val stringBuilder = StringBuilder()

        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val inputStream = xml.byteInputStream()
            val doc = builder.parse(inputStream)
            doc.documentElement.normalize()

            val pricePlanNodes = doc.getElementsByTagName("PricePlan")
            //for (i in 0 until pricePlanNodes.length) {
            //val node = pricePlanNodes.item(i)
            val node = pricePlanNodes.item(1)
            if (node.nodeType == Node.ELEMENT_NODE) {
                val element = node as Element

                val description = element.getElementsByTagName("Description").item(0)?.textContent ?: "N/A"
                val price = element.getElementsByTagName("Price").item(0)?.textContent ?: "N/A"
                val id = element.getElementsByTagName("ID").item(0)?.textContent ?: "N/A"
                val time = element.getElementsByTagName("Time").item(0)?.textContent ?: "N/A"
                val timeHHMM = element.getElementsByTagName("TimeHHMM").item(0)?.textContent ?: "N/A"
                val unlimitedTime = element.getElementsByTagName("UnlimitedTime").item(0)?.textContent ?: "N/A"
                val enabled = element.getElementsByTagName("Enabled").item(0)?.textContent ?: "N/A"

                // Ajout des informations formatées dans le StringBuilder
                //stringBuilder.append("Plan de prix #${i + 1} :\n")
                stringBuilder.append("Plan de prix #${1} :\n")
                stringBuilder.append("Description : $description\n")
                stringBuilder.append("Prix : $price\n")
                stringBuilder.append("ID : $id\n")
                stringBuilder.append("Durée : $time\n")
                stringBuilder.append("Heure (HH:MM) : $timeHHMM\n")
                stringBuilder.append("Temps illimité : $unlimitedTime\n")
                stringBuilder.append("Activé : $enabled\n")
                stringBuilder.append("\n")
            }
            //}

            // Affichage des informations formatées
            android.os.Handler(android.os.Looper.getMainLooper()).post {
                ui.message.text = stringBuilder.toString()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            android.os.Handler(android.os.Looper.getMainLooper()).post {
                ui.message.text = "Erreur lors de l'analyse du XML : ${e.message}"
            }
        }
    }*/

    fun convertSecondsToHHMMSS(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    }

    // Verification pour voir si la données ne sont pas déjà initialisées
    private fun estInitialise(realm: Realm): Boolean {
        return realm.where(Plan::class.java).count() > 0
    }
}