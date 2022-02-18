package statistic

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import time.FakeTimeManager.Companion.withFrozenTime
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.Calendar.*
import kotlin.test.assertEquals

internal class EventStatisticImplTest {
    private lateinit var eventStatistic: EventStatistic

    private lateinit var stream: ByteArrayOutputStream

    @BeforeEach
    internal fun setUp() {
        stream = ByteArrayOutputStream()
        System.setOut(PrintStream(stream))
    }

    @Test
    fun `incEvent returns true on a single increment`() = withFrozenTime {
        eventStatistic = EventStatisticImpl(this)

        assertEquals(true, eventStatistic.incEvent("abacaba"))
    }

    @Test
    fun `incEvent returns false on a save date increment`() = withFrozenTime {
        eventStatistic = EventStatisticImpl(this)

        assertEquals(true, eventStatistic.incEvent("abacaba"))
        assertEquals(false, eventStatistic.incEvent("abacaba"))
        assertEquals(false, eventStatistic.incEvent("abacaba"))
    }

    @Test
    fun `getEventStatisticByName returns zero on none events`() = withFrozenTime {
        eventStatistic = EventStatisticImpl(this)

        assertEquals(0.0, eventStatistic.getEventStatisticByName("abacaba"))
    }

    @Test
    fun `getEventStatisticByName returns zero on only ont in hour events`() = withFrozenTime {
        eventStatistic = EventStatisticImpl(this)

        val initialTime = clone()
        val hourAgo = add(HOUR_OF_DAY, -1)

        setThis(hourAgo.add(MINUTE, -10).now())
        eventStatistic.incEvent("a")
        eventStatistic.incEvent("a")

        setThis(hourAgo.add(MINUTE, -5).now())
        eventStatistic.incEvent("a")

        setThis(hourAgo.add(MINUTE, -1).now())
        eventStatistic.incEvent("b")

        setThis(initialTime.now())

        assertEquals(0.0, eventStatistic.getEventStatisticByName("a"))
        assertEquals(0.0, eventStatistic.getEventStatisticByName("b"))
        assertEquals(0.0, eventStatistic.getEventStatisticByName("c"))
        assertEquals(0.0, eventStatistic.getEventStatisticByName("abacaba"))
    }

    @Test
    fun `getEventStatisticByName returns correct answer on in hour events`() = withFrozenTime {
        eventStatistic = EventStatisticImpl(this)

        val initialTime = clone()

        setThis(initialTime.add(MINUTE, -10).now())
        eventStatistic.incEvent("a")
        eventStatistic.incEvent("a")

        setThis(initialTime.add(MINUTE, -5).now())
        eventStatistic.incEvent("a")

        setThis(initialTime.add(MINUTE, -1).now())
        eventStatistic.incEvent("b")

        setThis(initialTime.now())

        assertEquals(2.0 / 60.0, eventStatistic.getEventStatisticByName("a"))
        assertEquals(1.0 / 60.0, eventStatistic.getEventStatisticByName("b"))
        assertEquals(0.0, eventStatistic.getEventStatisticByName("c"))
        assertEquals(0.0, eventStatistic.getEventStatisticByName("abacaba"))
    }

    @Test
    fun `getAllEventStatistic returns empty on none events`() = withFrozenTime {
        eventStatistic = EventStatisticImpl(this)

        assertEquals(emptyMap(), eventStatistic.getAllEventStatistic())
    }

    @Test
    fun `getAllEventStatistic returns empty on only ont in hour events`() = withFrozenTime {
        eventStatistic = EventStatisticImpl(this)

        val initialTime = clone()
        val hourAgo = add(HOUR_OF_DAY, -1)

        setThis(hourAgo.add(MINUTE, -10).now())
        eventStatistic.incEvent("a")
        eventStatistic.incEvent("a")

        setThis(hourAgo.add(MINUTE, -5).now())
        eventStatistic.incEvent("a")

        setThis(hourAgo.add(MINUTE, -1).now())
        eventStatistic.incEvent("b")

        setThis(initialTime.now())

        assertEquals(emptyMap(), eventStatistic.getAllEventStatistic())
    }

    @Test
    fun `getAllEventStatistic returns correct answer on in hour events`() = withFrozenTime {
        eventStatistic = EventStatisticImpl(this)

        val initialTime = clone()

        setThis(initialTime.add(MINUTE, -10).now())
        eventStatistic.incEvent("a")
        eventStatistic.incEvent("a")

        setThis(initialTime.add(MINUTE, -5).now())
        eventStatistic.incEvent("a")

        setThis(initialTime.add(MINUTE, -1).now())
        eventStatistic.incEvent("b")

        setThis(initialTime.now())

        assertEquals(
            mapOf(
                "a" to 2.0 / 60.0,
                "b" to 1.0 / 60.0
            ), eventStatistic.getAllEventStatistic()
        )
    }

    @Test
    fun `printStatistic prints nothing on none events`() = withFrozenTime {
        eventStatistic = EventStatisticImpl(this)

        eventStatistic.printStatistic()

        assertEquals("", stream.toString())
    }

    @Test
    fun `printStatistic prints each stat correct`() = withFrozenTime {
        eventStatistic = EventStatisticImpl(this)

        val initialTime = clone()

        for (i in 0..100) {
            setThis(initialTime.add(MINUTE, -i).now())
            eventStatistic.incEvent("a")
            eventStatistic.incEvent("a")
        }

        for (i in 0..200) {
            setThis(initialTime.add(MINUTE, -i).add(YEAR, -1).now())
            eventStatistic.incEvent("b")
        }

        setThis(initialTime.now())

        eventStatistic.printStatistic()

        assertEquals(
            "a: 1.02020202020202\n" +
                    "b: 3.822753561722255E-4\n",
            stream.toString()
        )
    }
}