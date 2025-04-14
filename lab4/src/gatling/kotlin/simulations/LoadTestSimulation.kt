//package simulations
//import io.gatling.javaapi.core.CoreDsl.*
//import io.gatling.javaapi.http.HttpDsl.*
//import io.gatling.javaapi.core.*
//import io.gatling.javaapi.http.*
//import kotlin.time.Duration.Companion.milliseconds
//import kotlin.time.Duration.Companion.minutes
//import kotlin.time.Duration.Companion.seconds
//import kotlin.time.toJavaDuration
//
//class LoadTestSimulation : Simulation() {
//
//    private val httpProtocol = http
//        .baseUrl("http://localhost:8080")
//        .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
//        .doNotTrackHeader("1")
//        .acceptLanguageHeader("en-US,en;q=0.5")
//        .acceptEncodingHeader("gzip, deflate")
//        .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")
//
//    private val config1Scenario = scenario("Config 3 ($1700)")
//        .pace(1500.milliseconds.toJavaDuration())  // 40 requests/minute
//        .exec(http("Config1 Request")
//            .get("?token=495356269&user=-2105802621&config=1")
//            .check(status().`is`(200)))
//
//    private val config2Scenario = scenario("Config 2 ($3000)")
//        .pace(1500.milliseconds.toJavaDuration())  // 40 requests/minute
//        .exec(http("Config2 Request")
//            .get("?token=495356269&user=-2105802621&config=2")
//            .check(status().`is`(200)))
//
//    private val config3Scenario = scenario("Config 1 ($5500)")
//        .pace(1500.milliseconds.toJavaDuration())  // 40 requests/minute
//        .exec(http("Config3 Request")
//            .get("?token=495356269&user=-2105802621&config=3")
//            .check(status().`is`(200)))
//
//    init {
//        val totalUsers = 13
//        val rampUpTime = 30.seconds
//        val testDuration = 1.minutes
//        val totalDuration = rampUpTime + testDuration
//
//        setUp(
//            config1Scenario.injectClosed(
//                rampConcurrentUsers(0).to(totalUsers).during(rampUpTime.toJavaDuration()),
//                constantConcurrentUsers(totalUsers).during(testDuration.toJavaDuration())
//            ).protocols(httpProtocol),
//
//            config2Scenario.injectClosed(
//                rampConcurrentUsers(0).to(totalUsers).during(rampUpTime.toJavaDuration()),
//                constantConcurrentUsers(totalUsers).during(testDuration.toJavaDuration())
//            ).protocols(httpProtocol),
//
//            config3Scenario.injectClosed(
//                rampConcurrentUsers(0).to(totalUsers).during(rampUpTime.toJavaDuration()),
//                constantConcurrentUsers(totalUsers).during(testDuration.toJavaDuration())
//            ).protocols(httpProtocol)
//        )
//            .maxDuration(totalDuration.toJavaDuration())
//            .assertions(
//                global().responseTime().max().lt(760),
//                global().successfulRequests().percent().shouldBe(100.0)
//            )
//    }
//}