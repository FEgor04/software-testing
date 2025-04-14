package simulations

import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*
import io.gatling.javaapi.core.*
import io.gatling.javaapi.http.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class StressTestSimulation : Simulation() {

    private val httpProtocol: HttpProtocolBuilder = http
        .baseUrl("http://localhost:8080")
        .acceptHeader("application/json")
        .userAgentHeader("Gatling-Stress-Test")

    private val config1Scenario = scenario("Stress Test: Config 1 ($1700)")
        .exec(
            http("Request Config 1")
                .get("/?token=495356269&user=-2105802621&config=1")
                .check(
                    status().`is`(200),
                    status().not(403),
                    status().not(503)
                )
        )

    init {
        // Параметры стресс-теста
        val minUsers = 5              // начальное число пользователей
        val maxUsers = 200           // максимальное количество одновременных пользователей
        val step = 50                 // шаг увеличения нагрузки (по 50 пользователей)
        val rampTime = 5.seconds.toJavaDuration()   // время на разгон нагрузки между шагами
        val holdTime = 15.seconds.toJavaDuration()  // время удержания нагрузки на каждом уровне

        // Вычисляем количество шагов, чтобы знать общую продолжительность
        val steps = (maxUsers - minUsers) / step + 1

        // Список шагов инъекции нагрузки (нагрузка увеличивается поэтапно)
        val injectionSteps = mutableListOf<ClosedInjectionStep>()
        var currentUsers = minUsers

        // Заполняем список нагрузок: поэтапно увеличиваем пользователей
        while (currentUsers <= maxUsers) {
            // Плавный разгон от предыдущего уровня пользователей до текущего
            injectionSteps.add(
                rampConcurrentUsers(if (currentUsers == minUsers) 0 else currentUsers - step)
                    .to(currentUsers)
                    .during(rampTime)
            )

            // Удерживаем текущую нагрузку в течение holdTime
            injectionSteps.add(
                constantConcurrentUsers(currentUsers)
                    .during(holdTime)
            )

            currentUsers += step // переходим к следующему шагу
        }

        // Устанавливаем план тестирования
        setUp(
            config1Scenario.injectClosed(*injectionSteps.toTypedArray()) // запускаем нагрузку
        )
            .protocols(httpProtocol) // используем заданный протокол
            .maxDuration( // ограничиваем максимальную длительность теста (на всякий случай)
                rampTime.multipliedBy(steps.toLong()).plus(holdTime.multipliedBy(steps.toLong()))
            )
            .assertions( // Устанавливаем критерии успешности теста
                global().responseTime().max().lt(760),       // максимальное время отклика должно быть < 760 мс
                global().successfulRequests().percent().gt(95.0) // минимум 95% успешных запросов
            )
    }
}