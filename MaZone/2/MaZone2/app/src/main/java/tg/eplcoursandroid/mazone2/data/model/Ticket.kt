package tg.eplcoursandroid.mazone2.data.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "ticket", strict = false)
data class Ticket(
    @field:Element(name = "username")
    var username: String? = null,

    @field:Element(name = "password")
    var password: String? = null,

    @field:Element(name = "timeLeft")
    var timeLeft: String? = null,

    @field:Element(name = "unlimitedQuota")
    var unlimitedQuota: String? = null,

    @field:Element(name = "quota")
    var quota: String? = null
)
