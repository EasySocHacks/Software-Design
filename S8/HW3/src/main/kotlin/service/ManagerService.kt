package service

import domain.Client
import domain.Subscription
import event.*
import org.bson.types.ObjectId
import java.util.*

class ManagerService(
    private val databaseService: DatabaseService
) {
    fun getClientInfo(clientId: ObjectId): Client {
        val client = Client()
        databaseService.findClientEventsById(clientId).parseEventDocuments().apply(client)

        return client
    }

    fun createClient(
        name: String,
        birthDate: Date,
        timestamp: Date = Date()
    ): Client {
        val documentId = databaseService.appendEvent(
            CreateClientEvent(
                name = name,
                birthDate = birthDate,
                timestamp = timestamp
            )
        )

        val client = Client()
        databaseService.findDocumentById(documentId)!!.parseEventDocument().apply(client)

        return client
    }

    fun getSubscriptionInfo(subscriptionId: ObjectId): Subscription {
        val subscription = Subscription()
        databaseService.findSubscriptionEventsById(subscriptionId).parseEventDocuments().apply(subscription)

        return subscription
    }

    fun createSubscription(
        clientId: ObjectId,
        startDate: Date,
        endDate: Date,
        timestamp: Date = Date()
    ): Subscription {
        val documentId = databaseService.appendEvent(
            CreateSubscriptionEvent(
                clientId = clientId,
                startDate = startDate,
                endDate = endDate,
                timestamp = timestamp
            )
        )

        val subscription = Subscription()
        databaseService.findDocumentById(documentId)!!.parseEventDocument().apply(subscription)

        return subscription
    }

    fun renewSubscription(
        subscriptionId: ObjectId,
        endDate: Date,
        timestamp: Date = Date()
    ): Subscription {
        val subscription = getSubscriptionInfo(subscriptionId)

        if (subscription.endDate!! < endDate) {
            val documentId = databaseService.appendEvent(
                RenewSubscriptionEvent(
                    subscriptionId = subscriptionId,
                    endDate = endDate,
                    timestamp = timestamp
                )
            )

            databaseService.findDocumentById(documentId)!!.parseEventDocument().apply(subscription)
        }


        return subscription
    }
}