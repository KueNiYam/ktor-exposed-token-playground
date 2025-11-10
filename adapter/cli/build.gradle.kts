plugins {
    application
}

dependencies {
    implementation(project(":core"))
}

application {
    mainClass.set("CliMainKt")
}

tasks.jar {
    archiveBaseName.set("tokenizer-cli")
    manifest {
        attributes["Main-Class"] = "CliMainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
