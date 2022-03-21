package domain

import org.bson.Document
import org.bson.types.ObjectId

enum class ActivityType {
    ENTRANCE,
    EXIT;

}

fun Document.getActivityType(key: String): ActivityType {
    return ActivityType.valueOf(getString(key))
}

class EventActivity(
    val id: ObjectId? = ObjectId.get(),
    val type: ActivityType? = null
) : EventDomain("Activity") {
    override fun toBodyDocument(): Document = Document()
        .append("id", id)
        .append("type", type.toString())
}

class Activity : Domain() {
    var id: ObjectId? = null
    var clientId: ObjectId? = null
    var subscriptionId: ObjectId? = null
    var type: ActivityType? = null

    override fun toString(): String {
        return "Activity(id=$id, clientId=$clientId, subscriptionId=$subscriptionId, type=$type)"
    }
}