package tg.eplcoursandroid.mazone.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import tg.eplcoursandroid.mazone.data.network.PaymentConfirmationListener

class SmsReceiver : BroadcastReceiver() {

    var listener: PaymentConfirmationListener? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<*>
                val messages = pdus.map { pdu ->
                    SmsMessage.createFromPdu(pdu as ByteArray, bundle.getString("format"))
                }

                messages.forEach { message ->
                    val sender = message.displayOriginatingAddress // Numéro de l'expéditeur
                    val messageBody = message.messageBody // Contenu du SMS

                    // Debug : Affiche le contenu du SMS dans les logs
                    Log.d("SmsReceiver", "Message reçu de $sender : $messageBody")

                    // Vérification pour Flooz
                    if (messageBody.contains("Transfert reussi", ignoreCase = true) &&
                        messageBody.contains("Txn ID", ignoreCase = true) &&
                        messageBody.contains("Nouveau solde", ignoreCase = true)  &&
                        messageBody.contains("flooz", ignoreCase = true)
                    ) {
                        Toast.makeText(context, "Paiement Flooz confirmé !", Toast.LENGTH_LONG).show()
                        // Appeler une méthode pour signaler que le paiement est confirmé
                        (context.applicationContext as TicketsFragment).onPaymentConfirmed()
                    }

                    // Vérification pour Yas
                    else if (messageBody.contains("Mixx By Yas", ignoreCase = true) &&
                        messageBody.contains("vous avez envoyé", ignoreCase = true)  &&
                        messageBody.contains("nouveau solde", ignoreCase = true)
                    ) {
                        Toast.makeText(context, "Paiement Yas confirmé !", Toast.LENGTH_LONG).show()
                        // Appeler une méthode pour signaler que le paiement est confirmé
                        (context.applicationContext as TicketsFragment).onPaymentConfirmed()
                    }

                    // Paiement non effectué
                    else {
                        Toast.makeText(context, "Paiement non abouti !", Toast.LENGTH_LONG).show()
                        (context.applicationContext as TicketsFragment).onPaymentConfirmed()
                    }
                }
            }
            //listener?.onPaymentConfirmed()
        }
    }
}

