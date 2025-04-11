package tg.eplcoursandroid.mazone2.data.model

//data class Plan(
//    val id: String,
//    val description: String,
//    val price: String,
//    val time: String,
//    val timeHHMM: String,
//    val unlimitedTime: String,
//    val enabled: String
//)

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "plan", strict = false)
data class Plan(
    @field:Element(name = "description")
    var description: String? = null,

    @field:Element(name = "price")
    var price: String? = null,

    @field:Element(name = "identifiant")
    var identifiant: String? = null,

    @field:Element(name = "time")
    var time: String? = null,

    @field:Element(name = "timeHHMM")
    var timeHHMM: String? = null,

    @field:Element(name = "unlimitedTime")
    var unlimitedTime: String? = null,

    @field:Element(name = "enabled")
    var enabled: String? = null
)
