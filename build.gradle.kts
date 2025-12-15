plugins {
    kotlin("jvm") version "1.9.22"
    application
    id("io.gitlab.arturbosch.detekt") version "1.23.5"
    id("org.owasp.dependencycheck") version "9.0.7"
    jacoco
}

val ktorVersion = "3.0.0"
val exposedVersion = "0.50.1"
val postgresVersion = "42.7.3"

repositories {
    mavenCentral()
}

dependencies {
    // Ktor server
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:3.0.0")
    implementation("io.ktor:ktor-server-auth:3.0.0")
    implementation("io.ktor:ktor-server-auth-jvm:3.0.0")

    // Serialization
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    // Exposed ORM + PostgreSQL
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.postgresql:postgresql:$postgresVersion")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")
    // bcrypt dependency
    implementation("at.favre.lib:bcrypt:0.9.0")

    // Testing
    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("io.ktor:ktor-client-core:$ktorVersion")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")

    // Code quality
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.5")
}

application {
    mainClass.set("com.ipca.ApplicationKt")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

// Detekt configuration
detekt {
    config.setFrom(files("config/detekt/detekt.yml"))
    autoCorrect = false
    parallel = true
    ignoreFailures = false

    reports {
        xml.required.set(true)
        html.required.set(true)
        txt.required.set(false)
        sarif.required.set(false)
    }
}

// Distribution task
distributions {
    main {
        contents {
            from("src/main/resources") {
                into("conf")
            }
        }
    }
}

// Task to generate test report
tasks.register("testReport") {
    dependsOn(tasks.test)
    doLast {
        println("Test report generated at: build/reports/tests/test/index.html")
    }
}
