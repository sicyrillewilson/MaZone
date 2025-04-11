package tg.eplcoursandroid.mazone.data.network

import android.content.Context
import android.os.StrictMode
import java.net.HttpURLConnection
import java.net.URL

object TicketNetwork {
//    fun generateAccount(context: Context) {
//        val apiUrl = "http://192.168.137.1:82/generateaccounts?account=USER&number=2&priceplan=15&type=0&sell=1&autologin=1&inactivity=1&pass=SECRETPASS"
//        val threadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//        StrictMode.setThreadPolicy(threadPolicy)
//
//        try {
//            val url = URL(apiUrl)
//            val connection = url.openConnection() as HttpURLConnection
//            connection.requestMethod = "GET"
//            connection.setRequestProperty("Accept", "application/xml")
//
//            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
//                val inputStream = connection.inputStream
//                val response = inputStream.bufferedReader().use { it.readText() }
//
//                /*// Mise à jour de l'interface utilisateur avec la réponse
//                runOnUiThread {
//                    ui.message.text = "Compte créé : $response"
//                }*/
//                // Appel de parseAndDisplayXmlResponse avec la réponse XML
//                parseAndDisplayXmlResponse(response)
//            } else {
//                runOnUiThread {
//                    ui.message.text = "Erreur lors de la création : ${connection.responseCode}"
//                }
//            }
//            connection.disconnect()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            runOnUiThread {
//                ui.message.text = "Erreur : ${e.message}"
//            }
//        }
//    }
}