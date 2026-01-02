kotlin {
    jvmToolchain(17)
}

plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
    application
    id("io.gitlab.arturbosch.detekt") version "1.23.5"
    id("org.owasp.dependencycheck") version "9.0.7"
    jacoco
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

val ktorVersion = "2.3.5"
val exposedVersion = "0.50.1"
val postgresVersion = "42.7.3"

repositories {
    mavenCentral()
}

dependencies {
    // Ktor server
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jvm:$ktorVersion")

    // Serialization
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")


    // Exposed ORM + PostgreSQL
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.postgresql:postgresql:$postgresVersion")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")


    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")
    // bcrypt dependency
    implementation("at.favre.lib:bcrypt:0.9.0")
    // JWT
    implementation("com.auth0:java-jwt:4.4.0")
    // Status pages for error handling
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")

    // Testing
    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("io.ktor:ktor-client-core:$ktorVersion")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")
    // Test database
    testImplementation("com.h2database:h2:2.2.220")
    testImplementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    testImplementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")

    // Code quality
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.5")

    //swagger
    implementation("io.ktor:ktor-server-openapi-jvm:${ktorVersion}")
    implementation("io.ktor:ktor-server-swagger-jvm:${ktorVersion}")
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
    toolVersion = "1.23.5"
    config.setFrom(files("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    baseline = file("detekt-baseline.xml")
    autoCorrect = false
    parallel = true
    ignoreFailures = false
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
