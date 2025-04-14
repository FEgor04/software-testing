package simulations

import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*
import io.gatling.javaapi.core.*
import io.gatling.javaapi.http.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration


class StressTestSimulation : Simulation() {

    private val httpProtocol: HttpProtocolBuilder = http
        .baseUrl("http://localhost:8080")
        .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
        .doNotTrackHeader("1")
        .acceptLanguageHeader("en-US,en;q=0.5")
        .acceptEncodingHeader("gzip, deflate")
        .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

    // This will be modified based on the selected configuration from load testing
    private val selectedConfig = scenario("Selected Configuration Stress Test")
        .exec(http("request")
            .get("/?token=495356269&user=-2105802621&config=1") // Added leading slash to make it absolute
            .check(status().`is`(200)))


    init {
        // Stress test parameters
        val baseUsers = 50 // Starting number of users
        val maxUsers = 100 // Maximum number of users to test
        val stepDuration = 30.seconds.toJavaDuration() // Duration of each step
        val rampUpTime = 5.seconds.toJavaDuration() // Time to ramp up users in each step

        // Create a list of injection steps
        val injectionSteps = (baseUsers..maxUsers step 10).map { users ->
            constantConcurrentUsers(users).during(stepDuration)
        }

        setUp(
            selectedConfig.injectClosed(*injectionSteps.toTypedArray())
        ).maxDuration(stepDuration.multipliedBy((injectionSteps.size + 1).toLong()))
         .assertions(
            global().responseTime().max().lt(760), // Maximum response time should be less than 760ms
            global().successfulRequests().percent().gt(95.0) // 95% of requests should be successful
         ) .protocols(httpProtocol)
    }
}