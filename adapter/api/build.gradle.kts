plugins {
    application
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":core"))
    
    // Ktor Server
    implementation("io.ktor:ktor-server-core:2.3.6")
    implementation("io.ktor:ktor-server-netty:2.3.6")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.6")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.6")
    implementation("io.ktor:ktor-server-cors:2.3.6")
    
    // Logging
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
