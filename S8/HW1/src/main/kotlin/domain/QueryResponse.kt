package domain

import java.net.URL

class QueryResponse (
    val title: String,
    val url: URL,
    val resourceName: String
) {
    override fun toString(): String =
        "$resourceName :: {title: $title, url: $url}"
}