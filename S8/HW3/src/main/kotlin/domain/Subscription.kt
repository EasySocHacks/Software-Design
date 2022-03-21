package domain

import org.bson.Document
import org.bson.types.ObjectId
import java.util.*

class EventSubscription(
    val id: ObjectId? = ObjectId.get(),
    val startDate: Date? = null,
    val endDate: Date? = null
) : EventDomain("Subscription") {
    override fun toBodyDocument(): Document = Document()
        .appendNotNull("id", id)
        .appendNotNull("startDate", startDate)
        .appendNotNull("endDate", endDate)
}

class Subscription : Domain() {
    var id: ObjectId? = null
    var startDate: Date? = null
    var endDate: Date? = null
    var clientId: ObjectId? = null
    override fun toString(): String {
        return "Subscription(id=$id, startDate=$startDate, endDate=$endDate, clientId=$clientId)"
    }
}