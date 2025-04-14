import io.gatling.gradle.LogHttp

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.gatling.gradle") version "3.9.5.5"
}

group = "com.jellyone"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    gatling("io.gatling:gatling-core:3.9.5")
    gatling("io.gatling:gatling-http:3.9.5")
    gatling("io.gatling.highcharts:gatling-charts-highcharts:3.9.5")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

gatling {
    logLevel = "WARN"
    logHttp = LogHttp.ALL
}