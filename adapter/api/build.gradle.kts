plugins {
    application
    kotlin("plugin.serialization")
    id("io.ktor.plugin") version "3.3.2"
}

dependencies {
    implementation(project(":core"))
    
    // Ktor Server 기본 구성 (버전 일관성)
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-cors")
    
    // 로깅
    implementation("ch.qos.logback:logback-classic:1.4.11")
}

application {
    mainClass.set("ServerMainKt")
}

tasks.jar {
    archiveBaseName.set("tokenizer-api")
    manifest {
        attributes["Main-Class"] = "ServerMainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
