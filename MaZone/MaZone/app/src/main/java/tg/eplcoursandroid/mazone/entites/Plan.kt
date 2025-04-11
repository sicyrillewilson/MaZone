package tg.eplcoursandroid.mazone.entites

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Plan(
    @PrimaryKey var id : ObjectId? = null,
    var description: String? = null,
    var price: String? = null,
    var identifiant: String? = null,
    var time: String? = null,
    var timeHHMM: String? = null,
    var unlimitedTime: String? = null,
    var enabled: String? = null
) : RealmObject() {

    companion object {
        // noms des champs
        val ID = "id"
        val DESCRIPTION = "description"
        val PRICE = "price"
        val IDENTIFIANT = "identifiant"
        val TIME = "time"
        val TIMEHHMM = "timeHHMM"
        val UNLIMITEDTIME = "unlimitedTime"
        val ENABLED = "enabled"

        // crétion d'une instance
        fun create(realm: Realm,
                   description: String,
                   price: String,
                   identifiant: String,
                   time: String,
                   timeHHMM: String,
                   unlimitedTime: String,
                   enabled: String,
                   id: ObjectId = ObjectId()
        ): ObjectId {
            val plan = realm.createObject(Plan::class.java, id)
            plan.description = description
            plan.price = price
            plan.identifiant = identifiant
            plan.time = time
            plan.timeHHMM = timeHHMM
            plan.unlimitedTime = unlimitedTime
            plan.enabled = enabled

            return  id
        }
    }
}