package domain

import org.bson.Document
import service.getDocument

abstract class EventDomain(
    private val type: String
) {
    companion object {
        fun parseDocument(document: Document): EventDomain {
            val type = document.getString("type")
            val body = document.getDocument("body")

            return when (type) {
                "Client" ->
                    EventClient(
                        id = body.getObjectId("id"),
                        name = body.getString("name"),
                        birthDate = body.getDate("birthDate")
                    )

                "Subscription" ->
                    EventSubscription(
                        id = body.getObjectId("id"),
                        startDate = body.getDate("startDate"),
                        endDate = body.getDate("endDate")
                    )
                "Activity" ->
                    EventActivity(
                        id = body.getObjectId("id"),
                        type = body.getActivityType("type")
                    )

                else ->
                    throw IllegalStateException("Couldn't parse document $document")
            }
        }
    }

    abstract fun toBodyDocument(): Document

    fun toDocument(): Document = Document()
        .append("type", type)
        .append("body", toBodyDocument())
}

abstract class Domain

fun Document.appendNotNull(key: String, value: Any?): Document {
    if (value != null) {
        return append(key, value)
    }

    return this
}

fun List<Document>.parseDomainDocuments() = map { EventDomain.parseDocument(it) }

fun Document.parseDomainDocument() = EventDomain.parseDocument(this)