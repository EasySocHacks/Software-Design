package service

import domain.Activity
import domain.ActivityType
import domain.ActivityType.ENTRANCE
import domain.ActivityType.EXIT
import domain.Subscription
import event.ActivityOccurredEvent
import event.apply
import event.parseEventDocument
import event.parseEventDocuments
import org.bson.types.ObjectId
import java.util.*

class TurnstileService(
    private val databaseService: DatabaseService
) {
    private fun activityOccurred(
        clientId: ObjectId,
        subscriptionId: ObjectId,
        activityType: ActivityType,
        timestamp: Date = Date()
    ): Activity {
        val documentId = databaseService.appendEvent(
            ActivityOccurredEvent(
                clientId = clientId,
                subscriptionId = subscriptionId,
                activityType = activityType,
                timestamp = timestamp
            )
        )

        val activity = Activity()
        databaseService.findDocumentById(documentId)!!.parseEventDocument().apply(activity)

        return activity
    }

    fun entrance(
        clientId: ObjectId,
        subscriptionId: ObjectId,
        timestamp: Date = Date()
    ): Activity {
        val subscription = Subscription()
        databaseService.findSubscriptionEventsById(subscriptionId).parseEventDocuments().apply(subscription)

        val now = Date()
        if (subscription.startDate!! <= now && now <= subscription.endDate!!) {
            return activityOccurred(clientId, subscriptionId, ENTRANCE, timestamp)
        }

        return Activity()
    }

    fun exit(
        clientId: ObjectId,
        subscriptionId: ObjectId,
        timestamp: Date = Date()
    ): Activity {
        return activityOccurred(clientId, subscriptionId, EXIT, timestamp)
    }
}