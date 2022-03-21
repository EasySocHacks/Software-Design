import domain.ActivityType.ENTRANCE
import domain.ActivityType.EXIT
import domain.Client
import domain.Subscription
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import service.DatabaseService
import service.ManagerService
import service.TurnstileService
import java.util.*
import kotlin.test.assertEquals

class TurnstileServiceTest {
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
    private val notStartedSubscription = Subscription().apply {
        val startCalendar = Calendar.getInstance()
        startCalendar.add(Calendar.DAY_OF_YEAR, 300)

        startDate = startCalendar.time

        val endCalendar = Calendar.getInstance()
        endCalendar.add(Calendar.DAY_OF_YEAR, 600)

        endDate = endCalendar.time
    }
    private val workingSubscription = Subscription().apply {
        val startCalendar = Calendar.getInstance()
        startCalendar.add(Calendar.DAY_OF_YEAR, -100)

        startDate = startCalendar.time

        val endCalendar = Calendar.getInstance()
        endCalendar.add(Calendar.DAY_OF_YEAR, 100)

        endDate = endCalendar.time
    }
    private val expiredSubscription = Subscription().apply {
        val startCalendar = Calendar.getInstance()
        startCalendar.add(Calendar.DAY_OF_YEAR, -600)

        startDate = startCalendar.time

        val endCalendar = Calendar.getInstance()
        endCalendar.add(Calendar.DAY_OF_YEAR, -300)

        endDate = endCalendar.time
    }

    @BeforeEach
    fun beforeEach() {
        databaseService.clear()
    }

    @Test
    fun `allowed entrance`() {
        val client = managerService.createClient(
            testClient.name!!,
            testClient.birthDate!!
        )

        val subscription = managerService.createSubscription(
            client.id!!,
            workingSubscription.startDate!!,
            workingSubscription.endDate!!
        )

        val activity = turnstileService.entrance(
            client.id!!,
            subscription.id!!
        )

        assertEquals(client.id, activity.clientId)
        assertEquals(subscription.id, activity.subscriptionId)
        assertEquals(ENTRANCE, activity.type)
    }

    @Test
    fun `deny entrance with not started subscription`() {
        val client = managerService.createClient(
            testClient.name!!,
            testClient.birthDate!!
        )

        val subscription = managerService.createSubscription(
            client.id!!,
            notStartedSubscription.startDate!!,
            notStartedSubscription.endDate!!
        )

        val activity = turnstileService.entrance(
            client.id!!,
            subscription.id!!
        )

        assertEquals(null, activity.id)
        assertEquals(null, activity.clientId)
        assertEquals(null, activity.subscriptionId)
        assertEquals(null, activity.type)
    }

    @Test
    fun `deny entrance with expired subscription`() {
        val client = managerService.createClient(
            testClient.name!!,
            testClient.birthDate!!
        )

        val subscription = managerService.createSubscription(
            client.id!!,
            expiredSubscription.startDate!!,
            expiredSubscription.endDate!!
        )

        val activity = turnstileService.entrance(
            client.id!!,
            subscription.id!!
        )

        assertEquals(null, activity.id)
        assertEquals(null, activity.clientId)
        assertEquals(null, activity.subscriptionId)
        assertEquals(null, activity.type)
    }

    @Test
    fun `allowed exit`() {
        val client = managerService.createClient(
            testClient.name!!,
            testClient.birthDate!!
        )

        val subscription = managerService.createSubscription(
            client.id!!,
            workingSubscription.startDate!!,
            workingSubscription.endDate!!
        )

        val activity = turnstileService.exit(
            client.id!!,
            subscription.id!!
        )

        assertEquals(client.id, activity.clientId)
        assertEquals(subscription.id, activity.subscriptionId)
        assertEquals(EXIT, activity.type)
    }
}