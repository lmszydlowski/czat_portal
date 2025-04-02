kotlin {
    jvmToolchain(17) // This will set both Java and Kotlin to use JVM 17
}

plugins {
    application
    id("org.springframework.boot") version "3.1.1"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.8.21"
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.1.1")
    }
}

group = "example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

tasks.withType<JavaExec> {
    systemProperties(System.getenv().toMap() as Map<String, Any>)

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17" // Match this with your Java target
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_17.toString()
    targetCompatibility = JavaVersion.VERSION_17.toString()
}

repositories {
    mavenCentral()
}

dependencies {

    // websocket
    implementation("org.springframework.boot:spring-boot-starter-websocket:3.4.4")
    implementation("org.webjars:stomp-websocket:2.3.4")
    implementation("org.springframework:spring-messaging:6.0.11")

    // jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    //runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.security:spring-security-config")



    // WebFlux – aplikacja reaktywna
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    // Spring Security – zabezpieczenia
    implementation("org.springframework.boot:spring-boot-starter-security")
    // Jackson dla Kotlina
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // JJWT (JSON Web Token) – użyjemy wersji 0.11.5
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    
    // Chat Home Base dependencies
    // Socket.IO client for real-time communication
    implementation("com.tfowl.socketio:socket-io-coroutines:1.0.1")
    // Alternative Socket.IO implementation if needed
    implementation("io.dyte:socketio-jvm:1.0.0")
    
    // Chat bot functionality
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("org.json:json:20210307")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    
    // Database support for chat history
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.postgresql:r2dbc-postgresql")
    
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
    }
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
     // Add R2DBC for reactive database access
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("io.r2dbc:r2dbc-h2")
    implementation("org.springframework:spring-jdbc")
    
    // For admin dashboard features
    implementation("org.springframework.boot:spring-boot-starter-validation")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
application {
    mainClass = "example.chat.ReactiveChatApplicationKt" // Replace with your actual main class
}
