package actor.search.child

import domain.QueryResponse
import org.jsoup.Jsoup
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class YandexQuerySearchChildActor(
    private val method: String = "https",
    private val host: String = "yandex.ru",
    private val port: Int? = null
) : QuerySearchChildActor() {
    override fun sendSearchQuery(queryText: String): String {
        val url = URL(
            "$method://$host${if (port == null) "" else ":$port"}/search/?text=${
                URLEncoder.encode(
                    queryText,
                    Charsets.UTF_8
                )
            }"
        )

        val httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "GET"
        val bufferedInputStream = BufferedInputStream(httpURLConnection.inputStream)

        return String(bufferedInputStream.readAllBytes())
    }

    override fun proceedResponse(html: String, limit: Int): List<QueryResponse> {
        val queryResponseList = mutableListOf<QueryResponse>()

        val document = Jsoup.parse(html)

        val possibleResponses = document.getElementsByClass("serp-item")

        for (possibleResponse in possibleResponses) {
            try {
                val title = possibleResponse.getElementsByClass("OrganicTitle").first()!!
                val link = title.getElementsByTag("a").first()!!

                queryResponseList.add(
                    QueryResponse(
                        link.text(),
                        URL(link.attr("href")),
                        "Yandex"
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