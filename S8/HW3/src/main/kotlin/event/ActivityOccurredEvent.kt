package event

import domain.*
import org.bson.Document
import org.bson.types.ObjectId
import java.util.*

class ActivityOccurredEvent(
    val activityId: ObjectId = ObjectId.get(),
    val clientId: ObjectId,
    val subscriptionId: ObjectId,
    val activityType: ActivityType,
    override val timestamp: Date = Date()
) : Event("ActivityOccurred") {
    override fun toBodyDocument(): Document = Document()
        .append(
            "client", EventClient(
                id = clientId
            ).toDocument()
        )
        .append(
            "subscription", EventSubscription(
                id = subscriptionId
            ).toDocument()
        )
        .append(
            "activity", EventActivity(
                type = activityType
            ).toDocument()
        )

    override fun apply(domain: Domain) {
        when (domain) {
            is Activity -> {
                if (domain.id != null
                    || domain.clientId != null
                    || domain.subscriptionId != null
                    || domain.type != null
                ) {
                    throw IllegalStateException("Cannot create activity from non-empty domain $domain")
                }

                domain.apply {
                    id = this@ActivityOccurredEvent.activityId
                    clientId = this@ActivityOccurredEvent.clientId
                    subscriptionId = this@ActivityOccurredEvent.subscriptionId
                    type = this@ActivityOccurredEvent.activityType
                }
            }
        }
    }
}