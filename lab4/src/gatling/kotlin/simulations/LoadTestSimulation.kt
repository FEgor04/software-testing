package simulations
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*
import io.gatling.javaapi.core.*
import io.gatling.javaapi.http.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration
class LoadTestSimulation : Simulation() {

    private val httpProtocol = http
        .baseUrl("http://stload.se.ifmo.ru:8080")
        .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
        .doNotTrackHeader("1")
        .acceptLanguageHeader("en-US,en;q=0.5")
        .acceptEncodingHeader("gzip, deflate")
        .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

    private val config1Scenario = scenario("Config 1 ($1700)")
        .exec(http("request_1")
            .get("?token=495356269&user=-2105802621&config=1")
            .check(status().`is`(200))
        )

    private val config2Scenario = scenario("Config 2 ($3000)")
        .exec(http("request_2")
            .get("?token=495356269&user=-2105802621&config=2")
            .check(status().`is`(200))
        )

    private val config3Scenario = scenario("Config 3 ($5500)")
        .exec(http("request_3")
            .get("?token=495356269&user=-2105802621&config=3")
            .check(status().`is`(200))
        )

    init {
        // Load test parameters
        val users = 13 // Maximum number of parallel users
        val requestsPerMinute = 40 // Average load per user
        val rampUpTime = 30.seconds // Time to ramp up to full load
        val testDuration = 5.minutes // Test duration

        // Calculate request rate per second
        val requestRate = (users * requestsPerMinute) / 60.0

        setUp(
            config1Scenario.injectOpen(
                rampUsers(users).during(rampUpTime.toJavaDuration())
            ).protocols(httpProtocol),
            config2Scenario.injectOpen(
                rampUsers(users).during(rampUpTime.toJavaDuration())
            ).protocols(httpProtocol),
            config3Scenario.injectOpen(
                rampUsers(users).during(rampUpTime.toJavaDuration())
            ).protocols(httpProtocol)
        ).maxDuration(testDuration.toJavaDuration())
         .assertions(
            global().responseTime().max().lt(760), // Maximum response time should be less than 760ms
            global().successfulRequests().percent().gt(95.0) // 95% of requests should be successful
         )
    }
} 