import domain.Client
import domain.Subscription
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import service.DatabaseService
import service.ManagerService
import java.util.*
import java.util.Calendar.DAY_OF_YEAR
import java.util.Calendar.YEAR
import kotlin.test.assertEquals

class ManagerServiceTest {
    private val databaseService = DatabaseService(
        "mongodb://localhost:27017",
        "hw3"
    )
    private val managerService = ManagerService(databaseService)

    private val testClient = Client().apply {
        name = "name"
        birthDate = Date()
    }
    private val testSubscription = Subscription().apply {
        val startCalendar = Calendar.getInstance()
        startCalendar.add(DAY_OF_YEAR, -100)

        startDate = startCalendar.time

        val endCalendar = Calendar.getInstance()
        endCalendar.add(DAY_OF_YEAR, 100)

        endDate = endCalendar.time
    }

    @BeforeEach
    fun beforeEach() {
        databaseService.clear()
    }

    @Test
    fun `create client`() {
        val client = managerService.createClient(
            testClient.name!!,
            testClient.birthDate!!
        )

        assertEquals(testClient.name, client.name)
        assertEquals(testClient.birthDate, client.birthDate)
    }

    @Test
    fun `client info`() {
        val client = managerService.createClient(
            testClient.name!!,
            testClient.birthDate!!
        )

        val clientInfo = managerService.getClientInfo(client.id!!)

        assertEquals(client.id, clientInfo.id)
        assertEquals(client.name, clientInfo.name)
        assertEquals(client.birthDate, clientInfo.birthDate)
    }

    @Test
    fun `create subscription`() {
        val client = managerService.createClient(
            testClient.name!!,
            testClient.birthDate!!
        )

        val subscription = managerService.createSubscription(
            client.id!!,
            testSubscription.startDate!!,
            testSubscription.endDate!!
        )

        assertEquals(testSubscription.startDate, subscription.startDate)
        assertEquals(testSubscription.endDate, subscription.endDate)
    }

    @Test
    fun `subscription info`() {
        val client = managerService.createClient(
            testClient.name!!,
            testClient.birthDate!!
        )

        val subscription = managerService.createSubscription(
            client.id!!,
            testSubscription.startDate!!,
            testSubscription.endDate!!
        )

        val subscriptionInfo = managerService.getSubscriptionInfo(subscription.id!!)

        assertEquals(subscription.id, subscriptionInfo.id)
        assertEquals(subscription.clientId, subscriptionInfo.clientId)
        assertEquals(subscription.startDate, subscriptionInfo.startDate)
        assertEquals(subscription.endDate, subscriptionInfo.endDate)
    }

    @Test
    fun `renew subscription`() {
        val client = managerService.createClient(
            testClient.name!!,
            testClient.birthDate!!
        )

        val subscription = managerService.createSubscription(
            client.id!!,
            testSubscription.startDate!!,
            testSubscription.endDate!!
        )

        val calendar = Calendar.getInstance()
        calendar.add(YEAR, 10)
        val newDate = calendar.time

        val renewSubscription = managerService.renewSubscription(
            subscription.id!!,
            newDate
        )

        assertEquals(subscription.id, renewSubscription.id)
        assertEquals(subscription.clientId, renewSubscription.clientId)
        assertEquals(subscription.startDate, renewSubscription.startDate)
        assertEquals(newDate, renewSubscription.endDate)
    }
}