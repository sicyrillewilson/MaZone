package tg.eplcoursandroid.mazone.data.network

object PlanNetwork {
//    fun getPlans(context: Context) {
//        val apiUrl = "http://192.168.137.1:82/getpriceplans?pass=SECRETPASS"
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
//                requireActivity().runOnUiThread {
//                    binding.message.text = response
//                }
//
//                parseAndDisplayGetPlanPrice(response)
//            } else {
//                requireActivity().runOnUiThread {
//                    binding.message.text = "Erreur : ${connection.responseCode}"
//                }
//            }
//            connection.disconnect()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            requireActivity().runOnUiThread {
//                binding.message.text = "Erreur : ${e.message}"
//            }
//        }
////        //val apiUrl = "http://192.168.137.1:82/viewaccount?account=USER9&pass=SECRETPASS"
////        val apiUrl = "http://192.168.137.1:82/getpriceplans?pass=SECRETPASS"
////        val threadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
////        //var plans : MutableList<Plan> = mutableListOf()
////        StrictMode.setThreadPolicy(threadPolicy) // Pour permettre les requêtes réseau sur le thread principal (à éviter en production)
////
////        try {
////            val url = URL(apiUrl)
////            val connection = url.openConnection() as HttpURLConnection
////            connection.requestMethod = "GET"
////            connection.setRequestProperty("Accept", "application/xml") // Spécifie que la réponse doit être en XML
////
////            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
////                val inputStream = connection.inputStream
////                val response = inputStream.bufferedReader().use { it.readText() }
////
////                // Affiche la réponse dans le TextView
////                /*runOnUiThread {
////                    ui.message.text = response
////                }*/
////
////                // Appel de parseAndDisplayGetPlanPrice avec la réponse XML
////                PlanRepository.parseAndDisplayGetPlanPrice(context, response)
////            } else {
////                /*runOnUiThread {
////                    ui.message.text = "Erreur : ${connection.responseCode}"
////                }*/
////            }
////            connection.disconnect()
////        } catch (e: Exception) {
////            e.printStackTrace()
////            /*runOnUiThread {
////                ui.message.text = "Erreur : ${e.message}"
////            }*/
////        }
//    }
}