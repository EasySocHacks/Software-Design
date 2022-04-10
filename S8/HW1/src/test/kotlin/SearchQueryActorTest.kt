import actor.search.child.GoogleQuerySearchChildActor
import actor.search.child.QuerySearchChildActor
import actor.search.child.YandexQuerySearchChildActor
import actor.search.master.QuerySearchMasterActor
import akka.actor.ActorRef.noSender
import akka.actor.ActorSystem
import akka.actor.Props
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.matching.UrlPattern.ANY
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SearchQueryActorTest {
    private val wireMockServer = WireMockServer()
    private val actorSystem = ActorSystem.create()

    private val yandexResponseByteArray =
        this::class.java.getResource("yandexResponse.html")?.readBytes() ?: ByteArray(0)
    private val googleResponseByteArray =
        this::class.java.getResource("googleResponse.html")?.readBytes() ?: ByteArray(0)

    private val yandexExpectedResponse =
        "Yandex :: {title: Тест — Википедия, url: https://ru.wikipedia.org/wiki/%D0%A2%D0%B5%D1%81%D1%82}\n" +
                "Yandex :: {title: Тесты онлайн - узнайте, насколько хорошо вы..., url: https://weekend.rambler.ru/tests/}\n" +
                "Yandex :: {title: Test - Wikipedia, url: https://en.wikipedia.org/wiki/Test}\n" +
                "Yandex :: {title: Онлайн тесты c точным результатом от психологических..., url: https://testometrika.com/tests/}\n" +
                "Yandex :: {title: Тесты онлайн | Online Test Pad, url: https://onlinetestpad.com/ru/tests}\n"
    private val googleExpectedResponse =
        "Google :: {title: Speedtest от Ookla - Глобальный тест скорости ... https://www.speedtest.net › ..., url: https://www.speedtest.net/ru}\n" +
                "Google :: {title: Fast.com: Internet Speed Test https://fast.com, url: https://fast.com/}\n" +
                "Google :: {title: Тест скорости интернета: тестирование широкополосной ... https://www.nperf.com › ..., url: https://www.nperf.com/ru/}\n" +
                "Google :: {title: Тест - Википедия https://ru.wikipedia.org › wiki › Тест, url: https://ru.wikipedia.org/wiki/%D0%A2%D0%B5%D1%81%D1%82}\n" +
                "Google :: {title: Скорость интернета, Спидтест, Speed Test - Speedcheck.org https://www.speedcheck.org › ..., url: https://www.speedcheck.org/ru/}\n"

    private val outputStream = ByteArrayOutputStream()

    private fun awaitOutputStreamDone() {
        while (true) {
            if (outputStream.toString().contains("Done!") || outputStream.toString().contains("Time's out"))
                break
        }
    }

    @BeforeEach
    fun beforeEach() {
        wireMockServer.start()

        System.setOut(PrintStream(outputStream))
    }

    @AfterEach
    fun afterEach() {
        wireMockServer.stop()

        outputStream.reset()
    }

    @Test
    fun `search with no child actors`() {
        actorSystem.actorOf(
            Props.create(
                QuerySearchMasterActor::class.java,
                emptyList<Pair<Class<out QuerySearchChildActor>, List<Any?>>>(),
                1000L
            )
        ).tell("test", noSender())

        assertEquals("", outputStream.toString())

        verify(0, getRequestedFor(ANY))
    }

    @Test
    fun `search with yandex actor child`() {
        stubFor(
            get(urlMatching(".*"))
                .willReturn(aResponse().withBody(yandexResponseByteArray))
        )

        actorSystem.actorOf(
            Props.create(
                QuerySearchMasterActor::class.java,
                listOf<Pair<Class<out QuerySearchChildActor>, List<Any?>>>(
                    Pair(YandexQuerySearchChildActor::class.java, listOf("http", "localhost", 8080))
                ),
                1000L
            )
        ).tell("test", noSender())

        awaitOutputStreamDone()

        assertContains(outputStream.toString(), yandexExpectedResponse)

        verify(1, getRequestedFor(ANY))
    }

    @Test
    fun `search with google actor child`() {
        stubFor(
            get(urlMatching(".*"))
                .willReturn(aResponse().withBody(googleResponseByteArray))
        )

        actorSystem.actorOf(
            Props.create(
                QuerySearchMasterActor::class.java,
                listOf<Pair<Class<out QuerySearchChildActor>, List<Any?>>>(
                    Pair(GoogleQuerySearchChildActor::class.java, listOf("http", "localhost", 8080))
                ),
                1000L
            )
        ).tell("test", noSender())

        awaitOutputStreamDone()

        assertContains(outputStream.toString(), googleExpectedResponse)

        verify(1, getRequestedFor(ANY))
    }

    @Test
    fun `search with no actor response`() {
        stubFor(
            get(urlMatching(".*"))
                .willReturn(
                    aResponse()
                        .withBody(yandexResponseByteArray)
                        .withFixedDelay(2000)
                )
        )

        actorSystem.actorOf(
            Props.create(
                QuerySearchMasterActor::class.java,
                listOf<Pair<Class<out QuerySearchChildActor>, List<Any?>>>(
                    Pair(YandexQuerySearchChildActor::class.java, listOf("http", "localhost", 8080))
                ),
                1000L
            )
        ).tell("test", noSender())

        awaitOutputStreamDone()

        assertEquals("Time's out\n", outputStream.toString())

        verify(1, getRequestedFor(ANY))
    }

    @Test
    fun `search all hosts with done status`() {
        stubFor(
            get(urlMatching(".*"))
                .willReturn(aResponse().withBody(ByteArray(0)))
        )

        actorSystem.actorOf(
            Props.create(
                QuerySearchMasterActor::class.java,
                listOf<Pair<Class<out QuerySearchChildActor>, List<Any?>>>(
                    Pair(YandexQuerySearchChildActor::class.java, listOf("http", "localhost", 8080)),
                    Pair(GoogleQuerySearchChildActor::class.java, listOf("http", "localhost", 8080))
                ),
                1000L
            )
        ).tell("test", noSender())

        awaitOutputStreamDone()

        assertContains(outputStream.toString(), "Done!")

        verify(2, getRequestedFor(ANY))
    }

    @Test
    fun `search all hosts with time out status`() {
        stubFor(
            get(urlMatching(".*"))
                .willReturn(aResponse().withBody(ByteArray(0)).withFixedDelay(2000))
        )

        actorSystem.actorOf(
            Props.create(
                QuerySearchMasterActor::class.java,
                listOf<Pair<Class<out QuerySearchChildActor>, List<Any?>>>(
                    Pair(YandexQuerySearchChildActor::class.java, listOf("http", "localhost", 8080)),
                    Pair(GoogleQuerySearchChildActor::class.java, listOf("http", "localhost", 8080))
                ),
                1000L
            )
        ).tell("test", noSender())

        awaitOutputStreamDone()

        assertContains(outputStream.toString(), "Time's out")

        verify(2, getRequestedFor(ANY))
    }
}