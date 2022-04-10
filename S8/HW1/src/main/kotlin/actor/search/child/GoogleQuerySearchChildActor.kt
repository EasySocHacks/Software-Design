package actor.search.child

import domain.QueryResponse
import org.jsoup.Jsoup
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class GoogleQuerySearchChildActor(
    private val method: String = "https",
    private val host: String = "google.com",
    private val port: Int? = null
) : QuerySearchChildActor() {
    override fun sendSearchQuery(queryText: String): String {
        val url = URL(
            "$method://$host${if (port == null) "" else ":$port"}/search?q=${
                URLEncoder.encode(
                    queryText,
                    Charsets.UTF_8
                )
            }"
        )

        val httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "GET"
        httpURLConnection.setRequestProperty(
            "user-agent",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.80 Safari/537.36'"
        )

        val bufferedInputStream = BufferedInputStream(httpURLConnection.inputStream)

        return String(bufferedInputStream.readAllBytes())
    }

    override fun proceedResponse(html: String, limit: Int): List<QueryResponse> {
        val queryResponseList = mutableListOf<QueryResponse>()

        val document = Jsoup.parse(html)

        val possibleResponses = document.getElementsByClass("yuRUbf")

        for (possibleResponse in possibleResponses) {
            try {
                val link = possibleResponse.getElementsByTag("a").first()!!

                queryResponseList.add(
                    QueryResponse(
                        link.text(),
                        URL(link.attr("href")),
                        "Google"
                    )
                )

                if (queryResponseList.size == limit)
                    break
            } catch (e: Exception) {
                continue
            }
        }

        return queryResponseList
    }
}