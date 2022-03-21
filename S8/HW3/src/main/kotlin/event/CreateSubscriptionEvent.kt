package event

import domain.Domain
import domain.EventClient
import domain.EventSubscription
import domain.Subscription
import org.bson.Document
import org.bson.types.ObjectId
import java.util.*

class CreateSubscriptionEvent(
    val subscriptionId: ObjectId = ObjectId.get(),
    val clientId: ObjectId,
    val startDate: Date,
    val endDate: Date,
    override val timestamp: Date = Date()
) : Event("CreateSubscription") {
    override fun toBodyDocument(): Document = Document()
        .append(
            "client", EventClient(
                id = clientId
            ).toDocument()
        )
        .append(
            "subscription", EventSubscription(
                id = subscriptionId,
                startDate = startDate,
                endDate = endDate
            ).toDocument()
        )

    override fun apply(domain: Domain) {
        when (domain) {
            is Subscription -> {
                if (domain.id != null
                    || domain.startDate != null
                    || domain.endDate != null
                    || domain.clientId != null
                ) {
                    throw IllegalStateException("Cannot create subscription from non-empty domain $domain")
                }

                domain.apply {
                    id = this@CreateSubscriptionEvent.subscriptionId
                    startDate = this@CreateSubscriptionEvent.startDate
                    endDate = this@CreateSubscriptionEvent.endDate
                    clientId = this@CreateSubscriptionEvent.clientId
                }
            }
        }
    }
}