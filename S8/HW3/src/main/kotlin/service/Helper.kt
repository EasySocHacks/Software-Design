package service

import org.bson.Document

fun Document.getDocument(key: String): Document = get(key) as Document
fun Document.getNestedDocument(key: String): Document {
    var document = this

    key.split(".").forEach {
        document = document.getDocument(it)
    }

    return document
}