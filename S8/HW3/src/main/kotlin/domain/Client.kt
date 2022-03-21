package domain

import org.bson.Document
import org.bson.types.ObjectId
import java.util.*

class EventClient(
    val id: ObjectId? = ObjectId.get(),
    val name: String? = null,
    val birthDate: Date? = null
) : EventDomain("Client") {
    override fun toBodyDocument(): Document = Document()
        .appendNotNull("id", id)
        .appendNotNull("name", name)
        .appendNotNull("birthDate", birthDate)
}

class Client : Domain() {
    var id: ObjectId? = null
    var name: String? = null
    var birthDate: Date? = null

    override fun toString(): String {
        return "Client(id=$id, name=$name, birthDate=$birthDate)"
    }
}