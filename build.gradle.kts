kotlin {
    jvmToolchain(17)
}

plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
    application
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
    implementation("io.ktor:ktor-server-cors:$ktorVersion")

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
    //swagger
    implementation("io.ktor:ktor-server-openapi-jvm:${ktorVersion}")
    implementation("io.ktor:ktor-server-swagger-jvm:${ktorVersion}")
}

application {
    mainClass.set("com.ipca.ApplicationKt")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
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
