package tg.eplcoursandroid.mazone.entites

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Ticket(
    @PrimaryKey var id : ObjectId? = null,
    var username: String? = null,
    var password: String? = null,
    var timeLeft: String? = null,
    var unlimitedQuota: String? = null,
    var quota: String? = null
) : RealmObject() {

    companion object {
        // noms des champs
        val ID = "id"
        val USERNAME = "username"
        val PASSWORD = "password"
        val TIMELEFT = "timeLeft"
        val UNLIMITEDQUOTA = "unlimitedQuota"
        val QUOTA = "quota"

        // crétion d'une instance
        fun create(realm: Realm,
                   username: String,
                   password: String,
                   timeLeft: String,
                   unlimitedQuota: String,
                   quota: String,

                   id: ObjectId = ObjectId()
        ): ObjectId {
            val ticket = realm.createObject(Ticket::class.java, id)
            ticket.username = username
            ticket.password = password
            ticket.timeLeft = timeLeft
            ticket.unlimitedQuota = unlimitedQuota
            ticket.quota = quota

            return  id
        }
    }
}