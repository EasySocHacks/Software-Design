package event

import domain.*
import org.bson.Document
import service.getDocument
import java.util.*

abstract class Event(
    private val type: String
) {
    abstract val timestamp: Date

    companion object {
        fun parseDocument(document: Document): Event {
            val event = document.getDocument("event")
            val type = event.getString("type")
            val timestamp = event.getDate("timestamp")
            val body = event.getDocument("body")

            return when (type) {
                "CreateClient" -> {
                    val client = body.getDocument("client").parseDomainDocument() as EventClient
                    CreateClientEvent(
                        clientId = client.id!!,
                        name = client.name!!,
                        birthDate = client.birthDate!!,
                        timestamp = timestamp
                    )
                }

                "CreateSubscription" -> {
                    val client = body.getDocument("client").parseDomainDocument() as EventClient
                    val subscription = body.getDocument("subscription").parseDomainDocument() as EventSubscription
                    CreateSubscriptionEvent(
                        subscriptionId = subscription.id!!,
                        clientId = client.id!!,
                        startDate = subscription.startDate!!,
                        endDate = subscription.endDate!!,
                        timestamp = timestamp
                    )
                }

                "RenewSubscription" -> {
                    val subscription = body.getDocument("subscription").parseDomainDocument() as EventSubscription
                    RenewSubscriptionEvent(
                        subscriptionId = subscription.id!!,
                        endDate = subscription.endDate!!,
                        timestamp = timestamp
                    )
                }

                "ActivityOccurred" -> {
                    val client = body.getDocument("client").parseDomainDocument() as EventClient
                    val subscription = body.getDocument("subscription").parseDomainDocument() as EventSubscription
                    val activity = body.getDocument("activity").parseDomainDocument() as EventActivity
                    ActivityOccurredEvent(
                        activityId = activity.id!!,
                        clientId = client.id!!,
                        subscriptionId = subscription.id!!,
                        activityType = activity.type!!,
                        timestamp = timestamp
                    )
                }

                else ->
                    throw IllegalStateException("Couldn't parse document $document")
            }
        }
    }

    abstract fun toBodyDocument(): Document

    abstract fun apply(domain: Domain)
    fun toDocument(): Document = Document()
        .append(
            "event",
            Document()
                .append("type", type)
                .append("body", toBodyDocument())
                .append("timestamp", timestamp)
        )
}

fun List<Event>.apply(domain: Domain) {
    forEach {
        it.apply(domain)
    }
}

fun List<Document>.parseEventDocuments() = sortedBy { it.getDate("timestamp") }.map { Event.parseDocument(it) }

fun Document.parseEventDocument() = Event.parseDocument(this)