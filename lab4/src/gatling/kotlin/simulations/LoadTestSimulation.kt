package simulations

import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*
import io.gatling.javaapi.core.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class LoadTestSimulation : Simulation() {

    private val httpProtocol = http
        .baseUrl("http://localhost:8080")
        .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
        .doNotTrackHeader("1")
        .acceptLanguageHeader("en-US,en;q=0.5")
        .acceptEncodingHeader("gzip, deflate")
        .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

    // Сценарий для тестирования конфигурации 1 ($1700)
    private val config1Scenario = scenario("Config 1 ($1700)")
        .exec(
            http("Config1 Request")
                .get("?token=495356269&user=-2105802621&config=1")
                .check(
                    status().shouldBe(200).saveAs("httpStatus"),
                    status().not(403),
                    status().not(503)
                )
        )

    // Сценарий для тестирования конфигурации 2 ($3000)
    private val config2Scenario = scenario("Config 2 ($3000)")
        .exec(
            http("Config2 Request")
                .get("?token=495356269&user=-2105802621&config=2")
                .check(
                    status().shouldBe(200).saveAs("httpStatus"),
                    status().not(403),
                    status().not(503)
                )
        )

    // Сценарий для тестирования конфигурации 3 ($5500)
    private val config3Scenario = scenario("Config 3 ($5500)")
        .exec(
            http("Config3 Request")
                .get("?token=495356269&user=-2105802621&config=3")
                .check(
                    status().shouldBe(200).saveAs("httpStatus"),
                    status().not(403),
                    status().not(503)
                )
        )

    init {
        // Параметры нагрузки:
        val totalUsers = 13 // Общее количество виртуальных пользователей
        val rampUpTime = 30.seconds // Время наращивания нагрузки (разгон)
        val testDuration = 1.minutes // Продолжительность теста на полной нагрузке
        val totalDuration = rampUpTime + testDuration // Общая продолжительность теста
        val requestRate = 40.0 / 60 // Расчет RPS (40 запр/мин = ~0.666 запр/сек на пользователя)

        setUp(
            // Инъекция нагрузки с постоянным количеством пользователей
            config1Scenario.injectClosed(
                constantConcurrentUsers(totalUsers).during(totalDuration.toJavaDuration())
            ).throttle( // Ограничение скорости запросов
                reachRps((requestRate * totalUsers).toInt()).`in`(rampUpTime.toJavaDuration()), // Плавный выход на целевую RPS
                holdFor(testDuration.toJavaDuration()) // Удержание целевой RPS
            ).protocols(httpProtocol), // Применение HTTP-протокола

            config2Scenario.injectClosed(
                constantConcurrentUsers(totalUsers).during(totalDuration.toJavaDuration())
            ).throttle(
                reachRps((requestRate * totalUsers).toInt()).`in`(rampUpTime.toJavaDuration()),
                holdFor(testDuration.toJavaDuration())
            ).protocols(httpProtocol),


            config3Scenario.injectClosed(
                constantConcurrentUsers(totalUsers).during(totalDuration.toJavaDuration())
            ).throttle(
                reachRps((requestRate * totalUsers).toInt()).`in`(rampUpTime.toJavaDuration()),
                holdFor(testDuration.toJavaDuration())
            ).protocols(httpProtocol)
        )
            // Общие ограничения теста
            .maxDuration(totalDuration.toJavaDuration()) // Максимальная продолжительность
            .assertions( // Проверки результатов теста
                global().responseTime().max().lt(760), // Макс. время ответа должно быть <760 мс
                global().successfulRequests().percent().shouldBe(100.0), // 100% успешных запросов
                forAll().failedRequests().count().shouldBe(0) // Ни одного неудачного запроса
            )
    }
}