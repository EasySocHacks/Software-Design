package event

import domain.Client
import domain.Domain
import domain.EventClient
import org.bson.Document
import org.bson.types.ObjectId
import java.util.*


class CreateClientEvent(
    val clientId: ObjectId = ObjectId.get(),
    val name: String,
    val birthDate: Date,
    override val timestamp: Date = Date()
) : Event("CreateClient") {
    override fun toBodyDocument(): Document = Document()
        .append(
            "client", EventClient(
                id = clientId,
                name = name,
                birthDate = birthDate
            ).toDocument()
        )

    override fun apply(domain: Domain) {
        when (domain) {
            is Client -> {
                if (domain.id != null
                    || domain.name != null
                    || domain.birthDate != null
                ) {
                    throw IllegalStateException("Cannot create client from non-empty domain $domain")
                }

                domain.apply {
                    id = this@CreateClientEvent.clientId
                    name = this@CreateClientEvent.name
                    birthDate = this@CreateClientEvent.birthDate
                }
            }
        }
    }
}