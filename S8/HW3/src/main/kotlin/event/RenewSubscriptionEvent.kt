package event

import domain.Domain
import domain.EventSubscription
import domain.Subscription
import org.bson.Document
import org.bson.types.ObjectId
import java.util.*

class RenewSubscriptionEvent(
    val subscriptionId: ObjectId,
    val endDate: Date,
    override val timestamp: Date = Date()
) : Event("RenewSubscription") {
    override fun toBodyDocument(): Document = Document()
        .append(
            "subscription", EventSubscription(
                id = subscriptionId,
                endDate = endDate
            ).toDocument()
        )

    override fun apply(domain: Domain) {
        when (domain) {
            is Subscription -> {
                if (domain.id != subscriptionId) {
                    throw IllegalStateException("Cannot renew subscription with id $subscriptionId when domain id is ${domain.id}")
                }

                if (domain.endDate != null && domain.endDate!! >= endDate) {
                    throw IllegalStateException("Cannot renew subscription to end date $endDate when current end date id ${domain.endDate}")
                }

                domain.apply {
                    endDate = this@RenewSubscriptionEvent.endDate
                }
            }
        }
    }
}