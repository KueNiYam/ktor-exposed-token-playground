plugins {
    kotlin("jvm") version "2.1.0" apply false
    kotlin("plugin.serialization") version "2.1.0" apply false
}

allprojects {
    group = "com.tokenizer"
    version = "1.1.0"
    
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    
    dependencies {
        "implementation"(kotlin("stdlib"))
        "testImplementation"("org.junit.jupiter:junit-jupiter:5.10.0")
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
    }
    
    tasks.named<Test>("test") {
        useJUnitPlatform()
    }
}
