package service

import com.mongodb.client.FindIterable
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.all
import com.mongodb.client.model.Filters.eq
import event.Event
import listener.EventListener
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.types.ObjectId

class DatabaseService(
    url: String,
    databaseName: String
) {
    private val client = MongoClients.create(url)
    private val database: MongoDatabase = client.getDatabase(databaseName)
    private val collection: MongoCollection<Document>

    private val listeners = mutableListOf<EventListener>()

    init {
        if (!database.listCollectionNames().toList().contains("Events"))
            database.createCollection("Events")

        collection = database.getCollection("Events")
    }

    fun clear() {
        collection.deleteMany(Document())
    }

    fun listenDocuments(eventListener: EventListener) {
        listeners.add(eventListener)
    }

    fun appendEvent(event: Event): ObjectId {
        val document = event.toDocument()

        collection.insertOne(document)

        listeners.forEach { it.notifyEvent(event) }

        return document.getObjectId("_id")
    }

    fun findAll(): List<Document> = collection.find().toList()

    private fun findFilter(filter: Bson): FindIterable<Document> {
        return collection.find(filter)
    }

    fun findDocumentById(id: ObjectId): Document? {
        return findFilter(eq("_id", id)).first()
    }

    fun findClientEventsById(clientId: ObjectId): List<Document> {
        return findFilter(eq("event.body.client.body.id", clientId)).toList()
    }

    fun findSubscriptionEventsById(subscriptionId: ObjectId): List<Document> {
        return findFilter(eq("event.body.subscription.body.id", subscriptionId)).toList()
    }
}