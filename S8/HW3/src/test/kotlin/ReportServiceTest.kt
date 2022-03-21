import domain.Client
import domain.Subscription
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import service.DatabaseService
import service.ManagerService
import service.ReportService
import service.TurnstileService
import java.time.Duration
import java.util.*
import kotlin.test.assertEquals

class ReportServiceTest {
    private val databaseService = DatabaseService(
        "mongodb://localhost:27017",
        "hw3"
    )
    private val managerService = ManagerService(databaseService)
    private val turnstileService = TurnstileService(databaseService)

    private val testClient = Client().apply {
        name = "name"
        birthDate = Date()
    }
    private val testSubscription = Subscription().apply {
        val startCalendar = Calendar.getInstance()
        startCalendar.time = Date(0L)

        startDate = startCalendar.time

        val endCalendar = Calendar.getInstance()
        endCalendar.add(Calendar.DAY_OF_YEAR, 100)

        endDate = endCalendar.time
    }

    @BeforeEach
    fun beforeEach() {
        databaseService.clear()
    }

    @Test
    fun `get visit count by day of week`() {
        val reportService = ReportService(databaseService)

        val client = managerService.createClient(
            testClient.name!!,
            testClient.birthDate!!
        )

        val subscription = managerService.createSubscription(
            client.id!!,
            testSubscription.startDate!!,
            testSubscription.endDate!!
        )

        turnstileService.entrance(
            client.id!!,
            subscription.id!!,
            Date(0)
        )

        val visitCountByDayOfWeek = reportService.getVisitCountByDayOfWeek()

        assertEquals(listOf(0, 1, 0, 0, 0, 0, 0), visitCountByDayOfWeek)
    }

    @Test
    fun `get avg visit frequency`() {
        val reportService = ReportService(databaseService)

        val firstClient = managerService.createClient(
            testClient.name!!,
            testClient.birthDate!!
        )

        val secondClient = managerService.createClient(
            testClient.name!!,
            testClient.birthDate!!
        )

        val firstSubscription = managerService.createSubscription(
            firstClient.id!!,
            testSubscription.startDate!!,
            testSubscription.endDate!!
        )

        val secondSubscription = managerService.createSubscription(
            secondClient.id!!,
            testSubscription.startDate!!,
            testSubscription.endDate!!
        )

        // -- Date(0) -- BEGIN
        turnstileService.entrance(
            firstClient.id!!,
            firstSubscription.id!!,
            Date(0)
        )

        turnstileService.entrance(
            secondClient.id!!,
            secondSubscription.id!!,
            Date(0)
        )
        // -- Date(0) -- END

        // -- Date(1) -- BEGIN
        turnstileService.exit(
            firstClient.id!!,
            firstSubscription.id!!,
            Date(1)
        )

        turnstileService.exit(
            secondClient.id!!,
            secondSubscription.id!!,
            Date(1)
        )
        // -- Date(1) -- END

        // -- Date(2-3) -- BEGIN
        turnstileService.entrance(
            secondClient.id!!,
            secondSubscription.id!!,
            Date(2)
        )

        turnstileService.exit(
            secondClient.id!!,
            secondSubscription.id!!,
            Date(3)
        )
        // -- Date(2-3) -- END

        val avgVisitFrequency = reportService.getAvgVisitFrequency()

        assertEquals(1.5, avgVisitFrequency)
    }

    @Test
    fun `get avg duration frequency`() {
        val reportService = ReportService(databaseService)

        val firstClient = managerService.createClient(
            testClient.name!!,
            testClient.birthDate!!
        )

        val secondClient = managerService.createClient(
            testClient.name!!,
            testClient.birthDate!!
        )

        val firstSubscription = managerService.createSubscription(
            firstClient.id!!,
            testSubscription.startDate!!,
            testSubscription.endDate!!
        )

        val secondSubscription = managerService.createSubscription(
            secondClient.id!!,
            testSubscription.startDate!!,
            testSubscription.endDate!!
        )

        // -- Date(0) -- BEGIN
        turnstileService.entrance(
            firstClient.id!!,
            firstSubscription.id!!,
            Date(0)
        )

        turnstileService.entrance(
            secondClient.id!!,
            secondSubscription.id!!,
            Date(0)
        )
        // -- Date(0) -- END

        // -- Date(100) -- BEGIN
        turnstileService.exit(
            firstClient.id!!,
            firstSubscription.id!!,
            Date(100)
        )

        turnstileService.exit(
            secondClient.id!!,
            secondSubscription.id!!,
            Date(100)
        )
        // -- Date(100) -- END

        // -- Date(200-600) -- BEGIN
        turnstileService.entrance(
            secondClient.id!!,
            secondSubscription.id!!,
            Date(200)
        )

        turnstileService.exit(
            secondClient.id!!,
            secondSubscription.id!!,
            Date(600)
        )
        // -- Date(200-600) -- END

        val avgDurationFrequency = reportService.getAvgVisitDuration()

        assertEquals(Duration.ofMillis(200), avgDurationFrequency)
    }

    @Test
    fun `report service expand local storage on event`() {
        val client = managerService.createClient(
            testClient.name!!,
            testClient.birthDate!!
        )

        val subscription = managerService.createSubscription(
            client.id!!,
            testSubscription.startDate!!,
            testSubscription.endDate!!
        )

        turnstileService.entrance(
            client.id!!,
            subscription.id!!,
            Date(0)
        )

        val reportService = ReportService(databaseService)

        turnstileService.entrance(
            client.id!!,
            subscription.id!!,
            Date(1)
        )

        val visitCountByDayOfWeek = reportService.getVisitCountByDayOfWeek()

        assertEquals(listOf(0, 2, 0, 0, 0, 0, 0), visitCountByDayOfWeek)
    }
}