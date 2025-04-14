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
        .baseUrl("http://stload.se.ifmo.ru:8080")
        .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
        .doNotTrackHeader("1")
        .acceptLanguageHeader("en-US,en;q=0.5")
        .acceptEncodingHeader("gzip, deflate")
        .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

    // This will be modified based on the selected configuration from load testing
    private val selectedConfig = scenario("Selected Configuration Stress Test")
        .exec(http("request")
            .get("?token=495356269&user=-2105802621&config=1") // Config number will be changed based on load test results
            .check(status().`is`(200)))


    init {
        // Stress test parameters
        val baseUsers = 13 // Starting number of users
        val maxUsers = 50 // Maximum number of users to test
        val stepUsers = 5 // Number of users to add in each step
        val stepDuration = 2.minutes.toJavaDuration() // Duration of each step
        val rampUpTime = 30.seconds.toJavaDuration() // Time to ramp up users in each step

        // Create a list of injection steps
        val injectionSteps = (baseUsers..maxUsers step stepUsers).map { users ->
            rampUsers(users).during(rampUpTime)
        }

        setUp(
            selectedConfig.injectOpen(*injectionSteps.toTypedArray())
        ).maxDuration(stepDuration.multipliedBy((injectionSteps.size + 1).toLong()))
         .assertions(
            global().responseTime().max().lt(760), // Maximum response time should be less than 760ms
            global().successfulRequests().percent().gt(95.0) // 95% of requests should be successful
         )
    }
} 